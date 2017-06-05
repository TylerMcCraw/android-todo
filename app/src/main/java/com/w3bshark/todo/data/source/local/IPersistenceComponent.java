package com.w3bshark.todo.data.source.local;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Component which defines a dependency graph
 * for all persistence-related dependencies
 */

@Singleton
@Component(modules = {GsonModule.class})
public interface IPersistenceComponent {

    Gson gson();
}
