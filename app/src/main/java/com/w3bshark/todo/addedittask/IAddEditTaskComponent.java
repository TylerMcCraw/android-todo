package com.w3bshark.todo.addedittask;

import com.w3bshark.todo.data.source.ISessionComponent;
import com.w3bshark.todo.util.ActivityScoped;

import dagger.Component;

/**
 * Created by Tyler McCraw on 6/10/17.
 * <p/>
 * Dagger component for Add/Edit Task screen.
 * <p/>
 * Because this component depends on the {@link ISessionComponent},
 * which is a singleton, a scope must be specified. All activity/fragment components use a custom scope for this purpose.
 */

@ActivityScoped
@Component(dependencies = ISessionComponent.class, modules = AddEditTaskPresenterModule.class)
public interface IAddEditTaskComponent {

    void inject(AddEditTaskActivity addEditTaskActivity);
}
