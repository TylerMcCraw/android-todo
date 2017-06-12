package com.w3bshark.todo.addedittask;

import com.w3bshark.todo.util.IBasePresenter;
import com.w3bshark.todo.util.IBaseView;

/**
 * Created by Tyler McCraw on 6/10/17.
 * <p/>
 * Contract between View and Presenter for Add/Edit Task screen
 */

public interface IAddEditTaskContract {

    interface View extends IBaseView<Presenter> {

        void goToSignInScreen();

        void showTaskError();

        void goBackToTaskList();

        void setTitle(String title);

        void setDescription(String description);
    }

    interface Presenter extends IBasePresenter {

        void saveTask(String title, String description);

        void loadTask();

        void deleteTask();
    }
}
