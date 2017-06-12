package com.w3bshark.todo.addedittask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.w3bshark.todo.MainApplication;
import com.w3bshark.todo.R;
import com.w3bshark.todo.signin.SignInActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

/**
 * Created by Tyler McCraw on 6/10/17.
 * <p/>
 * Displays a screen which allows the user
 * enter task info and either create a new task
 * or update an existing task
 */

public class AddEditTaskActivity extends AppCompatActivity implements IAddEditTaskContract.View {

    private static final String EXTRA_TASK_ID = "com.w3bshark.todo.addedittask.AddEditTaskActivity.EXTRA_TASK_ID";

    @Inject
    AddEditTaskPresenter presenter;

    @State
    String taskId;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.task_title_input_layout)
    TextInputLayout titleInputLayout;
    @BindView(R.id.task_title)
    TextInputEditText titleEditText;
    @BindView(R.id.task_description)
    TextInputEditText descriptionEditText;

    public static void start(Context context, String taskId) {
        Intent starter = new Intent(context, AddEditTaskActivity.class);
        starter.putExtra(EXTRA_TASK_ID, taskId);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        } else if (getIntent() != null) {
            taskId = getIntent().getStringExtra(EXTRA_TASK_ID);
        }

        injectTaskListPresenter();
    }

    private void injectTaskListPresenter() {
        DaggerIAddEditTaskComponent.builder()
                .addEditTaskPresenterModule(new AddEditTaskPresenterModule(this, taskId))
                .iSessionComponent(((MainApplication) getApplication()).getSessionComponent())
                .build()
                .inject(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
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
        getMenuInflater().inflate(R.menu.menu_add_edit_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                presenter.deleteTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setPresenter(IAddEditTaskContract.Presenter presenter) {
        this.presenter = (AddEditTaskPresenter) presenter;
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        titleInputLayout.setError(null);
        presenter.saveTask(titleEditText.getText().toString(), descriptionEditText.getText().toString());
    }

    @Override
    public void goToSignInScreen() {
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    @Override
    public void showTaskError() {
        titleInputLayout.setError(getString(R.string.error_title_is_required));
    }

    @Override
    public void goBackToTaskList() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void setTitle(String title) {
        titleEditText.setText(title);
    }

    @Override
    public void setDescription(String description) {
        descriptionEditText.setText(description);
    }
}
