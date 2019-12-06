package com.blxt.clipboardmanager.server;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.blxt.clipboardmanager.Model;
import com.blxt.clipboardmanager.bean.ClipTextBean;
import com.blxt.clipboardmanager.receiver.RestartReceiver;
import com.blxt.clipboardmanager.unit.NotificationUtil;
import com.blxt.greendao.DaoUtils;
import com.blxt.utils.IntentJump;
import com.blxt.utils.ShareHelp;

/**
 * 剪辑板监听服务
 * 创建监听和剪贴板操作,如分享,搜索,拨打电话等
 */
public class ClipListenServer extends Service {
    final String TAG = "ClipListenServer";
    public static boolean isCopy = true;
    static Service intentFilter;
    static ClipboardManager clipboardManager;
    Context context;

    public static void start(Context context) {
        if(intentFilter == null){
            Intent starter = new Intent(context, ClipListenServer.class);
            starter.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            context.startService(starter);
        }
    }

    public static RestartReceiver mRService = new RestartReceiver();
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        intentFilter = this;

        Log.i(TAG,"onCreate");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mRService, filter);

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onPrimaryClipChanged() {
                // if(isCopy)
                {
                    ClipData data = clipboardManager.getPrimaryClip();
                    ClipData.Item item = data.getItemAt(0);
                    if(data == null){
                        Log.d(TAG, "剪辑板空");
                        return;
                    }
                    try {
                        Model.clipboardStr_now = new ClipTextBean(item.getText().toString().trim());
                    }catch (Exception e){
                        Log.i(TAG, "获取剪辑板失败");
                        return;
                    }
                    if (Model.clipboardStr_now == null || Model.clipboardStr_now.isEmpty()){
                        return;
                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            DaoUtils daoUtils = new DaoUtils(context);
                            if(daoUtils.queryClipByQueryBuilder(Model.clipboardStr_now.getIdS()).size() <= 1){
                                daoUtils.insertClip(Model.clipboardStr_now);
                            }
                        }
                    }).start();
                    NotificationUtil.showNotification(context,"剪贴板",Model.clipboardStr_now.getContent(),true);
                }
            }
        });

    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"onBind");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        intentFilter = null;
        Intent intent=new Intent();
        intent.setAction("RestartReceiver");
        this.sendBroadcast(intent);
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;

        if (intent != null) {
            String str = intent.getStringExtra("按钮");

            if(str == null){
                return super.onStartCommand(intent, flags, startId);
            }

            NotificationUtil.collapseStatusBar(this);// 关闭通知栏

            if (str.equals("分享")){
                Log.i("MoreService","分享");
                ShareHelp.share(this, "最好用的剪贴板助手" , Model.clipboardStr_now.getContent());
            }
            else if(str.equals("关闭")){
                Log.i("MoreService","关闭");
                NotificationUtil.close();
            }
            else if(str.equals("编辑")){
                Log.i("MoreService","编辑");
                FloatingServer.start(this);
             //   showOver();
//                DragFloatActionButton floatBtn = new DragFloatActionButton(context);
//
//                WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//                // 设置LayoutParams(全局变量）相关参数
//                WindowManager.LayoutParams windowManagerParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_TOAST,
//                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                        PixelFormat.TRANSLUCENT);
//                /**
//                 * 注意，flag的值可以为：
//                 * 下面的flags属性的效果形同“锁定”。
//                 * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
//                 * LayoutParams.FLAG_NOT_TOUCH_MODAL 不影响后面的事件
//                 * LayoutParams.FLAG_NOT_FOCUSABLE  不可聚焦
//                 * LayoutParams.FLAG_NOT_TOUCHABLE 不可触摸
//                 */
//                // 调整悬浮窗口至左上角，便于调整坐标
//                windowManagerParams.gravity = Gravity.LEFT | Gravity.TOP;
//                // 以屏幕左上角为原点，设置x、y初始值
//                windowManagerParams.x = 0;
//                windowManagerParams.y = 0;
//                // 设置悬浮窗口长宽数据
//                floatBtn.measure(0, 0);
//                floatBtn.setOriginWidth(floatBtn.getMeasuredWidth() - 50);
//                windowManagerParams.width = floatBtn.getOriginWidth();
//                windowManagerParams.height = windowManagerParams.width;
//                // 显示myFloatView图像
//                windowManager.addView(floatBtn, windowManagerParams);
            }
            else{
                str = Model.clipboardStr_now.getContent();
                Log.i("MoreService","更多");
                if(str != null){
                    switch (Model.ClipboardContentId){
                        case Model.CCID_DO_WEB:
                            // 打开网址
                            IntentJump.openWeb(str,this);
                            break;
                        case Model.CCID_DO_PHONE:
                            // 拨打电话
                            IntentJump.callPhone(str,this);
                            break;
                        case  Model.CCID_DO_OTHER:
                            // 百度搜索
                            IntentJump.goBaiDu(str,this);
                        default:
                    }
                }

            }


        }
        return super.onStartCommand(intent, flags, startId);
    }


}
