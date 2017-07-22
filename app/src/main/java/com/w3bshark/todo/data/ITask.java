package com.w3bshark.todo.data;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Task interface that exposes only functions
 * that are necessary for the UI to display
 * information about a Task
 */

public interface ITask {

    String getId();

    String getTitle();

    String getDescription();

    boolean isCompleted();
}
