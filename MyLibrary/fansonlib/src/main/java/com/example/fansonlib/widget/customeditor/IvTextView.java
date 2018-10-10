package com.example.fansonlib.widget.customeditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.fansonlib.R;
import com.example.fansonlib.image.ImageLoaderUtils;
import com.example.fansonlib.utils.DimensUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by：fanson
 * Created on：2017/10/18 12:45
 * Describe：图文混排View
 */

public class IvTextView extends ScrollView {

    /**
     * edittext常规padding是10dp
     */
    private static final int EDIT_PADDING = 10;

    /**
     * 新生的view都会打一个tag，对每个view来说，这个tag是唯一的
     */
    private int viewTagIndex = 1;

    /**
     * 这个是所有子view的容器，scrollView内部的唯一一个ViewGroup
     */
    private LinearLayout allLayout;

    private LayoutInflater inflater;
    private int editNormalPadding = 0;
    private OnClickImageListener mOnClickImageListener;

    public void setOnClickImageListener(OnClickImageListener listener) {
        mOnClickImageListener = listener;
    }

    public IvTextView(Context context) {
        this(context, null);
    }

    public IvTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IvTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);

        // 1. 初始化allLayout
        allLayout = new LinearLayout(context);
        allLayout.setOrientation(LinearLayout.VERTICAL);
        //allLayout.setBackgroundColor(Color.WHITE);//去掉背景
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        //设置间距，防止生成图片时文字太靠边
        allLayout.setPadding(30, 15, 30, 15);
        addView(allLayout, layoutParams);

        /**默认添加一个TextView**/
//        LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
//                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        //editNormalPadding = dip2px(EDIT_PADDING);
//        TextView firstText = createTextView(AppUtils.getAppContext().getString(R.string.no_content), dip2px(context, EDIT_PADDING));
//        allLayout.addView(firstText, firstEditParam);
    }

    public int dip2px(Context context, float dipValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }

    /**
     * 清除所有的view
     */
    public void clearAllLayout() {
        allLayout.removeAllViews();
    }

    /**
     * 获得最后一个子view的位置
     *
     * @return
     */
    public int getLastIndex() {
        int lastEditIndex = allLayout.getChildCount();
        return lastEditIndex;
    }

    /**
     * 生成文本
     */
    public TextView createTextView(String hint, int paddingTop) {
        TextView textView = (TextView) inflater.inflate(R.layout.custom_editor_tx, null);
        textView.setTag(viewTagIndex++);
        textView.setPadding(editNormalPadding, paddingTop, editNormalPadding, paddingTop);
        textView.setHint(hint);
        return textView;
    }

    /**
     * 生成图片View
     */
    private RelativeLayout createImageLayout() {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.custom_editor_iv, null);
        layout.setTag(viewTagIndex++);
        View closeView = layout.findViewById(R.id.image_close);
        closeView.setVisibility(GONE);
        return layout;
    }

    /**
     * 在特定位置插入TextView
     *
     * @param index   位置
     * @param editStr TextView显示的文字
     */
    public void addTextViewAtIndex(final int index, CharSequence editStr) {
        TextView textView = createTextView("", EDIT_PADDING);
        textView.setText(editStr);
        allLayout.addView(textView, index);
    }

    /**
     * 在特定位置添加ImageView
     *
     * @param index     序号
     * @param imagePath 图片Url
     */
    public void addImageViewAtIndex(final int index, final String imagePath) {
        setImageLayout(index, imagePath);
    }

    /**
     * 将图片资源加载入布局
     *
     * @param imagePath 图片Url
     * @param index     序号
     */
    private void setImageLayout(int index, String imagePath) {

        final RelativeLayout imageLayout = createImageLayout();
        ImageEditor imageView = (ImageEditor) imageLayout.findViewById(R.id.custom_edit_iv);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = 10;
        imageView.setLayoutParams(lp);
        onClickImageView(imageLayout, imagePath);
        allLayout.addView(imageLayout, index);
        loadImage(imagePath, imageView);
    }

    @SuppressLint("CheckResult")
    private void loadImage(final String imagePath, final ImageEditor imageView) {
        Observable.create(new ObservableOnSubscribe<float[]>() {
            @Override
            public void subscribe(ObservableEmitter<float[]> e) throws Exception {
                InputStream stream = null;
                Bitmap bitmap = null;
                try {
                    URLConnection connection = new URL(imagePath).openConnection();
                    connection.connect();
                    stream = connection.getInputStream();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                BitmapFactory.Options options = null;
                if (stream != null) {
                    options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(stream, null, options);
                }
                if (options != null) {
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;
                    e.onNext(new float[]{imageWidth, imageHeight});
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<float[]>() {
                    @Override
                    public void accept(@NonNull float[] image) throws Exception {
                        //为了图片宽度适应屏幕，所以按比例拉伸
                        float ratio = DimensUtils.getScreenWidth(getContext()) / image[0];
                        imageView.setAbsolutePath(imagePath);
                        if (ratio > 1) {
                            image[1] = (image[1] * ratio);
                        }
                        imageView.setMinimumHeight((int) image[1]);
                        ImageLoaderUtils.loadCornerImage(getContext(), imageView, imagePath,5);
                    }
                });
    }

    /**
     * 根据view的宽度，动态缩放bitmap尺寸
     *
     * @param width view的宽度
     */
    public Bitmap getScaledBitmap(String filePath, int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int sampleSize = options.outWidth > width ? options.outWidth / width
                + 1 : 1;
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(filePath, options);
    }

    private void onClickImageView(View clickView, final String imgPath) {
        clickView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TTT", "view : " + allLayout.indexOfChild(view));
                mOnClickImageListener.onClickImg(imgPath);
            }
        });
    }
}