package com.w3bshark.todo.tasklist;

import com.w3bshark.todo.data.source.IAuthComponent;
import com.w3bshark.todo.data.source.ISessionComponent;
import com.w3bshark.todo.util.ActivityScoped;

import dagger.Component;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Dagger component for Task List screen.
 * <p/>
 * Because this component depends on the {@link IAuthComponent},
 * which is a singleton, a scope must be specified. All activity/fragment components use a custom scope for this purpose.
 */

@ActivityScoped
@Component(dependencies = ISessionComponent.class, modules = TaskListPresenterModule.class)
public interface ITaskListComponent {

    void inject(TaskListActivity taskListActivity);
}
