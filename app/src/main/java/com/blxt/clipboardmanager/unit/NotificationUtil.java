package com.blxt.clipboardmanager.unit;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;


import com.blxt.clipboardmanager.server.ClipListenServer;
import com.blxt.clipboardmanager.Model;
import com.blxt.clipboardmanager.R;
import com.blxt.utils.TextAnalysis;

import java.lang.reflect.Method;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.os.Build.VERSION_CODES.LOLLIPOP_MR1;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 显示通知栏工具类
 * Created by Administrator on 2016-11-14.
 */

public class NotificationUtil {

    public static NotificationManager mNotificationManager;
    final static int NOTIFICATION_ID_DEFAULT = 254;

    final static String CHANNEL_ID = "剪辑板";
    final static String CHANNEL_NAME = "新消息";



    /**
     * 显示一个普通的通知
     *
     * @param context 上下文
     * @param title   标题
     * @param msg 内容
     * @param fal     是否允许消除,true 为不可消除
     */
    //@RequiresApi(api = Build.VERSION_CODES.O)
    public static void showNotification(Context context, String title, String msg, Boolean fal) {

        //先设定RemoteViews
        RemoteViews view_custom = new RemoteViews(context.getPackageName(), R.layout.item_clipboard);//布局
        if(msg != null){
            view_custom.setTextViewText(R.id.tv_board_item, msg);//文字
        }

        //////////////////////////////////
        //按钮监听设置
        Intent intentEdit= new Intent(context, ClipListenServer.class);
        intentEdit.putExtra("按钮","编辑");
        PendingIntent pIntentEdit = PendingIntent.getService(context, 0, intentEdit, 0);


        Intent intentShare = new Intent(context, ClipListenServer.class);
        intentShare.putExtra("按钮","分享");
        PendingIntent pIntentShare = PendingIntent.getService(context, 1, intentShare, 0);

        Intent intentClose = new Intent(context, ClipListenServer.class);
        intentClose.putExtra("按钮","关闭");
        PendingIntent pIntentClose = PendingIntent.getService(context, 2, intentClose, 0);

        view_custom.setOnClickPendingIntent(R.id.layout_noti_msg, pIntentEdit);
        view_custom.setOnClickPendingIntent(R.id.btn_noti_share, pIntentShare);
        view_custom.setOnClickPendingIntent(R.id.btn_noti_close, pIntentClose);

        Intent intentMore= new Intent(context, ClipListenServer.class);
        intentMore.putExtra("按钮","更多");

        if(msg != null){
            view_custom.setViewVisibility(R.id.btn_noti_more, VISIBLE);
            // 网址
            if(msg.startsWith("http:") || msg.startsWith("https:") || msg.startsWith("www.")){
                // 打开网址
                view_custom.setTextViewText(R.id.btn_noti_more, "访问");
                Model.ClipboardContentId = Model.CCID_DO_WEB;

            }
            else if(TextAnalysis.isPhone(msg)){
                // 报打电话
                view_custom.setTextViewText(R.id.btn_noti_more, "拨打");
                Model.ClipboardContentId = Model.CCID_DO_PHONE;
            }
            else{// 百度搜索
                view_custom.setTextViewText(R.id.btn_noti_more, "搜索");
                Model.ClipboardContentId = Model.CCID_DO_OTHER;
            }
            Log.i("发送通知",msg);
        }else{
            view_custom.setTextViewText(R.id.btn_noti_more, "搜索");
            view_custom.setViewVisibility(R.id.btn_noti_more, GONE);
            Model.ClipboardContentId = Model.CCID_DO_OTHER;
        }


        PendingIntent pIntentMore = PendingIntent.getService(context, 3, intentMore, 0);
        view_custom.setOnClickPendingIntent(R.id.btn_noti_more, pIntentMore);
        ////////////////////////


        showNotification(context,title,msg,fal,view_custom,null);
    }

    //获取系统服务
    public static NotificationManager getNotificationManager(Context context) {
        if (mNotificationManager == null){
            mNotificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }


    /**
     * 通用消息栏通知
     * @param context
     * @param title   标题
     * @param msg     消息
     */
    public static void showNotification(Context context,String title, String msg){
        showNotification(context, title, msg, false,null,null);
    }


    /**
     * 通用消息栏通知
     * @param context
     * @param title   标题
     * @param msg     消息
     * @param fal     是否常驻
     * @param view    自定义视图
     */
    public static void showNotification(Context context, String title, String msg, boolean fal, RemoteViews view, PendingIntent pintent){

        NotificationManager notificationManager = getNotificationManager(context);
        //notification
        Notification notification = null;

        //版本兼容，适配到4.1
        // 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH; //channel的重要性
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            notificationManager.createNotificationChannel(mChannel); //添加channel

            notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setContent(view) // 自定义视图
                    .setContentIntent(pintent) // 点击意图
                    .build();
        }
        // 22 - 26
        else if (Build.VERSION.SDK_INT >= LOLLIPOP_MR1) {
            notification = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContent(view) // 自定义视图
                    .setContentIntent(pintent) // 点击意图
                    .build();

        }
        // 16 -22
        else if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setContent(view) // 自定义视图
                    .setContentIntent(pintent) // 点击意图
                    .build();
        }
//        // 11-26
//        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//
//        }
//        // 11 一下，3.
//        else{
//
//        }


        if(fal){
            notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_FOREGROUND_SERVICE ; // 常驻
        }

        notificationManager.notify(NOTIFICATION_ID_DEFAULT, notification);
    }

    /**
     * 关闭通知
     */
    public static void close(){
        if(mNotificationManager != null){
            mNotificationManager.cancelAll();
        }
    }
    /**
     * 关闭通知
     */
    public static void close(int id){
        if(mNotificationManager != null){
            mNotificationManager.cancel(id);
        }
    }

    /**
     * 收起通知栏
     * 需要权限：<uses-permission android:name="android.permission.EXPAND_STATUS_BAR" />
     */
    public static void collapseStatusBar(Context context) {
        @SuppressLint("WrongConstant") Object service =context.getSystemService("statusbar");
        if (null == service) {
            return;
        }
        try {
            Class<?> clazz = Class.forName("android.app.StatusBarManager");
            int sdkVersion = android.os.Build.VERSION.SDK_INT;
            Method collapse = null;
            if (sdkVersion <= 16) {
                collapse = clazz.getMethod("collapse");
            } else {
                collapse = clazz.getMethod("collapsePanels");
            }
            collapse.setAccessible(true);
            collapse.invoke(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}