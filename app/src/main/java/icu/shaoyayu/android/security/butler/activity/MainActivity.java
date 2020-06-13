package icu.shaoyayu.android.security.butler.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.butler.util.PackageInfoUtil;
import icu.shaoyayu.android.security.common.activity.CustomizeActivity;
import icu.shaoyayu.android.security.presenter.util.PrefUtils;

/**
 * @author shaoyayu
 * 应用程序的启动见面
 */
public class MainActivity extends CustomizeActivity {

    private TextView tvVersionStatement,tvTimeCountdown;

    private long pauseTime = 5000;

    private final int COUNTDWON_CODE = 0x1000;

    private boolean whetherToStart = false;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case COUNTDWON_CODE :
                    int ti = (int)msg.obj;
                    tvTimeCountdown.setText("剩余时间："+ti);
                    if (ti==1){
                        //跳转下个页面，销毁当前的Activity
                        if (!whetherToStart){
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            finish();
                            whetherToStart=true;
                        }
                    }
                    break;
            }
        }
    };

    private Thread thread = new Thread(new UpdateCountdown());


    @Override
    protected void initWindows() {

    }

    @Override
    protected int getInterfaceResourceId() {
        return R.layout.activity_main;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initTheControl() {
        super.initTheControl();
        tvVersionStatement = findViewById(R.id.tv_version_statement);
        tvTimeCountdown = findViewById(R.id.tv_time_countdown);
    }

    @Override
    protected void initControlBinding() {
        super.initControlBinding();
        tvTimeCountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = mHandler.obtainMessage();
                message.what = COUNTDWON_CODE;
                message.obj = 1;
                mHandler.sendMessage(message);
                finish();
            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        super.initData();
        String s = getString(R.string.tv_version_statement);
        tvVersionStatement.setText(s+ PackageInfoUtil.getVersionNumber(getApplicationContext()));
        //答应一下开机日志
        String bootLog = PrefUtils.getString("boot",null,this);
        if (bootLog==null){
            Log.d(TAG,"开机没有被监听到");
        }else {
            Log.d(TAG,"开机被监听到："+bootLog);
        }
    }

    /**
     * 注意在Activity显示的时候开始计时
     */
    @Override
    protected void onResume() {
        super.onResume();
        thread.start();
    }

    /**
     * 暂停的时候需要将暂停
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 退出
     */
    @Override
    public void finish() {
        super.finish();
        //结束倒计时
    }

    class UpdateCountdown implements Runnable{
        @Override
        public void run() {
            int pauseSeconds = (int) pauseTime/1000;
            for (int i = 0; i < pauseSeconds; i++) {
                try {
                    if (whetherToStart){
                        break;
                    }
                    Message message = mHandler.obtainMessage();
                    message.what = COUNTDWON_CODE;
                    message.obj = 5-i;
                    mHandler.sendMessage(message);
                    Thread.sleep(1000);
                    pauseTime = pauseTime - 1000;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //更新上面的数据
            }
        }
    }
}
