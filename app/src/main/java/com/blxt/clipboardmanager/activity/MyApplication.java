package com.blxt.clipboardmanager.activity;

import android.app.Application;
import android.content.Context;

import com.blxt.greendao.DaoManager;

/**
 * Application
 * 初始化数据库
 */
public class MyApplication extends Application
{
    public static Context context;
    @Override
    public void onCreate()
    {
        super.onCreate();
        initGreenDao();
        context = this;
    }

    private void initGreenDao()
    {
        DaoManager mManager = DaoManager.getInstance();
        mManager.init(this);
    }
}