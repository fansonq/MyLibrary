package com.fanson.mylibrary;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.fansonlib.base.BaseFragment;
import com.example.fansonlib.widget.recyclerview.AutoLoadRecyclerView;
import com.fanson.mylibrary.adapter.Dog;
import com.fanson.mylibrary.adapter.TestAdapter;
import com.fanson.mylibrary.adapter.TestMultiAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by：fanson
 * Created on：2017/5/8 11:21
 * Describe：
 */

public class TestFragment extends BaseFragment {

    private TestAdapter adapter;
    private TestMultiAdapter multiAdapter;
    private AutoLoadRecyclerView mRecyclerview;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    protected View initView(View rootView, Bundle savedInstanceState) {
        mRecyclerview = findMyViewId(R.id.recyclerview);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(hostActivity));


//        List<IBean> list = new ArrayList<>();
        List<Dog> list = new ArrayList<>();
//        Dog dog = new Dog();
//        dog.setName("dog");
//        list.add(dog);
//        Cat cat = new Cat();
//        cat.setAge(18);
//        list.add(cat);
        adapter = new TestAdapter(hostActivity);
        mRecyclerview.setEmptyView(null);
        mRecyclerview.setAdapter(adapter);
        adapter.appendList(list);

//        View view = LayoutInflater.from(hostActivity).inflate(R.layout.include_top_bar,null);
//        adapter.addHeaderView(view);

//        multiAdapter = new TestMultiAdapter(hostActivity);
//        mRecyclerview.setAdapter(multiAdapter);
//        View view = LayoutInflater.from(hostActivity).inflate(R.layout.include_top_bar,null);
//        multiAdapter.addHeaderView(view);
//        multiAdapter.appendList(list);

        return rootView;
    }

    @Override
    protected void initData() {

    }
}
