package com.blxt.clipboardmanager.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.blxt.clipboardmanager.server.NotificationCollectorService;

import java.util.LinkedList;
import java.util.List;

/**
 * 权限申请
 */
public abstract class PermissionBaseActivity extends AppCompatActivity {

    Context context;
    Activity mActivity;

    private static final String TAG = "PermissionBaseActivity";
    private boolean isDefaultDialog = false;
    private static final int REQUST_CODE = 101;
    private String[] mPermissions = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.SYSTEM_ALERT_WINDOW
            //, Manifest.permission.TYPE_APPLICATION_OVERLAY
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.win_suspen);
        mActivity = this;
        context = this;

        initUI();
        initPermission();
        initData();
    }

    public abstract void initUI();

    public abstract void initData();

    /***
     * 初始化权限
     * @return
     */
    boolean initPermission(){
        Log.i(TAG,"检查权限");

        // 悬浮窗
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                //若没有权限，提示获取.
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                Toast.makeText(this,"需要取得权限以使用悬浮窗",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }

        // 通知栏使用权
        String string = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.CUPCAKE) {
            string = Settings.Secure.getString(getContentResolver(),
                    "enabled_notification_listeners");
        }
        if (string == null || !string.contains(NotificationCollectorService.class.getName())) {
            startActivity(new Intent(
                    "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }


        boolean isPermission = false;
        for(String s : mPermissions){
            if(!isPermissionGrant(s)){
                isPermission = true;
                break;
            }
        }
        if(isPermission){
            checkPermission();
        }

        return false;
    }

    /**
     * 检查 是否 有 xx权限
     * @param permission
     * @return
     */
    public boolean isPermissionGrant(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return mActivity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    /**
     * 发起请求权限
     * @return
     */
    public boolean checkPermission() {


        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }

        if (mPermissions == null || mActivity == null || mPermissions == null) {
            return false;
        }

        List<String> permissionToRequestList = new LinkedList<String>();
        for (String permission : mPermissions) {
            if(mActivity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionToRequestList.add(permission);
            }
        }
        String[] permissionToRequest = (String[])permissionToRequestList.toArray(new String[permissionToRequestList.size()]);
        if(permissionToRequest.length > 0){
            mActivity.requestPermissions(permissionToRequest, REQUST_CODE);
            if (mCallback != null) {
                mCallback.onRequest();
            }
        } else {
            if (mCallback != null) {
                mCallback.onGranted();
            }
        }

        return false;
    }


    private PermissionCheckCallback mCallback = new PermissionCheckCallback() {
        @Override
        public void onRequest() {
            Log.i(TAG,"onRequest");
        }

        @Override
        public void onGranted() {
            Log.i(TAG,"onGranted");
        }

        @Override
        public void onGrantSuccess() {
            Log.i(TAG,"授权成功");
        }

        @Override
        public void onGrantFail() {
            Log.i(TAG,"授权失败");
        }
    };

    /**
     * 结果回调接口
     */
    public interface PermissionCheckCallback {
        void onRequest();
        void onGranted();
        void onGrantSuccess();
        void onGrantFail();
    }



    /**
     * 权限结果回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mCallback != null) {
                        mCallback.onGrantSuccess();
                    }
                } else {
                    Log.v(TAG, "未检测到权限");
                    if (isDefaultDialog) {
                        popupWarningDialog();
                        return;
                    }
                    if (mCallback != null) {
                        mCallback.onGrantFail();
                    }
                }
                break;
            default:
                break;
        }
    }


    /**
     * 权限请求 提示 对话框
     */
    private void popupWarningDialog(){

        DialogInterface.OnClickListener dialogOnclicListener=new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case Dialog.BUTTON_POSITIVE:
                        if (mCallback != null) {
                            mCallback.onGranted();
                        }
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        mActivity.finish();
                        break;
                    default:
                        break;
                }
            }
        };

        int checkSelfPermission;
        try {
            checkSelfPermission = ActivityCompat.checkSelfPermission(mActivity, mPermissions[0]);
        } catch (RuntimeException e) {
            Toast.makeText(mActivity, "请打开必要权限", Toast.LENGTH_SHORT)
                    .show();
            Log.e(TAG, "RuntimeException:" + e.getMessage());
            return;
        }

        AlertDialog.Builder builder=new AlertDialog.Builder(mActivity);
        builder.setTitle("警告");
        builder.setMessage("请允许读写权限,以提供基础服务");
        builder.setPositiveButton("确认",dialogOnclicListener);
        builder.setNegativeButton("取消", dialogOnclicListener);
        builder.create().show();

    }

}
