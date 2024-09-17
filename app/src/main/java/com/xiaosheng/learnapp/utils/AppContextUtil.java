package com.xiaosheng.learnapp.utils;

import android.content.Context;

public class AppContextUtil {
    // A static reference to hold the application context
    private static Context appContext;

    // This method should be called once in the Application class to initialize the context
    public static void initialize(Context context) {
        if (appContext == null) {
            appContext = context.getApplicationContext();
        }
    }

    // Method to retrieve the application context safely
    public static Context getAppContext() {
        if (appContext != null) {
            return appContext;
        }
        throw new IllegalStateException("AppContext is not initialized, call initialize() method first.");
    }
}
