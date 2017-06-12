package com.w3bshark.todo.addedittask;

import android.support.annotation.Nullable;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Tyler McCraw on 6/10/17.
 * <p/>
 * Dagger Module for Add/Edit Task screen
 * We use this to pass in the View dependency to the {@link AddEditTaskPresenter}.
 */

@Module
public class AddEditTaskPresenterModule {

    private final IAddEditTaskContract.View view;
    private String taskId;

    public AddEditTaskPresenterModule(IAddEditTaskContract.View view, @Nullable @Named("taskId") String taskId) {
        this.view = view;
        this.taskId = taskId;
    }

    @Provides
    IAddEditTaskContract.View provideAddEditTaskContractView() {
        return view;
    }

    @Named("taskId")
    @Provides
    @Nullable
    String provideTaskId() {
        return taskId;
    }
}
