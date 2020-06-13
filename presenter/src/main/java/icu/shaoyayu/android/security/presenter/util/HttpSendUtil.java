package icu.shaoyayu.android.security.presenter.util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * @author shaoyayu
 * 模拟http请求
 */
public class HttpSendUtil {


    /**
     * 发送http请求，
     * @param address 请求网址
     * @param listener
     *
     * */
    public static void sendHttpRequest(String address, final HttpCallbackListener listener) {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(300, TimeUnit.SECONDS) //连接超时
                .readTimeout(300, TimeUnit.SECONDS) //读取超时
                .writeTimeout(300, TimeUnit.SECONDS) //写超时
                .build();
        Request request = new Request.Builder()
                .url(address)
                .build();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            String string = body.string();
            listener.onFinish(string);
        }catch (IOException e){
            listener.onError(e);
        }

    }

    public interface HttpCallbackListener {
        /**
         *请求成功后的处理
         * @param response 服务器返回值
         * */
        void onFinish(String response);

        /**
         * 请求异常的处理
         * @param e Exception
         * */
        void onError(Exception e);
    }

}
