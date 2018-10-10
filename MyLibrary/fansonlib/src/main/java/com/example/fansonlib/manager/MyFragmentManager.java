package com.example.fansonlib.manager;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.bumptech.glide.manager.SupportRequestManagerFragment;

import java.util.LinkedList;
import java.util.List;


/**
 * @author Created by：fanson
 * Created Time: 2018/4/17 13:14
 * Describe：管理App中Fragment的工具类
 */

public class MyFragmentManager {

    /**
     * 装载Fragment的集合
     */
    public List<Fragment> mFragmentList = new LinkedList<>();

    /**
     * 切换Fragment不带动画
     */
    private final int NO_ANIM = 0x11;

    /**
     * 获取Fragment的个数
     *
     * @return Fragment的个数
     */
    public int getSize() {
        return mFragmentList.size();
    }

    /**
     * 添加一个Fragment到集合
     *
     * @param fragment 指定Fragment
     */
    public synchronized void addToList(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    /**
     * 移除集合的顶部Fragment
     */
    public synchronized void deleteTopInList() {
        if (mFragmentList == null || mFragmentList.isEmpty()) {
            return;
        }
        mFragmentList.remove(mFragmentList.size() - 1);
    }

    /**
     * 移除集合的一个Fragment
     *
     * @param fragment 指定Fragment
     */
    public synchronized void deleteInList(Fragment fragment) {
        if (mFragmentList == null || mFragmentList.isEmpty() || fragment == null) {
            return;
        }
        if (mFragmentList.contains(fragment)) {
            mFragmentList.remove(fragment);
        }
    }

    /**
     * 清除集合里的所有Fragment
     */
    public synchronized void clearList() {
        Fragment fragment;
        for (int i = mFragmentList.size() - 1; i > -1; i--) {
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
     * 出栈顶的fragment
     *
     * @param fragmentManager fragment管理器
     * @return true: 出栈成功; false: 出栈失败
     */
    public boolean popTopFragment(@NonNull FragmentManager fragmentManager) {
        if (fragmentManager.popBackStackImmediate()) {
            deleteTopInList();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 指定fragment出栈
     *
     * @param fragmentManager fragment管理器
     * @param fragment        指定的fragment
     * @param isIncludeSelf   是否包括Fragment类自己
     * @return true: 出栈成功;false: 出栈失败
     */
    public boolean popFragment(@NonNull FragmentManager fragmentManager, Fragment fragment, boolean isIncludeSelf) {
        if (fragmentManager.popBackStackImmediate(fragment.getClass().getName(), isIncludeSelf ? FragmentManager.POP_BACK_STACK_INCLUSIVE : 0)) {
            deleteTopInList();
            return true;
        } else {
            return false;
        }
    }


    /**
     * 通过FragmentManager获取栈顶的Fragment
     *
     * @param fragmentManager Fragment管理类
     * @return 栈顶的Fragment
     */
    public synchronized Fragment getTopFragment(@NonNull FragmentManager fragmentManager) {
        if (getSize() <= 0) {
            return null;
        }
        Fragment fragment = null;
        for (int i = 0; i < fragmentManager.getFragments().size(); i++) {
            //判断Glide的SupportRequestManagerFragment问题
            if (!(fragmentManager.getFragments().get(getSize() - 1- i) instanceof SupportRequestManagerFragment)) {
                fragment = fragmentManager.getFragments().get(getSize() - 1 - i);
                break;
            }
        }
        return fragment;
    }

    /**
     * 在集合List里获取当前的Fragment
     *
     * @return 栈顶的Fragment
     */
    public synchronized Fragment getTopFragmentInList() {
        if (mFragmentList == null || mFragmentList.isEmpty()) {
            return null;
    }
        return mFragmentList.get(mFragmentList.size() - 1);
    }

    /**
     * 在List中按照指定类名找到fragment
     *
     * @param cls 指定的类名
     * @return 指定类名的fragment
     */
    public Fragment findFragmentInList(Class<?> cls) {
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
     * 查找指定Tag的Fragment
     *
     * @param fragmentManager fragment管理器
     * @param tag             标签
     * @return 指定Tag的Fragment
     */
    public Fragment findFragmentByTag(@NonNull FragmentManager fragmentManager, String tag) {
        return fragmentManager.findFragmentByTag(tag);
    }

    /**
     * 查找fragment
     *
     * @param fragmentManager fragment管理器
     * @param fragmentClass   fragment类
     * @return 查找到的fragment
     */
    public Fragment findFragment(@NonNull FragmentManager fragmentManager, Class<? extends Fragment> fragmentClass) {
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments.isEmpty()) {
            return null;
        }
        return fragmentManager.findFragmentByTag(fragmentClass.getName());
    }

    /**
     * @return 获取当前最顶部的fragment 名字
     */
    public String getTopFragmentName() {
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
    public void replaceFragment(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fragment) {
        replaceFragment(fragmentManager, layoutId, fragment, null, NO_ANIM, NO_ANIM);
    }

    /**
     * 加载Fragment（附带tag）
     *
     * @param layoutId
     * @param fragment
     */
    public void replaceFragment(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fragment, String tag) {
        replaceFragment(fragmentManager, layoutId, fragment, tag, NO_ANIM, NO_ANIM);
    }

    /**
     * 加载Fragment（附带动画）
     *
     * @param layoutId
     * @param fragment
     */
    public void replaceFragmentWithAnim(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fragment, int enter, int exit) {
        replaceFragment(fragmentManager, layoutId, fragment, null, enter, exit);
    }

    /**
     * 加载Fragment(带动画)
     *
     * @param layoutId
     * @param fragment
     */
    public void replaceFragment(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fragment, int enter, int exit) {
        replaceFragment(fragmentManager, layoutId, fragment, null, enter, exit, NO_ANIM, NO_ANIM);
    }

    /**
     * 加载Fragment(带动画和TAG)
     *
     * @param layoutId
     * @param fragment
     */
    public void replaceFragment(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fragment, String tag, int enter, int exit) {
        replaceFragment(fragmentManager, layoutId, fragment, tag, enter, exit, NO_ANIM, NO_ANIM);
    }

    /**
     * 加载Fragment(带出入栈动画和TAG)
     *
     * @param layoutId
     * @param fragment
     */
    public void replaceFragment(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fragment, String tag, int enter, int exit, int popEnter, int popExit) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (popEnter != NO_ANIM) {
            transaction.setCustomAnimations(enter, exit, popEnter, popExit);
        } else if (enter != NO_ANIM) {
            transaction.setCustomAnimations(enter, exit);
        }
        transaction.replace(layoutId, fragment, tag).addToBackStack(tag);
        transaction.commitAllowingStateLoss();
        addToList(fragment);
    }


    /**
     * 切换Fragment（hide/show）
     *
     * @param layoutId
     * @param fromFragment
     * @param toFragment
     */
    public void switchFragment(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fromFragment, Fragment toFragment) {
        switchFragment(fragmentManager, layoutId, fromFragment, toFragment, null, NO_ANIM, NO_ANIM, NO_ANIM, NO_ANIM);
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
    public void switchFragment(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fromFragment, Fragment toFragment, String tagOfTo) {
        switchFragment(fragmentManager, layoutId, fromFragment, toFragment, tagOfTo, NO_ANIM, NO_ANIM, NO_ANIM, NO_ANIM);
    }

    /**
     * 切换Fragment（hide/show）
     * 带动画
     *
     * @param layoutId
     * @param fromFragment
     * @param toFragment
     */
    public void switchFragmentWithAnim(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fromFragment, Fragment toFragment, int enter, int exit) {
        switchFragment(fragmentManager, layoutId, fromFragment, toFragment, null, enter, exit, NO_ANIM, NO_ANIM);
    }

    /**
     * 切换Fragment（hide/show）
     * 带动画和带TAG
     *
     * @param layoutId
     * @param fromFragment
     * @param toFragment
     */
    public void switchFragmentWithAnim(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fromFragment, Fragment toFragment, String tag, int enter, int exit) {
        switchFragment(fragmentManager, layoutId, fromFragment, toFragment, tag, enter, exit, NO_ANIM, NO_ANIM);
    }

    /**
     * 切换Fragment（hide/show）（带动画）
     *
     * @param layoutId
     * @param fromFragment
     * @param toFragment
     */
    public void switchFragment(@NonNull FragmentManager fragmentManager, int layoutId, Fragment fromFragment, Fragment toFragment, String tag, int enter, int exit, int popEnter, int popExit) {
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
            addToList(toFragment);
        }
    }

    /**
     * 隐藏指定的Fragment
     *
     * @param fragmentManager FragmentManager
     * @param fragment        指定的Fragment
     */
    public void hideFragment(@NonNull FragmentManager fragmentManager, Fragment fragment) {
        fragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss();
    }

    /**
     * 显示指定的Fragment
     *
     * @param fragmentManager FragmentManager
     * @param fragment        指定的Fragment
     */
    public void showFragment(@NonNull FragmentManager fragmentManager, Fragment fragment) {
        fragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss();
    }


    /*-------------------------------加载Fragment的方式-----------------------------------*/



    /*-----------------------------Fragment回退键处理-----------------------------------*/

    /**
     * 处理fragment回退键
     * 如果fragment实现了OnBackClickListener接口，返回true: 表示已消费回退键事件，反之则没消费
     *
     * @param fragment fragment
     * @return 是否消费回退事件
     */

    public boolean handlerBackPress(@NonNull Fragment fragment) {
        return handlerBackPress(fragment.getFragmentManager());
    }

    /**
     * 处理fragment回退键
     * 如果fragment实现了OnBackClickListener接口，返回 true: 表示已消费回退键事件，反之则没消费
     *
     * @param fragmentManager fragment管理器
     * @return 是否消费回退事件
     */
    public boolean handlerBackPress(@NonNull FragmentManager fragmentManager) {
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
    /*-----------------------------Fragment回退键处理-----------------------------------*/


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
