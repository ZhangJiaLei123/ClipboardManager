package com.blxt.clipboardmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blxt.clipboardmanager.activity.MainActivity;

public class RunReceiver extends BroadcastReceiver
{
    public RunReceiver()
    {
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("RunReceiver","onReceive");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) // 开机自
        {
            Intent i = new Intent(context, MainActivity.class); // 启动MainActivity
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}