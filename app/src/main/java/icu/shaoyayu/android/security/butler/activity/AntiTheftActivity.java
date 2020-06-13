package icu.shaoyayu.android.security.butler.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.butler.dialog.InputPasswordDialog;
import icu.shaoyayu.android.security.butler.receiver.AdminReceiver;
import icu.shaoyayu.android.security.butler.view.SimpleMenu;
import icu.shaoyayu.android.security.common.activity.SecurityFunctionActivity;
import icu.shaoyayu.android.security.presenter.service.AntiTheftServiceImpl;

/**
 * 手机防盗功能，
 * 1、检查是否开启房东啊功能
 * 2、检查是不是配置联系人
 * 3、检查是不是开启手机保护
 */
public class AntiTheftActivity extends SecurityFunctionActivity {

    private SimpleMenu smAntiTheft;
    private AntiTheftServiceImpl antiTheftService;
    private Switch swIsAntiTheft;
    private TextView tvIsAntiTheft;
    private Button btActivateAdmin,btSetAntiTheft;
    private ComponentName administratorComponent;
    private DevicePolicyManager mDM;
    private boolean isActivateAdmin = false;
    @Override
    protected void initWindows() {
        antiTheftService = new AntiTheftServiceImpl(getApplicationContext());
    }

    @Override
    protected int getInterfaceResourceId() {
        return R.layout.activity_anti_theft;
    }

    @Override
    protected void initTheControl() {
        super.initTheControl();
        smAntiTheft = findViewById(R.id.sm_anti_theft);
        tvIsAntiTheft = findViewById(R.id.tv_is_anti_theft);
        swIsAntiTheft = findViewById(R.id.sw_is_anti_theft);
        btActivateAdmin = findViewById(R.id.bt_activate_admin);
        btSetAntiTheft = findViewById(R.id.bt_set_up_anti_theft);
        administratorComponent = new ComponentName(getApplication(), AdminReceiver.class);
        mDM = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
    }

    //注意的是回到见面的时候有需要查询初始化数据
    @Override
    protected void initData() {
        super.initData();
        smAntiTheft.setTvMenuThemeText("手机防盗");
        //测试使用
/*        if (true){
            startActivity(new Intent(getApplicationContext(), GuideAntiTheftActivity.class));
        }*/
        //判断是不是开启房东啊功能
        if (antiTheftService.isAntiTheft()&&antiTheftService.getPassword()!=null){
            swIsAntiTheft.setChecked(true);

            showPasswordInput(new PasswordListener() {
                @Override
                public void onCancel() {
                    quitActivity();
                }

                @Override
                public void onYes() {

                }

                @Override
                public void onNo() {
                    quitActivity();
                }
            });
        }else {
            tvIsAntiTheft.setText("开启防盗...");
            swIsAntiTheft.setChecked(false);
        }
        //判断管理员是不是被激活，激活就不会显示控件
        if (mDM.isAdminActive(administratorComponent)){
            //如果开启的话，讲控件隐藏 设置为拥有管理员权限
            isActivateAdmin = true;
            antiTheftService.setUpAdmin();
            btActivateAdmin.setVisibility(View.GONE);
        }else {
            //点击控件前往激活
            isActivateAdmin = false;
            btActivateAdmin.setText("激活手机管理员");
            antiTheftService.removeAdmin();
        }
        if (!isActivateAdmin){
            btSetAntiTheft.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initControlBinding() {
        super.initControlBinding();
        swIsAntiTheft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //进入防盗页面引导 GuideAntiTheftActivity
                    startActivity(new Intent(getApplicationContext(), GuideAntiTheftActivity.class));
                }else {
                    showPasswordInput(new PasswordListener() {
                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onYes() {
                            //删除防盗状态
                            antiTheftService.removeAntiTheft();
                            //移除手机管理员
                        }
                        @Override
                        public void onNo() {
                            //密码输入错误
                        }
                    });
                }
            }
        });
        btActivateAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //激活管理员
                if (isActivateAdmin){
                    mDM.lockNow();
                    try {
                        //能够重新设置密码，但是会抛出异常。应该是版本问题，尚未解决
                        mDM.resetPassword("2222",0);
                    }catch (SecurityException r){
                        Log.d(TAG,"java.lang.SecurityException: Admin cannot change current password");
                    }

                }else {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,administratorComponent);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,R.string.device_admin_description);
                    startActivity(intent);
                }
            }
        });

        btSetAntiTheft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isActivateAdmin){
                    startActivity(new Intent(getApplicationContext(), GuideAntiTheftActivity.class));
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    private void quitActivity(){
        this.finish();
    }

    private void showPasswordInput(final PasswordListener listener){
        final InputPasswordDialog dialog = new InputPasswordDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("初始化你的防盗密码");
        dialog.setYesOnclickListener("确定", new InputPasswordDialog.onYesOnclickListener() {
            @Override
            public void onYesClick(String confirmPassword) {
                boolean b = antiTheftService.comparePasswords(confirmPassword);
                if (b){
                    showToast("密码正确");
                    dialog.dismiss();
                    listener.onYes();
                }else {
                    listener.onNo();
                    dialog.dismiss();
                    showToast("密码不匹配");
                }
            }
        });
        dialog.setNoOnclickListener("取消", new InputPasswordDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                showToast("点击取消");
                dialog.dismiss();
                listener.onCancel();
            }
        });
        dialog.show();
    }


    interface PasswordListener{
        void onCancel();
        void onYes();
        void onNo();
    }

}
