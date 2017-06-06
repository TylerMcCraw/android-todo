package com.w3bshark.todo.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.w3bshark.todo.MainApplication;
import com.w3bshark.todo.R;
import com.w3bshark.todo.tasklist.TaskListActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by Tyler McCraw on 4/19/17.
 * <p/>
 * Sign In activity which currently only
 * provides authentication via Google Sign-in
 */

public class SignInActivity extends AppCompatActivity implements ISignInContract.View {

    private static final int RC_GOOGLE_SIGN_IN = 9001;

    @Inject
    SignInPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        injectSignInPresenter();

        SignInButton signInButton = ButterKnife.findById(this, R.id.sign_in_button);
        signInButton.setOnClickListener(v -> presenter.signInWithGoogle());
        signInButton.setStyle(SignInButton.SIZE_WIDE, SignInButton.COLOR_AUTO);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            presenter.handleSignInResult(data);
        }
    }

    private void injectSignInPresenter() {
        DaggerISignInComponent.builder()
                .signInPresenterModule(new SignInPresenterModule(this))
                .iAuthComponent(((MainApplication) getApplication()).getAuthComponent())
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(ISignInContract.Presenter presenter) {
        this.presenter = (SignInPresenter) presenter;
    }

    @Override
    public void showGoogleSignInScreen(Intent googleSignInIntent) {
        startActivityForResult(googleSignInIntent, RC_GOOGLE_SIGN_IN);
    }

    @Override
    public void goToTaskListScreen() {
        startActivity(new Intent(SignInActivity.this, TaskListActivity.class));
        finish();
    }

    @Override
    public void showSignInError(@StringRes int errorMsgRes) {
        Toast.makeText(this, errorMsgRes, Toast.LENGTH_SHORT).show();
    }

}
