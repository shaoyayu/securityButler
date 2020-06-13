package icu.shaoyayu.android.security.presenter.entity;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import icu.shaoyayu.android.security.presenter.base.UpdateJson;
import icu.shaoyayu.android.security.presenter.util.PrefUtils;

/**
 * @author shaoyayu
 */
public class UpdateDataModel extends UpdateJsonBean implements UpdateJson {

    public UpdateDataModel(){

    }

    public UpdateDataModel(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        versionName = jsonObject.optString(BeanKey.VERSION_NAME);
        appName = jsonObject.optString(BeanKey.NAME);
        versionIntroduction = jsonObject.optString(BeanKey.INTRO);
        downloadAddress = jsonObject.optString(BeanKey.APK_LINK);
        releaseTime = jsonObject.optString(BeanKey.RELEASE_TIME);
    }

    /**
     * 把对象从xml文件中更新出来
     * @param context
     * @return
     */
    public UpdateDataModel(Context context){
        setVersionName(PrefUtils.getString(BeanKey.VERSION_NAME,null,context));
        setAppName(PrefUtils.getString(BeanKey.NAME,null,context));
        setVersionIntroduction(PrefUtils.getString(BeanKey.INTRO,null,context));
        setDownloadAddress(PrefUtils.getString(BeanKey.APK_LINK,null,context));
        setReleaseTime(PrefUtils.getString(BeanKey.RELEASE_TIME,null,context));
    }

    @Override
    public String getVersionName() {
        return versionName;
    }

    @Override
    public String getAppName() {
        return appName;
    }

    @Override
    public String getVersionIntroduction() {
        return versionIntroduction;
    }

    @Override
    public String getDownloadAddress() {
        return downloadAddress;
    }

    @Override
    public String getReleaseTime() {
        return releaseTime;
    }

    @Override
    public String toString() {
        return "UpdateDataModel{" +
                "versionName='" + versionName + '\'' +
                ", appName='" + appName + '\'' +
                ", versionIntroduction='" + versionIntroduction + '\'' +
                ", downloadAddress='" + downloadAddress + '\'' +
                ", releaseTime='" + releaseTime + '\'' +
                '}';
    }

    /**
     * 将更新信息存入xml文件中，
     * @return
     */
    public boolean storeUpdateInfo(Context context){
        try {
            PrefUtils.putString(BeanKey.VERSION_NAME,versionName,context);
            PrefUtils.putString(BeanKey.NAME,appName,context);
            PrefUtils.putString(BeanKey.INTRO,versionIntroduction,context);
            PrefUtils.putString(BeanKey.APK_LINK,downloadAddress,context);
            PrefUtils.putString(BeanKey.RELEASE_TIME,releaseTime,context);
            return true;
        }catch (Exception e){
            return false;
        }
    }


}
