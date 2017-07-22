package com.w3bshark.todo.tasklist;

import android.support.annotation.NonNull;

import com.w3bshark.todo.data.ITask;
import com.w3bshark.todo.util.IBasePresenter;
import com.w3bshark.todo.util.IBaseView;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Contract between View and Presenter for Task List screen
 */

public interface ITaskListContract {

    interface View extends IBaseView<Presenter> {

        void goToSignInScreen();

        void showTasksView();

        void goToAddTaskScreen();

        void goToEditTaskScreen(String taskId);

        void showLoadingTasksError();

        void showEmptyView();

        void addTask(int position, ITask task);

        void updateTask(int position, ITask task);

        void removeTask(int position, ITask task);

        void moveTask(int newPosition, ITask task);
    }

    interface Presenter extends IBasePresenter {

        void addNewTask();

        void openEditTask(@NonNull ITask requestedTask);

        void completeTask(@NonNull ITask task);

        void activateTask(@NonNull ITask task);

        void signOut();
    }

}
