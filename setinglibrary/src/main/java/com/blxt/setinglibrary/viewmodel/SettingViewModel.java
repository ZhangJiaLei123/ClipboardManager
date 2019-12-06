package com.blxt.quick;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModel;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.blxt.clipboardmanager.R;
import com.blxt.setinglibrary.BaseSetItemView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("StaticFieldLeak")
public class SettingViewModel extends ViewModel{
    View view;

    /**
     * 头部背景
     */
    private ImageView SetAppIcoBack;
    /**
     * 头部图标,applogo
     */
    private ImageView SetAppIco;
    /**
     * 版本和软件名称分隔符
     */
    private View SetLine;
    /**
     * 软件名称
     */
    private TextView SetAppName;
    /**
     * 软件版本
     */
    private TextView SetAppVersion;
    /**
     * 设置项容器
     */
    private LinearLayout SetLayout;

    protected List<View> viewItems = new ArrayList<>();

    BaseSetItemView.OnClickListenerCallBack clickListener = null;

    public void setRootView(View view){
        this.view = view;
        findViewById();
    }

    private boolean findViewById(){

        SetAppIcoBack = view.findViewById(R.id._set_app_ico_back);
        SetAppIco = view.findViewById(R.id._set_app_ico);
        SetLine = view.findViewById(R.id._set_line);
        SetAppName = view.findViewById(R.id._set_app_name);
        SetAppVersion = view.findViewById(R.id._set_app_version);
        SetLayout = view.findViewById(R.id._set_layout);

        return true;
    }

    /**
     * 添加配置项
     * @param view
     */
    public void addItem(View view){

        viewItems.add(view);
        if(view instanceof BaseSetItemView){
            ((BaseSetItemView) view).setOnClickListener(clickListener);
        }
        SetLayout.addView(view);
    }


    /**
     * 设置监听回调,需要在addItem()前使用
     * @param clickListener
     */
    public void setClickListener(BaseSetItemView.OnClickListenerCallBack clickListener){
        this.clickListener = clickListener;
    }

    public void setAppImage(int resID){
        SetAppIco.setBackgroundResource(resID);
    }

    public void setAppImageBack(int resID){
        SetAppIcoBack.setBackgroundResource(resID);
    }

    public void setAppName(int resId){
        SetAppName.setText(resId);
    }

    public void setAppVersion(int resId){
        SetAppName.setText(resId);
    }
    public void setAppName(String resId){
        SetAppName.setText(resId);
    }

    public void setAppVersion(String resId){
        SetAppName.setText(resId);
    }
}
