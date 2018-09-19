package com.zhangteng.baselibrary.utils;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 *
 * @author swing
 * @created 2018-1-30
 */
public class AppManager {
    private static Stack<Activity> activityStack = new Stack<Activity>();

    /**
     * 添加Activity到堆栈
     */
    public static void addActivity(Activity activity) {
        activityStack.push(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        if (activityStack == null || activityStack.empty()) {
            return null;
        }
        return activityStack.lastElement();
    }


    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public static void finishCurrentActivity() {
        Activity activity = activityStack.pop();
        activity.finish();
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    //退出栈中除指定的Activity外的所有
    public static void popAllActivityExceptOne(Class cls) {
        Activity activitys = null;
        int i = 0;
        while (true) {
            i = 0;
            Activity activity = currentActivity();
            if (activity == null) {
                if (activitys != null) {
                    AppManager.addActivity(activitys);
                }
                break;
            }
            if (activity.getClass().equals(cls)) {
                activitys = activity;
                i = 1;
            }
            destoryActivity(activity, i);
        }
    }


    public static void destoryActivity(Activity activity, int i) {
        if (activity == null) {
            return;
        }
        if (i == 0) {
            activity.finish();
        }
        if (activityStack.contains(activity)) {
            activityStack.remove(activity);
        }
        activity = null;
    }


    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        for (Activity activity : activityStack) {
            if (activity != null) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public static void AppExit(Context context) {
        try {
            finishAllActivity();
//            ActivityManager manager = (ActivityManager) context
//                    .getSystemService(Context.ACTIVITY_SERVICE);
//            manager.killBackgroundProcesses(context.getPackageName());
            //Process.killProcess(Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
