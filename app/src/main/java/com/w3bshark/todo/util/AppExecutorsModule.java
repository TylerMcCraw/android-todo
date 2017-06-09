package com.w3bshark.todo.util;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Tyler McCraw on 6/9/17.
 * <p/>
 * A Dagger Module which will provide a manager
 * instance of global executor pools
 */

@Module
public class AppExecutorsModule {

    @Provides
    AppExecutors provideAppExecutors() {
        return new AppExecutors();
    }
}
