package com.example.fansonlib.utils.log;

import android.text.TextUtils;

import com.dianping.logan.SendLogRunnable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Created by：Fanson
 * Created Time: 2019/4/4 10:45
 * Describe：发送日志文件到服务器的Runnable
 */
public class RealSendLogRunnable extends SendLogRunnable {

    private static final String TAG = RealSendLogRunnable.class.getSimpleName();

    /**
     * 上传日志的Url
     */
    private String mUploadLogUrl;
    /**
     * 上传日志的监听
     */
    private WeakReference<SendLogListener> mSendLogListener;

    public RealSendLogRunnable(SendLogListener listener) {
        mSendLogListener = new WeakReference<SendLogListener>(listener);
    }

    @Override
    public void sendLog(File logFile) {
        doSendFileByAction(logFile);
    }

    /**
     * 设置服务器IP地址
     *
     * @param ip 服务器IP地址
     */
    public void setIp(String ip) {
        mUploadLogUrl = ip;
    }

    /**
     * 获取ActionHeader
     *
     * @return
     */
    private HashMap<String, String> getActionHeader() {
        HashMap<String, String> map = new HashMap<>();
        //二进制上传
        map.put("Content-Type", "binary/octet-stream");
        map.put("client", "android");
        return map;
    }

    /**
     * 主动上报
     */
    private void doSendFileByAction(File logFile) {
        uploadLogFileByOkHttp(mUploadLogUrl, logFile);
    }

    /**
     * 通过OkHttp上传日志文件到服务器
     *
     * @param url  URL地址
     * @param file 日志文件
     */
    private void uploadLogFileByOkHttp(String url, final File file) {
        OkHttpClient okHttpClient = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        //第一个参数要与Servlet中的一致
        builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        MultipartBody multipartBody = builder.build();

        Request request = new Request.Builder().url(url).post(multipartBody).build();
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        // Must Call finish after send log
                        finish();
                        if (file.getName().contains(".copy")) {
                            file.delete();
                        }
                        MyLogUtils.getInstance().d("日志上传成功：" + response.toString());
                        if (mSendLogListener != null && mSendLogListener.get() != null) {
                            mSendLogListener.get().onSendLogSuccessful();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    MyLogUtils.getInstance().e("日志上传失败：" + response.toString());
                }
            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                MyLogUtils.getInstance().e("日志上传失败：" + e.toString());
            }
        });
    }

    /**
     * 处理上传日志接口返回的数据
     *
     * @param backData 返回数据
     * @return 是否成功
     * @throws JSONException
     */
    private boolean handleSendLogBackData(byte[] backData) throws JSONException {
        boolean isSuccess = false;
        if (backData != null) {
            String data = new String(backData);
            if (!TextUtils.isEmpty(data)) {
                JSONObject jsonObj = new JSONObject(data);
                if (jsonObj.optBoolean("success", false)) {
                    isSuccess = true;
                }
            }
        }
        return isSuccess;
    }
}

