package com.example.fansonlib.myinterface;

import android.support.annotation.LayoutRes;

import com.example.fansonlib.base.adapter.BaseHolder;

/**
 * Created by：fanson
 * Created on：2017/9/15 18:17
 * Describe：Recyclerview多布局的处理接口
 */

public interface IMultiItem<M> {

    /**
     * 不同类型的item请使用不同的布局文件，
     * 即使它们的布局是一样的，也要copy多一份出来。
     *
     * @return 返回item对应的布局id
     */
    @LayoutRes
    int getLayoutRes();

    /**
     * 进行数据处理，显示文本，图片等内容
     *
     * @param holder   Holder Helper
     * @param bean     bean
     * @param position position
     */
    void hanlderData(BaseHolder holder, M bean, int position);

    /**
     * 在布局为{@link android.support.v7.widget.GridLayoutManager}时才有用处，
     * 返回当前布局所占用的SpanSize
     */
    int getSpanSize();

}
