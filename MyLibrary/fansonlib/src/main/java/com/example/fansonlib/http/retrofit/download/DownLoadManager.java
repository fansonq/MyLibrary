package com.example.fansonlib.http.retrofit.download;

import com.example.fansonlib.http.retrofit.RetrofitClient;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by：fanson
 * Created on：2017/2/28 15:54
 * Describe：下载文件类
 */

public class DownLoadManager {

    private static final String TAG = DownLoadManager.class.getSimpleName();

    /**
     * 下载单文件
     * @param url 下载地址
     * @param fileDir 文件目录
     * @param fileName 文件名
     * @param fileDownLoadObserver
     * @return Disposable
     */
    public static Disposable downloadFile(String url, final String fileDir, final String fileName, final FileDownLoadObserver<File> fileDownLoadObserver) {
        return RetrofitClient.getRetrofit()
                .create(DownloadApi.class)
                .downloadFile(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(@NonNull ResponseBody responseBody) throws Exception {
                        return fileDownLoadObserver.saveFile(responseBody, fileDir, fileName);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(@NonNull File file) throws Exception {
                        fileDownLoadObserver.onDownLoadSuccess(file);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        fileDownLoadObserver.onDownLoadFail(throwable);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        fileDownLoadObserver.onComplete();
                    }
                });
    }
}
