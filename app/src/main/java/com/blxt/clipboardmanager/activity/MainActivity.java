package com.blxt.clipboardmanager.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.blxt.clipboardmanager.Model;
import com.blxt.clipboardmanager.R;
import com.blxt.clipboardmanager.server.ClipListenServer;
import com.blxt.clipboardmanager.unit.NotificationUtil;
import com.blxt.clipboardmanager.bean.ClipTextBean;
import com.blxt.greendao.DaoUtils;
import com.tencent.tauth.Tencent;

import java.util.List;

// Activitymanagement 管理者代表
public class MainActivity extends PermissionBaseActivity {
    public static Activity activity;
    public static String mAppid = "222222";
    public static Tencent mTencent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        context = this;

        initUI();
        initData();

    }


    public void initUI() {
        activity = this;

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(getWindow().getDecorView().findViewById(android.R.id.content),InputMethodManager.SHOW_FORCED);
    }


    public void initData() {
        MainActivity.mTencent = Tencent.createInstance(MainActivity.mAppid, MainActivity.this);
        //连续启动Service
        ClipListenServer.start(context);


        // 首次运行时,检查剪辑板
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData data = cm.getPrimaryClip();

        DaoUtils daoUtils = new DaoUtils(context);
        List<ClipTextBean> clips =  daoUtils.queryAllClip();

        if(data != null && data.getItemCount() > 0){
            ClipData.Item item = data.getItemAt(0);

            Model.clipboardStr_now = new ClipTextBean(item.getText().toString().trim());

        }


        if( Model.clipboardStr_now == null
                || Model.clipboardStr_now.getContent().isEmpty()
                ||  Model.clipboardStr_now.getContent().length() <= 0){

            Model.clipboardStr_now = new ClipTextBean("");
            if(clips.size() > 0){
                Model.clipboardStr_now = clips.get(clips.size() - 1);
            }
        }

        if( !Model.clipboardStr_now.isEmpty()) {
            daoUtils.insertClip(Model.clipboardStr_now);
        }

        NotificationUtil.showNotification(context,"剪贴板",Model.clipboardStr_now.getContent(),true);

    }



    /**
     * 隐藏主界面
     * @param view
     */
    public void onClicFinish(View view) {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MainActivity.mTencent != null) {
            MainActivity.mTencent.releaseResource();
        }
    }

}