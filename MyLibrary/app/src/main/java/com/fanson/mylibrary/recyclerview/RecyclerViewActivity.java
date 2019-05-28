package com.fanson.mylibrary.recyclerview;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.fansonlib.base.BaseActivity;
import com.example.fansonlib.widget.recyclerview.IRvLoadMoreListener;
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
public class RecyclerViewActivity extends BaseActivity<ActivityRecyclerviewBinding> implements IRvLoadMoreListener {

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
    }

    @Override
    protected void initData() {
        List<RecyclerViewBean.ListBean> listBeans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RecyclerViewBean.ListBean listBean = new RecyclerViewBean.ListBean();
            listBean.setName("name" + i);
            listBeans.add(listBean);
        }
        myRecyclerView.setList(listBeans);
    }

    @Override
    protected void listenEvent() {

    }

    /**
     * 设置空数据界面
     */
    private void setCustomNoDataView(){
        View view  = LayoutInflater.from(this).inflate(R.layout.layout_no_data,null);
        myRecyclerView.setCustomNoDataView(view);
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

}
