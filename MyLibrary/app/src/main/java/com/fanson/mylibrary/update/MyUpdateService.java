package com.fanson.mylibrary.update;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.fansonlib.function.update.UpdateAppBean;
import com.example.fansonlib.function.update.UpdateAppManager;
import com.example.fansonlib.function.update.UpdateCallback;

/**
 * Created by：fanson
 * Created on：2017/7/18 10:30
 * Describe：
 */

public class MyUpdateService extends IntentService {

    private static final String TAG = MyUpdateService.class.getSimpleName();

    public MyUpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String updateUrl = intent.getStringExtra("url"); //WVector/AppUpdateDemo/master/json/json.txt"
            new UpdateAppManager
                    .Builder()
                    //当前Activity
                    .setContext(this)
                    //实现httpManager接口的对象
                    .setHttpManager(new AppHttpUtils())
                    //更新地址
                    .setUpdateUrl(updateUrl)
                    .build()
                    //检测是否有新版本
                    .checkNewApp(new UpdateCallback() {
                        /**
                         * 有新版本
                         *
                         * @param updateApp        新版本信息
                         * @param updateAppManager app更新管理器
                         */
                        @Override
                        public void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                            //show dialog prompts a  new version
                            updateAppManager.showDialog();
//                        ShowToast.singleLong("have a new version");
                        }

                        /**
                         * 网络请求之前
                         */
                        @Override
                        public void onBefore() {
                            //show progress dialog
                        }

                        /**
                         * 网路请求之后
                         */
                        @Override
                        public void onAfter() {
                            //show progress dialog
                        }

                        /**
                         * 没有新版本
                         */

                        @Override
                        public void noNewApp() {
                            //没有新版本
                        }
                    });
        }
    }
}
