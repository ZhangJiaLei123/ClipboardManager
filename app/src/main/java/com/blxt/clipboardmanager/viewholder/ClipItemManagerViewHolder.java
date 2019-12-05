package com.blxt.clipboardmanager.viewholder;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.blxt.clipboardmanager.R;
import com.blxt.clipboardmanager.unit.ClipboardItem;
import com.blxt.clipboardmanager.adapter.ListAdapter;
import com.blxt.clipboardmanager.bean.ClipTextBean;

import java.util.List;

/**
 * 剪辑板内容管理视图
 */
public class ClipItemManagerViewHolder implements View.OnClickListener {
    private static final String TAG = "ClipItemManagerViewHolder";
    View viewRoot;
    Context context;

    private ImageButton btnFind;
    private ImageButton bntClose;
    private ListView lvList;
    List<ClipTextBean> list;

    OnListener onListener = null;

    public ClipItemManagerViewHolder(View viewRoot){
        context = viewRoot.getContext();
        findviewbyme(viewRoot);
        initList();
    }

    private void findviewbyme( View view){
        btnFind =  view.findViewById(R.id.btn_find);
        bntClose = view.findViewById(R.id.bnt_close);
        lvList = view.findViewById(R.id.lv_list);

        btnFind.setOnClickListener(this);
        bntClose.setOnClickListener(this);

    }

    public void onDestroy()
    {

    }

    private void initList(){
        list = ClipboardItem.getItem(context);

        Log.i(TAG,"" + list.size());

        //生成动态数组，并且转载数据

        //生成适配器，数组===》ListItem
        ListAdapter listAdapter = new ListAdapter(context,list,R.layout.item_cllipboard_edit);
        //添加并且显示
        lvList.setAdapter(listAdapter);

    }

    public void setOnListener(OnListener onListener){
        this.onListener = onListener;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.btn_find:
                break;
            case R.id.bnt_close: // 结束窗口
                Log.i("WinActivity","结束窗口");
                onDestroy();
                break;
            default:
                break;
        }

        if (onListener != null){
            onListener.onClick(view);
        }
    }

    public interface OnListener{
        void onClick(View view);
    }

}
