package com.example.fansonlib.manager;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.LinkedList;
import java.util.List;


/**
 * @author Created by：fanson
 *         Created Time: 2018/4/17 13:14
 *         Describe：管理App中Fragment的工具类
 */

public class MyFragmentManager {

    /**
     * 装载Fragment的集合
     */
    public static List<Fragment> mFragmentList = new LinkedList<>();

    /**
     * 切换Fragment不带动画
     */
    private static final int NO_ANIM = 0x11;

/*    public volatile static MyFragmentManager mFragmentManager;

    public static MyFragmentManager getFragmentManager() {
        if (mFragmentManager == null) {
            synchronized (MyFragmentManager.class) {
                if (mFragmentManager == null) {
                    mFragmentManager = new MyFragmentManager();
                }
            }
        }
        return mFragmentManager;
    }*/

    /**
     * 获取Fragment的个数
     *
     * @return Fragment的个数
     */
    public int getSize() {
        return mFragmentList.size();
    }

    /**
     * 获取栈顶的Fragment
     *
     * @return 栈顶的Fragment
     */
    public synchronized Fragment getForwardFragment() {
        return getSize() > 0 ? mFragmentList.get(getSize() - 1) : null;
    }

    /**
     * 添加一个Fragment
     *
     * @param fragment 指定Fragment
     */
    public synchronized void add(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    /**
     * 移除栈堆的一个Fragment
     *
     * @param fragment 指定Fragment
     */
    public synchronized void remove(Fragment fragment) {
        if (mFragmentList.contains(fragment)) {
            mFragmentList.remove(fragment);
        }
    }

    /**
     * 清除所有的Fragment
     */
    public synchronized void clearAllFragment() {
        Fragment fragment;
        for (int i = mFragmentList.size(); i > -1; i--) {
            fragment = mFragmentList.get(i);
            mFragmentList.remove(fragment);
        }
        mFragmentList.clear();
    }


    /**
     * 清除所有Fragment（除了栈顶的以外）
     */
    public synchronized void clearExceptTop() {
        Fragment fragment;
        for (int i = mFragmentList.size() - 2; i > -1; i--) {
            fragment = mFragmentList.get(i);
            mFragmentList.remove(fragment);
        }
    }

    /**
     * 出栈fragment
     *
     * @param fragmentManager fragment管理器
     * @return { true}: 出栈成功; false: 出栈失败
     */
    public static boolean popFragment(@NonNull FragmentManager fragmentManager) {
        return fragmentManager.popBackStackImmediate();
    }

    /**
     * 出栈到指定fragment
     *
     * @param fragmentManager fragment管理器
     * @param fragmentClass   Fragment类
     * @param isIncludeSelf   是否包括Fragment类自己
     * @return  true: 出栈成功;false: 出栈失败
     */
    public static boolean popToFragment(@NonNull FragmentManager fragmentManager, Class<? extends Fragment> fragmentClass, boolean isIncludeSelf) {
        return fragmentManager.popBackStackImmediate(fragmentClass.getName(), isIncludeSelf ? FragmentManager.POP_BACK_STACK_INCLUSIVE : 0);
    }

    /**
     * 处理fragment回退键
     * 如果fragment实现了OnBackClickListener接口，返回true: 表示已消费回退键事件，反之则没消费
     *
     * @param fragment fragment
     * @return 是否消费回退事件
     */

    public static boolean handlerBackPress(@NonNull Fragment fragment) {
        return handlerBackPress(fragment.getFragmentManager());
    }

    /**
     * 处理fragment回退键
     * 如果fragment实现了OnBackClickListener接口，返回 true: 表示已消费回退键事件，反之则没消费
     *
     * @param fragmentManager fragment管理器
     * @return 是否消费回退事件
     */
    public static boolean handlerBackPress(@NonNull FragmentManager fragmentManager) {
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments == null || fragments.isEmpty()) {
            return false;
        }
        for (int i = fragments.size() - 1; i >= 0; --i) {
            Fragment fragment = fragments.get(i);
            if (fragment != null
                    && fragment.isResumed()
                    && fragment.isVisible()
                    && fragment.getUserVisibleHint()
                    && fragment instanceof OnBackClickListener
                    && ((OnBackClickListener) fragment).onBackClick()) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取当前的Fragment
     *
     * @return 栈顶的Fragment
     */
    public static synchronized Fragment getCurrentFragment() {
        if (mFragmentList == null || mFragmentList.isEmpty()) {
            return null;
        }
        return mFragmentList.get(mFragmentList.size() - 1);
    }

    /**
     * 移除当前的Fragment
     */
    public static synchronized void finishCurrentFragment() {
        if (mFragmentList == null || mFragmentList.isEmpty()) {
            return;
        }
        finishFragment(mFragmentList.get(mFragmentList.size() - 1));
    }

    /**
     * 移除指定的Fragment
     *
     * @param fragment 指定的Fragment
     */
    public static synchronized void finishFragment(Fragment fragment) {
        if (mFragmentList == null || mFragmentList.isEmpty()) {
            return;
        }
        if (fragment != null) {
            mFragmentList.remove(fragment);
        }
    }

    /**
     * 结束指定类名的Fragment
     */
    public static void finishFragment(Class<?> cls) {
        if (mFragmentList == null || mFragmentList.isEmpty()) {
            return;
        }
        for (Fragment fragment : mFragmentList) {
            if (fragment.getClass().equals(cls)) {
                finishFragment(fragment);
            }
        }
    }

    /**
     * 按照指定类名找到fragment
     *
     * @param cls 指定的类名
     * @return 指定类名的fragment
     */
    public static Fragment findFragment(Class<?> cls) {
        Fragment targetFragment = null;
        if (mFragmentList != null) {
            for (Fragment fragment : mFragmentList) {
                if (fragment.getClass().equals(cls)) {
                    targetFragment = fragment;
                    break;
                }
            }
        }
        return targetFragment;
    }

    /**
     * @return 获取当前最顶部的fragment 名字
     */
    public static String getTopFragmentName() {
        Fragment fragment;
        synchronized (mFragmentList) {
            final int size = mFragmentList.size() - 1;
            if (size < 0) {
                return null;
            }
            fragment = mFragmentList.get(size);
        }
        return fragment.getClass().getName();
    }


    /*-------------------------------加载Fragment的方式-----------------------------------*/

    /**
     * 加载Fragment
     *
     * @param layoutId
     * @param fragment
     */
    public static void replaceFragment(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fragment) {
        replaceFragment(fragmentManager,layoutId, fragment, null, NO_ANIM, NO_ANIM);
    }

    /**
     * 加载Fragment（附带tag）
     *
     * @param layoutId
     * @param fragment
     */
    public static void replaceFragmentWithTag(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fragment, String tag) {
        replaceFragment(fragmentManager,layoutId, fragment, tag, NO_ANIM, NO_ANIM);
    }

    /**
     * 加载Fragment（附带动画）
     *
     * @param layoutId
     * @param fragment
     */
    public static void replaceFragmentWithAnim(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fragment, int enter, int exit) {
        replaceFragment(fragmentManager,layoutId, fragment, null, enter, exit);
    }

    /**
     * 加载Fragment(带动画)
     *
     * @param layoutId
     * @param fragment
     */
    public static void replaceFragment(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fragment, int enter, int exit) {
        replaceFragment(fragmentManager,layoutId, fragment, null, enter, exit, NO_ANIM, NO_ANIM);
    }

    /**
     * 加载Fragment(带动画和TAG)
     *
     * @param layoutId
     * @param fragment
     */
    public static void replaceFragment(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fragment, String tag, int enter, int exit) {
        replaceFragment(fragmentManager,layoutId, fragment, tag, enter, exit, NO_ANIM, NO_ANIM);
    }

    /**
     * 加载Fragment(带出入栈动画和TAG)
     *
     * @param layoutId
     * @param fragment
     */
    public static void replaceFragment(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fragment, String tag, int enter, int exit, int popEnter, int popExit) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (popEnter != NO_ANIM) {
            transaction.setCustomAnimations(enter, exit, popEnter, popExit);
        } else if (enter != NO_ANIM) {
            transaction.setCustomAnimations(enter, exit);
        }
        transaction.replace(layoutId, fragment, tag).addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 添加Fragment(带动画)
     *
     * @param layoutId
     * @param fragment
     * @param enter    进场动画
     * @param exit     退场动画
     * @param tag
     */
    public static void addFragmentWithTag(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fragment, int enter, int exit, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(enter, exit);
        transaction.add(layoutId, fragment, tag).addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 添加Fragment
     *
     * @param layoutId
     * @param fragment
     * @param tag
     */
    public static void addFragmentWithTag(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fragment, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(layoutId, fragment, tag).addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 切换Fragment（hide/show）
     *
     * @param layoutId
     * @param fromFragment
     * @param toFragment
     */
    public static void switchFragment(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fromFragment, Fragment toFragment) {
        switchFragment(fragmentManager,layoutId, fromFragment, toFragment, null, NO_ANIM, NO_ANIM, NO_ANIM, NO_ANIM);
    }

    /**
     * 切换Fragment（hide/show）
     * 带TAG
     *
     * @param layoutId
     * @param fromFragment
     * @param toFragment
     * @param tagOfTo      标识
     */
    public static void switchFragmentWithTag(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fromFragment, Fragment toFragment, String tagOfTo) {
        switchFragment(fragmentManager,layoutId, fromFragment, toFragment, tagOfTo, NO_ANIM, NO_ANIM, NO_ANIM, NO_ANIM);
    }

    /**
     * 切换Fragment（hide/show）
     * 带动画
     *
     * @param layoutId
     * @param fromFragment
     * @param toFragment
     */
    public static void switchFragmentWithAnim(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fromFragment, Fragment toFragment, int enter, int exit) {
        switchFragment(fragmentManager,layoutId, fromFragment, toFragment, null, enter, exit, NO_ANIM, NO_ANIM);
    }

    /**
     * 切换Fragment（hide/show）
     * 带动画和带TAG
     *
     * @param layoutId
     * @param fromFragment
     * @param toFragment
     */
    public static void switchFragmentWithAnim(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fromFragment, Fragment toFragment, String tag, int enter, int exit) {
        switchFragment(fragmentManager,layoutId, fromFragment, toFragment, tag, enter, exit, NO_ANIM, NO_ANIM);
    }

    /**
     * 切换Fragment（hide/show）（带动画）
     *
     * @param layoutId
     * @param fromFragment
     * @param toFragment
     */
    public static void switchFragment(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fromFragment, Fragment toFragment, String tag, int enter, int exit, int popEnter, int popExit) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (toFragment.isAdded()) {
            if (popEnter != NO_ANIM) {
                transaction.setCustomAnimations(enter, exit, popEnter, popExit);
            } else if (enter != NO_ANIM) {
                transaction.setCustomAnimations(enter, exit);
            }
            transaction.show(toFragment).hide(fromFragment).commitAllowingStateLoss();
        } else {
            if (popEnter != NO_ANIM) {
                transaction.setCustomAnimations(enter, exit, popEnter, popExit);
            } else if (enter != NO_ANIM) {
                transaction.setCustomAnimations(enter, exit);
            }
            transaction.add(layoutId, toFragment, tag).hide(fromFragment).addToBackStack(tag).commitAllowingStateLoss();
        }
    }

    /**
     * 查找指定Tag的Fragment
     *
     * @param tag
     * @return
     */
    public static Fragment findFragmentByTag(@NonNull FragmentManager fragmentManager, String tag) {
        return fragmentManager.findFragmentByTag(tag);
    }

    /**
     * 查找fragment
     *
     * @param fragmentManager fragment管理器
     * @param fragmentClass   fragment类
     * @return 查找到的fragment
     */
    public static Fragment findFragment(@NonNull FragmentManager fragmentManager, Class<? extends Fragment> fragmentClass) {
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments.isEmpty()) {
            return null;
        }
        return fragmentManager.findFragmentByTag(fragmentClass.getName());
    }

    /**
     * 删除指定tag的Fragment
     *
     * @param tag 需要删除的tag
     */
    public static void removeFragment(@NonNull FragmentManager fragmentManager, String tag) {
        removeFragment(fragmentManager.findFragmentByTag(tag));
        fragmentManager.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * 删除指定的Fragment
     *
     * @param fragment 需要删除的Fragment
     */
    public static void removeFragment(Fragment fragment) {
        if (fragment != null) {
            fragment.getFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
    }

    /**
     * 隐藏指定的Fragment
     * @param fragmentManager FragmentManager
     * @param fragment 指定的Fragment
     */
    public static void hideFragment(@NonNull FragmentManager fragmentManager, Fragment fragment){
            fragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss();
    }

    /**
     * 显示指定的Fragment
     * @param fragmentManager FragmentManager
     * @param fragment 指定的Fragment
     */
    public static void showFragment(@NonNull FragmentManager fragmentManager, Fragment fragment){
        fragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss();
    }


    /*-------------------------------加载Fragment的方式-----------------------------------*/


    /*------------------------------interface-----------------------------------*/

    /**
     * 给Fragment实现，监听回退键
     */
    public interface OnBackClickListener {
        /**
         * 给Fragment实现，监听Fragment回退键
         *
         * @return 返回{@code true}: 表示已消费回退键事件，反之则没消费
         */
        boolean onBackClick();
    }

}
