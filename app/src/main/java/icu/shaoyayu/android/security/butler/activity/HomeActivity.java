package icu.shaoyayu.android.security.butler.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.butler.adapter.SecurityFunctionAdapter;
import icu.shaoyayu.android.security.butler.task.AntiTheftCheckTask;
import icu.shaoyayu.android.security.butler.task.UpdateCheckTask;
import icu.shaoyayu.android.security.butler.view.CircleProgressBar;
import icu.shaoyayu.android.security.common.activity.CustomizeActivity;
import icu.shaoyayu.android.security.common.bean.SecurityFunctionBean;
import icu.shaoyayu.android.security.common.task.InspectionTask;

/**
 * @author shaoyayu
 */
public class HomeActivity extends CustomizeActivity {

    private static final String TAG = "HomeActivity";

    private CircleProgressBar cprSafetyRate;

    private RecyclerView rvFunctionList;
    //AntiTheft、
    private List<SecurityFunctionBean> securityFunctions = new ArrayList<>();

    private TextView tvSystemCheck;

    //更新和检查手机安全的异步任务，
    private InspectionTask inspectionTask;

    @Override
    protected void initWindows() {

    }

    @Override
    protected int getInterfaceResourceId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initTheControl() {
        super.initTheControl();
        cprSafetyRate = findViewById(R.id.cpr_safety_rate);
        tvSystemCheck = findViewById(R.id.tv_system_check);
        //默认初始值
        rvFunctionList = findViewById(R.id.rv_function_list);
        rvFunctionList.setHasFixedSize(false);
        rvFunctionList.setNestedScrollingEnabled(false);
        inspectionTask = new InspectionTask();
        // adapter
    }

    @Override
    protected void initData() {
        super.initData();
        //添加功能到主页上显示
        securityFunctions.add(new SecurityFunctionBean(R.drawable.ic_anti_theft_mobile_phone_foreground,"手机防盗","为了你的手机安全",AntiTheftActivity.class));
        securityFunctions.add(new SecurityFunctionBean(R.drawable.ic_communications_guard_foreground,"通讯卫士","防止电信诈骗",CommGuardActivity.class));
        securityFunctions.add(new SecurityFunctionBean(R.drawable.ic_software_manage_foreground,"软件管理","管理手机安装APP",SoftwareManageActivity.class));
        securityFunctions.add(new SecurityFunctionBean(R.drawable.ic_phone_process_foreground,"进程管理","为手机加速",ProcessManageActivity.class));
        securityFunctions.add(new SecurityFunctionBean(R.drawable.ic_traffic_statistics_foreground,"流量监控","流量数据分析",DataMonitorActivity.class));
        securityFunctions.add(new SecurityFunctionBean(R.drawable.ic_mobile_phone_antivirus_foreground,"手机杀毒","确保手机安全环境",MobilePhoneAntivirusActivity.class));
        securityFunctions.add(new SecurityFunctionBean(R.drawable.ic_garbage_collection_foreground,"清理垃圾","回收手机垃圾文件",CleanUpTrashActivity.class));
        securityFunctions.add(new SecurityFunctionBean(R.drawable.ic_advanced_tools_foreground,"高级功能","想想之内，手机之外",AdvancedFeaturesActivity.class));
        securityFunctions.add(new SecurityFunctionBean(R.drawable.ic_setting_center_foreground,"设置中心","手机卫士设置中心",SettingCenterActivity.class));
        SecurityFunctionAdapter securityFunctionAdapter = new SecurityFunctionAdapter(getApplicationContext(),securityFunctions);
        //注册一个监听器电机条目回调
        securityFunctionAdapter.setItemOnClickListener(new SecurityFunctionAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(View view, int position) {
                startActivity(new Intent(getApplicationContext(),securityFunctions.get(position).getSecurityFunctionActivityClass()));
            }
        });
        rvFunctionList.setAdapter(securityFunctionAdapter);
        GridLayoutManager serviceManager = new GridLayoutManager(
                getApplicationContext() ,2, GridLayoutManager.VERTICAL , false) ;
        rvFunctionList.setLayoutManager(serviceManager);
        //创建一个检查后台更新的任务
        UpdateCheckTask updateCheckTask = new UpdateCheckTask(tvSystemCheck,this);
        //创建一个收集防盗检查任务
        AntiTheftCheckTask antiTheftCheckTask = new AntiTheftCheckTask(getApplicationContext(),tvSystemCheck);
        //把任务交给环境检查任务队列来执行
        inspectionTask.execute(updateCheckTask,antiTheftCheckTask);
    }

    @Override
    protected void initControlBinding() {
        super.initControlBinding();
        //任务监听器
        inspectionTask.setOnStatus(new InspectionTask.TaskStatus() {
            @Override
            public void onStartOverallTask() {
                tvSystemCheck.setText("正在检查");
            }

            @Override
            public void onNormalOverallTask() {
                tvSystemCheck.setText("开始执行任务");
            }

            @Override
            public void onBackOverallTask(int finalRating) {
                //执行完异步检查的任务队列回调的时候，会传递每个每个任务打分后的打分，
                //设置到环形进度条里面
                cprSafetyRate.setProgress(finalRating);
                //告诉任务检查完毕
                tvSystemCheck.setText("检查完毕");
            }

            @Override
            public void onGetAllRatings(int... allRatings) {
            }
        });
    }
}
