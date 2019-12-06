package com.blxt.clipboardmanager.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blxt.clipboardmanager.R;
import com.blxt.setinglibrary.BaseSettingFragment;
import com.blxt.setinglibrary.item.BaseSetItemView;
import com.blxt.setinglibrary.item.SetItemView;
import com.blxt.setinglibrary.item.SetItemViewSw;
import com.blxt.utils.IntentJump;

public class SettingFragment extends BaseSettingFragment implements BaseSetItemView.OnClickListenerCallBack {

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setClickListener(this);
        mViewModel = getSettingViewModel();
        initItems();
        mViewModel.setAppName(R.string.app_name);
        mViewModel.setAppVersion(R.string.app_version);
        mViewModel.setAppImage(R.mipmap.ic_launcher_round);
    }

    private void initItems(){

        addItem(new SetItemViewSw(getContext(), "开机自启").setTitleImage(R.mipmap.ic_launcher_round));
        addItem(new SetItemViewSw(getContext(), "固定通知").setTitleImage(R.mipmap.ic_launcher_round));
        addItem(new SetItemView(getContext(), "检查更新").setTitleImage(R.mipmap.ic_launcher_round));
        addItem(new SetItemView(getContext(), "分享").setTitleImage(R.mipmap.ic_launcher_round));
        addItem(new SetItemView(getContext(), "支持").setTitleImage(R.mipmap.ic_launcher_round));
        addItem(new SetItemView(getContext(), "GitHub开源").setTitleImage(R.mipmap.ic_launcher_round));

    }


    public boolean onClickSetItem(View view) {
       // super
        if(view instanceof BaseSetItemView){
            String title = ((BaseSetItemView) view).getTitle();
            Log.i("点击",title);
            if(title.equals("GitHub开源")){
                IntentJump.openWeb("https://github.com/ZhangJiaLei123/ClipboardManager",view.getContext());
            }
            else if(title.equals("开机自启")){
                return false;
            }

        }
        return true;
    }

}
