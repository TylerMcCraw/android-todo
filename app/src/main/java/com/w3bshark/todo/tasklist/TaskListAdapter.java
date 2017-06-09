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

    private List<? extends ITask> tasks;

    TaskListAdapter(List<ITask> tasks) {
        this.tasks = tasks;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(TaskViewHolder taskViewHolder, int i) {
        taskViewHolder.bindTask(tasks.get(i));
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.task_list_item;
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<? extends ITask> tasks) {
        this.tasks = tasks;
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
