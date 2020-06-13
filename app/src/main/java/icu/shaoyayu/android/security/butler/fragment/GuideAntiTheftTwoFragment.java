package icu.shaoyayu.android.security.butler.fragment;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.butler.dialog.InitialPasswordDialog;
import icu.shaoyayu.android.security.butler.util.PackageInfoUtil;
import icu.shaoyayu.android.security.common.fragment.CustomizeFragment;
import icu.shaoyayu.android.security.presenter.service.AntiTheftServiceImpl;

/**
 * @author shaoyayu
 * 绑定SIM卡
 */
public class GuideAntiTheftTwoFragment extends CustomizeFragment {

    private Switch swIsBindingSim;
    private AntiTheftServiceImpl antiTheftService;
    private boolean bindingSIM = false;

    public static Fragment newInstance() {
        return new GuideAntiTheftTwoFragment();
    }

    @Override
    protected int getInterfaceResourceId() {
        return R.layout.fragment_guide_anti_theft_two;
    }


    @Override
    protected void initTheControl(View view) {
        super.initTheControl(view);
        swIsBindingSim = mRoot.findViewById(R.id.sw_is_binding_sim);
        antiTheftService = new AntiTheftServiceImpl(getContext());
    }

    @Override
    protected void initData() {
        super.initData();
        bindingSIM = antiTheftService.gutIsBindingSim();
        swIsBindingSim.setChecked(bindingSIM);
    }

    @Override
    protected void initControlBinding() {
        super.initControlBinding();
        swIsBindingSim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //开启绑定.弹出初始化密码窗口 InitialPasswordDialog
                    final InitialPasswordDialog dialog = new InitialPasswordDialog(getContext());
                    dialog.setTitle("提示");
                    dialog.setMessage("初始化你的防盗密码");
                    dialog.setYesOnclickListener("确定", new InitialPasswordDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick(String promptPassword, String confirmPassword) {
                            if (promptPassword.equals(confirmPassword)) {
                                //两次密码相同，保存密码，获取SIM卡
                                String sim = PackageInfoUtil.getSIMCardNumber(getContext());
                                if (sim==null){
                                    //没有获取到权限
                                    showToast("请授权获取电话状态");
                                    swIsBindingSim.setChecked(false);
                                    dialog.dismiss();
                                    return;
                                }
                                Log.d(TAG,"SIM:"+sim);
                                //开启防盗状态，保存密码，开启绑定状态，保存SIM号码
                                antiTheftService.setUpAntiTheft();
                                antiTheftService.setUpAntiTheft();
                                antiTheftService.setUpPassword(confirmPassword);
                                antiTheftService.setUpIsBindingSim();
                                antiTheftService.setUpSIMNumber(sim);
                                dialog.dismiss();
                            }else {
                                swIsBindingSim.setChecked(false);
                                showToast("两次密码不一致");
                            }
                        }
                    });
                    dialog.setNoOnclickListener("取消", new InitialPasswordDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            showToast("点击取消");
                            swIsBindingSim.setChecked(false);
                           dialog.dismiss();
                        }
                    });
                    dialog.show();
                }else {
                    //关闭绑定，绑定、弹出密码确定框
                    antiTheftService.removeIsBindingSim();
                }
            }
        });
    }
}
