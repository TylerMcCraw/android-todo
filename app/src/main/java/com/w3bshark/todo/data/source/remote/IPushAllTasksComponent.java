package com.w3bshark.todo.data.source.remote;

import com.w3bshark.todo.data.source.IBackgroundJobComponent;
import com.w3bshark.todo.data.source.ISessionComponent;
import com.w3bshark.todo.util.ActivityScoped;

import dagger.Component;

/**
 * Created by Tyler McCraw on 6/12/17.
 * <p/>
 * Dagger component for PushAllTasksJobService
 * <p/>
 * Because this component depends on the {@link ISessionComponent},
 * which is a singleton, a scope must be specified. All activity/fragment components use a custom scope for this purpose.
 */

@ActivityScoped
@Component(dependencies = IBackgroundJobComponent.class)
public interface IPushAllTasksComponent {

    void inject(PushAllTasksJobService pushAllTasksJobService);
}