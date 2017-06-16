package com.w3bshark.todo;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.w3bshark.todo.data.source.DaggerIAuthComponent;
import com.w3bshark.todo.data.source.DaggerISessionComponent;
import com.w3bshark.todo.data.source.IAuthComponent;
import com.w3bshark.todo.data.source.ISessionComponent;
import com.w3bshark.todo.data.source.local.DiskModule;
import com.w3bshark.todo.data.source.local.GsonModule;
import com.w3bshark.todo.data.source.remote.FirebaseAuthModule;
import com.w3bshark.todo.data.source.remote.GoogleApiModule;
import com.w3bshark.todo.data.source.remote.NetworkModule;
import com.w3bshark.todo.util.ApplicationModule;
import com.w3bshark.todo.util.FirebaseCrashReportingTree;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Tyler McCraw on 5/26/17.
 * <p/>
 * Custom application class which handles setup for
 * dependency injection and other global singleton instances
 */

public class MainApplication extends Application {

    private IAuthComponent authComponent;
    private ISessionComponent sessionComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initSingletons();
        initDefaultFont();
        initComponents();
    }

    private void initSingletons() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        Timber.plant(new FirebaseCrashReportingTree());

        LeakCanary.install(this);

        Stetho.initializeWithDefaults(this);
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

    private void initComponents() {
        ApplicationModule applicationModule = new ApplicationModule(this);
        FirebaseAuthModule firebaseAuthModule = new FirebaseAuthModule();
        GoogleApiModule googleApiModule = new GoogleApiModule();

        authComponent = DaggerIAuthComponent.builder()
                .firebaseAuthModule(firebaseAuthModule)
                .googleApiModule(googleApiModule)
                .applicationModule(applicationModule)
                .build();

        sessionComponent = DaggerISessionComponent.builder()
                .firebaseAuthModule(firebaseAuthModule)
                .googleApiModule(googleApiModule)
                .applicationModule(applicationModule)
                .networkModule(new NetworkModule(BuildConfig.HOSTNAME))
                .diskModule(new DiskModule())
                .gsonModule(new GsonModule())
                .build();
    }

    public IAuthComponent getAuthComponent() {
        return authComponent;
    }

    public ISessionComponent getSessionComponent() {
        return sessionComponent;
    }
}
