package com.fanson.mylibrary.recyclerview;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fanson.mylibrary.R;

/**
 * @author Created by：Fanson
 * Created Time: 2019/5/25 9:41
 * Describe：测试RecyclerView的适配器
 */
public class RecyclerViewAdapter extends BaseQuickAdapter<RecyclerViewBean.ListBean,BaseViewHolder>{


    public RecyclerViewAdapter() {
        super(R.layout.item_recyclerview);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecyclerViewBean.ListBean item) {
        helper.setText(R.id.tv,item.getName());
    }
}
