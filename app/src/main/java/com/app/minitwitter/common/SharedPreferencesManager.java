package com.app.minitwitter.common;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String APP_SETTINGS_FILE = "APP_SETTINGS";

    private SharedPreferencesManager(){

    }

    private static SharedPreferences getSharedPreferences(){
        return MyApplication.getContext().getSharedPreferences( APP_SETTINGS_FILE, Context.MODE_PRIVATE);
    }

    public static void setStringValue(String dataLabel, String dataValue){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(dataLabel,dataValue);
        editor.apply();
    }

    public static String getStringValue(String dataLabel){
        return getSharedPreferences().getString(dataLabel, "");
    }
}
