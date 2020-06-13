package icu.shaoyayu.android.security.presenter.service;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

import icu.shaoyayu.android.security.presenter.entity.AppInfoEntity;


/**
 * @author shaoyayu
 */
public class AppsInfoServiceImpl implements AppsInfoService {

    public static final int DEFAULT = 0; // 默认 所有应用
    public static final int SYSTEM_APP = DEFAULT + 1; // 系统应用
    public static final int NONSYSTEM_APP = DEFAULT + 2; // 非系统应用

    private Context context;

    public AppsInfoServiceImpl(Context context) {
        this.context = context;
    }

    /**
     * 根据包名获取相应的应用信息
     * @param packageName
     * @return 返回包名所对应的应用程序的名称。
     */
    public  String getProgramNameByPackageName(String packageName) {
        PackageManager pm = context.getPackageManager();
        String name = null;
        try {
            name = pm.getApplicationLabel(
                    pm.getApplicationInfo(packageName,
                            PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 获取手机所有应用信息
     *
     * @param allApplist
     * @param context
     */
    public  void getAllProgramInfo(List<AppInfoEntity> allApplist,
                                         Context context) {
        getAllProgramInfo(allApplist, DEFAULT);
    }

    /**
     * 获取手机所有应用信息
     *
     * @param applist
     * @param type
     *            标识符 是否区分系统和非系统应用
     */
    public  void getAllProgramInfo(List<AppInfoEntity> applist,int type) {
        ArrayList<AppInfoEntity> appList = new ArrayList<AppInfoEntity>(); // 用来存储获取的应用信息数据
        List<PackageInfo> packages = context.getPackageManager()
                .getInstalledPackages(0);

        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            AppInfoEntity tmpInfo = new AppInfoEntity();

            String appName= packageInfo.applicationInfo.loadLabel(
            context.getPackageManager()).toString();
            tmpInfo.setAppName(appName);
            tmpInfo.setPackageName(packageInfo.packageName);
            tmpInfo.setVersionName(packageInfo.versionName);
            tmpInfo.setVersionCode(packageInfo.versionCode);
            tmpInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(context
                    .getPackageManager()));
            switch (type) {
                case NONSYSTEM_APP:
                    if (!isSystemAPP(packageInfo)) {
                        applist.add(tmpInfo);
                    }
                    break;
                case SYSTEM_APP:
                    if (isSystemAPP(packageInfo)) {
                        applist.add(tmpInfo);
                    }
                    break;
                default:
                    applist.add(tmpInfo);
                    break;
            }

        }
    }

    /**
     * 获取所有系统应用信息
     *
     * @param context
     * @return
     */
    public  List<AppInfoEntity> getAllSystemProgramInfo(Context context) {
        List<AppInfoEntity> systemAppList = new ArrayList<AppInfoEntity>();
        getAllProgramInfo(systemAppList, SYSTEM_APP);
        return systemAppList;
    }

    /**
     * 获取所有非系统应用信息
     *
     * @return
     */
    public  List<AppInfoEntity> getAllNonsystemProgramInfo() {
        List<AppInfoEntity> nonsystemAppList = new ArrayList<AppInfoEntity>();
        getAllProgramInfo(nonsystemAppList, NONSYSTEM_APP);
        return nonsystemAppList;
    }

    /**
     * 判断是否是系统应用
     *
     * @param packageInfo
     * @return
     */
    public  Boolean isSystemAPP(PackageInfo packageInfo) {
        if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) { // 非系统应用
            return false;
        } else { // 系统应用
            return true;
        }
    }

}
