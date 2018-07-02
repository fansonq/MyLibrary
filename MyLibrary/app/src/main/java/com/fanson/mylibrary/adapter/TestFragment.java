package com.fanson.mylibrary.adapter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fansonlib.base.BaseFragment;
import com.fanson.mylibrary.R;
import com.fanson.mylibrary.databinding.FragmentTestBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by：fanson
 * Created on：2017/5/8 11:21
 * Describe：
 */

public class TestFragment extends BaseFragment<FragmentTestBinding> {

    private TestAdapter adapter;
    private TestMultiAdapter multiAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    protected View initView(View rootView, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(hostActivity));
        //添加头部View
//        View view = LayoutInflater.from(hostActivity).inflate(R.layout.include_top_bar,null);
//        adapter.addHeaderView(view);
        return rootView;
    }

    @Override
    protected void initData() {
        testSimpleAdapter();
    }

    /**
     * 测试单类型的适配器
     */
    private void testSimpleAdapter() {
        Dog dog = new Dog();
        dog.setName("dog");
        adapter = new TestAdapter(hostActivity);
        mBinding.recyclerview.setAdapter(adapter);
        //显示无数据界面
//        mBinding.recyclerview.setEmptyView(null);
        adapter.appendItem(dog);
    }

    private void testMultiAdapter() {
        List<IBean> list = new ArrayList<>();
        Dog dog = new Dog();
        dog.setName("dog");
        list.add(dog);
        Cat cat = new Cat();
        cat.setAge(18);
        list.add(cat);

        multiAdapter = new TestMultiAdapter(hostActivity);
        mBinding.recyclerview.setAdapter(multiAdapter);
        View view = LayoutInflater.from(hostActivity).inflate(R.layout.include_top_bar, null);
        multiAdapter.addHeaderView(view);
        multiAdapter.appendList(list);
    }

}
