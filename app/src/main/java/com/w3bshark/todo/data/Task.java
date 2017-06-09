package com.w3bshark.todo.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.w3bshark.todo.util.FireBasePushIdGenerator;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Note: it would be great if we could
 */

@Entity(indices = {@Index("id"), @Index("user")}, primaryKeys = {"id"})
public final class Task implements ITask {

    /* We can't make these all `final` or `private` due to current limitations of Room API */
    @SerializedName("id")
    public String id;

    @SerializedName("user")
    public final String user;

    @SerializedName("title")
    public final String title;

    @SerializedName("description")
    @Nullable
    public final String description;

    @SerializedName("completed")
    public final boolean completed;

    public Task(@NonNull String user, String title, @Nullable String description, boolean completed) {
        this.id = FireBasePushIdGenerator.generatePushId();
        this.user = user;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    @Override
    public String getId() {
        return id;
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
