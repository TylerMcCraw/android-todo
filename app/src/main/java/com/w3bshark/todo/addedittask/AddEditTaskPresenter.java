package com.w3bshark.todo.addedittask;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.w3bshark.todo.data.ITask;
import com.w3bshark.todo.data.Task;
import com.w3bshark.todo.data.source.ITasksDataSource;
import com.w3bshark.todo.data.source.TasksRepository;

import javax.inject.Inject;
import javax.inject.Named;

import timber.log.Timber;

/**
 * Created by Tyler McCraw on 6/10/17.
 * <p/>
 * Listens to user actions from the UI ({@link AddEditTaskActivity}),
 * retrieves the data and updates the UI as required.
 */

final class AddEditTaskPresenter implements IAddEditTaskContract.Presenter, ITasksDataSource.LoadTaskCallback {

    private final ITasksDataSource tasksRepository;
    private final FirebaseAuth firebaseAuth;
    private final IAddEditTaskContract.View view;
    @Nullable
    private String taskId;

    @Inject
    AddEditTaskPresenter(TasksRepository tasksRepository,
                         FirebaseAuth firebaseAuth,
                         IAddEditTaskContract.View view,
                         @Nullable @Named("taskId") String taskId) {
        this.tasksRepository = tasksRepository;
        this.firebaseAuth = firebaseAuth;
        this.view = view;
        this.taskId = taskId;
    }

    /**
     * Method injection is used here to safely reference {@code this} after the object is created.
     */
    @Inject
    void setupListeners() {
        view.setPresenter(this);
    }

    @Override
    public void start() {
        if (!isNewTask()) {
            loadTask();
        }
    }

    @Override
    public void stop() {
        // N/A
    }

    @Override
    public void saveTask(String title, String description) {
        if (isNewTask()) {
            createTask(title, description);
        } else {
            updateTask(title, description);
        }
    }

    @Override
    public void loadTask() {
        if (isNewTask()) {
            throw new RuntimeException("loadTask() was called but task is new.");
        }
        tasksRepository.getTask(taskId, this);
    }

    @Override
    public void deleteTask() {
        if (TextUtils.isEmpty(taskId)) {
            throw new RuntimeException("deleteTask() was called but task ID is null.");
        }
        tasksRepository.deleteTask(taskId);
        view.goBackToTaskList();
    }

    @Override
    public void onTaskLoaded(ITask task) {
        view.setTitle(task.getTitle());
        view.setDescription(task.getDescription());
    }

    @Override
    public void onDataNotAvailable() {
        view.showTaskError();
    }

    private boolean isNewTask() {
        return TextUtils.isEmpty(taskId);
    }

    private void createTask(String title, String description) {
        FirebaseUser user = getUser();
        if (user == null) {
            return;
        }

        Task newTask = new Task(user.getUid(), title, description, false);
        if (!newTask.isValid()) {
            view.showTaskError();
        } else {
            tasksRepository.saveTask(newTask);
            view.goBackToTaskList();
        }
    }

    private void updateTask(String title, String description) {
        if (isNewTask()) {
            throw new RuntimeException("updateTask() was called but task is new.");
        }

        FirebaseUser user = getUser();
        if (user == null) {
            return;
        }
        tasksRepository.saveTask(new Task(taskId, user.getUid(), title, description, false));
        view.goBackToTaskList();
    }

    @Nullable
    private FirebaseUser getUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            view.goToSignInScreen();
            Timber.d("FirebaseUser was null. Logging user out.");
            return null;
        }
        return firebaseUser;
    }
}
