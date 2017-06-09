package com.w3bshark.todo.tasklist;

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
        void goToSignInScreen();

        void goToAddNewTaskScreen(android.view.View viewClicked);

        void showTasks(List<? extends ITask> tasks);

        void showEmptyView();
    }

    interface Presenter extends IBasePresenter {
        void loadTasks(boolean forceUpdate);

        void addNewTask();

        void signOut();
    }

}
