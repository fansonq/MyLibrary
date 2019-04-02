package com.example.fansonlib.impl;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.example.fansonlib.R;

/**
 * @author Created by：Fanson
 * Created Time: 2019/4/2 11:00
 * Describe：第三方适配器的加载View
 */
public class CustomLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.view_adapter_load;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
