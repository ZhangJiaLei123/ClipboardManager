package com.blxt.clipboardmanager.activity;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blxt.clipboardmanager.Model;
import com.blxt.clipboardmanager.R;
import com.blxt.clipboardmanager.fragment.SettingFragment;
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
        SettingFragment settingFragment = SettingFragment.newInstance();
        changeFragment(settingFragment);
    }


    public void initUI() {
        activity = this;

      //  InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      //  imm.showSoftInput(getWindow().getDecorView().findViewById(android.R.id.content),InputMethodManager.SHOW_FORCED);
    }


    public void initData() {
        MainActivity.mTencent = Tencent.createInstance(MainActivity.mAppid, MainActivity.this);
        //连续启动Service
        ClipListenServer.start(context);


        // 首次运行时,检查剪辑板
        ClipboardManager cm = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        }
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

    private void changeFragment(Fragment fragment){
        //实例化碎片管理器对象
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        //选择fragment替换的部分
        ft.replace(R.id.cl_content,fragment);
        ft.commit();
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
