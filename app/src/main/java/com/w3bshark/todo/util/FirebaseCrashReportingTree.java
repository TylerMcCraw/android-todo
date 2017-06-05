package com.w3bshark.todo.util;

import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import timber.log.Timber;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Custom Timber tree to be used for logging
 * specific details about an app crash to the
 * Firebase console
 * <p/>
 * https://firebase.google.com/docs/crash/android
 */

public class FirebaseCrashReportingTree extends Timber.Tree {
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return;
        }

        if (t == null) {
            FirebaseCrash.log(message);
        } else {
            FirebaseCrash.log(t.getMessage());
        }
    }
}