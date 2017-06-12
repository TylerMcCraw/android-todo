package com.w3bshark.todo.signin;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.w3bshark.todo.R;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Listens to user actions from the UI ({@link SignInActivity}),
 * retrieves the data and updates the UI as required.
 */

final class SignInPresenter implements ISignInContract.Presenter, GoogleApiClient.OnConnectionFailedListener {

    private final FirebaseAuth firebaseAuth;
    private final GoogleApiClient googleApiClient;
    private final ISignInContract.View view;

    @Inject
    SignInPresenter(FirebaseAuth firebaseAuth,
                    GoogleApiClient googleApiClient,
                    ISignInContract.View view) {
        this.firebaseAuth = firebaseAuth;
        this.googleApiClient = googleApiClient;
        this.view = view;
    }

    /**
     * Method injection is used here to safely reference {@code this} after the object is created.
     */
    @Inject
    void setupListeners() {
        view.setPresenter(this);
    }

    @Override
    public void start() {
        googleApiClient.registerConnectionFailedListener(this);
        googleApiClient.connect();
    }

    @Override
    public void stop() {
        googleApiClient.unregisterConnectionFailedListener(this);
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void signInWithGoogle() {
        view.showGoogleSignInScreen(Auth.GoogleSignInApi.getSignInIntent(googleApiClient));
    }

    @Override
    public void handleSignInResult(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);
        } else {
            Timber.e("Google Sign In failed.");
            view.showSignInError(R.string.google_sign_in_failed);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Timber.d("signInWithCredential:onComplete: %s", task.getException());
                        view.showSignInError(R.string.auth_has_failed);
                    } else {
                        view.goToTaskListScreen();
                    }
                })
                .addOnFailureListener(e -> {
                    Timber.e(e, "signInWithCredential:onFailure:");
                    view.showSignInError(R.string.auth_has_failed);
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.d("onConnectionFailed: %s", connectionResult);
        view.showSignInError(R.string.google_play_services_error);
    }
}
