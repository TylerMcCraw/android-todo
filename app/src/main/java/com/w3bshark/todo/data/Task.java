package com.w3bshark.todo.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Immutable Task model
 */

@IgnoreExtraProperties
public final class Task implements ITask {

    /* We can't make these all `final` due to current limitations of FirebaseDatabase API */
    private String id;
    private String title;
    @Nullable
    private String description;
    private boolean completed;

    public Task() {
        // Default constructor required for calls to DataSnapshot.getValue(Task.class)
    }

    public Task(@NonNull String id, String title, @Nullable String description, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    @Exclude
    @Override
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    @Nullable
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    @Exclude
    public boolean isValid() {
        return !TextUtils.isEmpty(getTitle());
    }

}
