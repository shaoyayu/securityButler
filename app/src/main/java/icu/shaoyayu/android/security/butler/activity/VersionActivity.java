package icu.shaoyayu.android.security.butler.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.List;

import icu.shaoyayu.android.security.butler.BuildConfig;
import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.butler.util.PackageInfoUtil;
import icu.shaoyayu.android.security.butler.view.SimpleMenu;
import icu.shaoyayu.android.security.common.activity.CustomizeActivity;
import icu.shaoyayu.android.security.presenter.entity.UpdateDataModel;
import icu.shaoyayu.android.security.presenter.service.HttpDownloadTask;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author shaoyayu
 * 版本管理 升级和版本介绍
 */
public class VersionActivity extends CustomizeActivity implements EasyPermissions.PermissionCallbacks {

    private SimpleMenu smVersionTitle;

    private UpdateDataModel mUpdateDataModel;

    private TextView tvVersionName,tvVersionIntro;

    private Button btUpdateCheck;
    //0 最新版本， 1 更新版本， 2 开始下载，3 下载中......， 4 下载失败， 5 下载成功
    //1,下载更新 5 安装新版本
    private int sameVersion = 0;

    //下载的文件路径。下载的文件地
    private String savePath = "shaoyayu/security/";
    private String filename = "securityButler.apk";

    private ProgressBar pbUpdateProgress;

    AlertDialog.Builder mPermissionDialog;

    public static final int PERMISSION_STORAGE_CODE = 10001;
    public static final String PERMISSION_STORAGE_MSG = "此app需要获取SD卡读取权限";
    public static final String[] PERMISSION_STORAGE = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};


    @Override
    protected void initWindows() {

    }

    @Override
    protected int getInterfaceResourceId() {
        return R.layout.activity_version;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mUpdateDataModel = new UpdateDataModel(getApplicationContext());
       return true;
    }

    @Override
    protected void initTheControl() {
        super.initTheControl();
        smVersionTitle = findViewById(R.id.sm_version);
        smVersionTitle.setTvMenuThemeText("版本更新");
        tvVersionName = findViewById(R.id.tv_version_name);
        tvVersionIntro = findViewById(R.id.tv_version_intro);
        btUpdateCheck = findViewById(R.id.bt_update_check);
        pbUpdateProgress = findViewById(R.id.pb_update_progress);
        pbUpdateProgress.setMax(100);
    }

    @Override
    protected void initData() {
        super.initData();
        tvVersionName.setText(mUpdateDataModel.getAppName());
        tvVersionIntro.setText(mUpdateDataModel.getVersionIntroduction());
        //判断两个版本是不是一样，更新bt的状态
        if (!PackageInfoUtil.getVersionNumber(getApplicationContext()).equals(mUpdateDataModel.getVersionName())){
            sameVersion = 1;
        }
        btUpdateCheck.setText((sameVersion==0?"最新版本":"更新版本"));
        mPermissionDialog = new AlertDialog.Builder(getApplicationContext());

    }

    @Override
    protected void initControlBinding() {
        super.initControlBinding();
        btUpdateCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sameVersion==0){
                    showToast("已经是最新版本");
                }else if (sameVersion==1){
                    initSimple();
                }else if (sameVersion==5){
                    /*installAPK(savePath,filename);*/
                    showToast("前往文件夹安装");
//                    openAssignFolder(savePath);
                }
            }
        });
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        return EasyPermissions.hasPermissions(context, permissions);
    }

    /**
     * @param context
     * @return
     */
    public static boolean hasStoragePermission(Context context) {
        return hasPermissions(context, PERMISSION_STORAGE);
    }

    @AfterPermissionGranted(PERMISSION_STORAGE_CODE)
    public void initSimple() {
        if (hasStoragePermission(getApplicationContext())) {
            //有权限
            Log.d(TAG,"已经授权:");

            downloadUpdates();
        } else {
            //申请权限
            EasyPermissions.requestPermissions(this, PERMISSION_STORAGE_MSG, PERMISSION_STORAGE_CODE, PERMISSION_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //成功
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        Log.d(TAG,"权限申请成功");
        downloadUpdates();
    }

    //失败
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        Log.d(TAG,"权限申请失败");
    }

    public void downloadUpdates(){
        HttpDownloadTask httpDownloadTask = new HttpDownloadTask(savePath, new HttpDownloadTask.OnDownloadListener() {
            @Override
            public void onDownloadSuccess() {
                Log.d(TAG,"文件下载成功");
                btUpdateCheck.setText("安装");
                sameVersion = 5;
            }

            @Override
            public void onDownloading(int progress) {
                pbUpdateProgress.setProgress(progress);

            }

            @Override
            public void onDownloadFailed() {
                Log.d(TAG,"文件下载失败");
            }
        });
        httpDownloadTask.execute(mUpdateDataModel.getDownloadAddress());
    }

    /*
     * 下载到本地后执行安装
     */
    protected void installAPK(String mSavePath,String mVersion_name) {
        File apkFile = new File(Environment.getExternalStorageDirectory(), mSavePath+"/"+mVersion_name);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileProvider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }

    private void openAssignFolder(String path){
        File file = new File(Environment.getExternalStorageDirectory(), path);
        if(null==file || !file.exists()){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "file/*");
        try {
            startActivity(intent);
            //startActivity(Intent.createChooser(intent,"选择浏览工具"));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

}
