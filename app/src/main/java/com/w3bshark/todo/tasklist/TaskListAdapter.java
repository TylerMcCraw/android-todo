package com.w3bshark.todo.tasklist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.w3bshark.todo.R;
import com.w3bshark.todo.data.ITask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tyler McCraw on 6/4/17.
 * <p/>
 * Recycler Adapter for a list of Tasks
 */

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {

    private List<ITask> tasks;
    private final TaskListListener listener;

    interface TaskListListener {
        void onTaskClicked(ITask task, View viewClicked);
        void onCompleteTaskClick(ITask task, View viewClicked);
        void onActivateTaskClick(ITask task, View viewClicked);
    }

    TaskListAdapter(List<ITask> tasks, TaskListListener listener) {
        this.tasks = tasks;
        this.listener = listener;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(TaskViewHolder taskViewHolder, int i) {
        taskViewHolder.bindTask(tasks.get(taskViewHolder.getAdapterPosition()));
        taskViewHolder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTaskClicked(tasks.get(taskViewHolder.getAdapterPosition()), v);
            }
        });
        taskViewHolder.completeCheckBox.setOnClickListener(v -> {
            if (listener != null) {
                final ITask task = tasks.get(taskViewHolder.getAdapterPosition());
                if (task.isCompleted()) {
                    listener.onActivateTaskClick(task, v);
                } else {
                    listener.onCompleteTaskClick(task, v);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.task_list_item;
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    void addTask(int position, ITask task) {
        tasks.add(position, task);
        notifyItemInserted(position);
    }

    void removeTask(int position, ITask task) {
        tasks.remove(task);
        notifyItemRemoved(position);
    }

    void updateTask(int position, ITask task) {
        tasks.set(position, task);
        notifyItemChanged(position);
    }

    void moveTask(int newPosition, ITask task) {
        final int currentPosition = tasks.indexOf(task);
        notifyItemMoved(currentPosition, newPosition);
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.task_text_view_title)
        TextView titleTextView;
        @BindView(R.id.task_checkbox_complete)
        CheckBox completeCheckBox;

        TaskViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindTask(ITask task) {
            titleTextView.setText(task.getTitle());
            completeCheckBox.setChecked(task.isCompleted());
        }
    }
}
