package com.blxt.quick;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blxt.setinglibrary.BaseSetItemView;

public class BaseSettingFragment extends Fragment  {

    private SettingViewModel mViewModel;
    private View rootView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       // rootView = inflater.inflate(R.layout.setting_fragment, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);
        mViewModel.setRootView(rootView);


        // TODO: Use the ViewModel
    }

    /**
     * 设置监听回调,需要在addItem()前使用
     * @param clickListener
     */
    public void setClickListener(BaseSetItemView.OnClickListenerCallBack clickListener){
        mViewModel.setClickListener(clickListener);
    }

    public void addItem(View view){
        mViewModel.addItem(view);
    }


}
