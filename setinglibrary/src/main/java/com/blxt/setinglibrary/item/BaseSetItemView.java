package com.blxt.setinglibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class BaseSetItemView extends LinearLayout {


    protected static SharedPreferences SP = null;

    SharedPreferences getSP(Context context){
        if(SP == null){
            SP = context.getSharedPreferences(context.getPackageName() + "_preferences", MODE_PRIVATE);
        }
        return SP;
    }
    protected BaseSetItemView instance;
    protected ImageView iv_logo;//item的图标
    protected TextView tv_title; //item的title文字
    protected TextView tv_hint; //item的内容提示文字
    protected ImageView iv_bottom; // 底部分割线


    /** 关联子视图 */
    protected List<View> subView = new ArrayList<>();

    private CharSequence tipText = null;

    public BaseSetItemView(Context context) {
        super(context);

        SP = getSP(context);

    }

    protected void initview() {

        int visibility = this.getVisibility();
        if(subView != null){
            setSubViewVisibility(visibility);
        }

        tv_hint.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickListener != null){
                    clickListener.onClick(instance);
                }

            }
        });

    }

    /**
     *
     * 设置标题
     * @param title
     */
    public void setTitle(String title){
        tv_title.setText(title);
    }

    /**
     * 获取标题
     * @return
     */
    public String getTitle(){
        return tv_title.getText().toString();
    }

    /**
     * 设置提示文本
     * @param tipText
     */
    public void setHint(String tipText){
        this.tipText = tipText;
        tv_hint.setText(tipText);
       // saveValue(tipText);
    }

    /**
     * 设置提示文本和值
     * @param tipText  显示的文本
     * @param vueal    实际保存的值
     */
    public void setHint(CharSequence tipText, String vueal){
        this.tipText = tipText;
        tv_hint.setText(tipText);
      //  saveValue(vueal);
    }

    public String getHintText(){
        return tv_hint.getText().toString();
    }


    public void setSubView(View view){
        subView.add(view);
    }



    public void setEnabled(boolean isbootom){
        tv_hint.setEnabled(isbootom);
    }

    public void setOnClickListener(final OnClickListenerCallBack clickListener) {
        this.clickListener = clickListener;

    }

    /**
     * 获取当前视图
     * @return
     */
    public View getView(){
        return this;
    }


    /** 当前设置可见性，关联子视图 */
    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if(subView != null){
            setSubViewVisibility(visibility);
        }
    }

    /** 设置子视图的可见性 */
    protected void setSubViewVisibility(int visibility){
        for(int i = 0; i < subView.size(); i++) {
            subView.get(i).setVisibility(visibility);
        }
    }


    protected void saveValue(String value){
        if(SP != null) {
            String key = getKey();
            if(key == null || key.length() <= 0){
                return;
            }
            SP.edit().putString(getKey() , value).commit();
        }
    }

    protected String getValue(){

        return SP.getString(getKey(), "null");
    }

    protected String getKey(){
        String key = null;

        if(tv_title.getText() != null ){
            key = tv_title.getText().toString();
        }
        if(key == null || key.trim().length() <= 0){
            if(getTag() != null){
                key = getTag().toString();
            }
        }
        if(key == null || key.trim().length() <= 0){
            key = getId() + "";
        }

        return key;
    }


    OnClickListenerCallBack clickListener = null;
    public interface OnClickListenerCallBack{
        void onClick(View view);
    }
}
