package icu.shaoyayu.android.security.butler.activity;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.butler.view.SimpleMenu;
import icu.shaoyayu.android.security.common.activity.SecurityFunctionActivity;

/**
 * @author shaoyayu
 */
public class CommGuardActivity extends SecurityFunctionActivity {

    private SimpleMenu smCommGuard;

    @Override
    protected void initWindows() {

    }

    @Override
    protected int getInterfaceResourceId() {
        return R.layout.activity_comm_guard;
    }

    @Override
    protected void initTheControl() {
        super.initTheControl();
        smCommGuard = findViewById(R.id.sm_comm_guard);
    }

    @Override
    protected void initData() {
        super.initData();
        smCommGuard.setTvMenuThemeText("通讯卫士");
    }
}
