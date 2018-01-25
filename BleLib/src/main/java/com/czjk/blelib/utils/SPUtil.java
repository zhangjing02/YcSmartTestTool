/*
 * -----------------------------------------------------------------
 * Copyright (C) 2012-2013, by Het, ShenZhen, All rights reserved.
 * -----------------------------------------------------------------
 *
 * File: AppPreferencesUtils.java
 * Author: clark
 * Version: 1.0
 * Create: 2013-9-27
 *
 * Changes (from 2013-9-27)
 * -----------------------------------------------------------------
 * 2013-9-27 : 创建 AppPreferencesUtils.java (clark);
 * -----------------------------------------------------------------
 */
package com.czjk.blelib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;




public final class SPUtil {

    public static SharedPreferences sp;

    public static SharedPreferences getInstance(Context context) {
        sp = context.getSharedPreferences(Constant.SP_NAME,context.MODE_PRIVATE);
        return sp;
    }

    public static void clearAllData() {
        Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public static void setStringValue(String key, String value) {
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringValue(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public static void setBooleanValue(String key, boolean value) {
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBooleanValue(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public static void setIntValue(String key, int value) {
        Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getIntValue(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public static void setFloatValue(String key, float value) {
        Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static float getFloatValue(String key, float defValue) {
        return sp.getFloat(key, defValue);
    }
}
