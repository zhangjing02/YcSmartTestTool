package com.czjk.bleDemo.base;

import android.app.Application;
import android.content.Context;

import senssun.ycblelib.YCProtocolUtils;


public class MyApplication extends Application {
    private static MyApplication mApplication;
    public static MyApplication getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        YCProtocolUtils.getInstance().init(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public void killMyself() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
