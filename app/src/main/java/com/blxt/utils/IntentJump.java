package com.blxt.utils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 常用意图实现,如打电话,打开网页等
 */
public class IntentJump {
    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhone(String phoneNum, Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }


    /**
     * 打开百度搜索
     * @param key
     * @param context
     */
    public static void goBaiDu(String key, Context context){
        key = "http://www.baidu.com/s?wd=" + key;
        openWeb(key, context);
    }

    /**
     * 打开网页
     * @param path
     * @param context
     */
    public static void openWeb(String path, Context context){
        if(!path.startsWith("http")){
            path = "https://" + path;
        }
        Uri uri = Uri.parse(path);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }
}
