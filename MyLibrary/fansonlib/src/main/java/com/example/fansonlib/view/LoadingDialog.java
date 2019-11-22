package com.example.fansonlib.view;

import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;

import com.example.fansonlib.R;
import com.example.fansonlib.widget.dialogfragment.base.BaseDialogFragment;
import com.example.fansonlib.widget.dialogfragment.base.ViewHolder;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * @author Created by：Fanson
 * Created Time: 2019/4/2 15:56
 * Describe：数据加载框LoadingDialog
 */
public class LoadingDialog extends BaseDialogFragment implements DialogInterface.OnKeyListener{

    private static final String TAG = LoadingDialog.class.getSimpleName();
    private AVLoadingIndicatorView mLoadingView;

    /**
     * 颜色id
     */
    private int mColor;

    public LoadingDialog( ){
        this.setWidth(120);
        this.setHeight(120);
        this.setOutCancel(false);
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_loading;
    }

    /**
     * 设置LoadingView圈的颜色
     * @param color 颜色id
     */
    public void setColor(int color){
        mColor = color;
    }

    @Override
    public void convertView(ViewHolder holder, BaseDialogFragment baseDialogFragment) {
        mLoadingView = holder.getView(R.id.loadingView);
        mLoadingView.setIndicatorColor(mColor);
        mLoadingView.show();
        this.getDialog().setOnKeyListener(this);
    }

    /**
     * 移除自己这个对话框
     * @param manager FragmentManager
     */
    public void hideDialog(FragmentManager manager){
        if (getShowsDialog()){
            manager.beginTransaction().remove(this).commit();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mLoadingView!=null){
            mLoadingView.hide();
            mLoadingView = null;
        }
    }

    @Override
    public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            dismiss();
            return true;
        }else {
            return false;
        }
    }
}
