package com.example.fansonlib.widget.loading;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;

import com.example.fansonlib.R;

/**
 * Created by：fanson
 * Created on：2016/12/17 10:41
 * Describe：
 */
public class ProgressAlertDialog extends Dialog{

    private View mDialogView;
    private AnimationSet mModalInAnim;
    private AnimationSet mModalOutAnim;
    private boolean mCloseFromCancel;
    private TextView mLoadingTv;
    private String mLoadingText = "加载中...";

    public ProgressAlertDialog(Context context) {
        super(context, R.style.alert_dialog);
        //默认返回键可以取消
        setCancelable(true);
        //其他区域不可取消
        setCanceledOnTouchOutside(false);
        mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_out);

        mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCloseFromCancel) {
                            ProgressAlertDialog.super.cancel();
                        } else {
                            ProgressAlertDialog.super.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_loading_view);
        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
//        mLoadingTv = (TextView) findViewById(R.id.loading_text);
//        setLoadingText(mLoadingText);
    }

    @Override
    protected void onStart() {
        mDialogView.startAnimation(mModalInAnim);
    }

    @Override
    public void cancel() {
        dismissWithAnimation(true);
    }

    private void dismissWithAnimation(boolean fromCancel) {
        mCloseFromCancel = fromCancel;
        mDialogView.startAnimation(mModalOutAnim);
    }

    /**
     * 设置加载中文字
     * @param loadingText
     */
//    public void setLoadingText(String loadingText) {
//        if(!TextUtils.isEmpty(loadingText)){
//            mLoadingTv.setText(loadingText);
//        }
//    }
}
