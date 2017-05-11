package com.example.fansonlib.base;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.example.fansonlib.R;
import com.example.fansonlib.bean.EventNetWork;
import com.example.fansonlib.utils.NetWorkUtil;

import de.greenrobot.event.EventBus;

/**
 * Created by fanson on 2016/8/23.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    private Context mContext;

    public static final String CHINESE = "zh";
    public static final String ENGLISH = "en";

    /**
     * 监听网络连接状态的广播
     */
    private BroadcastReceiver netStateBroadcastReceiver;
    private AlertDialog.Builder dialogBuilder;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    /**
     * 记录退出时间
     */
    private long exitTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getContentView());
        initNetStateBroadCastReceiver();
        //注册EventBus
        EventBus.getDefault().registerSticky(this);
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
//        MobclickAgent.onResume(this);
//        loadLanguageToViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销EventBus
        EventBus.getDefault().unregister(this);

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
//        SampleApplicationLike.getRefWatcher(this).watch(this);
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
     * 初始化监听网络连接的广播
     */
    private void initNetStateBroadCastReceiver() {
        netStateBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    if (NetWorkUtil.isNetWordConnected(mContext)) {
                        EventBus.getDefault().postSticky(new EventNetWork(EventNetWork.AVAILABLE));
                    } else {
                        EventBus.getDefault().postSticky(new EventNetWork(EventNetWork.UNAVAILABLE));
                    }
                }
            }
        };
        //注册广播
        registerReceiver(netStateBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    //被动调用
    public void onEvent(EventNetWork event) {
        if (event.getResult() == EventNetWork.UNAVAILABLE) {
            if (dialogBuilder == null) {
                dialogBuilder = new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.no_net))
                        .setMessage(getResources().getString(R.string.go_open_net))
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialogBuilder.show();
            }
        }
    }

    /**
     * 加载Fragment(带动画)
     *
     * @param id_content
     * @param fragment
     */
    protected void replaceFragment(int id_content, Fragment fragment, int enter, int exit) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(enter, exit);
        transaction.replace(id_content, fragment);
        transaction.commit();
    }

    /**
     * 加载Fragment
     *
     * @param id_content
     * @param fragment
     */
    protected void replaceFragment(int id_content, Fragment fragment) {
        replaceFragmentWithTag(id_content,fragment,null);
    }

    /**
     * 加载Fragment（附带tag）
     *
     * @param id_content
     * @param fragment
     */
    protected void replaceFragmentWithTag(int id_content, Fragment fragment, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(id_content, fragment, tag);
        transaction.commit();
    }

    /**
     * 加载Fragment,添加回退栈
     *
     * @param id_content
     * @param fragment   目标Fragment
     * @param tag        标志
     */
    protected void replaceFragmentToStack(int id_content, Fragment fragment, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(id_content, fragment, tag).addToBackStack(null);
        transaction.commit();
    }

    /**
     * 添加Fragment
     *
     * @param id_content
     * @param fragment
     * @param enter      进场动画
     * @param exit       退场动画
     * @param tag
     */
    protected void addFragmentWithTag(int id_content, Fragment fragment, int enter, int exit, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(enter, exit);
        transaction.add(id_content, fragment, tag);
        transaction.commit();
    }

    /**
     * 添加Fragment
     *
     * @param id_content
     * @param fragment
     * @param tag
     */
    protected void addFragmentWithTag(int id_content, Fragment fragment, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(id_content, fragment, tag);
        transaction.commit();
    }

    /**
     * 切换Framgment（hide/show）
     *
     * @param id_content
     * @param fromFragment
     * @param toFragment
     */
    protected void switchFragment(int id_content, Fragment fromFragment, Fragment toFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (toFragment.isAdded()) {
            transaction.hide(fromFragment).show(toFragment).commit();
        } else {
            transaction.hide(fromFragment).add(id_content, toFragment).commit();
        }
    }

    /**
     * 显示存在的Fragment
     *
     * @param fragment
     */
    protected void showFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragment.isAdded()) {
            transaction.show(fragment).commit();
        }
    }

    /**
     * 删除指定的Fragment
     *
     * @param fragment
     */
    protected void removeFragment(Fragment fragment) {
        if (fragmentManager != null && fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
    }

    /**
     * 查找指定Tag的Fragment
     *
     * @param tag
     * @return
     */
    public Fragment findFragmentByTag(String tag) {
        if (fragmentManager != null) {
            return fragmentManager.findFragmentByTag(tag);
        }
        return null;
    }

    /**
     * 查找指定Id的Fragment
     *
     * @param id
     * @return
     */
    public Fragment findFragmentById(int id) {
        if (fragmentManager != null) {
            return fragmentManager.findFragmentById(id);
        }
        return null;
    }

    /**
     * 删除指定tag的Fragment
     *
     * @param tag
     */
    protected void removeFragment(String tag) {
        if (fragmentManager != null) {
            removeFragment(fragmentManager.findFragmentByTag(tag));
            fragmentManager.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if ((System.currentTimeMillis() - exitTime) > 2000) {
//                exitTime = System.currentTimeMillis();
//                ShowToast.Short(getResources().getString(R.string.again_exit));
//            } else {
//                finish();
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }


    /**
     * 退出所有的Activity
     */
    public void CloseAllActivity() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}





