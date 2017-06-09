package com.w3bshark.todo.tasklist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.w3bshark.todo.data.ITask;
import com.w3bshark.todo.signin.SignInActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Displays a list of tasks, provides a button
 * for creating new tasks, and lets the user edit tasks
 */

public class TaskListActivity extends AppCompatActivity implements ITaskListContract.View {

    @Inject
    TaskListPresenter presenter;

    private TaskListAdapter adapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tasks_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.empty_message)
    TextView emptyMessageTextView;

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
        adapter = new TaskListAdapter(new ArrayList<>());
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

    @Override
    public void goToSignInScreen() {
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    @Override
    public void goToAddNewTaskScreen(View viewClicked) {

    }

    @Override
    public void showTasks(List<? extends ITask> tasks) {
        recyclerView.setVisibility(View.VISIBLE);
        emptyMessageTextView.setVisibility(View.GONE);
        adapter.setTasks(tasks);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showEmptyView() {

    }
}
