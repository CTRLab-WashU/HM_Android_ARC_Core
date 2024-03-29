package com.healthymedium.arc.core;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import androidx.annotation.Nullable;

import android.util.Log;

import com.healthymedium.arc.notifications.NotificationTypes;
import com.healthymedium.arc.notifications.types.NotificationType;
import com.healthymedium.arc.study.Study;

import com.healthymedium.arc.utilities.CacheManager;
import com.healthymedium.arc.utilities.PreferencesManager;
import com.healthymedium.arc.utilities.VersionUtil;

import net.danlew.android.joda.JodaTimeAndroid;
import java.util.ArrayList;
import java.util.List;

/**
 * This class was originally designed to be a sub-class of Android Application class
 * However, due to the global nature of it's use through the library,
 * It is better to push it to it's own class that is owned by the actual Android Application class
 */
public class Application implements LifecycleObserver {

    public interface StudyComponentProvider {
        public void registerStudyComponents();
    }

    private static final String tag = "Application";
    public static final String TAG_RESTART = "TAG_APPLICATION_RESTARTING";

    public static Application getInstance() {
        return instance;
    }

    public Context getAppContext() {
        return context;
    }

    public Resources getResources() {
        return context.getResources();
    }

    public ContentResolver getContentResolver() {
        return context.getContentResolver();
    }

    static Application instance;

    private Context context;  // is the app context\

    private List<NotificationType> notificationTypes;
    private List<Locale> localeOptions;

    private boolean checkContext() {
        if (context == null) {
            Log.e(tag, "Attempting to access context before it has been set");
            return false;
        }
        return true;
    }

    java.util.Locale locale;
    boolean visible = false;

    /**
     * @param appContext MUST BE APPLICATION CONTEXT
     * @param provider provider of study components
     */
    public static void initialize(Context appContext, StudyComponentProvider provider) {
        if (instance != null) {
            Log.e(tag, "Cannot initialize app twice");
            return;
        }
        instance = new Application(appContext, provider);

        instance.setLocaleOptions(createDefaultLocaleOptions());
        instance.setNotificationTypes(createDefaultNotificationTypes());

        instance.initializeStudy(appContext, provider);
        Study.getInstance().load();
    }

    /**
     * Only to be used with the validation app, always overwrites the study
     * @param appContext MUST BE APPLICATION CONTEXT
     */
    @VisibleForTesting
    public static synchronized void initializeValidationAppOnly(
            Context appContext, StudyComponentProvider provider) {

        instance = new Application(appContext, provider);

        instance.setLocaleOptions(createDefaultLocaleOptions());
        instance.setNotificationTypes(createDefaultNotificationTypes());

        instance.initializeStudyValidationAppOnly(appContext, provider);

        Study.getInstance().load();
    }

    /**
     * Only to be used with the validation app, always overwrites the study
     * @param appContext MUST BE APPLICATION CONTEXT
     */
    @VisibleForTesting
    private void initializeStudyValidationAppOnly(
            @NonNull Context appContext, StudyComponentProvider provider) {

        // Initialize study and assign study-specific components
        Study.initializeValidationAppOnly(context);
        provider.registerStudyComponents();
    }

    protected Application(Context appContext, StudyComponentProvider provider) {
        context = appContext;
        if (!checkContext()) {
            return;
        }

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        JodaTimeAndroid.init(appContext);
        VersionUtil.initialize(appContext);
        PreferencesManager.initialize(appContext);
        CacheManager.initialize(appContext);
        Device.initialize(appContext);
        updateLocale(appContext);
    }

    public void initializeStudy(@NonNull Context appContext, StudyComponentProvider provider) {
        // Initialize study and assign study-specific components
        Study.initialize(context);
        provider.registerStudyComponents();
    }

    public void setNotificationTypes(List<NotificationType> types) {
        notificationTypes = types;
    }

    // list all notification types offered by the app
    public List<NotificationType> getNotificationTypes() {
        return notificationTypes;
    }

    public static List<NotificationType> createDefaultNotificationTypes() {
        List<NotificationType> types = new ArrayList<>();
        types.add(NotificationTypes.TestConfirmed);
        types.add(NotificationTypes.TestMissed);
        types.add(NotificationTypes.TestNext);
        types.add(NotificationTypes.TestTake);
        if(Config.ENABLE_VIGNETTES) {
            types.add(NotificationTypes.VisitNextDay);
            types.add(NotificationTypes.VisitNextWeek);
            types.add(NotificationTypes.VisitNextMonth);
        }
        return types;
    }

    public void onConfigurationChanged(Configuration config) {
        Log.i("Application","onConfigurationChanged");
        if (!checkContext()) {
            return;
        }
        updateLocale(context);
    }

    public void attachBaseContext(Context context) {
        if (!checkContext()) {
            return;
        }
        updateLocale(context);
    }

    public void updateLocale(@Nullable Context context) {
        PreferencesManager preferences = PreferencesManager.getInstance();
        if (preferences == null) {
            return;
        }
        if (preferences.contains(Locale.TAG_LANGUAGE)) {
            String language = preferences.getString(Locale.TAG_LANGUAGE, Locale.LANGUAGE_ENGLISH);
            String country = preferences.getString(Locale.TAG_COUNTRY, Locale.COUNTRY_UNITED_STATES);
            locale = new java.util.Locale(language, country);

            if (!checkContext()) {
                return;
            }

            // update application
            Resources appResources = context.getResources();
            Configuration config = appResources.getConfiguration();
            config.setLocale(locale);
            appResources.updateConfiguration(config, appResources.getDisplayMetrics());

            if (context != null) {
                Resources activityResources = context.getResources();
                activityResources.updateConfiguration(config, activityResources.getDisplayMetrics());
            }
        }
    }

    // list all locale options offered by the app
    public List<Locale> getLocaleOptions() {
        return localeOptions;
    }

    public void setLocaleOptions(List<Locale> locales) {
        localeOptions = locales;
    }

    public static List<Locale> createDefaultLocaleOptions() {
        List<Locale> locales = new ArrayList<>();
        locales.add(new Locale(true, Locale.LANGUAGE_ENGLISH,Locale.COUNTRY_UNITED_STATES));
        return locales;
    }

    public java.util.Locale getLocale() {
        if(locale==null){
            PreferencesManager preferences = PreferencesManager.getInstance();
            String language = preferences.getString(Locale.TAG_LANGUAGE,Locale.LANGUAGE_ENGLISH);
            String country = preferences.getString(Locale.TAG_COUNTRY,Locale.COUNTRY_UNITED_STATES);
            locale = new java.util.Locale(language,country);
        }
        return locale;
    }

    public void restart() {
        if (!checkContext()) {
            return;
        }
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);

        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(TAG_RESTART, true);
        context.startActivity(intent);
        Runtime.getRuntime().exit(0);
    }

    public boolean isVisible(){
        return visible;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStartForeground() {
        visible = true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStopForeground() {
        visible = false;
    }
}
