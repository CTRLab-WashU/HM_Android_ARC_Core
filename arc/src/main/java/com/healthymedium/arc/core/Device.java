package com.healthymedium.arc.core;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import androidx.annotation.Dimension;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.healthymedium.arc.utilities.PreferencesManager;

import java.util.UUID;

public class Device {

    public static final String TAG_DEVICE_ID = "deviceId";

    private static String id;
    private static String info;
    private static String name;
    private static boolean initialized = false;

    private Device(){

    }

    public static void initialize(Context context) {

        if(PreferencesManager.getInstance().contains(TAG_DEVICE_ID)){
            id = PreferencesManager.getInstance().getString(TAG_DEVICE_ID,"");
        } else {
            id = UUID.randomUUID().toString();
            PreferencesManager.getInstance().putString(TAG_DEVICE_ID,id);
        }

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            name = capitalize(model);
        } else {
            name = capitalize(manufacturer) + " " + model;
        }
        info = "Android|"+name+"|"+ Build.VERSION.RELEASE+"|"+Build.VERSION.SDK_INT+"|"+Build.FINGERPRINT;
        initialized = true;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }


    public static String getId(){
        return id;
    }

    public static String getInfo(){
        return info;
    }

    public static String getName(){
        return name;
    }

    public static boolean screenIsBiggerThan(@Dimension int inchesX, @Dimension double inchesY){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float inY = metrics.heightPixels/metrics.ydpi;
        float inX = metrics.widthPixels/metrics.xdpi;
        Log.i("Device","width = "+inX+" inches, height = "+inY+" inches");
        return (inchesX>inX && inchesY>inY);

    }

}
