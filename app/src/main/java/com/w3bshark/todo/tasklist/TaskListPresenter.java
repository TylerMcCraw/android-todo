package com.w3bshark.todo.tasklist;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.w3bshark.todo.data.ITask;
import com.w3bshark.todo.data.Task;
import com.w3bshark.todo.data.source.ITasksDataSource;
import com.w3bshark.todo.data.source.TasksRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Listens to user actions from the UI ({@link TaskListActivity}),
 * retrieves the data and updates the UI as required.
 */

final class TaskListPresenter implements ITaskListContract.Presenter, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "com.w3bshark.todo.tasklist.TaskListPresenter";

    private final FirebaseAuth firebaseAuth;
    private final GoogleApiClient googleApiClient;
    private final ITasksDataSource tasksRepository;
    private final ITaskListContract.View view;

    private Map<String, ITask> cachedTasks;
    private List<String> taskKeys; // used to keep track of task order

    @Inject
    TaskListPresenter(FirebaseAuth firebaseAuth,
                      GoogleApiClient googleApiClient,
                      TasksRepository tasksRepository,
                      ITaskListContract.View view) {
        this.firebaseAuth = firebaseAuth;
        this.googleApiClient = googleApiClient;
        this.tasksRepository = tasksRepository;
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

        loadTasks();
    }

    @Override
    public void stop() {
        googleApiClient.unregisterConnectionFailedListener(this);
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }

        FirebaseUser firebaseUser = getUser();
        if (firebaseUser == null) {
            return;
        }
    }

    private void loadTasks() {
        FirebaseUser firebaseUser = getUser();
        if (firebaseUser == null) {
            return;
        }

        String userId = firebaseUser.getUid();
        tasksRepository.listenForTasks(userId, new ITasksDataSource.LoadTasksCallback() {
            @Override
            public void onTaskAdded(ITask task, String previousTaskKey) {
                // If user is adding the first task, we must show the tasks view
                if (getCachedTasks().size() == 0) {
                    view.showTasksView();
                }

                int posPrevTask = getTaskKeys().indexOf(previousTaskKey);
                posPrevTask = posPrevTask < 0 ? 0 : posPrevTask;
                if (!getCachedTasks().containsKey(task.getId())) {
                    if (previousTaskKey == null) {
                        view.addTask(0, task);
                        getTaskKeys().add(0, task.getId());
                    } else {
                        view.addTask(posPrevTask + 1, task);
                        getTaskKeys().add(posPrevTask + 1, task.getId());
                    }
                } else {
                    if (previousTaskKey == null) {
                        view.updateTask(0, task);
                    } else {
                        view.updateTask(posPrevTask + 1, task);
                    }
                }

                getCachedTasks().put(task.getId(), task);
            }

            @Override
            public void onTaskChanged(ITask task, String previousTaskKey) {
                final int currentPosition = getTaskKeys().indexOf(task.getId());
                getCachedTasks().put(task.getId(), task);
                view.updateTask(currentPosition, task);
            }

            @Override
            public void onTaskRemoved(ITask task) {
                final int currentPosition = getTaskKeys().indexOf(task.getId());
                view.removeTask(currentPosition, getCachedTasks().remove(task.getId()));
                getTaskKeys().remove(task.getId());
                // If user is removing the last task, we must show the empty view
                if (getCachedTasks().size() == 0) {
                    view.showEmptyView();
                }
            }

            @Override
            public void onTaskMoved(ITask task, String previousTaskKey) {
                int newPosition = getTaskKeys().indexOf(previousTaskKey);
                newPosition = newPosition < 0 ? 0 : newPosition;
                getTaskKeys().remove(task.getId());
                getTaskKeys().add(newPosition, task.getId());
                view.moveTask(newPosition, task);
            }

            @Override
            public void onDataNotAvailable() {
                view.showLoadingTasksError();
            }
        }, TAG);
    }

    @Override
    public void addNewTask() {
        view.goToAddTaskScreen();
    }

    @Override
    public void openEditTask(@NonNull ITask requestedTask) {
        view.goToEditTaskScreen(requestedTask.getId());
    }

    @Override
    public void completeTask(@NonNull ITask task) {
        FirebaseUser user = getUser();
        if (user == null) {
            return;
        }

        tasksRepository.saveTask(user.getUid(), new Task(task.getId(), task.getTitle(), task.getDescription(), true));
    }

    @Override
    public void activateTask(@NonNull ITask task) {
        FirebaseUser user = getUser();
        if (user == null) {
            return;
        }

        tasksRepository.saveTask(user.getUid(), new Task(task.getId(), task.getTitle(), task.getDescription(), false));
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

    private Map<String, ITask> getCachedTasks() {
        if (cachedTasks == null) {
            cachedTasks = new HashMap<>();
        }
        return cachedTasks;
    }

    private List<String> getTaskKeys() {
        if (taskKeys == null) {
            taskKeys = new ArrayList<>();
        }
        return taskKeys;
    }
}
