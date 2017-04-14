package com.example.fansonlib.function.ijkplayer.listener;
/**
 * ========================================
 * 描 述：操作面板显示或者隐藏改变监听
 * <p>
 * ========================================
 */
public interface OnControlPanelVisibilityChangeListener {

    /**true 为显示 false为隐藏*/
    void change(boolean isShowing);
}