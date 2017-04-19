package com.w3bshark.todo;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initSingletons();
        initDefaultFont();
    }

    private void initSingletons() {

    }

    /**
     * This will initialize caching of our most-often used font
     * via Calligraphy (https://github.com/chrisjenx/Calligraphy)
     */
    private void initDefaultFont() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
