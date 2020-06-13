package icu.shaoyayu.android.security.butler.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.butler.dialog.AppInfoBottomDialog;
import icu.shaoyayu.android.security.butler.util.PackageInfoUtil;
import icu.shaoyayu.android.security.butler.view.SimpleMenu;
import icu.shaoyayu.android.security.common.activity.SecurityFunctionActivity;
import icu.shaoyayu.android.security.presenter.entity.AppInfoEntity;
import icu.shaoyayu.android.security.presenter.service.AppsInfoServiceImpl;

/**
 * @author shaoyayu
 */
public class SoftwareManageActivity extends SecurityFunctionActivity {

    private SimpleMenu mSimpleMenu;

    private SwipeRefreshLayout srlAppManage;

    private PackageManager mPackageManager;


    private List<AppInfoEntity> appInfoBases ;

    private LinearLayoutManager mLinearLayoutManager;

    private AppOnBottomDialogListener appOnBottomDialogListener;

    private RecyclerView rvAppManage;


    @Override
    protected void initWindows() {

    }

    @Override
    protected int getInterfaceResourceId() {
        return R.layout.activity_software_manage;
    }

    @Override
    protected void initTheControl() {
        super.initTheControl();
        mSimpleMenu = findViewById(R.id.sm_app_manage);
        mPackageManager = getPackageManager();
        srlAppManage = findViewById(R.id.srl_app_manage);
        rvAppManage = findViewById(R.id.rv_app_manage);
        appInfoBases = new ArrayList<>();
        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        appOnBottomDialogListener = new AppOnBottomDialogListener();
    }

    @Override
    protected void initData() {
        super.initData();
        mSimpleMenu.setTvMenuThemeText("软件管理");
        srlAppManage.setRefreshing(true);
        initInstallApps();
    }

    @Override
    protected void initControlBinding() {
        super.initControlBinding();
        srlAppManage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initInstallApps();
            }
        });

    }

    /**
     * 显示安装
     */
    private void initInstallApps() {
        GetApplicationInfoTask applicationInfoTask = new GetApplicationInfoTask();
        applicationInfoTask.execute(mPackageManager);
        mSimpleMenu.setTvMenuThemeText("正在刷新应用程序列表......");
    }

    class GetApplicationInfoTask extends AsyncTask<PackageManager,String,String>{

        @Override
        protected void onPreExecute() {
            //开始执行任务前，更新列表
            appInfoBases.clear();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(PackageManager... packageManagers) {
            AppsInfoServiceImpl appsInfoService = new AppsInfoServiceImpl(getApplicationContext());
            appInfoBases = appsInfoService.getAllNonsystemProgramInfo();
            return "任务加载完毕";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //更新适配器
            AppInfoAdapter adapter  = new AppInfoAdapter();
            adapter.setAppInfoItemOnClickListener(new AppInfoItemOnClickListener() {
                @Override
                public boolean onAppInfoItemOnClickListener(View view, int position) {
                    //弹出层
                    showBottomDialog(appInfoBases.get(position));
                    return true;
                }
            });
            rvAppManage.setAdapter(adapter);
            rvAppManage.setLayoutManager(mLinearLayoutManager);
            srlAppManage.setRefreshing(false);
            mSimpleMenu.setTvMenuThemeText("应用程序列表");
        }
    }

    private void showBottomDialog(AppInfoEntity appInfo){
        AppInfoBottomDialog dialog = new AppInfoBottomDialog(appInfo);
        dialog.setOnBottomDialogListener(appOnBottomDialogListener);
        dialog.show(getSupportFragmentManager(),"应用信息");
    }

    /**
     * 适配器
     */
    class AppInfoAdapter extends RecyclerView.Adapter<AppInfoItemHolder> {

        private AppInfoItemOnClickListener AppInfoItemOnClickListener;

        public void setAppInfoItemOnClickListener(AppInfoItemOnClickListener AppInfoItemOnClickListener) {
            this.AppInfoItemOnClickListener = AppInfoItemOnClickListener;
        }

        @NonNull
        @Override
        public AppInfoItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View view = inflater.inflate(R.layout.adapter_app_manage_item,parent,false);
            return new AppInfoItemHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AppInfoItemHolder holder, final int position) {
            Drawable appIcon = appInfoBases.get(position).getAppIcon();
            holder.ivAppIcoItem.setImageDrawable(appIcon);
            holder.tvAppNameItem.setText(appInfoBases.get(position).getAppName());
            holder.tvAppVersionItem.setText(appInfoBases.get(position).getVersionName());
            if (AppInfoItemOnClickListener!=null){
                holder.rlAppInfoItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppInfoItemOnClickListener.onAppInfoItemOnClickListener(v,position);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return appInfoBases.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    class AppInfoItemHolder extends RecyclerView.ViewHolder{
        public RelativeLayout rlAppInfoItem;
        public ImageView ivAppIcoItem;
        public TextView tvAppNameItem,tvAppVersionItem;
        public AppInfoItemHolder(@NonNull View itemView) {
            super(itemView);
            ivAppIcoItem = itemView.findViewById(R.id.iv_app_ico_item);
            tvAppNameItem = itemView.findViewById(R.id.tv_app_name_item);
            tvAppVersionItem = itemView.findViewById(R.id.tv_app_version_item);
            rlAppInfoItem = itemView.findViewById(R.id.rl_app_info_item);
        }

    }

    /**
     * 事件回调
     */
    interface AppInfoItemOnClickListener{
        boolean onAppInfoItemOnClickListener(View view,int position);
    }

    //底部弹出层的回调方法 AppInfoBottomDialog
    class AppOnBottomDialogListener implements AppInfoBottomDialog.OnBottomDialogListener {
        //运行程序
        @Override
        public void onAppRun(AppInfoEntity appInfoEntity) {
            Intent intent = mPackageManager.getLaunchIntentForPackage(appInfoEntity.getPackageName());
            startActivity(intent);
        }
        //分享程序
        @Override
        public void onAppShare(AppInfoEntity appInfoEntity) {
            //分享功能
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,"为你推荐一款软件："+appInfoEntity.getAppName()+",下载地址：https://play.google.com/store/apps/details?id="+appInfoEntity.getPackageName());
            startActivity(intent);
        }
        //卸载程序
        @Override
        public void onAppUninstall(AppInfoEntity appInfoEntity) {
            if (appInfoEntity.getPackageName().equals(PackageInfoUtil.getPackageNames(getApplicationContext()))){
                showToast("当前应用不能卸载");
                return;
            }
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setAction("android.intent.action.DELETE");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse("package:"+appInfoEntity.getPackageName()));
            startActivityForResult(intent, 0);
            //从新加载数据
        }

    }

}
