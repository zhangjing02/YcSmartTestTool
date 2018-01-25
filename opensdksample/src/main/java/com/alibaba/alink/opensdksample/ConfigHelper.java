package com.alibaba.alink.opensdksample;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;

/**
 * Created by guikong on 16/9/26.
 */
public class ConfigHelper {
    public static String ip = "";

    public void loadFromDisk(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        ip = sharedPreferences.getString("debug_bone_ip", "");
    }

    public void saveToDisk(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("debug_bone_ip", ip);

        editor.apply();
    }
}
