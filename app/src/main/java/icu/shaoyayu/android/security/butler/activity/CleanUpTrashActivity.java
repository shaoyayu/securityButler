package icu.shaoyayu.android.security.butler.activity;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.butler.view.SimpleMenu;
import icu.shaoyayu.android.security.common.activity.SecurityFunctionActivity;

/**
 * @author shaoyayu
 * 清理垃圾功能
 */
public class CleanUpTrashActivity extends SecurityFunctionActivity {

    SimpleMenu smCleanUpTrash;

    @Override
    protected void initWindows() {

    }

    @Override
    protected int getInterfaceResourceId() {
        return R.layout.activity_clean_up_trash;
    }

    @Override
    protected void initTheControl() {
        super.initTheControl();
        smCleanUpTrash = findViewById(R.id.sm_clean_up_trash);
    }

    @Override
    protected void initData() {
        super.initData();
        smCleanUpTrash.setTvMenuThemeText("清理垃圾");
    }
}
