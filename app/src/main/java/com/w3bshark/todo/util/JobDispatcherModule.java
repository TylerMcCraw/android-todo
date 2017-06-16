package com.w3bshark.todo.util;

import android.content.Context;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Tyler McCraw on 6/11/17.
 */

@Module
public class JobDispatcherModule {

    @Provides
    @Singleton
    FirebaseJobDispatcher provideFirebaseJobDispatcher(@ApplicationContext Context context) {
        return new FirebaseJobDispatcher(new GooglePlayDriver(context));
    }
}
