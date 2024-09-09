package com.xiaosheng.learnapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    public  static  final String tag = "xiaosheng";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(tag, "接受到消息");
    }
}
