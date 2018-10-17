package com.example.fansonlib.base;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.example.fansonlib.R;
import com.example.fansonlib.utils.MySnackBarUtils;
import com.example.fansonlib.utils.NetWorkUtil;

/**
 * @author Created by：Fanson
 * Created on：2016/8/23
 * Description：Activity基类(带DataBinding)
 */
public abstract class BaseActivity<D extends ViewDataBinding> extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    protected Context mContext;
    protected D mBinding;

    /**
     * 监听网络连接状态的广播
     */
    private BroadcastReceiver netStateBroadcastReceiver;
    private AlertDialog.Builder dialogBuilder;

    /**
     * 记录退出时间
     */
    private long exitTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mBinding = DataBindingUtil.setContentView(this, getContentView());
        initNetStateBroadCastReceiver();
        initView();
        initData();
        listenEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销广播
        if (netStateBroadcastReceiver != null) {
            try {
                unregisterReceiver(netStateBroadcastReceiver);
            } catch (IllegalArgumentException ex) {
                if (ex.getMessage().contains("Receiver not registered")) {
                    //Ignore this exception
                } else {
                    throw ex;
                }
            }
        }
//        InputMethodUtils.fixInputMethodManagerLeak(this);
    }

    public BaseActivity() {
        mContext = this;
    }

    /**
     * 加载layout
     */
    protected abstract int getContentView();

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 点击事件
     */
    protected abstract void listenEvent();

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
        netStateBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ((ConnectivityManager.CONNECTIVITY_ACTION).equals(intent.getAction())) {
                    if (!NetWorkUtil.isNetWordConnected(mContext)) {
                        MySnackBarUtils.showIndefinite(getWindow().getDecorView(), getResources().getString(R.string.no_net)).show();
                        if (MySnackBarUtils.getSnackbarView() != null) {
                            MySnackBarUtils.getSnackbarView().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                                }
                            });
                        }
                    } else {
                        MySnackBarUtils.dismiss();
                    }
                }
            }
        };
        //注册广播
        registerReceiver(netStateBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    /**
     * 此方法的目的是子类使用此方法findViewById不再需要强转，注意：接受类型一定不要写错
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T> T findMyViewId(int id) {
        T view = (T) findViewById(id);
        return view;
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





