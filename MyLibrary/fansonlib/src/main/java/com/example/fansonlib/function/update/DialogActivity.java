package com.example.fansonlib.function.update;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fansonlib.R;
import com.example.fansonlib.constant.LibNotificationID;
import com.example.fansonlib.function.download.RxDownload;
import com.example.fansonlib.function.download.entity.DownloadStatus;
import com.example.fansonlib.function.update.utils.ColorUtil;
import com.example.fansonlib.function.update.utils.DrawableUtil;
import com.example.fansonlib.function.update.utils.Utils;
import com.example.fansonlib.function.update.view.NumberProgressBar;
import com.example.fansonlib.utils.notification.MyNotificationUtils;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 新版本提交对话框
 */
public class DialogActivity extends FragmentActivity implements View.OnClickListener {

private static final String TAG = DialogActivity.class.getSimpleName();
    public static boolean isShow = false;
    private TextView mContentTextView;
    private Button mUpdateOkButton;
    private UpdateAppBean mUpdateApp;
    private NumberProgressBar mNumberProgressBar;
    private ImageView mIvClose;
    private TextView mTitleTextView;
    private File mAppFile; // 更新下载到的Apk文件

    private LinearLayout mLlClose;
    //默认色
    private int mDefaultColor = 0xffe94339;
    private int mDefaultPicResId = R.mipmap.lib_update_app_top_bg;
    private ImageView mTopIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShow = true;
        setContentView(R.layout.lib_update_app_dialog);
        initView();
        initData();
    }

    private void initView() {
//        提示内容
        mContentTextView = (TextView) findViewById(R.id.tv_update_info);
        //标题
        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        //更新按钮
        mUpdateOkButton = (Button) findViewById(R.id.btn_ok);
        //进度条
        mNumberProgressBar = (NumberProgressBar) findViewById(R.id.npb);
        //关闭按钮
        mIvClose = (ImageView) findViewById(R.id.iv_close);
        //关闭按钮+线 的整个布局
        mLlClose = (LinearLayout) findViewById(R.id.ll_close);
        //顶部图片
        mTopIv = (ImageView) findViewById(R.id.iv_top);
    }

    private void initData() {
        mUpdateApp = (UpdateAppBean) getIntent().getSerializableExtra(UpdateAppManager.INTENT_KEY);
        //设置主题色
        initTheme();

        if (mUpdateApp != null) {
            //弹出对话框
            String newVersion = mUpdateApp.getNewVersion();
            String targetSize = mUpdateApp.getAppSize();
            String updateLog = mUpdateApp.getUpdateLog();

            String msg = "";

            if (!TextUtils.isEmpty(targetSize)) {
                msg = "新版本大小：" + targetSize + "\n\n";
            }

            if (!TextUtils.isEmpty(updateLog)) {
                msg += updateLog;
            }

            //更新内容
            mContentTextView.setText(msg);
            //标题
            mTitleTextView.setText(String.format("是否升级到%s版本？", newVersion));
            //强制更新
            if (mUpdateApp.isConstraint()) {
//                mIvClose.setVisibility(View.GONE);
                mLlClose.setVisibility(View.GONE);
            }
            initEvents();
        }
    }

    /**
     * 初始化主题色
     */
    private void initTheme() {


        final int color = getIntent().getIntExtra(UpdateAppManager.THEME_KEY, -1);

        final int topResId = getIntent().getIntExtra(UpdateAppManager.TOP_IMAGE_KEY, -1);


        if (-1 == topResId) {
            if (-1 == color) {
                //默认红色
                setDialogTheme(mDefaultColor, mDefaultPicResId);
            } else {
                setDialogTheme(color, mDefaultPicResId);
            }

        } else {
            if (-1 == color) {
                //自动提色
                Palette.from(Utils.drawableToBitmap(this.getResources().getDrawable(topResId))).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        int mDominantColor = palette.getDominantColor(mDefaultColor);
                        setDialogTheme(mDominantColor, topResId);
                    }
                });
            } else {
                //更加指定的上色
                setDialogTheme(color, topResId);
            }
        }


    }

    /**
     * 设置
     *
     * @param color    主色
     * @param topResId 图片
     */
    private void setDialogTheme(int color, int topResId) {
        mTopIv.setImageResource(topResId);
        mUpdateOkButton.setBackgroundDrawable(DrawableUtil.getDrawable(Utils.dip2px(4, this), color));
        mNumberProgressBar.setProgressTextColor(color);
        mNumberProgressBar.setReachedBarColor(color);
        //随背景颜色变化
        mUpdateOkButton.setTextColor(ColorUtil.isTextColorDark(color) ? Color.BLACK : Color.WHITE);
    }

    private void initEvents() {
        mUpdateOkButton.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_ok) {
            installApp();
            mUpdateOkButton.setVisibility(View.GONE);
        } else if (i == R.id.iv_close) {
            if (mNumberProgressBar.getVisibility() == View.VISIBLE) {
                Toast.makeText(getApplicationContext(), "后台更新app", Toast.LENGTH_LONG).show();
            }
            onBackPressed();
        }
    }

    private void installApp() {
        String apkUrl = mUpdateApp.getApkFileUrl();
        final String appName = apkUrl.substring(apkUrl.lastIndexOf("/") + 1, apkUrl.length());
        mAppFile = new File(mUpdateApp.getTargetPath()
//                .concat(File.separator + mUpdateApp.getNewVersion())
                .concat(File.separator + appName));
        //md5不为空
        //文件存在
        if (!TextUtils.isEmpty(mUpdateApp.getNewMd5())
                && mAppFile.exists()) {
            if (getPackageManager().queryIntentActivities(getInstallIntent(mAppFile), 0).size() > 0) {
                startActivity(getInstallIntent(mAppFile));
            }
            //安装完自杀
            onBackPressed();
        } else {
            downloadApp();
            if (mUpdateApp.isHideDialog()) {
                onBackPressed();
            }
        }
    }

    private Intent getInstallIntent(File appFile){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri fileUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileProvider", appFile);
            intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(appFile), "application/vnd.android.package-archive");
        }
        return intent;
    }


    /**
     * 开启后台服务下载
     */
    private void downloadApp() {
        //使用ApplicationContext延长他的生命周期
//        DownloadService.bindService(getApplicationContext(), conn);
//        Intent intent = new Intent(this,DownloadService.class);
//        intent.putExtra("UpdateBean",mUpdateApp);
//        startService(intent);

        Disposable disposable = RxDownload.getInstance(getApplicationContext())
                .download(mUpdateApp.getApkFileUrl())
//                .download("https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk")//只传url即可
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DownloadStatus>() {
                    @Override
                    public void accept(DownloadStatus status) throws Exception {
                        mNumberProgressBar.setVisibility(View.VISIBLE);
                        mNumberProgressBar.setProgress((int)status.getPercentNumber());
                        Log.d(TAG,"下载进度:"+(int)status.getPercentNumber());
                        MyNotificationUtils.init(getApplicationContext());
                        MyNotificationUtils.buildProgress(LibNotificationID.UPDATE_PROGRESS,R.mipmap.app_driver_icon,getString(R.string.downloading),
                                (int)status.getPercentNumber(),100).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,"下载失败");
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                       Log.d(TAG,"下载成功");
                        PendingIntent intent = PendingIntent.getActivity(DialogActivity.this, 0, getInstallIntent(mAppFile), PendingIntent.FLAG_UPDATE_CURRENT);
                        MyNotificationUtils.buildSimple(LibNotificationID.UPDATE_PROGRESS,R.mipmap.app_driver_icon,getString(R.string.tip),
                                getString(R.string.download_complete) ,intent).show();
                        installApp();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        //禁用
        if (mUpdateApp != null && mUpdateApp.isConstraint()) {
//          ActManager.getInstance().finishAllActivity();
//            android.os.Process.killProcess(android.os.Process.myPid());
            //返回桌面
            startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        isShow = false;
        super.onDestroy();
    }
}
