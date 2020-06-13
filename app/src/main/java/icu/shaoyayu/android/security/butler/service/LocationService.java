package icu.shaoyayu.android.security.butler.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.util.List;

import icu.shaoyayu.android.security.common.bean.AddressBookBean;
import icu.shaoyayu.android.security.presenter.service.AddressBookServiceImpl;

/**
 * @author shaoyayu
 * 获取经纬度的服务
 */
public class LocationService extends Service {

    private  static final String TAG = "LocationService";

    protected LocationManager locationManager;
    private ServiceLocationListener serviceLocationListener;

    private ReturnLocationListener onReturnLocationListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //没有授予权限
            return;
        }
        serviceLocationListener = new ServiceLocationListener();
        locationManager.requestLocationUpdates(bestProvider, 0, 0, serviceLocationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //关闭位置更新
        locationManager.removeUpdates(serviceLocationListener);
        locationManager = null;
    }

    public void setOnReturnLocationListener(ReturnLocationListener onReturnLocationListener) {
        this.onReturnLocationListener = onReturnLocationListener;
    }

    class ServiceLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            //位置回调
            Log.d(TAG,"获取到的经纬度："+location.getLongitude()+","+location.getLatitude());
            AddressBookServiceImpl addressBookService = new AddressBookServiceImpl(getApplicationContext());
            List<AddressBookBean> contactPersons = addressBookService.getContactPerson();
            if (contactPersons==null||(contactPersons.size()==0)){
                //没有联系人
                return;
            }
            String content = "当前手机位置 \n 经度："+location.getLongitude()+"\n 纬度 :"+location.getLatitude()+"\n\n\t 【手机已经丢了，当你收到这短信的时候发给我邮箱（shaoyayu0419@qq.com）,谢谢】";
            SmsManager smsManager = SmsManager.getDefault();
            for (AddressBookBean contactPerson : contactPersons) {
                Log.d(TAG,"发送到："+contactPerson.getPhone());
                smsManager.sendTextMessage(contactPerson.getPhone(), null, content, null, null);
            }
            if (onReturnLocationListener!=null){
                onReturnLocationListener.onReturnLocationListener(location);
            }
            stopSelf();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    interface  ReturnLocationListener{
        void onReturnLocationListener(Location location);
    }
}
