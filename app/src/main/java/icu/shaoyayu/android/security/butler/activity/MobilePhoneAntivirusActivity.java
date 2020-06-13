package icu.shaoyayu.android.security.butler.activity;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.butler.view.SimpleMenu;
import icu.shaoyayu.android.security.common.activity.SecurityFunctionActivity;

/**
 * @author shaoyayu
 */
public class MobilePhoneAntivirusActivity extends SecurityFunctionActivity {

    private SimpleMenu smMobilePhoneAntivirus;

    @Override
    protected void initWindows() {

    }

    @Override
    protected int getInterfaceResourceId() {
        return R.layout.activity_mobile_phone_antivirus;
    }

    @Override
    protected void initTheControl() {
        super.initTheControl();
        smMobilePhoneAntivirus = findViewById(R.id.sm_mobile_phone_antivirus);
    }

    @Override
    protected void initData() {
        super.initData();
        smMobilePhoneAntivirus.setTvMenuThemeText("手机杀毒");
    }
}