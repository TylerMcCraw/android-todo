package com.w3bshark.todo.data.source.remote;

import android.content.Context;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.w3bshark.todo.R;
import com.w3bshark.todo.util.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * A Dagger Module which will provide singletons
 * for Google Sign-In authentication
 */

@Module
public class GoogleApiModule {

    @Provides
    @Singleton
    GoogleApiClient provideGoogleApiClient(@ApplicationContext Context context) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        return new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
}
