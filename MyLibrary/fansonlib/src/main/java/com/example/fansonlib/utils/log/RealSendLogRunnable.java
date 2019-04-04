package com.example.fansonlib.utils.log;

import android.text.TextUtils;
import android.util.Log;

import com.dianping.logan.SendLogRunnable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * @author Created by：Fanson
 * Created Time: 2019/4/4 10:45
 * Describe：发送日志文件到服务器的Runnable
 */
public class RealSendLogRunnable extends SendLogRunnable {

    private static final String TAG = RealSendLogRunnable.class.getSimpleName();
    private String mUploadLogUrl ;

    @Override
    public void sendLog(File logFile) {
        boolean success = doSendFileByAction(logFile);
        Log.d(TAG, "日志上传测试结果：" + success);
        // Must Call finish after send log
        finish();
        if (logFile.getName().contains(".copy")) {
            logFile.delete();
        }
    }

    /**
     * 设置服务器IP地址
     * @param ip 服务器IP地址
     */
    public void setIp(String ip) {
        mUploadLogUrl = ip;
    }

    /**
     * 获取ActionHeader
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
    private boolean doSendFileByAction(File logFile) {
        boolean isSuccess = false;
        try {
            FileInputStream fileStream = new FileInputStream(logFile);
            byte[] backData = doPostRequest(mUploadLogUrl, fileStream, getActionHeader());
            isSuccess = handleSendLogBackData(backData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * post请求
     * @param url url
     * @param inputData 写入日期
     * @param headerMap headerMap
     * @return
     */
    private byte[] doPostRequest(String url, InputStream inputData, Map<String, String> headerMap) {
        byte[] data = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        HttpURLConnection c = null;
        ByteArrayOutputStream back;
        byte[] Buffer = new byte[2048];
        try {
            java.net.URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            if (c instanceof HttpsURLConnection) {
                ((HttpsURLConnection) c).setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            }
            Set<Map.Entry<String, String>> entrySet = headerMap.entrySet();
            for (Map.Entry<String, String> tempEntry : entrySet) {
                c.addRequestProperty(tempEntry.getKey(), tempEntry.getValue());
            }
            c.setReadTimeout(15000);
            c.setConnectTimeout(15000);
            c.setDoInput(true);
            c.setDoOutput(true);
            c.setRequestMethod("POST");
            outputStream = c.getOutputStream();
            int i;
            while ((i = inputData.read(Buffer)) != -1) {
                outputStream.write(Buffer, 0, i);
            }
            outputStream.flush();
            int res = c.getResponseCode();
            if (res == 200) {
                back = new ByteArrayOutputStream();
                inputStream = c.getInputStream();
                while ((i = inputStream.read(Buffer)) != -1) {
                    back.write(Buffer, 0, i);
                }
                data = back.toByteArray();
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputData != null) {
                try {
                    inputData.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (c != null) {
                c.disconnect();
            }
        }
        return data;
    }

    /**
     * 处理上传日志接口返回的数据
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

