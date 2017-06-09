package com.w3bshark.todo.tasklist;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.w3bshark.todo.data.ITask;
import com.w3bshark.todo.data.source.ITasksDataSource;
import com.w3bshark.todo.data.source.TasksRepository;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Listens to user actions from the UI ({@link TaskListActivity}),
 * retrieves the data and updates the UI as required.
 */

final class TaskListPresenter implements ITaskListContract.Presenter, GoogleApiClient.OnConnectionFailedListener {

    private final FirebaseAuth firebaseAuth;
    private final GoogleApiClient googleApiClient;
    private final TasksRepository tasksDataSource;
    private ITaskListContract.View view;

    @Inject
    TaskListPresenter(FirebaseAuth firebaseAuth,
                      GoogleApiClient googleApiClient,
                      TasksRepository tasksRepository,
                      ITaskListContract.View view) {
        this.firebaseAuth = firebaseAuth;
        this.googleApiClient = googleApiClient;
        this.tasksDataSource = tasksRepository;
        this.view = view;
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
        FirebaseUser firebaseUser = getUser();

        if (firebaseUser != null) {
            //TODO display this some where for the user profile
            String username = firebaseUser.getDisplayName();
        }

        googleApiClient.registerConnectionFailedListener(this);
        googleApiClient.connect();

        loadTasks(false);
    }

    @Override
    public void stop() {
        googleApiClient.unregisterConnectionFailedListener(this);
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void loadTasks(boolean forceUpdate) {
        FirebaseUser firebaseUser = getUser();
        if (firebaseUser == null) {
            return;
        }

        String userId = firebaseUser.getUid();
        tasksDataSource.getTasks(userId, new ITasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<? extends ITask> tasks) {
                if (tasks == null || tasks.isEmpty()) {
                    view.showEmptyView();
                } else {
                    view.showTasks(tasks);
                }
                Timber.d("Received %s tasks", tasks.size());
            }

            @Override
            public void onDataNotAvailable() {
                view.showEmptyView();
                //TODO show error
            }
        });
    }

    @Override
    public void addNewTask() {

    }

    @Override
    public void signOut() {
        firebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient);
        view.goToSignInScreen();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.d("onConnectionFailed: %s", connectionResult);
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
