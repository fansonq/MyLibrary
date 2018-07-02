package com.fanson.mylibrary.adapter;

import com.example.fansonlib.base.BaseActivity;
import com.example.fansonlib.manager.MyFragmentManager;
import com.fanson.mylibrary.R;
import com.fanson.mylibrary.databinding.ActivityRecyclerviewBinding;

/**
 * @author Created by：Fanson
 * Created Time: 2018/6/29 17:59
 * Describe：
 */
public class RecyclerViewActivity extends BaseActivity<ActivityRecyclerviewBinding>{


    @Override
    protected int getContentView() {
        return R.layout.activity_recyclerview;
    }

    @Override
    protected void initView() {
        MyFragmentManager manager = new MyFragmentManager();
        manager.replaceFragment(getSupportFragmentManager(),R.id.fl_main,new TestFragment());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void listenEvent() {

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
