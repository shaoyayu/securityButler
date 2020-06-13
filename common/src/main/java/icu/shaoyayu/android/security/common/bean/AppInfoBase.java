package icu.shaoyayu.android.security.common.bean;

import android.graphics.drawable.Drawable;

/**
 * @author shaoyayu
 * 定义一个基础的AppInfo
 */
public interface AppInfoBase {

    Drawable getAppIcon();

    String getAppName();

    String getAppVersion();

    String getPackageNames();

}
