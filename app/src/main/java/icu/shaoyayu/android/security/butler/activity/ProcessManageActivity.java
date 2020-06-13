package icu.shaoyayu.android.security.butler.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.butler.view.SimpleMenu;
import icu.shaoyayu.android.security.common.activity.SecurityFunctionActivity;

/**
 * @author shaoyayu
 * 尽量坐在时时的监控
 * 内存使用情况，各种资源监控
 * android6.0以上的getRunningAppProcesses也失效，系统关闭了三方软件对系统进程的访问，出于安全考虑。
 * 推荐解决方案地址：https://www.cnblogs.com/zhujiabin/p/9342067.html
 */
public class ProcessManageActivity extends SecurityFunctionActivity {

    private SimpleMenu smProcessManage;

    private SwipeRefreshLayout srlProcessManage;

    private NumberProgressBar npbRamSize;

    private RecyclerView rvProcessManage;

    private FloatingActionButton fabClearUp;


    @Override
    protected void initWindows() {

    }

    @Override
    protected int getInterfaceResourceId() {
        return R.layout.activity_process_manage;
    }

    @Override
    protected void initTheControl() {
        super.initTheControl();


        smProcessManage = findViewById(R.id.sm_process_manage);
        srlProcessManage = findViewById(R.id.srl_process_manage);
        npbRamSize = findViewById(R.id.npb_ram_size);
        rvProcessManage = findViewById(R.id.rv_process_manage);
        fabClearUp = findViewById(R.id.fab_clear_up);
    }

    @Override
    protected void initData() {
        super.initData();
        smProcessManage.setTvMenuThemeText("任务进程管理");

    }

    @Override
    protected void initControlBinding() {
        super.initControlBinding();
        test();
    }

    private void test(){
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
        List<ActivityManager.RunningServiceInfo> serviceInfos = am.getRunningServices(100);
        for (int i = 0; i < infoList.size(); i++) {
            Log.d(TAG,infoList.get(i).processName+",,正在运行");
        }
    }

}
