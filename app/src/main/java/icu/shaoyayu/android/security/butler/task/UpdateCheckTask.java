package icu.shaoyayu.android.security.butler.task;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.butler.util.NotificationUtil;
import icu.shaoyayu.android.security.butler.util.PackageInfoUtil;
import icu.shaoyayu.android.security.common.task.InspectionTask;
import icu.shaoyayu.android.security.presenter.entity.UpdateDataModel;
import icu.shaoyayu.android.security.presenter.util.HttpSendUtil;

/**
 * @author shaoyayu
 * 更新检查任务
 */
public class UpdateCheckTask extends CheckTheEnvironTask {

    private static final String TAG = "UpdateCheckTask";

    private TextView tvInfoShow;
    private AppCompatActivity activity;

    private boolean sameVersion = true;

    private UpdateDataModel updateJson;

    private boolean entErr = false;

    public UpdateCheckTask(TextView tvInfoShow, AppCompatActivity activity) {
        this.tvInfoShow = tvInfoShow;
        this.activity = activity;
    }

    @Override
    public String getTaskName() {
        return "检查更新";
    }

    @Override
    public void startTask() {
        tvInfoShow.setText("开始检查更新......");
    }

    //开始任务
    @Override
    public void run(final InspectionTask task) {
        final Map<String,Object> stringMap = new HashMap<>();
        stringMap.put(P.INS,"正在网络请求中......");
        task.onProgressUpdate(stringMap);
        final String url = activity.getString(R.string.url_update_app);
        HttpSendUtil.sendHttpRequest(url, new HttpSendUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                stringMap.put(P.INS,"数据解析中......");
                //视觉效果
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                task.onProgressUpdate(stringMap);
                //解析是不是符合当前的版本
                try {
                    updateJson = new UpdateDataModel(response);
                    //判断两个版本是不是一样的
                    if (!updateJson.getVersionName().equals(PackageInfoUtil.getVersionNumber(activity.getApplicationContext()))){
                        //两个版本一样
                        sameVersion = false;
                    }
                } catch (JSONException e) {
                    //发送一个通知提示用户出现数据解析异常
                    NotificationUtil.showNotification(activity,0x52,R.mipmap.ico,"解析异常","数据解析异常，请联系客户服务");
                }
            }
            @Override
            public void onError(Exception e) {
                //通知用户，当前网络异常
                NotificationUtil.showNotification(activity,0x52,R.mipmap.ico,"网络异常","检查你的网络状态无误后，再联系客服，谢谢");
                Log.d(TAG,"网络异常");
                stringMap.put(P.INS,"网络异常");
                task.onProgressUpdate(stringMap);
                entErr = true;
            }
        });

        task.onProgressUpdate(stringMap);
    }
    //在任务中更新视图
    @Override
    public void updateView(Map<String, Object>... values) {
        //更新视图中的TextView
        tvInfoShow.setText((String)values[0].get(P.INS));
    }

    /**
     * 检查是不是含有最新的版本出现
     * 当出现新的版本的时候，我们需要发起通知。
     * 当用户点击通知的时候
     * 把新版本的内容发送到下一个活动里面 去更新
     */
    @Override
    public void endTask() {
        if (!sameVersion){
            Intent intent = new Intent();
            //把最新更新放置在内存里面，当
            if (updateJson.storeUpdateInfo(activity.getApplicationContext())){
                intent.setAction("icu.shaoyayu.android.security.butler.version");
                intent.addCategory("android.intent.category.DEFAULT");
                NotificationUtil.showNotification(activity,intent,0x52,R.mipmap.ico,updateJson.getAppName(),updateJson.getVersionIntroduction());
            }
        }

    }
    //返回的任务评分
    @Override
    public int taskScoring() {
        //判断两个系统的版本是否一致，不一致的话发回30分

        if (entErr){
            //发生异常，打分10分
            return 10;
        }

        if (updateJson.getVersionName().equals(PackageInfoUtil.getVersionNumber(activity))){
            //版本一致，当前任务打分100分
            return 100;
        }else {
            //版本不一致打分30分
            return 30;
        }

    }

    /**
     * 创建Map的key值
     */
    interface P {
        String INS = "updateProgress";
    }

}
