package com.w3bshark.todo;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.w3bshark.todo.data.source.DaggerIAuthComponent;
import com.w3bshark.todo.data.source.DaggerIBackgroundJobComponent;
import com.w3bshark.todo.data.source.DaggerISessionComponent;
import com.w3bshark.todo.data.source.IAuthComponent;
import com.w3bshark.todo.data.source.IBackgroundJobComponent;
import com.w3bshark.todo.data.source.ISessionComponent;
import com.w3bshark.todo.data.source.local.DiskModule;
import com.w3bshark.todo.data.source.local.GsonModule;
import com.w3bshark.todo.data.source.remote.FirebaseModule;
import com.w3bshark.todo.data.source.remote.GoogleApiModule;
import com.w3bshark.todo.data.source.remote.NetworkModule;
import com.w3bshark.todo.util.AppExecutorsModule;
import com.w3bshark.todo.util.ApplicationModule;
import com.w3bshark.todo.util.FirebaseCrashReportingTree;
import com.w3bshark.todo.util.JobDispatcherModule;

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
    private IBackgroundJobComponent backgroundJobComponent;

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
        FirebaseModule firebaseModule = new FirebaseModule();
        GoogleApiModule googleApiModule = new GoogleApiModule();
        NetworkModule networkModule = new NetworkModule(BuildConfig.HOSTNAME);
        DiskModule diskModule = new DiskModule();
        GsonModule gsonModule = new GsonModule();

        authComponent = DaggerIAuthComponent.builder()
                .firebaseModule(firebaseModule)
                .googleApiModule(googleApiModule)
                .applicationModule(applicationModule)
                .build();

        sessionComponent = DaggerISessionComponent.builder()
                .firebaseModule(firebaseModule)
                .googleApiModule(googleApiModule)
                .applicationModule(applicationModule)
                .networkModule(networkModule)
                .diskModule(diskModule)
                .gsonModule(gsonModule)
                .appExecutorsModule(new AppExecutorsModule())
                .jobDispatcherModule(new JobDispatcherModule())
                .build();

        backgroundJobComponent = DaggerIBackgroundJobComponent.builder()
                .applicationModule(applicationModule)
                .networkModule(networkModule)
                .diskModule(diskModule)
                .gsonModule(gsonModule)
                .build();
    }

    public IAuthComponent getAuthComponent() {
        return authComponent;
    }

    public ISessionComponent getSessionComponent() {
        return sessionComponent;
    }

    public IBackgroundJobComponent getBackgroundJobComponent() {
        return backgroundJobComponent;
    }
}
