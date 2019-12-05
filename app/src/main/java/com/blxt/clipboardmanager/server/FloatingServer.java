package com.blxt.clipboardmanager.server;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.blxt.clipboardmanager.R;
import com.blxt.clipboardmanager.viewholder.ClipItemManagerViewHolder;

public class FloatingServer extends Service {

    private static final String TAG = "MainService";
    Context context;


    static FloatingServer intentFilter;
    WindowManager windowManager;
    WindowManager.LayoutParams layoutParams;
    View rootView;


    public FloatingServer() {
    }

    public static void start(Context context){
        if(intentFilter == null){
            Intent starter = new Intent(context, FloatingServer.class);
            starter.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            context.startService(starter);
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;

        // 新建悬浮窗控件

        rootView = initView();
        windowManager = initWindows();
        layoutParams = initWinParams();

        windowManager.addView(rootView, layoutParams);

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 初始化viewHolder
     * @return
     */
    private View initView(){
        View rootView = LayoutInflater.from(this).inflate(R.layout.win_suspen, null);
        rootView.setOnTouchListener(new FloatingOnTouchListener());

        ClipItemManagerViewHolder clipItemManagerViewHolder = new ClipItemManagerViewHolder(rootView);
        clipItemManagerViewHolder.setOnListener(new ClipItemManagerViewHolder.OnListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();

                switch (id){
                    case R.id.btn_find:
                        break;
                    case R.id.bnt_close: // 结束窗口
                        onDestroy();
                        break;
                    default:
                        break;
                }
            }
        });

        return rootView;
    }

    /**
     * 初始化窗口
     * @return
     */
    private WindowManager initWindows() {

        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // 设置LayoutParams(全局变量）相关参数
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        //宽高自适应
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.x = 20 ;
        layoutParams.y = 20 ;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP ;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.format = PixelFormat.RGBA_8888 | PixelFormat.TRANSLUCENT;

        return windowManager;
    }

    /**
     * 初始化layout参数
     * @return
     */
    private WindowManager.LayoutParams initWinParams(){
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();


        //  兼容android O
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        //宽高自适应
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.x = 20 ;
        layoutParams.y = 20 ;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP ;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.format = PixelFormat.RGBA_8888 | PixelFormat.TRANSLUCENT;

        return layoutParams;
    }


    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    // 更新悬浮窗控件布局
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                default:
                    break;
            }
            return false;
        }
    }


    @Override
    public void onDestroy()
    {
        intentFilter = null;
        windowManager.removeView(rootView);
        super.onDestroy();
    }


}
