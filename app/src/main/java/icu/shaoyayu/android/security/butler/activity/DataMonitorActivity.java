package icu.shaoyayu.android.security.butler.activity;

import android.os.Build;

import androidx.annotation.RequiresApi;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.butler.view.SimpleMenu;
import icu.shaoyayu.android.security.common.activity.SecurityFunctionActivity;

/**
 * @author shaoyayu
 * 流量监控
 */
public class DataMonitorActivity extends SecurityFunctionActivity {

    private SimpleMenu smDataMonitor;

    @Override
    protected void initWindows() {

    }

    @Override
    protected int getInterfaceResourceId() {
        return R.layout.activity_data_monitor;
    }

    @Override
    protected void initTheControl() {
        super.initTheControl();
        smDataMonitor = findViewById(R.id.sm_data_monitor);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initData() {
        super.initData();
        smDataMonitor.setTvMenuThemeText("流量监控");
    }

    //获取流量统计


}
