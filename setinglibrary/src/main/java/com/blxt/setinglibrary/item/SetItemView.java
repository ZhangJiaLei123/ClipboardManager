package com.blxt.setinglibrary;


import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;


/**
 * 设置页面的item
 * @author Zhang
 */
public class SetItemView extends BaseSetItemView {


    private ImageView iv_right; // 右边箭头


    public SetItemView(Context context, String title) {
        super(context);

        instance = this;
        LayoutInflater.from(getContext()).inflate(R.layout._item_set__simple,this);

        iv_bottom =findViewById(R.id._item_bottom);
        iv_logo =findViewById(R.id._item_title_ic);
        tv_title =findViewById(R.id._item_title);
        tv_hint = findViewById(R.id._item_tip);
        iv_right = findViewById(R.id._item_iright); //

        setTitle(title);
        initview();
    }






}
