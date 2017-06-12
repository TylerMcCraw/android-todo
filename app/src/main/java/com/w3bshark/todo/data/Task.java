package com.w3bshark.todo.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.w3bshark.todo.util.FireBasePushIdGenerator;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Note: it would be great if we could
 */

@Entity(tableName = Task.TABLE_NAME, indices = {@Index("id"), @Index("user")}, primaryKeys = {"id"})
public final class Task implements ITask {

    public static final String TABLE_NAME = "task";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER = "user";
    public static final String COLUMN_COMPLETED = "completed";

    /* We can't make these all `final` or `private` due to current limitations of Room API */
    @SerializedName(COLUMN_ID)
    public String id;

    @SerializedName(COLUMN_USER)
    public final String user;

    @SerializedName("title")
    public final String title;

    @SerializedName("description")
    @Nullable
    public final String description;

    @SerializedName(COLUMN_COMPLETED)
    public final boolean completed;

    public Task(@NonNull String id, @NonNull String user, String title, @Nullable String description, boolean completed) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    /**
     * Constructor to create new Tasks with a auto-generated ID
     */
    @Ignore
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
    @Nullable
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(getTitle());
    }

    public String getUser() {
        return user;
    }
}
