package com.w3bshark.todo.util;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Tyler McCraw on 3/7/17.
 * <p/>
 * A Dagger Module which will provide global application
 * context which may be used anywhere within our app.
 */

@Module
public final class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }
}