package com.example.fansonlib.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fansonlib.R;
import com.example.fansonlib.callback.OnDialogConfirmListener;
import com.example.fansonlib.callback.OnDialogDoubleListener;


/**
 * Created by：fanson
 * Created on：2016/9/26 9:57
 * Describe：自定义View的Dialog
 */
public class MyDialogView {

    /**
     * 一个按钮的Dialog
     * 没监听接口
     *
     * @Param
     */
    public static void showConfirm(Context mContext, String content) {

        //加载布局
        View view = View.inflate(mContext, R.layout.dialog_confirm, null);
        TextView tv_content = (TextView) view.findViewById(R.id.text);
        Button btn_confirm = (Button) view.findViewById(R.id.confirm);
        tv_content.setText(content);

        //创建Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        final Dialog dialog = builder.create();
        dialog.setCancelable(false);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /**
     * 一个按钮的Dialog
     * 带点击监听接口
     * @Param
     */
    public static void showConfirm(Context mContext, String content,final OnDialogConfirmListener onDialogClickListener) {

        //加载布局
        View view = View.inflate(mContext, R.layout.dialog_confirm, null);
        TextView tv_content = (TextView) view.findViewById(R.id.text);
        Button btn_confirm = (Button) view.findViewById(R.id.confirm);
        tv_content.setText(content);

        //创建Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        final Dialog dialog = builder.create();
        dialog.setCancelable(false);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDialogClickListener.onConfirm();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /**
     * 两个按钮的Dialog
     * @param context
     * @param content
     * @param onDialogDoubleListener
     */
    public static void showConfirmCancel(Context context, String content, final OnDialogDoubleListener onDialogDoubleListener) {
        //加载布局
        View view = View.inflate(context, R.layout.dialog_confirm_cancel, null);
        Button btn_yes = (Button) view.findViewById(R.id.btn_yes);
        Button btn_no = (Button) view.findViewById(R.id.btn_no);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_content.setText(content);

        //创建Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        final Dialog dialog = builder.create();
        dialog.setCancelable(false);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDialogDoubleListener.onConfirm();
                dialog.dismiss();
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDialogDoubleListener.onCancel();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}
