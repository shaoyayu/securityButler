package icu.shaoyayu.android.security.presenter.service;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

/**
 * @author shaoyayu
 * 位置服务，获取位置信息
 */
public class LocationServiceImpl {

    private Context context;

    public LocationServiceImpl(Context context){
        this.context = context;
    }

    public Location getLocation() {
        String serviceString = Context.LOCATION_SERVICE;// 获取的是位置服务
        LocationManager locationManager = (LocationManager) context.getSystemService(serviceString);// 调用getSystemService()方法来获取LocationManager对象
        String provider = LocationManager.GPS_PROVIDER;// 指定LocationManager的定位方法
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Location location = locationManager.getLastKnownLocation(provider);// 调用getLastKnownLocation()方法获取当前的位置信息
        return location;
    }

}
