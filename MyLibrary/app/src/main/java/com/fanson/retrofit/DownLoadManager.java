package com.fanson.retrofit;

import android.content.Context;
import android.util.Log;

import com.example.fansonlib.utils.ShowToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.ResponseBody;

/**
 * Created by：fanson
 * Created on：2017/2/28 15:54
 * Describe：下载文件类
 */

public class DownLoadManager {

    private static final String TAG = DownLoadManager.class.getSimpleName();

    private static String fileSuffix = ""; //下载文件的类型

    private static Disposable disposable;

    /**
     * 开启下载
     *
     * @param context context
     * @param url     文件的url
     */
    public static void start(final Context context, String url) {

        ProgressListener downloadProgress = new ProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                Log.d(TAG, "file complete: " + (float)bytesRead/contentLength*100);
            }
        };

        Flowable<ResponseBody> flowable=  RetrofitClient.getRetrofitProgress(downloadProgress).create(ApiStores.class).download(url);
        ResourceSubscriber resourceSubscriber = new ResourceSubscriber() {
            @Override
            public void onNext(Object response) {
                if (writeResponseBodyToDisk(context, (ResponseBody) response)){
                    ShowToast.singleShort("download completed.");
                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        };

        disposable = RetrofitClient.startObservable(flowable,resourceSubscriber);
    }

    public static void pause(){
        if (disposable!=null){
            disposable.dispose();
        }
    }

    public static boolean writeResponseBodyToDisk(Context context, ResponseBody body) {
        String type = body.contentType().toString();
        Log.d(TAG, "contentType:>>>>" + type);
        switch (type) {
            case "application/vnd.android.package-archive":
                fileSuffix = ".apk";
                break;
            case "image/png":
                fileSuffix = ".png";
                break;
            case "image/jpeg":
                fileSuffix = ".jpeg";
                break;
        }
        String path = context.getExternalFilesDir(null) + File.separator + System.currentTimeMillis() + fileSuffix;
        Log.d(TAG, "path:>>>>" + path);
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(path);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
