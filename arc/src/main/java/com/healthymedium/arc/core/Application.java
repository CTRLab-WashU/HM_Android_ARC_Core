package com.healthymedium.arc.core;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import com.healthymedium.arc.utilities.Log;


import com.healthymedium.arc.notifications.NotificationTypes;
import com.healthymedium.arc.notifications.types.NotificationType;
import com.healthymedium.arc.study.Study;

import com.healthymedium.arc.utilities.CacheManager;
import com.healthymedium.arc.utilities.PreferencesManager;
import com.healthymedium.arc.utilities.VersionUtil;

import net.danlew.android.joda.JodaTimeAndroid;
import java.util.ArrayList;
import java.util.List;

public class Application extends android.app.Application {

    private static final String tag = "Application";
    static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        JodaTimeAndroid.init(this);
        VersionUtil.initialize(this);
        PreferencesManager.initialize(this);
        CacheManager.initialize(this);
        Device.initialize(this);
        initializeStudy();
    }

    public void initializeStudy() {
        Study.initialize(this);
        registerStudyComponents();
        Study.getInstance().load();
    }

    // register different behaviors here
    public void registerStudyComponents() {
        //Study.getInstance().registerParticipantBehavior();
        //Study.getInstance().registerMigrationBehavior();
        //Study.getInstance().registerSchedulerBehavior();
        //Study.getInstance().registerRestClient();
        //Study.getInstance().registerStudyBehavior();
    }

    // list all notification types offered by the app
    public List<NotificationType> getNotificationTypes() {
        List<NotificationType> types = new ArrayList<>();
        types.add(NotificationTypes.TestConfirmed);
        types.add(NotificationTypes.TestMissed);
        types.add(NotificationTypes.TestNext);
        types.add(NotificationTypes.TestTake);
//        types.add(NotificationTypes.TestTakeFirstOfDay);
//        types.add(NotificationTypes.TestTakeSecondOfDay);
//        types.add(NotificationTypes.TestTakeThirdOfDay);
//        types.add(NotificationTypes.TestTakeLastOfDay);
//        types.add(NotificationTypes.TestTakeFirstOfDay4);
//        types.add(NotificationTypes.TestTakeFirstOfCycle);
//        types.add(NotificationTypes.TestTakeLastOfCycle);
        if(Config.ENABLE_VIGNETTES) {
            types.add(NotificationTypes.VisitNextDay);
            types.add(NotificationTypes.VisitNextWeek);
            types.add(NotificationTypes.VisitNextMonth);
        }
        return types;
    }

    // list all locale options offered by the app
    public List<Locale> getLocaleOptions() {
        List<Locale> locales = new ArrayList<>();
        locales.add(new Locale(Locale.COUNTRY_UNITED_STATES,Locale.LANGUAGE_ENGLISH));
        return locales;
    }

    public java.util.Locale getLocale() {
        PreferencesManager preferences = PreferencesManager.getInstance();
        String language = preferences.getString("language","en");
        String country = preferences.getString("country","US");
        return new java.util.Locale(language,country);
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        Log.i("Application","onConfigurationChanged");
        super.onConfigurationChanged(config);
        updateLocale(getBaseContext());
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        updateLocale(context);
    }

    public void updateLocale(@Nullable Context context){
        PreferencesManager preferences = PreferencesManager.getInstance();
        if(preferences == null) {
            return;
        }
        if(preferences.contains(Locale.TAG_LANGUAGE)){
            String language = preferences.getString(Locale.TAG_LANGUAGE,Locale.LANGUAGE_ENGLISH);
            String country = preferences.getString(Locale.TAG_COUNTRY,Locale.COUNTRY_UNITED_STATES);
            java.util.Locale locale = new java.util.Locale(language,country);

            // update application
            Resources appResources = getResources();
            Configuration config = appResources.getConfiguration();
            config.setLocale(locale);
            appResources.updateConfiguration(config, appResources.getDisplayMetrics());

            if(context!=null) {
                Resources activityResources = context.getResources();
                activityResources.updateConfiguration(config, activityResources.getDisplayMetrics());
            }
        }
    }

    @Override
    public void onLowMemory() {
        Log.w(tag,"onLowMemory");
        super.onLowMemory();
    }

    public static Application getInstance(){
        return instance;
    }

}
