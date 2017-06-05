package com.w3bshark.todo;

import timber.log.Timber;

/**
 * Created by Tyler McCraw on 4/19/17.
 * <p/>
 * Custom application class which extends our
 * custom MainApplication class and provides
 * debug-specific setup for logging, etc.
 */

public class MainDebugApplication extends MainApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
