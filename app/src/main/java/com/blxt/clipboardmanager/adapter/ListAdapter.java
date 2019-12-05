package com.blxt.clipboardmanager.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blxt.clipboardmanager.R;
import com.blxt.clipboardmanager.bean.ClipTextBean;
import com.blxt.clipboardmanager.unit.ClipboardItem;
import com.blxt.utils.KeyBoardUtils;
import com.blxt.utils.ShareHelp;
import com.blxt.utils.TimeTools;

import java.util.Calendar;
import java.util.List;

public class ListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater mInflater; // 布局
    int resourceId; // 布局id

    List<ClipTextBean> mList;
    ViewHolder holder_last = null;

    Calendar calendar;

    public ListAdapter(Context context, List<ClipTextBean> mList, int resourceId) {
        this.context = context;
        this.mList = mList;
        this.resourceId = resourceId;
        this.mInflater=LayoutInflater.from(context);
        calendar = Calendar.getInstance();

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(mList.size() - position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;

        if (convertView==null){
            convertView=mInflater.inflate(resourceId,null);
            holder=new ViewHolder(convertView, position);
            convertView.setTag(holder);
        }
        else{
            holder= (ViewHolder) convertView.getTag();
          //  Log.d("getView", "" + holder.position);
        }


        final ViewHolder holder1 = holder;
        holder.tvShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(holder_last == null){
                            holder_last = holder1;
                        }
                        // 关闭上次item
                        if(!holder_last.equals(holder1)){
                            holder_last.close();
                        }

                        holder1.open();
                    }
                });

        holder.editText.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if(holder_last == null){
                        holder_last = holder1;
                    }
                    // 关闭上次item
                    if(!holder_last.equals(holder1)){
                        holder_last.close();
                    }

                  //  holder1.open();
                } else {
                    // 此处为失去焦点时的处理内容
                    if(holder_last == null){
                        holder_last = holder1;
                    }
                    // 关闭上次item
                    if(holder_last.equals(holder1)){
                        holder_last.close();
                    }

                   // holder_last.close();
                }
            }
        });

        holder.editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder_last == null){
                    holder_last = holder1;
                }
                // 关闭上次item
                if(!holder_last.equals(holder1)){
                    holder_last.close();
                }

                holder1.open();
            }
        });

        holder.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 修改
                ClipTextBean bean = (ClipTextBean)getItem(position);
                ClipTextBean beanNew = new ClipTextBean(holder1.editText.getText().toString());
                beanNew.setId(bean.getId());
                mList = ClipboardItem.change(context, beanNew);
                holder1.close();
            }
        });

        holder.btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(holder1.editText.getText().toString().trim());
                holder1.close();
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList = ClipboardItem.remove(context, (ClipTextBean)getItem(position));
                // 刷新
                notifyDataSetChanged();

            }
        });


        if(position == 0){
            holder.ivLab.setVisibility(View.VISIBLE);
        }else{
            holder.ivLab.setVisibility(View.INVISIBLE);
        }

        ClipTextBean cpEntity = (ClipTextBean)getItem(position);

        holder.tvShow.setText(cpEntity.getContent());
        holder.editText.setText(cpEntity.getContent());
        holder.btnDelete.setVisibility(View.VISIBLE);
        holder.layoutBtnDoMore.setVisibility(View.GONE);

        if(cpEntity.getTime() != null && !cpEntity.getTime().trim().equals("null")){
            try {
                long t = Long.parseLong(cpEntity.getTime().trim());
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(t);
                holder.tvTime.setText(TimeTools.compareTime(c, calendar));
            }catch (Exception e){
                holder.tvTime.setText("null");
            }
        }
        else{
            holder.tvTime.setText("很久之前");
        }

        return convertView;
    }

    //

    public  final class ViewHolder implements View.OnClickListener {
        int position;
        private EditText editText;
        private TextView tvShow;

        private ImageView ivLab;
        private TextView tvTime;
        private ImageButton btnShare;
        private ImageButton btnCopy;
        private ImageButton btnOk;
        private ImageButton btnDelete;

        private LinearLayout layoutEt;
        private LinearLayout layoutBtnDoMore;
        private LinearLayout layoutEdit;
        private LinearLayout layoutShow;

        public ViewHolder(View view, int position){
            this.position = position;
            tvShow = view.findViewById(R.id.tv_show);
            editText = view.findViewById(R.id.editText);
            ivLab = view.findViewById(R.id.iv_lab);
            tvTime = view.findViewById(R.id.tv_time);
            btnShare =  view.findViewById(R.id.btn_share);
            btnCopy = view.findViewById(R.id.btn_copy);
            btnOk =  view.findViewById(R.id.btn_ok);
            btnDelete = view.findViewById(R.id.btn_delete);
            layoutBtnDoMore = view.findViewById(R.id.layout_btn_do_more);
            layoutBtnDoMore.setVisibility(View.GONE);
            layoutEt = view.findViewById(R.id.layout_et);

            layoutEdit = view.findViewById(R.id.layout_edit);
            layoutShow = view.findViewById(R.id.layout_show);


            layoutShow.setVisibility(View.VISIBLE);
            tvShow.setVisibility(View.VISIBLE);
            editText.setVisibility(View.GONE);
            layoutEdit.setVisibility(View.GONE);


            btnShare.setOnClickListener(this);

            close();
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
//                case R.id.editText:
//                    editText.setEnabled(true);
//                    layoutBtnDoMore.setVisibility(View.VISIBLE);
//                    btnDelete.setVisibility(View.GONE);
//                    break;
                case R.id.btn_share:
                    ShareHelp.share(v.getContext(), "最好用的剪贴板助手" , editText.getText().toString());
                    close();
                    break;
//                case R.id.btn_copy:
//                  //  editText.setEnabled(true);
//                    break;
//                case R.id.btn_ok:
//                    editText.setEnabled(false);
//                    layoutBtnDoMore.setVisibility(View.GONE);
//                    btnDelete.setVisibility(View.VISIBLE);
//                    break;
//                case R.id.btn_more:
//                 //   editText.setEnabled(true);
//                    break;
                    default:
            }
        }

        public void open(){
            layoutShow.setVisibility(View.GONE);
            tvShow.setVisibility(View.GONE);
            editText.setVisibility(View.VISIBLE);
            layoutEdit.setVisibility(View.VISIBLE);
            editText.setMinLines(10);
            editText.setMaxLines(11);
            layoutBtnDoMore.setVisibility(View.VISIBLE);
        //    btnDelete.setVisibility(View.GONE);
//            // 软键盘
//            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
//            KeyBoardUtils.openKeybord(editText,context);
        }

        public void close(){

            tvShow.setText(editText.getText());
            layoutShow.setVisibility(View.VISIBLE);
            tvShow.setVisibility(View.VISIBLE);
            layoutEdit.setVisibility(View.GONE);
            editText.setMinLines(2);
            editText.setMaxLines(2);
            editText.setVisibility(View.GONE);
            layoutBtnDoMore.setVisibility(View.GONE);
         //   btnDelete.setVisibility(View.VISIBLE);
            KeyBoardUtils.closeKeybord(editText,context);

        }


        @Override
        public boolean equals(Object obj) {
            return this.position ==((ViewHolder)obj).position;
        }
    }


}
