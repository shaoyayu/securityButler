package icu.shaoyayu.android.security.butler.activity;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.butler.view.SimpleMenu;
import icu.shaoyayu.android.security.common.activity.SecurityFunctionActivity;

public class SettingCenterActivity extends SecurityFunctionActivity {

    private SimpleMenu smSettingCenter;

    @Override
    protected void initWindows() {

    }

    @Override
    protected int getInterfaceResourceId() {
        return R.layout.activity_setting_center;
    }

    @Override
    protected void initTheControl() {
        super.initTheControl();
        smSettingCenter = findViewById(R.id.sm_setting_center);
    }

    @Override
    protected void initData() {
        super.initData();
        smSettingCenter.setTvMenuThemeText("设置中心");
    }
}
