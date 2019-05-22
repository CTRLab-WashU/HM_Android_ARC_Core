package com.healthymedium.arc.utilities;

import android.util.Log;

import com.google.gson.JsonObject;
import com.healthymedium.arc.study.StudyState;
import com.healthymedium.arc.study.StudyStateCache;
import com.healthymedium.arc.study.StudyStateMachine;

public class MigrationUtil {

    public void checkForUpdate(){

        // library migration

        long newLibVersion = VersionUtil.getLibraryVersionCode();
        long oldLibVersion = PreferencesManager.getInstance().getLong("libVersion", newLibVersion);

        Log.i("MigrationUtil", "old library version="+oldLibVersion);
        Log.i("MigrationUtil", "new library version="+newLibVersion);

        if(newLibVersion > oldLibVersion) {
            Log.i("MigrationUtil", "migrating library data from "+oldLibVersion+" to "+newLibVersion);
            if(migrateLibraryData(oldLibVersion,newLibVersion)) {
                PreferencesManager.getInstance().putLong("libVersion", newLibVersion);
            }
        }

        // app migration

        long newAppVersion = VersionUtil.getAppVersionCode();
        long oldAppVersion = PreferencesManager.getInstance().getLong("appVersion", newAppVersion);

        Log.i("MigrationUtil", "old app version="+oldAppVersion);
        Log.i("MigrationUtil", "new app version="+newAppVersion);

        if(newLibVersion > oldLibVersion) {
            Log.i("MigrationUtil", "migrating app data from "+oldAppVersion+" to "+newAppVersion);
            if(migrateAppData(oldAppVersion,newAppVersion)) {
                PreferencesManager.getInstance().putLong("appVersion", newAppVersion);
                Log.i("MigrationUtil", "migration successful");
            } else {
                Log.i("MigrationUtil", "migration failed");
            }
        }
    }


    protected boolean migrateAppData(long oldVersion, long newVersion){
        return true;
    }

    protected boolean migrateLibraryData(long oldVersion, long newVersion){

        boolean successful = true;

        // change this as necessary when merging the branch
        if(oldVersion < 1010000){
            successful = migratePreferencesToCache();
        }



        return successful;
    }

    private boolean migratePreferencesToCache(){

        JsonObject json = PreferencesManager.getInstance().getObject("StateMachine", JsonObject.class);
        PreferencesManager.getInstance().remove("StateMachine");

        StudyState state = new StudyState();
        if(json.has("lifecycle")) {
            state.lifecycle = json.get("lifecycle").getAsInt();
        }
        if(json.has("currentPath")) {
            state.currentPath = json.get("currentPath").getAsInt();
        }
        PreferencesManager.getInstance().putObject(StudyStateMachine.TAG_STUDY_STATE,state);

        StudyStateCache cache = new StudyStateCache();
        if(json.has("segments")) {
            cache.segments = PreferencesManager.getInstance().getGson().fromJson(json.get("segments"), cache.segments.getClass());
        }
        if(json.has("cache")) {
            cache.data = PreferencesManager.getInstance().getGson().fromJson(json.get("cache"), cache.data.getClass());
        }
        CacheManager.getInstance().putObject(StudyStateMachine.TAG_STUDY_STATE_CACHE,cache);

        return true;
    }

}
