package com.example.fansonlib.widget.segmentbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.example.fansonlib.R;


/**
 * Created by：fanson
 * Created on：2017/4/5 17:39
 * Describe：标签选项按钮
 */

public class MySegmentButton extends View {

    private String[] mTexts;
    private Rect[] mCacheBounds;
    private Rect[] mTextBounds;

    private RadiusDrawable mBackgroundDrawable;
    private RadiusDrawable mSelectedDrawable;

    private int mCurrentIndex;

    private int mTouchSlop;
    private boolean inTapRegion;
    private float mStartX;
    private float mStartY;
    private float mCurrentX;
    private float mCurrentY;

    private int mHorizonGap;
    private int mVerticalGap;

    private int mCenterX;
    private int mCenterY;

    private int mChildrenWidth;
    private int mChildrenHeight;

    private int mSingleChildWidth;
    private int mSingleChildHeight;

    private Paint mPaint;
    private int mSelectedTextColors;
    private Direction mDirection;
    private int mTextSize;
    private int mSelectedBackgroundColors;
    private int mCornerRadius;
    private OnSegmentControlClickListener mOnSegmentControlClickListener;

    public MySegmentButton(Context context) {
        this(context, null);
    }

    public MySegmentButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySegmentButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SegmentControl);

        String textArray = ta.getString(R.styleable.SegmentControl_texts);

        if (textArray != null) {
            mTexts = textArray.split("\\|");
        }


        mTextSize = ta.getDimensionPixelSize(R.styleable.SegmentControl_android_textSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, context.getResources().getDisplayMetrics()));
        mSelectedBackgroundColors = ta.getColor(R.styleable.SegmentControl_selectedBackgroundColors, 0xffffffff);
        mSelectedTextColors = ta.getColor(R.styleable.SegmentControl_selectedTextColors, 0xffffffff);
        mCornerRadius = ta.getDimensionPixelSize(R.styleable.SegmentControl_cornerRadius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics()));
        mDirection = Direction.values()[ta.getInt(R.styleable.SegmentControl_direction, 0)];

        mHorizonGap = ta.getDimensionPixelSize(R.styleable.SegmentControl_horizonGap, 0);
        mVerticalGap = ta.getDimensionPixelSize(R.styleable.SegmentControl_verticalGap, 0);

        mCurrentIndex = ta.getInt(R.styleable.SegmentControl_selectedIndex, 0);//默认选中第一个
        if (mTexts != null && mTexts.length > 0 && mCurrentIndex > mTexts.length - 1) {
            mCurrentIndex = mTexts.length - 1;
        }

        int gap = ta.getDimensionPixelSize(R.styleable.SegmentControl_gaps, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics()));

        if (mHorizonGap == 0) mHorizonGap = gap;
        if (mVerticalGap == 0) mVerticalGap = gap;

        ta.recycle();

        mBackgroundDrawable = new RadiusDrawable(mCornerRadius, true, 0);
        mBackgroundDrawable.setStrokeWidth(2);
        mBackgroundDrawable.setStrokeColor(mSelectedBackgroundColors);

        if (Build.VERSION.SDK_INT < 16) {
            setBackgroundDrawable(mBackgroundDrawable);
        } else {
            setBackground(mBackgroundDrawable);
        }

        mSelectedDrawable = new RadiusDrawable(mCornerRadius, false, mSelectedBackgroundColors);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mSelectedBackgroundColors);

        //here's the tricky thing, when you doing a click detect on a capacitive touch screen,
        //sometimes the touch points of touchDown and touchUp are different(it's call slop) even when you didn't actually move your finger,
        //so we set a distance limit for the distance of this two touch points to create a better user experience;
        int touchSlop ;

        final ViewConfiguration config = ViewConfiguration.get(context);
        touchSlop = config.getScaledTouchSlop();

        mTouchSlop = touchSlop * touchSlop;
        inTapRegion = false;
    }

    public void setOnSegmentControlClickListener(OnSegmentControlClickListener onSegmentControlClickListener) {
        mOnSegmentControlClickListener = onSegmentControlClickListener;
    }

    public OnSegmentControlClickListener getOnSegmentControlClicklistener() {
        return mOnSegmentControlClickListener;
    }

    public void setText(String... texts) {
        mTexts = texts;

        if (mTexts != null) {
            requestLayout();
        }
    }

    public void setCurrentIndex(int index) {
        mCurrentIndex = index;
        invalidate();
    }

    /**
     * 设置选中状态下文字的颜色
     *
     * @param selectedTextColor
     */
    public void setSelectedTextColor(int selectedTextColor) {
        mSelectedTextColors = selectedTextColor;
        invalidate();
    }

    /**
     * 设置选中状态下按钮的背景颜色
     */
    public void setSelectedBackgroundColors(int backgroundColors) {
        mSelectedBackgroundColors = backgroundColors;

        if (mBackgroundDrawable != null) {
            mBackgroundDrawable.setStrokeColor(backgroundColors);
        }

        if (mSelectedDrawable != null) {
            mSelectedDrawable.setColor(backgroundColors);
        }

        mPaint.setColor(backgroundColors);

        invalidate();
    }

    public void setCornerRadius(int cornerRadius) {
        mCornerRadius = cornerRadius;

        if (mBackgroundDrawable != null) {
            mBackgroundDrawable.setRadius(cornerRadius);
        }

        invalidate();
    }

    public void setDirection(Direction direction) {
        Direction tDirection = mDirection;

        mDirection = direction;

        if (tDirection != direction) {
            requestLayout();
            invalidate();
        }
    }

    public void setTextSize(int textSize) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    public void setTextSize(int unit, int textSize) {
        mPaint.setTextSize((int) (TypedValue.applyDimension(unit, textSize, getContext().getResources().getDisplayMetrics())));

        if (textSize != mTextSize) {
            mTextSize = textSize;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        if (mTexts != null && mTexts.length > 0) {

            if (mCacheBounds == null || mCacheBounds.length != mTexts.length) {
                mCacheBounds = new Rect[mTexts.length];
            }

            if (mTextBounds == null || mTextBounds.length != mTexts.length) {
                mTextBounds = new Rect[mTexts.length];
            }

            for (int i = 0; i < mTexts.length; i++) {
                String text = mTexts[i];

                if (text != null) {

                    if (mTextBounds[i] == null) mTextBounds[i] = new Rect();

                    mPaint.getTextBounds(text, 0, text.length(), mTextBounds[i]);

                    if (mSingleChildWidth < mTextBounds[i].width() + mHorizonGap * 2)
                        mSingleChildWidth = mTextBounds[i].width() + mHorizonGap * 2;
                    if (mSingleChildHeight < mTextBounds[i].height() + mVerticalGap * 2)
                        mSingleChildHeight = mTextBounds[i].height() + mVerticalGap * 2;
                }
            }

            for (int i = 0; i < mTexts.length; i++) {

                if (mCacheBounds[i] == null) mCacheBounds[i] = new Rect();

                if (mDirection == Direction.HORIZON) {
                    mCacheBounds[i].left = i * mSingleChildWidth;
                    mCacheBounds[i].top = 0;
                } else {
                    mCacheBounds[i].left = 0;
                    mCacheBounds[i].top = i * mSingleChildHeight;
                }

                mCacheBounds[i].right = mCacheBounds[i].left + mSingleChildWidth;
                mCacheBounds[i].bottom = mCacheBounds[i].top + mSingleChildHeight;
            }

            switch (widthMode) {
                case MeasureSpec.AT_MOST:
                    if (mDirection == Direction.HORIZON) {
                        if (widthSize <= mSingleChildWidth * mTexts.length) {
                            mSingleChildWidth = widthSize / mTexts.length;
                            width = widthSize;
                        } else {
                            width = mSingleChildWidth * mTexts.length;
                        }
                    } else {
                        width = widthSize <= mSingleChildWidth ? widthSize : mSingleChildWidth;
                    }
                    break;
                case MeasureSpec.EXACTLY:
                    width = widthSize;
                    break;
                case MeasureSpec.UNSPECIFIED:
                    if (mDirection == Direction.HORIZON) {
                        width = mSingleChildWidth * mTexts.length;
                    } else {
                        width = widthSize <= mSingleChildWidth ? widthSize : mSingleChildWidth;
                    }
                    break;
            }

            switch (heightMode) {
                case MeasureSpec.AT_MOST:
                    if (mDirection == Direction.VERTICAL) {
                        if (heightSize <= mSingleChildHeight * mTexts.length) {
                            mSingleChildHeight = heightSize / mTexts.length;
                            height = heightSize;
                        } else {
                            height = mSingleChildHeight * mTexts.length;
                        }
                    } else {
                        height = heightSize <= mSingleChildHeight ? heightSize : mSingleChildHeight;
                    }
                    break;
                case MeasureSpec.EXACTLY:
                    height = heightSize;
                    break;
                case MeasureSpec.UNSPECIFIED:
                    if (mDirection == Direction.VERTICAL) {
                        height = mSingleChildHeight * mTexts.length;
                    } else {
                        height = heightSize <= mSingleChildHeight ? heightSize : mSingleChildHeight;
                    }
                    break;
            }

            mChildrenWidth = mDirection == Direction.HORIZON ? mSingleChildWidth * mTexts.length : mSingleChildWidth;
            mChildrenHeight = mDirection == Direction.VERTICAL ? mSingleChildHeight * mTexts.length : mSingleChildHeight;
        } else {
            width = widthMode == MeasureSpec.UNSPECIFIED ? 0 : widthSize;
            height = heightMode == MeasureSpec.UNSPECIFIED ? 0 : heightSize;
        }

        mCenterX = width / 2;
        mCenterY = height / 2;

        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                inTapRegion = true;

                mStartX = event.getX();
                mStartY = event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentX = event.getX();
                mCurrentY = event.getY();

                int dx = (int) (mCurrentX - mStartX);
                int dy = (int) (mCurrentY - mStartY);

                int distance = dx * dx + dy * dy;

                if (distance > mTouchSlop) {
                    inTapRegion = false;
                }

                break;
            case MotionEvent.ACTION_UP:
                if (inTapRegion) {
                    int index ;
                    if (mDirection == Direction.HORIZON) {
                        index = (int) (mStartX / mSingleChildWidth);
                    } else {
                        index = (int) (mStartY / mSingleChildHeight);
                    }

                    if (mCurrentIndex != index) {
                        if (mOnSegmentControlClickListener != null)
                            mOnSegmentControlClickListener.onSegmentControlClick(index);
                    }

                    mCurrentIndex = index;

                    invalidate();
                }
                break;
        }

        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mTexts != null && mTexts.length > 0) {
            for (int i = 0; i < mTexts.length; i++) {

                //draw separate lines
                if (i < mTexts.length - 1) {
                    mPaint.setColor(mSelectedBackgroundColors);
                    mPaint.setStrokeWidth(2);
                    if (mDirection == Direction.HORIZON) {
                        canvas.drawLine(mCacheBounds[i].right, 0, mCacheBounds[i].right, getHeight(), mPaint);
                    } else {
                        canvas.drawLine(mCacheBounds[i].left, mSingleChildHeight * (i + 1), mCacheBounds[i].right, mSingleChildHeight * (i + 1), mPaint);
                    }
                }

                //draw selected drawable
                if (i == mCurrentIndex && mSelectedDrawable != null) {
                    int topLeftRadius = 0;
                    int topRightRadius = 0;
                    int bottomLeftRadius = 0;
                    int bottomRightRadius = 0;

                    if (mDirection == Direction.HORIZON) {
                        if (i == 0) {
                            topLeftRadius = mCornerRadius;
                            bottomLeftRadius = mCornerRadius;
                        }
                        if (i == mTexts.length - 1) {
                            topRightRadius = mCornerRadius;
                            bottomRightRadius = mCornerRadius;
                        }
                    } else {
                        if (i == 0) {
                            topLeftRadius = mCornerRadius;
                            topRightRadius = mCornerRadius;
                        }
                        if (i == mTexts.length - 1) {
                            bottomLeftRadius = mCornerRadius;
                            bottomRightRadius = mCornerRadius;
                        }
                    }

                    mSelectedDrawable.setRadiuses(topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius);
                    mSelectedDrawable.setBounds(mCacheBounds[i]);
                    mSelectedDrawable.draw(canvas);

                    mPaint.setColor(mSelectedTextColors);
                } else {
                    mPaint.setColor(mSelectedBackgroundColors);
                }

                //draw texts
                canvas.drawText(mTexts[i], mCacheBounds[i].left + (mSingleChildWidth - mTextBounds[i].width()) / 2, mCacheBounds[i].top + (mSingleChildHeight - mPaint.ascent() - mPaint.descent()) / 2, mPaint);
            }
        }

    }

    public enum Direction {
        HORIZON(0), VERTICAL(1);

        int mV;

        Direction(int v) {
            mV = v;
        }
    }

    public interface OnSegmentControlClickListener {
        void onSegmentControlClick(int index);
    }
}

