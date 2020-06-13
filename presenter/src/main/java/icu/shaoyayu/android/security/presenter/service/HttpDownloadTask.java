package icu.shaoyayu.android.security.presenter.service;

import android.os.AsyncTask;
import android.os.Environment;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author shaoyayu
 * 依赖okHttp的下载任务
 */
public class HttpDownloadTask extends AsyncTask<String,Integer,Integer> {

    private String storagePath;
    private OnDownloadListener listener;

    public HttpDownloadTask(String storagePath, OnDownloadListener onDownloadListener) {
        this.storagePath = storagePath;
        this.listener = onDownloadListener;
    }

    /**
     * 认为开始的时候
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    /**
     * 执行的耗时任务
     * @param vars
     * @return
     */
    @Override
    protected Integer doInBackground(final String... vars) {
        //需要下载的文件开始
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(vars[0]).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                listener.onDownloadFailed();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(storagePath);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, getNameFromUrl(vars[0]));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess();
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
        return null;
    }

    /**
     * 更新UI的任务
     * @param values
     */
    @Override
    public void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    /**
     * 任务执行完毕以后
     * @param stringObjectMap
     */
    @Override
    protected void onPostExecute(Integer stringObjectMap) {
        super.onPostExecute(stringObjectMap);
    }


    /**
     * @param saveDir
     * @return
     * @throws IOException
     * 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.exists()) {
            downloadFile.mkdirs();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }


    /**
     * @param url
     * @return
     * 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {

        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress
         * 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }

}
