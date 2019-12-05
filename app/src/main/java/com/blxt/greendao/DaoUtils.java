package com.blxt.greendao;

import android.content.Context;


import com.blxt.clipboardmanager.bean.ClipTextBean;
import com.blxt.greendao.gen.ClipTextBeanDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class DaoUtils
{
    private static final String TAG = DaoUtils.class.getSimpleName();
    private DaoManager mManager;

    public DaoUtils(Context context){
        mManager = DaoManager.getInstance();
        mManager.init(context);
    }

    /**
     * 完成meizi记录的插入，如果表未创建，先创建Meizi表
     * @param bean
     * @return
     */
    public boolean insertClip(ClipTextBean bean){
        boolean flag = false;
        List<ClipTextBean> list = queryClipByQueryBuilder(bean.getIdS());
        if(list.size() == 0){
            flag = mManager.getDaoSession().getClipTextBeanDao().insert(bean) == -1 ? false : true;

        }
        else{
            bean.setContent(list.get(0).getContent());
            bean.setIdS(list.get(0).getIdS());
            flag = updateClip(bean);
        }
        return flag;
    }

    /**
     * 插入多条数据，在子线程操作
     * @param beanList
     * @return
     */
    public boolean insertMultClip(final List<ClipTextBean> beanList) {
        boolean flag = false;
        try {
            mManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (ClipTextBean meizi : beanList) {
                        mManager.getDaoSession().insertOrReplace(meizi);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 修改一条数据
     * @param bean
     * @return
     */
    public boolean updateClip(ClipTextBean bean){
        boolean flag = false;
        try {
            mManager.getDaoSession().update(bean);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除单条记录
     * @param bean
     * @return
     */
    public boolean deleteClip(ClipTextBean bean){
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().delete(bean);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除所有记录
     * @return
     */
    public boolean deleteAll(){
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().deleteAll(ClipTextBean.class);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询所有记录
     * @return
     */
    public List<ClipTextBean> queryAllClip(){
        return mManager.getDaoSession().loadAll(ClipTextBean.class);
    }

    /**
     * 根据主键id查询记录
     * @param key
     * @return
     */
    public ClipTextBean queryClipById(long key){
        return mManager.getDaoSession().load(ClipTextBean.class, key);
    }

    /**
     * 使用native sql进行查询操作
     */
    public List<ClipTextBean> queryClipByNativeSql(String sql, String[] conditions){
        return mManager.getDaoSession().queryRaw(ClipTextBean.class, sql, conditions);
    }

    /**
     * 使用queryBuilder进行查询
     * @return
     */
    public List<ClipTextBean> queryClipByQueryBuilder(String ids){
        QueryBuilder<ClipTextBean> queryBuilder = mManager.getDaoSession().queryBuilder(ClipTextBean.class);
        return queryBuilder.where(ClipTextBeanDao.Properties.IdS.eq(ids)).list();
//        return queryBuilder.where(MeiziDao.Properties._id.ge(id)).list();
    }
}