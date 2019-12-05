package com.blxt.clipboardmanager.unit;

import android.content.Context;

import com.blxt.clipboardmanager.Model;
import com.blxt.clipboardmanager.bean.ClipTextBean;
import com.blxt.greendao.DaoUtils;

import java.util.List;


/***
 * 剪辑板文件读写
 * @author Zhang
 */
public class ClipboardItem {

    static String TAG = "保存";

    /**
     * 获取本地剪辑板历史
     * @return
     */
    public static List<ClipTextBean> getItem(Context context){
        ClipTextBean bean = new ClipTextBean(Model.clipboardStr_now.getContent());
        DaoUtils daoUtils = new DaoUtils(context);
        return daoUtils.queryAllClip();
    }


    /**
     * 修改
     */
    public static List<ClipTextBean> change(Context context, ClipTextBean beanOld){

        DaoUtils daoUtils = new DaoUtils(context);
        boolean flag  = daoUtils.updateClip(beanOld);
        return daoUtils.queryAllClip();
    }

    /**
     * 删除内容
     * */
    public static List<ClipTextBean> remove(Context context, ClipTextBean beanOld){

        DaoUtils daoUtils = new DaoUtils(context);
        daoUtils.deleteClip(beanOld);
        return daoUtils.queryAllClip();
    }


}
