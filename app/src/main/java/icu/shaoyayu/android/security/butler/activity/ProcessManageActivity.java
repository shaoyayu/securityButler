package icu.shaoyayu.android.security.butler.activity;

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
    }

    @Override
    protected void initData() {
        super.initData();
        smProcessManage.setTvMenuThemeText("任务进程管理");

    }

    @Override
    protected void initControlBinding() {
        super.initControlBinding();
    }

}
