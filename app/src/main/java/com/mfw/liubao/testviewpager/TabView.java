package com.mfw.liubao.testviewpager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ElasticScrollView有弹性的ScrollView
 */
public class TabView extends LinearLayout {
    private String LOG_TAG = TabView.class.getSimpleName();
    private ViewPager mPager;
    private PageListener mPageListener = new PageListener();
    private int mChildCount;
    private int mWidth;
    private int mPieceWidht;
    private int mCurrentPosition;
    private float mCurrentPositionOffset;
    private int mPaddingLeft = 0;
    private int mPaddingRight = 0;
    private Paint mRectPaint;
    private int mTabBackgroundColor;
    private int mTabTextUnselectColor = Color.BLACK;
    private int mTabTextSelectedColor = Color.YELLOW;
    private int mIndicatorHeight = 12;
    private int mIndicatorColor = Color.BLUE;
    private int mTabTextColor = Color.BLACK;
    private int mTabTextSize = 10;
    private int mBottom;

    public TabView(Context context) {
        super(context);
        intiView();
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        intiView();

    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intiView();
    }

    public void setmTabBackgroundColor(int mTabBackgroundColor) {
        this.mTabBackgroundColor = mTabBackgroundColor;
    }

    public void setmIndicatorColor(int mIndicatorColor) {
        this.mIndicatorColor = mIndicatorColor;
    }

    public void setmIndicatorHeight(int mIndicatorHeight) {
        this.mIndicatorHeight = mIndicatorHeight;
    }

    public void setmTabTextColor(int mTabTextColor) {
        this.mTabTextColor = mTabTextColor;
    }

    public void setmTabTextSize(int mTabTextSize) {
        this.mTabTextSize = mTabTextSize;
    }

    private void intiView() {
        //强制调用ondraw方法
        setWillNotDraw(false);
        this.setOrientation(HORIZONTAL);
        mRectPaint = new Paint();
        mRectPaint.setColor(mIndicatorColor);
        mRectPaint.setAntiAlias(true);
        mRectPaint.setStyle(Paint.Style.FILL);
    }

    //增加一个tab
    public void addTab(String tabName) {
        TextView textView = new TextView(getContext());
        textView.setText(tabName);
        textView.setTextColor(mTabTextColor);
        textView.setTextSize(mTabTextSize);
        textView.setGravity(Gravity.CENTER);
        addView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mChildCount++;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = 0;
        int height = 0;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        }
        for (int i = 0; i < mChildCount; i++) {
            getChildAt(i).measure(widthMeasureSpec / mChildCount, heightMeasureSpec);
        }
        mWidth = width;
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mChildCount == 0) {
            return;
        }
        mBottom = b;
        mPieceWidht = mWidth / mChildCount;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int childWidth = child.getWidth();
            int left = l + mPieceWidht / 2 + mPieceWidht * i - childWidth / 2;
            child.layout(left, t, left + childWidth, b);
        }
        if (mChildCount > 0) {
            select((TextView) getChildAt(0));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mChildCount == 0) {
            return;
        }
        if (mCurrentPosition < mChildCount) {
            Pair<Float, Float> pair = getIndicatorCoordinates();
            canvas.drawRect(pair.first + mPaddingLeft,
                    mBottom - mIndicatorHeight,
                    pair.second + mPaddingRight,
                    mBottom,
                    mRectPaint);
        }
    }

    //获得indicator的左右位置
    private Pair<Float, Float> getIndicatorCoordinates() {
        View curChild = getChildAt(mCurrentPosition);
        float curLeft = curChild.getLeft();
        float curRight = curChild.getRight();
        if (mCurrentPositionOffset > 0 && mCurrentPosition < mChildCount - 1) {
            View nextChild = getChildAt(mCurrentPosition + 1);
            final float nextLeft = nextChild.getLeft();
            final float nextRight = nextChild.getRight();
            curLeft = curLeft + (nextLeft - curLeft) * mCurrentPositionOffset;
            curRight = curRight + (nextRight - curRight) * mCurrentPositionOffset;
        }
        return new Pair<>(curLeft, curRight);
    }


    public void setViewPager(ViewPager pager) {
        this.mPager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        pager.setOnPageChangeListener(mPageListener);

//        pager.getAdapter().registerDataSetObserver(mAdapterObserver);
//        mAdapterObserver.setAttached(true);
//        notifyDataSetChanged();
    }

    private void unselect(TextView view) {
        if (view != null) {
            view.setTextColor(mTabTextUnselectColor);
        }
    }

    private void select(TextView view) {
        if (view != null) {
            view.setTextColor(mTabTextSelectedColor);
        }
    }

    private void updateSelection(int position) {
        for (int i = 0; i < mChildCount; ++i) {
            TextView tv = (TextView) getChildAt(i);
            final boolean selected = i == position;
            if (selected) {
                select(tv);
            } else {
                unselect(tv);
            }
        }
    }

    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mCurrentPosition = position;
            mCurrentPositionOffset = positionOffset;
            //重绘
            invalidate();
        }

        @Override
        public void onPageSelected(int position) {
            TextView child = (TextView) getChildAt(position);
            select(child);
//            updateSelection(position);
            Log.i(LOG_TAG + "state", mCurrentPosition + "");
        }


        @Override
        public void onPageScrollStateChanged(int state) {
            if (mPager.getCurrentItem() - 1 >= 0) {
                TextView privChild = (TextView) getChildAt(mPager.getCurrentItem() - 1);
                unselect(privChild);
            }
            if (mPager.getCurrentItem() < mChildCount - 1) {
                TextView nextChild = (TextView) getChildAt(mPager.getCurrentItem() + 1);
                unselect(nextChild);
            }
        }
    }
}
