package com.alibaba.alink.opensdksample.envconfig;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.aliyun.alink.bone.CdnEnv;
import com.aliyun.alink.business.alink.ALinkEnv;

/**
 * Created by huanyu.zhy on 17/6/6.
 *
 * 当前环境配置
 */
public class EnvConfigure {
    public static ALinkEnv aLinkEnv = null;
    public static CdnEnv cdnEnv = null;
    public static String deviceConfigMode = "addDevice";


    static public void init(Context context) {
        try {
            SharedPreferences sp = context.getSharedPreferences("envConfig", 0);
            if (sp != null) {
                String alinkEnvConf = sp.getString("aLinkEnv", (String) null);
                if (!TextUtils.isEmpty(alinkEnvConf))
                    aLinkEnv = ALinkEnv.valueOf(alinkEnvConf);

                String cdnEnvConf = sp.getString("cdnEnv", (String) null);
                if (!TextUtils.isEmpty(cdnEnvConf))
                    cdnEnv = CdnEnv.valueOf(cdnEnvConf);

                String deviceConfigMode = sp.getString("deviceConfigMode", (String) null);
                if ("auth".equalsIgnoreCase(deviceConfigMode)) {
                    EnvConfigure.deviceConfigMode = "auth";
                } else {
                    EnvConfigure.deviceConfigMode = "addDevice";
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static public void saveAlinkEnv(Context context, ALinkEnv env) {
        aLinkEnv = env;
        SharedPreferences sharedPreferences = context.getSharedPreferences("envConfig", 0);
        Editor editor = sharedPreferences.edit();
        editor.putString("aLinkEnv", env.name()).apply();
    }

    static public void saveCDNEnv(Context context, CdnEnv env) {
        cdnEnv = env;
        SharedPreferences sharedPreferences = context.getSharedPreferences("envConfig", 0);
        Editor editor = sharedPreferences.edit();
        editor.putString("cdnEnv", env.name()).apply();
    }

    static public void saveDeviceConfigMode(Context context, String deviceConfigMode) {
        EnvConfigure.deviceConfigMode = deviceConfigMode;
        SharedPreferences sharedPreferences = context.getSharedPreferences("envConfig", 0);
        Editor editor = sharedPreferences.edit();
        editor.putString("deviceConfigMode", deviceConfigMode).apply();
    }
}
