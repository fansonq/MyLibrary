package com.example.fansonlib.widget.shapebutton;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

/**
 * @author Created by：Fanson
 *         Created Time: 2018/5/17 11:36
 *         Describe：带Ripple效果/边框的Drawable
 */

public class MyRippleDrawable extends GradientDrawable {

    /**
     * 每次绘制Ripple的半径（刷新速率）
     */
    private int mDrawRippleRadius = 10;

    /**
     * 每次绘制Ripple的时间间隔（ms）
     */
    private static final int DEF_DRAW_DELAY_TIME = 2;

    /**
     * 标识是否绘画中，如果绘画中，则不接受其他绘制
     */
    private boolean mIsDrawing;

    /**
     * 是否开启Ripple效果
     */
    private boolean mOpenRipple;

    /**
     * 边框线宽度
     */
    private int mStrokeWidth;

    /**
     * 边框线颜色
     */
    private int mStrokeColor;

    // 默认透明度
    private int mAlpha = 255;

    //画笔
    private Paint mPaint;

    //绘制的宽和高
    private int mWidth, mHeight;

    //默认画笔颜色
    private int mPaintColor = 0;

    //波纹圆心，半径
    private float mRippleCircleX, mRippleCircleY, mRippleCircleRadius;

    //矩形四个角的圆角半径
    private int mLeftTopRadius, mRightTopRadius, mRightBottomRadius, mLeftBottomRadius;

    //背景颜色
    private int mBgColor;

    //默认背景颜色
    private int mDefaultBgColor = Color.YELLOW;

    //默认背景矩形
    private RectF mBgRectF;

    //矩形的四个圆角半径
    private float mFourCircleRadius;

    //点击按钮时的背景颜色
    private int mBgPressColor = Color.GRAY;

    //Ripple效果的颜色
    private int mBgRippleColor = Color.GRAY;


    private Handler mHandler = new Handler(Looper.getMainLooper());


    public MyRippleDrawable() {
        mPaint = new Paint();
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        //设置防抖动
        mPaint.setDither(true);

    }


    @Override
    public void draw(Canvas canvas) {
        if (mOpenRipple) {
            if (canvas == null) {
                return;
            }
            canvas.save();
            //绘制区域
            int canvasWidth = canvas.getWidth();
            int canvasHeight = canvas.getHeight();
            //新建图层
            int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
            mPaint.setColor(mBgColor);
            //绘制圆角矩形
            if (mBgRectF != null) {
                canvas.drawRoundRect(mBgRectF, mFourCircleRadius, mFourCircleRadius, mPaint);
            }

            //设置图形重叠模式
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            //绘制波纹Ripple
            mPaint.setColor(mBgRippleColor);
            canvas.drawCircle(mRippleCircleX, mRippleCircleY, mRippleCircleRadius, mPaint);

            //将画笔去除Xfermode
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            canvas.restoreToCount(layerId);
        } else {
            super.draw(canvas);
            setCornerRadii(new float[]{mLeftTopRadius, mLeftTopRadius, mRightTopRadius, mRightTopRadius, mRightBottomRadius,
                    mRightBottomRadius, mLeftBottomRadius, mLeftBottomRadius});
        }
    }

    /**
     * 设置矩形四个圆角的半径（Ripple效果）
     *
     * @param canvas
     */
    private void initEveryCornerRadius(Canvas canvas) {
        if (mLeftTopRadius != 0) {
            canvas.drawRect( mLeftTopRadius, mLeftTopRadius,mLeftTopRadius, mLeftTopRadius, mPaint);
        }
        if (mRightTopRadius!=0){
            canvas.drawRect( mLeftTopRadius, mLeftTopRadius,mLeftTopRadius, mLeftTopRadius, mPaint);
        }
    }

    /**
     * 设置矩形四个角的圆角半径（左上，右上，右下，左下。八个参数）
     *
     * @param leftTop     左上半径
     * @param rightTop    右上半径
     * @param rightBottom 右下半径
     * @param leftBottom  左下半径
     */
    public void setEveryCornerRadius(int leftTop, int rightTop, int rightBottom, int leftBottom) {
        mLeftTopRadius = leftTop;
        mRightTopRadius = rightTop;
        mRightBottomRadius = rightBottom;
        mLeftBottomRadius = leftBottom;
    }

    /**
     * 接收到触摸事件，进行绘制处理
     *
     * @param event 触摸事件
     */
    public void setTouchEvent(MotionEvent event) {
        //这里加上刷新，触摸时就可以有颜色变化
        invalidateSelf();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                onTouchDown(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchMove(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                onTouchUp(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_CANCEL:
                onTouchCancel();
                break;
            case MotionEvent.ACTION_MASK:
                mBgColor = Color.RED;
                break;
            case MotionEvent.ACTION_HOVER_EXIT:
                mBgColor = mDefaultBgColor;
                break;
            default:
                break;
        }
    }

    /**
     * 触摸按钮（Down）
     *
     * @param x X方向值
     * @param y Y方向值
     */
    private void onTouchDown(float x, float y) {
        if (mOpenRipple) {
            if (!mIsDrawing) {
                mBgColor = mBgPressColor;
                mRippleCircleX = x;
                mRippleCircleY = y;
                mRippleCircleRadius = 0;
            }
        } else {
            setStroke(0, mBgPressColor);
            setColor(mBgPressColor);
        }
    }

    /**
     * 触摸按钮（Move）
     *
     * @param x X方向值
     * @param y Y方向值
     */
    private void onTouchMove(float x, float y) {
        if (!mIsDrawing) {
            mRippleCircleX = x;
            mRippleCircleY = y;
        }
    }

    /**
     * 触摸按钮（Up）
     *
     * @param x X方向值
     * @param y Y方向值
     */
    private void onTouchUp(float x, float y) {
        if (mOpenRipple) {
            if (!mIsDrawing) {
                mRippleCircleX = x;
                mRippleCircleY = y;
                mHandler.post(mDrawRunnable);
            }
            mBgColor = mDefaultBgColor;
        } else {
            setColor(mDefaultBgColor);
            setStroke(mStrokeWidth, mStrokeColor);
        }
    }

    /**
     * 触摸按钮（取消）
     */
    private void onTouchCancel() {
        mRippleCircleX = 0;
        mRippleCircleY = 0;
    }

    /**
     * 绘制波纹Ripple的任务
     */
    private Runnable mDrawRunnable = new Runnable() {
        @Override
        public void run() {
            //刷新
            invalidateSelf();
            if (mRippleCircleRadius < mWidth) {
                mIsDrawing = true;
                mRippleCircleRadius += mDrawRippleRadius;
                mHandler.postDelayed(mDrawRunnable, DEF_DRAW_DELAY_TIME);
            } else {
                mIsDrawing = false;
                mRippleCircleRadius = 0;
                mRippleCircleX = 0;
                mRippleCircleY = 0;
            }
        }
    };

    /**
     * 设置绘制的宽和高，并生成矩形
     *
     * @param width 绘制的宽
     */
    public void setWidthAndHeight(int width, int height) {
        mWidth = width;
        mHeight = height;
        mBgRectF = new RectF(0, 0, mWidth, mHeight);
    }

    /**
     * 设置圆角半径
     *
     * @param fourCircleRadius 矩形的四个圆角半径
     */
    public void setCircleRadius(float fourCircleRadius) {
        mFourCircleRadius = fourCircleRadius ;
    }

    /**
     * 设置按下时的背景颜色
     *
     * @param pressColor 颜色（int）
     */
    public void setBgPressColor(int pressColor) {
        mBgPressColor = pressColor;
    }

    /**
     * 设置默认的背景颜色
     *
     * @param bgDefaultColor 颜色（int）
     */
    public void setBgDefaultColor(int bgDefaultColor) {
        mDefaultBgColor = bgDefaultColor;
        mBgColor = mDefaultBgColor;
    }

    /**
     * 设置边框
     *
     * @param width 宽度
     * @param color 颜色
     */
    public void setMyStroke(int width, int color) {
        mStrokeWidth = width;
        mStrokeColor = color;
        setStroke(width, color);
    }

    /**
     * 设置矩形四个角为圆角
     *
     * @param cornerRadius 圆角半径
     */
    public void setFourCornerRadius(float cornerRadius) {
        mFourCircleRadius = cornerRadius;
        if (cornerRadius != 0) {
            super.setCornerRadius(cornerRadius);
        }
    }

    /**
     * 设置Ripple的颜色
     *
     * @param rippleColor 颜色（int）
     */
    public void setRippleColor(int rippleColor) {
        mBgRippleColor = rippleColor;
    }

    /**
     * 设置画笔的颜色
     *
     * @param paintColor 颜色（int）
     */
    public void setPaintColor(int paintColor) {
        mPaintColor = paintColor;
    }

    /**
     * 每次绘制Ripple的半径值（刷新速率）
     *
     * @param drawRadius 绘制Ripple的半径值
     */
    public void setDrawRippleRadius(int drawRadius) {
        mDrawRippleRadius = drawRadius;
    }

    @Override
    public void setAlpha(int alpha) {
        super.setAlpha(alpha);
        mAlpha = alpha;
        onColorOrAlphaChange();
    }

    @Override
    public int getAlpha() {
        return mAlpha;
    }

    /**
     * 设置是否开启Ripple效果
     *
     * @param isOpen true or false
     */
    public void setOpenRipple(boolean isOpen) {
        mOpenRipple = isOpen;
    }

    /**
     * 设置画笔的透明度/颜色
     */
    private void onColorOrAlphaChange() {
        mPaint.setColor(mPaintColor);
        //获取画笔透明度
        if (mAlpha != 255) {
            int paintAlpha = mPaint.getAlpha();
            int realAlpha = (int) (paintAlpha * (mAlpha / 255f));
            mPaint.setAlpha(realAlpha);
        }
    }

    /**
     * 判断drawable是否有透明度
     *
     * @return drawable透明度
     */
    @Override
    public int getOpacity() {
        int alpha = mPaint.getAlpha();
        if (alpha == 255) {
            return PixelFormat.OPAQUE;
        } else if (alpha == 0) {
            return PixelFormat.TRANSPARENT;
        } else {
            return PixelFormat.TRANSLUCENT;
        }
    }

    /**
     * 设置颜色滤镜
     *
     * @param colorFilter 颜色滤镜
     */
    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        if (mPaint.getColorFilter() != colorFilter) {
            mPaint.setColorFilter(colorFilter);
        }
    }
}
