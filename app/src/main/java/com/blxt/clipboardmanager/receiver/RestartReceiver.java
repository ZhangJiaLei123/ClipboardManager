package com.blxt.clipboardmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blxt.clipboardmanager.server.ClipListenServer;

/**
 * 重启服务
 * @author Zhang
 */
public class RestartReceiver extends BroadcastReceiver {

    public RestartReceiver(){
        Log.d("RestartReceiver","RestartReceiver");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("RestartReceiver","onReceive");

        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
            Log.i("RestartReceiver","开屏");
        //    ClipListenServer.start(context);
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
            Log.d("RestartReceiver","锁屏");
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
            Log.d("RestartReceiver","解锁");
        }

        // 重启服务
        if(intent.getAction().equals("RestartReceiver")){
            ClipListenServer.start(context);
        }


    }

}