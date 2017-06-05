package com.w3bshark.todo.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Immutable Task data model
 */

public final class Task implements ITask {

    private final String user;

    private final String title;

    @Nullable
    private final String description;

    private final boolean completed;

    public Task(@NonNull String user, String title, @Nullable String description, boolean completed) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }
}
