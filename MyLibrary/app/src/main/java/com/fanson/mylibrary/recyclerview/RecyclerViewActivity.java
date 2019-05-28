package com.fanson.mylibrary.recyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.fansonlib.base.BaseActivity;
import com.example.fansonlib.widget.recyclerview.IRvLoadMoreListener;
import com.example.fansonlib.widget.recyclerview.IRvRetryListener;
import com.example.fansonlib.widget.recyclerview.MyRecyclerView;
import com.fanson.mylibrary.R;
import com.fanson.mylibrary.databinding.ActivityRecyclerviewBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by：Fanson
 * Created Time: 2019/5/25 9:38
 * Describe：测试RecyclerView
 */
public class RecyclerViewActivity extends BaseActivity<ActivityRecyclerviewBinding> implements IRvLoadMoreListener, IRvRetryListener {

    private static final String TAG = RecyclerViewActivity.class.getSimpleName();

    private MyRecyclerView<RecyclerViewBean.ListBean, RecyclerViewAdapter> myRecyclerView;

    @Override
    protected int getContentView() {
        return R.layout.acitivity_recyclerview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        myRecyclerView = new MyRecyclerView<>(this);
        myRecyclerView = findMyViewId(R.id.recyclerView);
        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setRvAdapter(new RecyclerViewAdapter());
        myRecyclerView.setLoadMoreListener(this);
        setCustomLoadingView();
    }

    @Override
    protected void initData() {
        testLoadError();
    }

    @Override
    protected void listenEvent() {

    }

    /**
     * 设置加载中的界面
     */
    private void setCustomLoadingView() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_loading_progress, null);
        myRecyclerView.setLoadingView(view);
    }

    /**
     * 设置空数据界面
     */
    private void setCustomNoDataView() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_no_data, null);
        myRecyclerView.setNoDataView(view);
    }

    /**
     * 设置错误界面
     */
    private void setCustomErrorView() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_no_data, null);
        myRecyclerView.setErrorView(view);
    }

    /**
     * 模拟加载数据出错
     */
    private void testLoadError() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setCustomErrorView();
                myRecyclerView.setRetryListener(RecyclerViewActivity.this);
                myRecyclerView.setList(null);
            }
        }, 5000);
    }

    @Override
    public void onRvLoadMore(int pageNum) {
        Log.d(TAG, "onRvLoadMore");
        myRecyclerView.getRvAdapter().loadMoreEnd();
        List<RecyclerViewBean.ListBean> listBeans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RecyclerViewBean.ListBean listBean = new RecyclerViewBean.ListBean();
            listBean.setName("loadMore" + i);
            listBeans.add(listBean);
        }
        myRecyclerView.addList(listBeans);
    }

    /**
     * 重试加载
     */
    @Override
    public void onRvRetryLoad() {
        Log.d(TAG, "onRvRetryLoad");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<RecyclerViewBean.ListBean> listBeans = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    RecyclerViewBean.ListBean listBean = new RecyclerViewBean.ListBean();
                    listBean.setName("RetryLoad" + i);
                    listBeans.add(listBean);
                }
                myRecyclerView.setList(listBeans);
            }
        }, 2000);

    }
}
