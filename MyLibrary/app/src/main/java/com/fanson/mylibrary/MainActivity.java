package com.fanson.mylibrary;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.fansonlib.base.AppUtils;
import com.example.fansonlib.base.BaseMvpActivity;
import com.example.fansonlib.http.HttpUtils;
import com.example.fansonlib.http.retrofit.RetrofitClient;
import com.example.fansonlib.http.retrofit.RetrofitStrategy;
import com.example.fansonlib.http.retrofit.download.FileDownLoadObserver;
import com.example.fansonlib.http.retrofit.download.MyDownLoadManager;
import com.example.fansonlib.image.ImageLoaderConfig;
import com.example.fansonlib.image.ImageLoaderUtils;
import com.example.fansonlib.rxbus.MyRxbus2;
import com.example.fansonlib.rxbus.annotation.Subscribe;
import com.example.fansonlib.rxbus.event.EventThread;
import com.example.fansonlib.utils.ShowToast;
import com.example.fansonlib.utils.notification.MyNotificationUtils;
import com.example.fansonlib.widget.dialogfragment.DoubleDialog;
import com.example.fansonlib.widget.dialogfragment.base.ICancelListener;
import com.example.fansonlib.widget.dialogfragment.base.IConfirmListener;
import com.example.fansonlib.widget.loading.MyLoadingView;
import com.fanson.mylibrary.adapter.RecyclerViewActivity;
import com.fanson.mylibrary.constant.RxBusTag;
import com.fanson.mylibrary.databinding.ActivityMainBinding;
import com.fanson.mylibrary.mvp.ContractTest;
import com.fanson.mylibrary.mvp.TestPresenter;
import com.fanson.mylibrary.mvvm.ViewModelActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MainActivity extends BaseMvpActivity<TestPresenter,ActivityMainBinding> implements ContractTest.TestView {


    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageView iv_pic;
    private TestPresenter mTestPresenter;
    private Button mBtnNet, mBtnDownload, btn_fragment, btn_upload, mBtnDialog;
    private Button mBtnNotification, mBtnRxBus;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    /**
     * 接收Rx事件
     *
     * @param content 内容
     */
    @Subscribe(eventTag = RxBusTag.TEST, threadMode = EventThread.NEW_THREAD)
    public void receiverRxMessage(Integer content) {
        mBtnRxBus.setText(content + " 线程：" + (getMainLooper() == Looper.myLooper()));
    }


    @Override
    protected void initView(Bundle savedInstanceState) {

        AppUtils.init(getApplicationContext());
        MyRxbus2.getInstance().register(this);

        mBtnNet = findMyViewId(R.id.btn_net);
        mBtnRxBus = findMyViewId(R.id.btn_rxBus);
        mBtnNotification = findMyViewId(R.id.btn_notification);
        mBtnDialog = findMyViewId(R.id.btn_dialog_fragment);
        mBtnDownload = findMyViewId(R.id.btn_download);
        btn_upload = findMyViewId(R.id.btn_upload);
        btn_fragment = findMyViewId(R.id.btn_fragment);
        mBtnNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                testPopuWindow();
                testBaseModel();
//                testImageLoader();
//                testLoadingView();
//                ShowToast.Config.getInstance().setInfoColor(ContextCompat.getColor(MainActivity.this,R.color.colorAccent)).apply();
//                ShowToast.Long("QQQQ");
            }
        });

        mBtnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDownLoadManager.downloadFile("http://p.gdown.baidu.com/f2db1156581f17f40fa491c9ef82e14e3e94b33d48fe2815156ff454b02fdb429d2dc99a926495e2aa1d9d90683e825e8d5853beab866f64a9498d23cf9e5c7582fddefa770767c812d0fd36d771e944677ad1a8b12fbf832d08dd1279d3e0d9f40b3e93d357373a98ab9162535fec522093102d7e6c0d3e362af2a3ae9b2f662e403a4bd66b45fcd92f4332ea90b831dd5538c47c7f39703b15856488028ecc6aaa028bd1797736cbb37d2d4b64148a2d25c17fe2804a56631b6938747e707344f8802c06dfe467",
                        AppUtils.getAppContext().getExternalCacheDir() + "/apk/", "Test.apk", new FileDownLoadObserver<File>() {
                            @Override
                            public void onDownLoadSuccess(File file) {
                                Log.d(TAG,"onDownLoadSuccess");
                            }

                            @Override
                            public void onDownLoadFail(Throwable throwable) {

                            }

                            @Override
                            public void onProgress(int progress, long total) {
                                Log.d(TAG,"progress = "+(float) progress % total);
                            }
                        });
            }
        });


        mBtnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testDialogFragment();
            }
        });

        btn_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMyActivity(RecyclerViewActivity.class);
            }
        });

        mBtnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri path = Uri.parse("android.resource://" + getPackageName()  + "/" + R.raw.sound_money);
                MyNotificationUtils.init(getApplicationContext());
//                MyNotificationUtils.buildSimple(1, MyNotificationUtils.CHANNEL_ID_SERVICE, R.mipmap.default_image, "通知栏标题",
//                        "通知栏内容", MyNotificationUtils.buildIntent(MainActivity.class)).show();

                MyNotificationUtils.buildProgress(1, MyNotificationUtils.CHANNEL_ID_SERVICE, R.mipmap.default_image, "通知栏标题",
                        1, 100).show();
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(1000);
                        MyNotificationUtils.updateProgress(i).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mBinding.btnLoadingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startMyActivity(TestLoadingActivity.class);
            }
        });


        mBtnRxBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyRxbus2.getInstance().send(RxBusTag.TEST, 100);
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //上传图文(多图)
                String path1 = Environment.getExternalStorageDirectory() + File.separator + "DCIM/camera/IMG_20170821_181327.jpg";
                ArrayList<String> pathList = new ArrayList<>();
                pathList.add(path1);
                Map<String, Object> bodyMap = new HashMap<>();
                File file = null;
                if (pathList.size() > 0) {
                    for (int i = 0; i < pathList.size(); i++) {
                        file = new File(pathList.get(i));
                        bodyMap.put("file" + i + "\";filename=\"" + file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
                    }
                }
                bodyMap.put("text", "测试文字");
                RetrofitClient.init(ApiStores.API_SERVER_URL);
                RetrofitStrategy strategy = new RetrofitStrategy();
                strategy.setApi(new ApiFactoryImpl());
                HttpUtils.init(strategy);

                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
//                bodyMap.put("part",part);

//                RetrofitClient.getRetrofit(ApiStores.class).uploadMulti("app/file/test", part).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<ResponseBody>() {
//                            @Override
//                            public void accept(@io.reactivex.annotations.NonNull ResponseBody responseBody) throws Exception {
//                                Log.d("TAG", responseBody.string());
//                            }
//                        });

//                HttpUtils.getHttpUtils().post("post.php", bodyMap, new HttpResponseCallback<TestBean>() {
//                    @Override
//                    public void onSuccess(TestBean bean) {
//                        Log.d("TAG",bean.getData());
//                    }
//
//                    @Override
//                    public void onFailure(String errorMsg) {
//                        Log.d("TAG",errorMsg);
//                    }
//                });
            }
        });

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

        mBinding.btnViewModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMyActivity(ViewModelActivity.class);
            }
        });

        mBinding.btnLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testImageLoader();
            }
        });

    }

    private void testDialogFragment() {
        DoubleDialog.newInstance("提示", "抱歉！暂时没有在线客服人员，请稍后再试")
                .setConfirmListener(new IConfirmListener() {
                    @Override
                    public void onConfirm() {
                        ShowToast.singleLong("onConfirm");
                    }
                }).setCancelListener(new ICancelListener() {
            @Override
            public void onCancel() {
                ShowToast.singleLong("onCancel");
            }
        }).show(getSupportFragmentManager());

    }


    private void testImageLoader() {
        iv_pic = (ImageView) findViewById(R.id.iv_pic);
        String picUrl = "http://guolin.tech/book.png";
        ImageLoaderUtils.init();
        ImageLoaderUtils.getInstance()
                .setImageLoaderConfig(new ImageLoaderConfig.Builder().placePicRes(R.mipmap.default_image).build())
                .loadImage(this,iv_pic,picUrl);

//        ImageLoaderUtils.loadCornerImage(this,iv_pic,picUrl);
//        ImageLoaderUtils.loadImageWithListener(this, iv_pic, picUrl, new OnUniversalListener() {
//            @Override
//            public void loadStart() {
//                Log.d(TAG, "loadStart");
//            }
//
//            @Override
//            public void loadSuccess() {
//                Log.d(TAG, "loadSuccess");
//            }
//
//            @Override
//            public void loadFailed() {
//                Log.d(TAG, "loadFailed");
//            }
//        }, new OnUniversalProgress() {
//            @Override
//            public void onProgressUpdate(int current, int total) {
//                Log.d(TAG, "onProgress :　" + ((double) current / total * 100));
//            }
//        });
    }

    /**
     * 测试网络
     */
    private void testBaseModel() {
        mPresenter.testPresenterMethod();
        mPresenter.observe(this, new Observer<SimpleBean>() {
            @Override
            public void onChanged(@Nullable SimpleBean bean) {
                if (bean != null) {
                    mBtnNet.setText(bean.getData().getName());
                }
            }
        });
    }

    private void testLoadingView() {
//        View view = this.getLayoutInflater().inflate(R.layout.view_loading, null);
        final MyLoadingView myLoadingView = (MyLoadingView) this.findViewById(R.id.loadingView);
//        myLoadingView.setLoadingTextColor(R.color.colorPrimary);
//        myLoadingView.setProgressWheelColor(ContextCompat.getColor(this,R.color.colorAccent));
//        myLoadingView.setLoadingText("登录中");
//        myLoadingView.setCustomLoadingView(view);
        myLoadingView.setLoadingModel(MyLoadingView.MODEL_ALERT);
        myLoadingView.setOnBtnClickListener(new MyLoadingView.OnRetryClickListener() {
            @Override
            public void onRetry() {
                myLoadingView.loading();
            }
        });
        myLoadingView.loading();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        myLoadingView.failRetry(null);
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
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected TestPresenter createPresenter() {
        return new TestPresenter(this, this);
    }

    @Override
    public void showFailure(String errorMsg) {

    }
}
