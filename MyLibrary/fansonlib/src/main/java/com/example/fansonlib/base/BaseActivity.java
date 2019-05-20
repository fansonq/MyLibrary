package com.example.fansonlib.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.example.fansonlib.R;
import com.example.fansonlib.callback.IBackFragmentListener;
import com.example.fansonlib.callback.IFragmentListener;
import com.example.fansonlib.constant.BaseConFragmentCode;
import com.example.fansonlib.manager.MyFragmentManager;
import com.example.fansonlib.utils.NetWorkUtil;
import com.example.fansonlib.utils.log.MyLogUtils;
import com.example.fansonlib.view.LoadingDialog;

import org.aviran.cookiebar2.CookieBar;

/**
 * @author Created by：Fanson
 * Created on：2016/8/23
 * Description：Activity基类(带DataBinding)
 */
public abstract class BaseActivity<D extends ViewDataBinding> extends AppCompatActivity implements IFragmentListener, IBackFragmentListener {

    private static final String TAG = BaseActivity.class.getSimpleName();
    protected Context mContext;
    protected D mBinding;
    /**
     * 当前生命周期状态
     */
    protected String mCurrentStatus = "";
    protected static final String STATUS_CREATE = "onCreate";
    protected static final String STATUS_START = "onStart";
    protected static final String STATUS_RESUME = "onResume";
    protected static final String STATUS_PAUSE = "onPause";
    protected static final String STATUS_STOP = "onStop";
    protected static final String STATUS_DESTROY = "onDestroy";

    /**
     * 加载动画框
     */
    private LoadingDialog mLoadingDialog;
    /**
     * Fragment的管理类
     */
    protected MyFragmentManager mFragmentManager;
    /**
     * 标记是否内存不足被重建
     */
    protected boolean mIsRecreate = false;
    protected static final String PARAM_RECREATE = "IS_RECREATE";

    /**
     * 监听网络连接状态的广播
     */
    private BroadcastReceiver mNetStateBroadcastReceiver;

    /**
     * 记录点击返回按钮的时间
     */
    protected long mClickBackTime;
    /**
     * 指定时间2秒内，双击了返回按钮则退出
     */
    protected static final long EXIT_TIME = 2000;

    /**
     * 延迟加载的Handler
     */
    private Handler mDelayLoadHandler;
    private Runnable mDelayLoadRunnable;


    public BaseActivity() {
        mContext = this;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mIsRecreate = savedInstanceState.getBoolean(PARAM_RECREATE);
        }
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mBinding = DataBindingUtil.setContentView(this, getContentView());
        mCurrentStatus = STATUS_CREATE;
        initNetStateBroadCastReceiver();
        if (mFragmentManager == null) {
            mFragmentManager = new MyFragmentManager();
        }
        initView(savedInstanceState);
        initData();
        listenEvent();
    }

    /**
     * 初始化延迟加载数据的功能
     *
     * @param delayTime 延迟时间：毫秒
     */
    protected void initDelayLoadData(int delayTime) {
        mDelayLoadHandler = new Handler();
        mDelayLoadRunnable = new Runnable() {
            @Override
            public void run() {
                startDelayLoad();
            }
        };
        mDelayLoadHandler.postDelayed(mDelayLoadRunnable, delayTime);
    }

    /**
     * 延迟加载数据
     * <p>调用此方法之前，必须先调用initDelayLoadData方法
     */
    protected void startDelayLoad() {
        //用户手动覆写
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PARAM_RECREATE, true);
    }

    /**
     * 加载layout
     *
     * @return 布局ID
     */
    protected abstract int getContentView();

    /**
     * 初始化View
     *
     * @param savedInstanceState savedInstanceState
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 点击事件
     */
    protected abstract void listenEvent();


    @Override
    protected void onStart() {
        super.onStart();
        mCurrentStatus = STATUS_START;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentStatus = STATUS_RESUME;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCurrentStatus = STATUS_PAUSE;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCurrentStatus = STATUS_STOP;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCurrentStatus = STATUS_DESTROY;
        if (mFragmentManager != null) {
            mFragmentManager.clearList();
            mFragmentManager = null;
        }
        unregisterNetReceiver();
        releaseDelayHandler();
    }

    /**
     * 释放延迟加载数据的Handler
     */
    private void releaseDelayHandler() {
        if (mDelayLoadHandler != null) {
            mDelayLoadHandler.removeCallbacks(mDelayLoadRunnable);
            mDelayLoadHandler = null;
        }
    }

    /**
     * 注销监听网络状态的广播
     */
    private void unregisterNetReceiver() {
        if (mNetStateBroadcastReceiver != null) {
            try {
                unregisterReceiver(mNetStateBroadcastReceiver);
            } catch (IllegalArgumentException ex) {
                if (ex.getMessage().contains("Receiver not registered")) {
                    //Ignore this exception
                } else {
                    throw ex;
                }
            }
        }
    }

    /**
     * 获取DataBinding的绑定
     *
     * @return DataBinding
     */
    public D getBinding() {
        return (D) mBinding;
    }

    /**
     * 初始化监听网络连接的广播
     */
    private void initNetStateBroadCastReceiver() {
        try {
            mNetStateBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if ((ConnectivityManager.CONNECTIVITY_ACTION).equals(intent.getAction())) {
                        if (NetWorkUtil.isNetWordConnected(mContext)) {
                            MyLogUtils.getInstance().d("BaseActivity - 恢复网络链接");
                            CookieBar.dismiss(BaseActivity.this);
                        } else {
                            MyLogUtils.getInstance().d("BaseActivity - 失去网络链接");
                            CookieBar.build(BaseActivity.this)
                                    .setTitle(getResources().getString(R.string.no_net))
                                    .setBackgroundColor(R.color.black)
                                    .setEnableAutoDismiss(false)
                                    .setSwipeToDismiss(true)
                                    .setAnimationIn(R.anim.slide_from_top, R.anim.slide_to_top)
                                    .show();
                        }
                    }
                }
            };
            //注册广播
            registerReceiver(mNetStateBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 此方法的目的是子类使用此方法findViewById不再需要强转，注意：接受类型一定不要写错
     *
     * @param id  控件ID
     * @param <T> 泛型
     * @return 控件view
     */
    public <T> T findMyViewId(int id) {
        T view = (T) findViewById(id);
        return view;
    }

    @Override
    public void onFragmentCallback(Object... object) {
        switch ((int) object[0]) {
            case BaseConFragmentCode.FRAGMENT_BACK:
                onBackPressed();
                break;

            case BaseConFragmentCode.POP_FRAGMENT:
                mFragmentManager.popTopFragment(getSupportFragmentManager());
                break;

            case BaseConFragmentCode.SHOW_LOADING:
                showLoading();
                break;

            case BaseConFragmentCode.HIDE_LOADING:
                hideLoading();
                break;
            default:
                break;
        }
    }


    @Override
    public void currentFragmentBack(Fragment fragment) {
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager != null && mFragmentManager.handlerBackPress(getSupportFragmentManager()) && getSupportFragmentManager().getBackStackEntryCount() > 0) {
            return;
        }
        finish();
    }

    /**
     * 显示加载框
     */
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog();
        }
        mLoadingDialog.show(getSupportFragmentManager());
    }

    /**
     * 隐藏加载框
     */
    public void hideLoading() {
        if (mLoadingDialog != null) {
            try {
                mLoadingDialog.hideDialog(getSupportFragmentManager());
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            mLoadingDialog = null;
        }
    }

    /**
     * 跳转Activity
     *
     * @param targetActivity 目标
     */
    protected void startMyActivity(@NonNull Class<?> targetActivity) {
        startActivity(new Intent(this, targetActivity));
    }

    /**
     * 跳转Activity
     *
     * @param targetActivity 目标
     * @param bundle         数据
     */
    protected void startMyActivityWithData(Class<?> targetActivity, Bundle bundle) {
        if (bundle != null) {
            startActivity(new Intent(this, targetActivity).putExtras(bundle));
        }
    }

}





