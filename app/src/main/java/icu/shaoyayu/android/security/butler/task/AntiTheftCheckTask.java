package icu.shaoyayu.android.security.butler.task;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.butler.activity.AntiTheftActivity;
import icu.shaoyayu.android.security.butler.util.NotificationUtil;
import icu.shaoyayu.android.security.common.task.InspectionTask;
import icu.shaoyayu.android.security.presenter.service.AntiTheftServiceImpl;

/**
 * @author shaoyayu
 * 手机防盗检查
 */
public class AntiTheftCheckTask extends CheckTheEnvironTask {

    private Context mContext;
    //显示内容
    private TextView tvInfoShow;
    //防盗评分
    private int scoring = 0;

    private AntiTheftServiceImpl antiTheftService;
    private Intent intent ;

    public AntiTheftCheckTask(Context mContext, TextView tvInfoShow) {
        this.mContext = mContext;
        this.tvInfoShow = tvInfoShow;
        antiTheftService = new AntiTheftServiceImpl(this.mContext);
        intent = new Intent(mContext, AntiTheftActivity.class);
    }

    @Override
    public String getTaskName() {
        return "手机防盗检查";
    }

    @Override
    public void startTask() {
        tvInfoShow.setText("开始防盗检查......");
        repose();
    }

    @Override
    public void run(InspectionTask task) {
        final Map<String,Object> stringMap = new HashMap<>();
        //任务检查中
        //检查防盗是否开启
        stringMap.put(CheckName.MSG_NAME,"手机防盗检查......");
        task.onProgressUpdate(stringMap);
        repose();
        if (!antiTheftService.isAntiTheft()){
            //没有开启防盗
            NotificationUtil.showNotification(mContext,intent,0x53,R.mipmap.ico,"安全卫士","开启手机防盗");
            return;
        }else {
            //开启防盗
            scoring = scoring +50;
        }
        stringMap.put(CheckName.MSG_NAME,"检查SIM卡绑定......");
        task.onProgressUpdate(stringMap);
        repose();
        if (antiTheftService.gutIsBindingSim()){
            //绑定SIM卡
            scoring = scoring +20;
        }else {
            NotificationUtil.showNotification(mContext,intent,0x54,R.mipmap.ico,"安全卫士","防盗功能需要绑定SIM卡");
            return;
        }
        stringMap.put(CheckName.MSG_NAME,"检查联系人绑定......");
        task.onProgressUpdate(stringMap);
        repose();
        if (antiTheftService.getContactPerson()!=null){
            //绑定联系人
            scoring = scoring +20;
        }else {
            NotificationUtil.showNotification(mContext,intent,0x55,R.mipmap.ico,"安全卫士","防盗功能需绑定紧急联系人");
            return;
        }
        stringMap.put(CheckName.MSG_NAME,"检查是否开启管理员......");
        task.onProgressUpdate(stringMap);
        repose();
        if (antiTheftService.isSetUpAdmin()){
            //开启管理员权限
            scoring = scoring +10;
        }else {
            NotificationUtil.showNotification(mContext,intent,0x55,R.mipmap.ico,"安全卫士","防盗功能需开启管理员模式");
            return;
        }

    }

    @Override
    public void updateView(Map<String, Object>... values) {
        tvInfoShow.setText((String)values[0].get(CheckName.MSG_NAME));
    }

    @Override
    public void endTask() {
        //发送通知跳转开启防盗见面
        tvInfoShow.setText("手机防盗检查完毕");
        repose();
    }

    @Override
    public int taskScoring() {
        return scoring;
    }

    protected void repose(){
        try {
            Thread.sleep(900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    interface CheckName{
        String MSG_NAME = "msg_name";
    }
}
