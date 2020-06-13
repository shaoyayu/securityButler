package icu.shaoyayu.android.security.common.task;

import android.os.AsyncTask;

import java.util.Map;

/**
 * @author shaoyayu
 * 检查后台任务，
 * 包括，后台更新、防盗检查等其他的检查，
 */
public class InspectionTask  extends AsyncTask<Task, Map<String,Object>,Map<String,Object>> {

    //所有任务集和
    private Task[] taskStack;
    //监听任务的当前实在那个阶段
    private TaskStatus onStatus;
    //当前任务栈的任务索引
    private int taskIndex;

    private int[] marks;

    public void setOnStatus(TaskStatus status) {
        this.onStatus = status;
    }

    /**
     * 认为开始的时候
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (onStatus!=null){
            onStatus.onStartOverallTask();
        }
    }

    /**
     * 执行的耗时任务
     * @param tasks
     * @return
     */
    @Override
    protected Map<String, Object> doInBackground(Task... tasks) {
        if (onStatus!=null){
            onStatus.onNormalOverallTask();
        }
        this.taskStack = tasks;
        marks = new int[taskStack.length];
        for (int i = 0; i < taskStack.length; i++) {
            taskStack[i].startTask();
            taskIndex = i;
            taskStack[i].run(this);
            taskStack[i].endTask();
            //获取前任务结束后的任务评分
            marks[i] = taskStack[i].taskScoring();
        }
        return null;
    }

    /**
     * 更新UI的任务
     * @param values
     */
    @Override
    public void onProgressUpdate(Map<String, Object>... values) {
        super.onProgressUpdate(values);
        taskStack[taskIndex].updateView(values);
    }

    /**
     * 任务执行完毕以后
     * @param stringObjectMap
     */
    @Override
    protected void onPostExecute(Map<String, Object> stringObjectMap) {
        super.onPostExecute(stringObjectMap);
        if (onStatus!=null){
            //计算评分
            double weight = (100/marks.length) * 0.01;
            double sum = 0;
            for (int mark : marks) {
                sum = sum + mark*weight;
            }
            onStatus.onBackOverallTask((int)sum);
            onStatus.onGetAllRatings(marks);
        }
    }

    public interface TaskStatus{
        void onStartOverallTask();
        void onNormalOverallTask();
        void onBackOverallTask(int finalRating);
        void onGetAllRatings(int... allRatings);
    }
}
