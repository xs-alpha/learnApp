package com.xiaosheng.learnapp.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;


public class MyService extends Service {
    public static final String tag = "xiaosheng";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(tag,"onBind");
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(tag,"onCreate");
        super.onCreate();
        new Thread(()->{
            for (int i = 0; i < 1000; i++) {
                Log.i(tag, i+"");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(tag,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(tag, "destory");
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i(tag, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        Log.i(tag, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(tag, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(tag, "onRebind");
        super.onRebind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i(tag, "onTaskRemoved");
        super.onTaskRemoved(rootIntent);
    }
}
