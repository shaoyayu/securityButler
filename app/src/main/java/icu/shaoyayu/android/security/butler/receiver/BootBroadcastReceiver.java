package icu.shaoyayu.android.security.butler.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import icu.shaoyayu.android.security.butler.util.PackageInfoUtil;
import icu.shaoyayu.android.security.common.bean.AddressBookBean;
import icu.shaoyayu.android.security.presenter.service.AntiTheftServiceImpl;
import icu.shaoyayu.android.security.presenter.service.SMSServiceImpl;
import icu.shaoyayu.android.security.presenter.util.PrefUtils;

/**
 * @author shaoyayu
 * 注册一个手机开机监听
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "BootBroadcastReceiver";
    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        PrefUtils.putString("boot","手机系统消息"+System.currentTimeMillis()+":"+intent.getAction(),context);
        System.out.println("手机启动");
        if (intent.getAction().equals(ACTION_BOOT)) { //开机启动完成后，要做的事情
            PrefUtils.putString("boot","开机已经被监听到"+System.currentTimeMillis(),context);
            String sim = PackageInfoUtil.getSIMCardNumber(context);
            if (sim==null){
                //没有授权访问
                return;
            }
            AntiTheftServiceImpl antiTheftService = new AntiTheftServiceImpl(context);
            //判断手机是否开启防盗，保存有SIM卡号和紧急联系人
            boolean turnOnAntiTheft = antiTheftService.isAntiTheft();
            String saveSIM = antiTheftService.getSIMNumber();
            List<AddressBookBean> bookBeans = antiTheftService.getContactPerson();
            if (turnOnAntiTheft){
                if (saveSIM!=null && bookBeans!=null){
                    //判断SIM卡是否一致
                    if (sim.equals(saveSIM)){
                        Log.d(TAG,"两次SIM卡一致");
                        PrefUtils.putString("boot","两次SIM卡一致"+System.currentTimeMillis(),context);
                        return;
                    }else {
                        //发送报警短信到紧急联系人中,
                        SMSServiceImpl smsService = new SMSServiceImpl(context);
                        for (AddressBookBean bookBean : bookBeans) {
                            smsService.sendSMSMessage(bookBean.getPhone(),"我的手机被盗了，你是紧急联系人，可以通过以下指令帮我找回手机 \n ",false);
                        }
                    }
                }
            }
        }
    }
}