package com.example.fansonlib.widget.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @author Created by：Fanson
 * Created Time: 2019/12/13 14:09
 * Describe：自定义LinearLayoutManager，处理IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter异常
 */
public class LinearLayoutManagerWrapper extends LinearLayoutManager {

    private static final String TAG = LinearLayoutManagerWrapper.class.getSimpleName();

    public LinearLayoutManagerWrapper(Context context) {
        super(context);
    }

    public LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public LinearLayoutManagerWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "IndexOutOfBoundsException in RecyclerView");
        }
    }
}
