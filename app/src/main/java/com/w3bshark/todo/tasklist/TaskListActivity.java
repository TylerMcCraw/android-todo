package com.w3bshark.todo.tasklist;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.w3bshark.todo.MainApplication;
import com.w3bshark.todo.R;
import com.w3bshark.todo.addedittask.AddEditTaskActivity;
import com.w3bshark.todo.data.ITask;
import com.w3bshark.todo.signin.SignInActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Displays a list of tasks, provides a button
 * for creating new tasks, and lets the user edit tasks
 */

public class TaskListActivity extends AppCompatActivity implements ITaskListContract.View, TaskListAdapter.TaskListListener {

    @Inject
    TaskListPresenter presenter;

    private TaskListAdapter adapter;

    private Menu menu;
    @BindView(R.id.root_view)
    CoordinatorLayout rootView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tasks_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.empty_message)
    TextView emptyMessageTextView;
    @BindDrawable(R.drawable.ic_sync_white_24dp)
    Drawable syncNormalDrawable;
    @BindDrawable(R.drawable.ic_sync_problem_white_24dp)
    Drawable syncErrorDrawable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        setUpRecyclerView();
        injectTaskListPresenter();
    }

    private void setUpRecyclerView() {
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.loadTasks(true));

        adapter = new TaskListAdapter(new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    private void injectTaskListPresenter() {
        DaggerITaskListComponent.builder()
                .taskListPresenterModule(new TaskListPresenterModule(this))
                .iSessionComponent(((MainApplication) getApplication()).getSessionComponent())
                .build()
                .inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    protected void onStop() {
        presenter.stop();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sync:
                swipeRefreshLayout.setRefreshing(true);
                presenter.loadTasks(true);
                return true;
            case R.id.sign_out_menu:
                presenter.signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setPresenter(ITaskListContract.Presenter presenter) {
        this.presenter = (TaskListPresenter) presenter;
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        presenter.addNewTask();
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(active));
    }

    @Override
    public void goToSignInScreen() {
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    @Override
    public void onTaskClicked(ITask task, View viewClicked) {
        presenter.openEditTask(task);
    }

    @Override
    public void onCompleteTaskClick(ITask task, View viewClicked) {
        presenter.completeTask(task);
    }

    @Override
    public void onActivateTaskClick(ITask task, View viewClicked) {
        presenter.activateTask(task);
    }

    @Override
    public void showTasks(List<? extends ITask> tasks) {
        setSyncMenuDrawable(false);
        emptyMessageTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        adapter.setTasks(tasks);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showEmptyView() {
        setSyncMenuDrawable(false);
        recyclerView.setVisibility(View.GONE);
        emptyMessageTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void goToAddTaskScreen() {
        AddEditTaskActivity.start(this, null);
    }

    @Override
    public void goToEditTaskScreen(String taskId) {
        AddEditTaskActivity.start(this, taskId);
    }

    @Override
    public void showLoadingTasksError() {
        setSyncMenuDrawable(true);
        showMessage(getString(R.string.loading_tasks_error));
    }

    private void setSyncMenuDrawable(boolean error) {
        if (menu != null) {
            menu.findItem(R.id.sync).setIcon(error ? syncErrorDrawable: syncNormalDrawable);
        }
    }

    private void showMessage(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }
}
