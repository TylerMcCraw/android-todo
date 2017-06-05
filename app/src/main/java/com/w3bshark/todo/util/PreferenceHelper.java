package com.w3bshark.todo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import javax.inject.Inject;

/**
 * Created by Tyler McCraw on 6/1/17.
 * <p/>
 * Utility class which can be extended for
 * easily persisting custom model objects as
 * Json in the application's Shared Preferences
 */
public class PreferenceHelper<T> {

    private static SharedPreferences preferences;
    private final Gson gson;

    /**
     * Constructs an application scoped shared preferences
     *
     * @param context - application context
     */
    @Inject
    public PreferenceHelper(@ApplicationContext Context context, Gson gson) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.gson = gson;
    }

    /**
     * Converts the provided object to the json string and stores it
     * in the application's local shared preferences
     *
     * @param key    - key to associate with this object
     * @param object - object to persist
     */
    public T persistObject(String key, T object) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, gson.toJson(object));
        editor.apply();
        return object;
    }

    /**
     * Removes the value mapping at the provided key
     *
     * @param key - key of the preference to remove
     */
    public void removeObjectForKey(String key) {
        preferences.edit().remove(key).apply();
    }

    /**
     * Return a deserialized json string from shared preferences as the
     * specified object type
     *
     * @param key   - key of the preference to retrieve
     * @param clazz - class of the returned object type
     * @return object associated with the key, or null if an error occurred
     */
    public T getObject(String key, Class<T> clazz) {
        T t = null;
        try {
            String objectAsJson = preferences.getString(key, null);
            t = gson.fromJson(objectAsJson, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}