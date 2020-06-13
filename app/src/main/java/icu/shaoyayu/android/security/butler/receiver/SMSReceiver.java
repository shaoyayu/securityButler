package icu.shaoyayu.android.security.butler.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.List;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.butler.service.LocationService;
import icu.shaoyayu.android.security.common.bean.AddressBookBean;
import icu.shaoyayu.android.security.presenter.service.AntiTheftServiceImpl;
import icu.shaoyayu.android.security.presenter.service.SMSServiceImpl;

import static android.content.Context.DEVICE_POLICY_SERVICE;

/**
 * @author shaoyayu
 */
public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG = "SMSReceiver";

    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    private SMSServiceImpl smsService;
    private AntiTheftServiceImpl antiTheftService;

    private ComponentName administratorComponent;
    private DevicePolicyManager mDM;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
            smsService = new SMSServiceImpl(context);
            antiTheftService = new AntiTheftServiceImpl(context);
            SmsMessage[] messages = smsService.getMessagesFromIntent(intent);
            for (SmsMessage message : messages) {
                /*Log.i(TAG, message.getOriginatingAddress() + " : " +
                        message.getDisplayOriginatingAddress() + " : " +
                        message.getDisplayMessageBody() + " : " +
                        message.getTimestampMillis());*/
                //本应该判断手机是不是在被盗的情况下执行指令的，测试期间无法完成
                List<AddressBookBean> contactPerson = antiTheftService.getContactPerson();
                if (contactPerson==null){
                    return;
                }
                for (AddressBookBean person : contactPerson) {
                    if (person.getPhone().equals(message.getDisplayOriginatingAddress())){
                        //指令判断
                        if (context.getString(R.string.instruction_location).equals(message.getDisplayMessageBody())){
                            //发送定位
                            Log.d(TAG,"发送位置信息");
                            //启动一个服务发送位置信息
                            context.startService(new Intent(context, LocationService.class));
                        }else if (context.getString(R.string.instruction_alarm).equals(message.getDisplayMessageBody())){
                            Log.d(TAG,"播放报警");
                            MediaPlayer player = MediaPlayer.create(context,R.raw.isp);
                            player.setVolume(1.0f,1.0f);
                            player.setLooping(true);
                            player.start();
                            /*abortBroadcast();*/
                        }else if (context.getString(R.string.instruction_wipedate).equals(message.getDisplayMessageBody())){
                            Log.d(TAG,"删除文件");
                            administratorComponent = new ComponentName(context, AdminReceiver.class);
                            mDM = (DevicePolicyManager) context.getSystemService(DEVICE_POLICY_SERVICE);
                            //判断是否有开启管理员
                            if (mDM.isAdminActive(administratorComponent)){
                                //删除数据
                                mDM.wipeData(0);
                            }else {
                                Log.d(TAG,"尚未开启管理员权限");
                            }
                        }else if (context.getString(R.string.instruction_lockSereen).equals(message.getDisplayMessageBody())){
                            Log.d(TAG,"远程锁屏");
                            administratorComponent = new ComponentName(context, AdminReceiver.class);
                            mDM = (DevicePolicyManager) context.getSystemService(DEVICE_POLICY_SERVICE);
                            //判断是否有开启管理员
                            if (mDM.isAdminActive(administratorComponent)){
                                mDM.lockNow();
                                try {
                                    //能够重新设置密码，但是会抛出异常。应该是版本问题，尚未解决
                                    mDM.resetPassword("2222",0);
                                }catch (SecurityException r){
                                    Log.d(TAG,"java.lang.SecurityException: Admin cannot change current password");
                                }
                            }else {
                                Log.d(TAG,"尚未开启管理员权限");
                            }
                        }else {
                            Log.d(TAG,"普通短信");
                        }
                    }
                }
            }
        }
    }


}
