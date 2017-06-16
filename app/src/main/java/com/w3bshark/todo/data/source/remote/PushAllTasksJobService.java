package com.w3bshark.todo.data.source.remote;

import android.os.Bundle;
import android.text.TextUtils;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.w3bshark.todo.MainApplication;
import com.w3bshark.todo.data.ITask;
import com.w3bshark.todo.data.Task;
import com.w3bshark.todo.data.source.ITasksDataSource;
import com.w3bshark.todo.data.source.Local;
import com.w3bshark.todo.data.source.Remote;
import com.w3bshark.todo.data.source.local.TasksLocalDataSource;
import com.w3bshark.todo.util.AppExecutors;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Tyler McCraw on 6/11/17.
 */

public class PushAllTasksJobService extends JobService {

    public static final String KEY_USER_ID = "com.w3bshark.todo.data.source.remote.PushAllTasksJobService.KEY_USER_ID";

    @Inject
    @Remote
    ITasksDataSource tasksRemoteDataSource;
    @Inject
    @Local
    ITasksDataSource tasksLocalDataSource;
    @Inject
    AppExecutors appExecutors;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerIPushAllTasksComponent.builder()
                .iBackgroundJobComponent(((MainApplication) getApplication()).getBackgroundJobComponent())
                .build()
                .inject(this);
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Bundle jobExtras = jobParameters.getExtras();
        String userId = null;
        if (jobExtras != null) {
            userId = jobExtras.getString(KEY_USER_ID);
        }
        if (TextUtils.isEmpty(userId)) {
            return false;
        }

        final String finalUserId = userId;
        appExecutors.diskIO().execute(() ->
                tasksLocalDataSource.getTasks(finalUserId, new ITasksDataSource.LoadTasksCallback() {
                    @Override
                    public void onTasksLoaded(List<? extends ITask> tasks) {
                        pushAllTasksToRemote(jobParameters, (List<Task>) tasks);
                        Timber.d("local pull success");
                    }

                    @Override
                    public void onDataNotAvailable() {
                        // If we can't load from persistence, then cancel the job
                        jobFinished(jobParameters, false);
                        Timber.d("local pull failure");
                    }
                })
        );
        return true;
    }

    private void pushAllTasksToRemote(JobParameters jobParameters, List<Task> tasks) {
        appExecutors.networkIO().execute(() ->
                tasksRemoteDataSource.saveTasks(tasks, new ITasksDataSource.SaveTaskCallback() {
                    @Override
                    public void onTaskSaved() {
                        // We pushed all tasks to server, so we can finish the job.
                        jobFinished(jobParameters, false);
                        Timber.d("remote push success");
                    }

                    @Override
                    public void onSaveTaskFailed() {
                        // If we can't push the tasks, then reschedule
                        jobFinished(jobParameters, true);
                        Timber.d("remote push failure");
                    }
                })
        );
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Timber.d("Job STOPPED");
        return false;
    }
}
