package com.fanson.mylibrary;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.fansonlib.base.BaseFragment;
import com.example.fansonlib.widget.recyclerview.AutoLoadRecyclerView;
import com.fanson.mylibrary.adapter.Cat;
import com.fanson.mylibrary.adapter.Dog;
import com.fanson.mylibrary.adapter.IBean;
import com.fanson.mylibrary.adapter.TestMultiAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by：fanson
 * Created on：2017/5/8 11:21
 * Describe：
 */

public class TestFragment extends BaseFragment {

    private TestMultiAdapter adapter;
    private AutoLoadRecyclerView mRecyclerview;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    protected View initView(View rootView, Bundle savedInstanceState) {
        mRecyclerview = findMyViewId(R.id.recyclerview);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(hostActivity));

        List<IBean> list = new ArrayList<>();
        Dog dog = new Dog();
        dog.setName("dog");
        list.add(dog);
        Cat cat = new Cat();
        cat.setAge(18);
        list.add(cat);
        adapter = new TestMultiAdapter(hostActivity, list);
        mRecyclerview.setAdapter(adapter);

        return rootView;
    }

    @Override
    protected void initData() {

    }
}
