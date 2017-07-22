package com.w3bshark.todo.data.source;

import android.support.annotation.NonNull;
import android.util.ArrayMap;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.w3bshark.todo.data.Task;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Created by Tyler McCraw on 5/27/17.
 * <p/>
 * Singleton Repository which manages
 * all local and remote sources of Task data
 * <p/>
 * See Repository pattern: @link https://msdn.microsoft.com/en-us/library/ff649690.aspx
 */
@Singleton
public class TasksRepository implements ITasksDataSource {

    private final FirebaseDatabase database;
    private DatabaseReference dbRef;

    private Map<Object, ChildEventListener> childEventListenerMap = new ArrayMap<>();

    /**
     * By marking the constructor with {@code @Inject}, Dagger will try to inject the dependencies
     * required to create an instance of the TasksRepository.
     */
    @Inject
    TasksRepository(FirebaseDatabase firebaseDatabase) {
        this.database = firebaseDatabase;
    }

    private DatabaseReference getDbRef(@NonNull String userId) {
        if (dbRef == null) {
            dbRef = database.getReference("v1").child("users").child(userId).child("tasks");
        }
        return dbRef;
    }

    @Override
    public void listenForTasks(@NonNull String userId, @NonNull LoadTasksCallback callback, @NonNull Object tag) {
        // Don't add another listener if there's already one set for this tag
        if (childEventListenerMap.containsKey(tag)) {
            return;
        }

        final ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                final Task task = checkTaskIsValid(dataSnapshot);
                if (task != null) {
                    callback.onTaskAdded(task, previousChildName);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                final Task task = checkTaskIsValid(dataSnapshot);
                if (task != null) {
                    callback.onTaskChanged(task, previousChildName);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                final Task task = checkTaskIsValid(dataSnapshot);
                if (task != null) {
                    callback.onTaskRemoved(task);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                final Task task = checkTaskIsValid(dataSnapshot);
                if (task != null) {
                    callback.onTaskMoved(task, previousChildName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.e("%s\n\n%s\n\n%s", databaseError.getCode(), databaseError.getMessage(), databaseError.getDetails());
                callback.onDataNotAvailable();
            }
        };
        childEventListenerMap.put(tag, childEventListener);
        getDbRef(userId).addChildEventListener(childEventListener);
    }

    private static Task checkTaskIsValid(DataSnapshot dataSnapshot) {
        if (!dataSnapshot.exists()) {
            return null;
        } else {
            final Task task = dataSnapshot.getValue(Task.class);
            if (task != null) {
                task.setId(dataSnapshot.getKey());
            }
            return task;
        }
    }

    @Override
    public void getTask(@NonNull String userId, @NonNull String taskId, @NonNull LoadTaskCallback callback) {
        getDbRef(userId).child(taskId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Task task = checkTaskIsValid(dataSnapshot);
                if (task != null) {
                    callback.onTaskLoaded(task);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.e("%s\n\n%s\n\n%s", databaseError.getCode(), databaseError.getMessage(), databaseError.getDetails());
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void createTask(@NonNull String userId, Task newTask) {
        final String newTaskId = getDbRef(userId).push().getKey();
        newTask.setId(newTaskId);
        getDbRef(userId).child(newTaskId).setValue(newTask);
        saveTaskWithCallback(userId, newTask);
    }

    @Override
    public void saveTask(@NonNull String userId, @NonNull Task task) {
        saveTaskWithCallback(userId, task);
    }

    private void saveTaskWithCallback(@NonNull String userId, @NonNull Task task) {
        getDbRef(userId).child(task.getId()).setValue(task);
    }

    @Override
    public void refreshTasks() {

    }

    @Override
    public void deleteTask(@NonNull String userId, @NonNull String taskId) {
        getDbRef(userId).child(taskId).removeValue();
    }

    @Override
    public void stopListeners(@NonNull String userId, @NonNull Object tag) {
        if (childEventListenerMap.containsKey(tag)) {
            ChildEventListener childEventListener = childEventListenerMap.get(tag);
            getDbRef(userId).removeEventListener(childEventListener);
            childEventListenerMap.remove(tag);
        }
    }
}