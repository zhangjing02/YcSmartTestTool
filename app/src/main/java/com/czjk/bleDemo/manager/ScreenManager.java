package com.czjk.bleDemo.manager;

import android.app.Activity;

import java.util.Stack;

/**
 * use for manager Activity task
 */
public class ScreenManager {
    private static Stack<Activity> activityStack;

    private static ScreenManager instance;

    private ScreenManager() {
    }

    public static ScreenManager getScreenManager() {
        if (instance == null) {
            synchronized (ScreenManager.class) {
                if (instance == null) {
                    instance = new ScreenManager();
                }
            }
        }
        return instance;
    }

    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }


}
