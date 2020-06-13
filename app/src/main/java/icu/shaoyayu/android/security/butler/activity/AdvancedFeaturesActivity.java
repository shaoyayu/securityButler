package icu.shaoyayu.android.security.butler.activity;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.butler.view.SimpleMenu;
import icu.shaoyayu.android.security.common.activity.SecurityFunctionActivity;

/**
 * @author shaoyayu
 * 高级功能
 */
public class AdvancedFeaturesActivity extends SecurityFunctionActivity {

    private SimpleMenu smAdvancedFeatures;

    @Override
    protected void initWindows() {

    }

    @Override
    protected int getInterfaceResourceId() {
        return R.layout.activity_advanced_features;
    }

    @Override
    protected void initTheControl() {
        super.initTheControl();
        smAdvancedFeatures = findViewById(R.id.sm_advanced_features);
    }

    @Override
    protected void initData() {
        super.initData();
        smAdvancedFeatures.setTvMenuThemeText("高级功能");
    }
}
