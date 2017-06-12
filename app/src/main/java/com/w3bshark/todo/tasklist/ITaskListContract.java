package com.w3bshark.todo.tasklist;

import android.support.annotation.NonNull;

import com.w3bshark.todo.data.ITask;
import com.w3bshark.todo.util.IBasePresenter;
import com.w3bshark.todo.util.IBaseView;

import java.util.List;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Contract between View and Presenter for Task List screen
 */

public interface ITaskListContract {

    interface View extends IBaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void goToSignInScreen();

        void showTasks(List<? extends ITask> tasks);

        void goToAddTaskScreen();

        void goToEditTaskScreen(String taskId);

        void showLoadingTasksError();

        void showEmptyView();
    }

    interface Presenter extends IBasePresenter {

        void loadTasks(boolean forceUpdate);

        void addNewTask();

        void openEditTask(@NonNull ITask requestedTask);

        void completeTask(@NonNull ITask task);

        void activateTask(@NonNull ITask task);

        void signOut();
    }

}
