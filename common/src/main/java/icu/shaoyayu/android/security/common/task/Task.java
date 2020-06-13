package icu.shaoyayu.android.security.common.task;

import java.util.Map;

/**
 * @author shaoyayu
 * 任务的方法
 */
public interface Task{
    //任务名称
    String getTaskName();

    //开始执行任务
    void startTask();

    //执行的任务
    void run(InspectionTask task);

    //更新任务UI的回调
    void updateView(Map<String,Object>... values);

    //完成当前任务的回调
    void endTask();

    //最终任务评分
    int taskScoring();
}
