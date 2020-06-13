package icu.shaoyayu.android.security.presenter.service;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.List;

import icu.shaoyayu.android.security.common.bean.AddressBookBean;


/**
 * @author shaoyayu
 */
public class SMSServiceImpl {

    private static  final  String TAG = "SMSServiceImpl";

    private Context context;

    public SMSServiceImpl(Context context) {
        this.context = context;
    }

    /**
     * <uses-permission android:name="android.permission.SEND_SMS" />
     * @param phoneNo
     * @param message
     */
    public void sendSMSMessage(String phoneNo, String message,boolean delete) {
        Log.d(TAG,"电话："+phoneNo+",内容："+message);
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
        } catch (Exception e) {
            Log.d(TAG,"短信发送异常");
            e.printStackTrace();
        }
    }

    /**
     * 取出广播中的短信
     * @param intent
     * @return
     */
    public SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        byte[][] pduObjs = new byte[messages.length][];
        for (int i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }
        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }

    /**
     * 发送位置短信
     */
    public void sendLocationSMS(){
        LocationServiceImpl locationService = new LocationServiceImpl(context);
        Location location = locationService.getLocation();
        Log.d(TAG,location.getLatitude()+","+location.getLongitude());
        if (location==null){
            //没有授权
            return;
        }
        AddressBookServiceImpl addressBookService = new AddressBookServiceImpl(context);
        List<AddressBookBean> contactPersons = addressBookService.getContactPerson();
        if (contactPersons==null||(contactPersons.size()==0)){
            //没有联系人
            return;
        }
        String content = "当前手机位置 \n 经度："+location.getLongitude()+"\n 纬度 :"+location.getLatitude()+"\n\n\t 【手机已经丢了，当你收到这短信的时候发给我邮箱（shaoyayu0419@qq.com）,谢谢】";
        sendSMSMessage(contactPersons.get(0).getPhone(),content,false);
    }
}
