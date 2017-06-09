package com.w3bshark.todo.tasklist;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Dagger Module for Task List screen
 * We use this to pass in the View dependency to the {@link TaskListPresenter}.
 */

@Module
public class TaskListPresenterModule {

    private final ITaskListContract.View view;

    public TaskListPresenterModule(ITaskListContract.View view) {
        this.view = view;
    }

    @Provides
    ITaskListContract.View provideTaskListContractView() {
        return view;
    }
}
