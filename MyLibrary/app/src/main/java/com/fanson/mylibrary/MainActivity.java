package com.fanson.mylibrary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.fansonlib.base.AppUtils;
import com.example.fansonlib.base.SwipeBackActivity;
import com.example.fansonlib.image.ImageLoaderUtils;
import com.example.fansonlib.image.universalloader.OnUniversalListener;
import com.example.fansonlib.image.universalloader.OnUniversalProgress;
import com.example.fansonlib.widget.dialogfragment.ConfirmDialog;
import com.example.fansonlib.widget.loading.MyLoadingView;
import com.fanson.mylibrary.mvp.Test2Prensenter;
import com.fanson.mylibrary.mvp.TestPresenter;
import com.fanson.mylibrary.update.MyUpdateService;
import com.fanson.mylibrary.update.TestWindow;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends SwipeBackActivity implements Observer {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageView iv_pic;
    //    private MyPermissionHelper myPermissionHelper;
    private TestPresenter mTestPresenter;
    private Test2Prensenter mTestPresenter2;
    private Button button, button2;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        AppUtils.init(getApplicationContext());
        button = findMyViewId(R.id.btn);
        button2 = findMyViewId(R.id.btn2);
        Log.d("TTT", "initView");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                testUpdate();
//                testPopuWindow();
//                testBaseModel();
//                testImageLoader();
                testDialogFragment();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTestPresenter.detachView();
            }
        });

//        testLoadingView();

//        addFragmentWithTag(R.id.fl_main,new TestFragment(),R.anim.slide_from_top,R.anim.slide_to_bottom,null);

//        myPermissionHelper = new MyPermissionHelper(this);
//        myPermissionHelper.requestPermissions("paizhao", new MyPermissionHelper.PermissionListener() {
//            @Override
//            public void doAfterGrand(String... permission) {
//                ImagePicker.getInstance().takePicture(MainActivity.this,100);
//            }
//
//            @Override
//            public void doAfterDenied(String... permission) {
//
//            }
//        }, Manifest.permission.CAMERA);
    }

    private void testDialogFragment() {
        ConfirmDialog.newInstance("提示","你预约成功！")
                .setMargin(60)
                .setOutCancel(false)
                .show(getSupportFragmentManager());

    }


    private void testImageLoader() {
        iv_pic = (ImageView) findViewById(R.id.iv_pic);
        String picUrl = "http://img02.tooopen.com/images/20140504/sy_60294738471.jpg";
//        ImageLoaderUtils.loadCircleImage(this,iv_pic,picUrl);
        ImageLoaderUtils.loadImageWithListener(this, iv_pic, picUrl, new OnUniversalListener() {
            @Override
            public void loadStart() {
                Log.d(TAG, "loadStart");
            }

            @Override
            public void loadSuccess() {
                Log.d(TAG, "loadSuccess");
            }

            @Override
            public void loadFailed() {
                Log.d(TAG, "loadFailed");
            }
        }, new OnUniversalProgress() {
            @Override
            public void onProgressUpdate(int current, int total) {
                Log.d(TAG, "onProgress :　" + ((double) current / total * 100));
            }
        });
    }

    private void testPopuWindow() {
        TestWindow window = new TestWindow(this);
        window.showPopupWindow();
    }

    private void testBaseModel() {
        mTestPresenter = new TestPresenter();
        mTestPresenter.attachView(null);
        mTestPresenter.testMethod();
        Log.d("TTT", "1");
//        mTestPresenter2 = new Test2Prensenter();
//        mTestPresenter2.attachView(null);
//        mTestPresenter2.methodTest2();
//        Log.d("TTT", "2");
    }

    private void testUpdate() {
        String updateUrl = "WVector/AppUpdateDemo/master/json/json.txt";
        Intent intent = new Intent(this, MyUpdateService.class);
        intent.putExtra("url", updateUrl);
        startService(intent);
    }

    private void testLoadingView() {
        View view = this.getLayoutInflater().inflate(R.layout.view_loading, null);
        final MyLoadingView myLoadingView = (MyLoadingView) this.findViewById(R.id.loadingView);
//        myLoadingView.setLoadingTextColor(R.color.colorPrimary);
//        myLoadingView.setProgressWheelColor(ContextCompat.getColor(this,R.color.colorAccent));
//        myLoadingView.setLoadingText("登录中");
        myLoadingView.setCustomLoadingView(view);
        myLoadingView.setLoadingModel(MyLoadingView.MODEL_DEFAULT);
        myLoadingView.setOnBtnClickListener(new MyLoadingView.OnRetryClickListener() {
            @Override
            public void onRetry() {
                myLoadingView.loading();
            }
        });
        myLoadingView.loading();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        myLoadingView.failRetry("失败");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (myPermissionHelper != null) {
//            myPermissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void listenEvent() {

    }

    @Override
    public void update(Observable observable, Object o) {
        Log.d("TTT", o.toString());
    }
}
