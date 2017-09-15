package com.example.fansonlib.widget.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by：fanson
 * Created on：2017/5/24 15:48
 * Describe：RecyclerView点击事件（长按，双击等）
 */

public class RecyclerViewListener implements AutoLoadRecyclerView.OnItemTouchListener{

    private GestureDetector  mGestureDetector;
    private OnItemClickListener mListener;

    /**
     * RecyclerView内部接口
     */
    public interface OnItemClickListener{

        /**
         * 长按Item
         */
        void onLongClick(View view, int position);

        /**
         * 短按Item
         */
        void onSingleClick(View view, int position);

//        /**
//         * 双击Item
//         * @param view
//         * @param position
//         */
//        void onDoubleClick(View view, int position);

    }

    public RecyclerViewListener(Context context, final AutoLoadRecyclerView recyclerView, OnItemClickListener listener){
        mListener = listener;
        mGestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if (mListener!=null&&childView!=null){
                    mListener.onSingleClick(childView,recyclerView.getChildLayoutPosition(childView));
                    return true;
                }else{
                    return  false;
                }
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if (mListener!=null&&childView!=null){
                    mListener.onLongClick(childView,recyclerView.getChildLayoutPosition(childView));
                }
            }
        });
    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        //把事件交给GestureDetector处理
        if (mGestureDetector.onTouchEvent(e)) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
