package com.blxt.mview.item;


import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.blxt.clipboardmanager.R;

/**
 * 设置页面的item
 * @author Zhang
 */
public class SetItemViewSw extends BaseSetItemView implements CompoundButton.OnCheckedChangeListener {


    private Switch aSwitch; // 右边箭头


    public SetItemViewSw(Context context, String title) {
        super(context);

        instance = this;
        LayoutInflater.from(getContext()).inflate(R.layout._item_set__sw,this);

        iv_bottom =findViewById(R.id._item_bottom);
        iv_logo =findViewById(R.id._item_title_ic);
        tv_title =findViewById(R.id._item_title);
        tv_hint = findViewById(R.id._item_tip);
        aSwitch = findViewById(R.id._item_sw); //

        setTitle(title);
        setHint("");
        setTextOff("开");
        setTextOn("关");
        initview();

        String value = getValue();
        if(value.equals("true") || value.equals("TRUE")){
            aSwitch.setChecked(true);
        }
        else{
            aSwitch.setChecked(false);
        }

        addListener();

    }

    public void addListener(){
        aSwitch.setOnCheckedChangeListener(this);
    }

    public void setTextOff(String textOff){
        aSwitch.setTextOff(textOff);
    }

    public void setTextOn(String textOn){
        aSwitch.setTextOn(textOn);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        saveValue(b + "");
    }
}
