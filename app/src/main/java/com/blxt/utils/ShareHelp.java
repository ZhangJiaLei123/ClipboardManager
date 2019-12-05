package com.blxt.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.blxt.clipboardmanager.activity.MainActivity;
import com.blxt.clipboardmanager.unit.ThreadManager;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

public class ShareHelp {

    // 通用分享, 分享文本
    public static void share(Context context, String title, String msg) {

        Intent intentShare = new Intent(Intent.ACTION_SEND);

        intentShare.setType("text/plain");

        intentShare.putExtra(Intent.EXTRA_SUBJECT, "Share");
        intentShare.putExtra(Intent.EXTRA_TEXT, msg);

        intentShare.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent out = Intent.createChooser(intentShare, title);
        out.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(out);
    }


    /***
     * QQ 分享类型
     */
    int shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;

    /**
     * 分享到QQ
     * @param title 标题
     * @param msg   消息
     * @param isQZONE   是否直接分享到QQ空间
     */
    public void send(String title,String msg,boolean isQZONE) {

        int mExtarFlag = 0x00;
        // 提交
        final Bundle params = new Bundle();
        {
            params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://hysylixos.top:81/");
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY,msg);
        }

        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, "/storage/emulated/0/tencent/MicroMsg/wallet/luckyMoneyEffect/1/package/cover.png");

        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "剪贴板");
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, shareType);

        if(isQZONE){ // 分享到QQ空间
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        }
        else {
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, mExtarFlag);
        }

        doShareToQQ(params);
        return;
    }



    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
            if (shareType != QQShare.SHARE_TO_QQ_TYPE_IMAGE) {
               // QQUtil.toastMessage(MoreService.activity, "onCancel: ");
            }
        }
        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
           // QQUtil.toastMessage(MoreService.activity, "onComplete: " + response.toString());
        }
        @Override
        public void onError(UiError e) {
            // TODO Auto-generated method stub
          //  QQUtil.toastMessage(MoreService.activity, "onError: " + e.errorMessage, "e");
        }
    };

    private void doShareToQQ(final Bundle params) {
        // QQ分享要在主线程做
        ThreadManager.getMainHandler().post(new Runnable() {

            @Override
            public void run() {
                if (null != MainActivity.mTencent) {
                    MainActivity.mTencent.shareToQQ(MainActivity.activity, params, qqShareListener);
                }
            }
        });
    }


}
