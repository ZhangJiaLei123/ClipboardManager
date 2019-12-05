package com.blxt.quick;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blxt.clipboardmanager.R;
import com.blxt.mview.item.BaseSetItemView;
import com.blxt.mview.item.SetItemView;
import com.blxt.mview.item.SetItemViewSw;
import com.blxt.utils.IntentJump;

import java.util.ArrayList;
import java.util.List;

import static android.view.Gravity.CENTER;

public class SettingViewModel extends ViewModel implements BaseSetItemView.OnClickListenerCallBack {
    // TODO: Implement the ViewModel
    View view;

    private ImageView SetAppIcoBack;
    private ImageView SetAppIco;
    private View SetLine;
    private TextView SetAppName;
    private TextView SetAppVersion;
    private LinearLayout SetLayout;

    protected List<BaseSetItemView> viewItems = new ArrayList<>();

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


    public void addItem(){

        SetItemViewSw item1 = new SetItemViewSw(view.getContext(), "开机自启");

        SetItemViewSw item2 = new SetItemViewSw(view.getContext(), "固定通知");

        SetItemView item3 = new SetItemView(view.getContext(), "检查更新");
        item3.setHint("v1.0");

        SetItemView item4 = new SetItemView(view.getContext(), "分享");
        item4.setHint("分享给好友");

        SetItemView item5 = new SetItemView(view.getContext(), "支持");
        item5.setHint("给个好评");

        SetItemView item6 = new SetItemView(view.getContext(), "GitHub开源");
        item6.setHint("欢迎Star");

        viewItems.add(item1);
        viewItems.add(item2);
        viewItems.add(item3);
        viewItems.add(item4);
        viewItems.add(item5);
        viewItems.add(item6);


    }

    public void attachItem(){
        SetLayout.removeAllViews();
        for(View v : viewItems){
            SetLayout.addView(v);
            if(v instanceof BaseSetItemView){
                ((BaseSetItemView) v).setOnClickListener(this);
            }
        }
    }

    public void attachItem(List<BaseSetItemView> viewItems){
        SetLayout.removeAllViews();
        for(View v : viewItems){
            SetLayout.addView(v);
        }
    }


    @Override
    public void onClick(View view) {
        if(view instanceof BaseSetItemView){
            String title = ((BaseSetItemView) view).getTitle();
            if(title.equals("GitHub开源")){
                IntentJump.openWeb("https://github.com/ZhangJiaLei123/ClipboardManager",view.getContext());
            }
        }
    }
}
