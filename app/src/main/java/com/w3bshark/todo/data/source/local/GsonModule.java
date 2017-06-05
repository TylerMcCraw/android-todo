package com.w3bshark.todo.data.source.local;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * A Dagger Module which will provide a
 * singleton Gson instance for serialization
 * and deserialization of Json
 */

@Module
public class GsonModule {

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().create();
    }
}
