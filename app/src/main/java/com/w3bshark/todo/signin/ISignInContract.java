package com.w3bshark.todo.signin;

import android.content.Intent;
import android.support.annotation.StringRes;

import com.w3bshark.todo.util.IBasePresenter;
import com.w3bshark.todo.util.IBaseView;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Contract between View and Presenter for Sign In screen
 */

public interface ISignInContract {

    interface View extends IBaseView<Presenter> {
        void goToTaskListScreen();

        void showGoogleSignInScreen(Intent googleSignInIntent);

        void showSignInError(@StringRes int errorMsgRes);
    }

    interface Presenter extends IBasePresenter {

        void signInWithGoogle();

        void handleSignInResult(Intent data);
    }
}
