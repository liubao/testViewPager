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
import android.view.ViewTreeObserver;
import android.view.WindowManager;
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
    private int mScreenHeight;
    private int mScreenWidht;
    private int mPieceWidht;
    private int mCurrentPosition;
    private float mCurrentPositionOffset;
    private int mPaddingLeft = 0;
    private int mPaddingRight = 0;
    private Paint mRectPaint;
    private int mTabBackgroundColor;
    private int mTabUnselectColor = Color.BLACK;
    private int mTabSelectColor = Color.YELLOW;
    private int mIndicatorHeight = 12;
    private int mIndicatorColor = Color.BLUE;
    private WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

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

    private void intiView() {
        //强制调用ondraw方法
        setWillNotDraw(false);
        this.setOrientation(HORIZONTAL);
        mScreenHeight = wm.getDefaultDisplay().getHeight();
        mScreenWidht = wm.getDefaultDisplay().getWidth();
        mRectPaint = new Paint();
        mRectPaint.setColor(mIndicatorColor);
        mRectPaint.setAntiAlias(true);
        mRectPaint.setStyle(Paint.Style.FILL);
    }

    //增加一个tab
    public void addTab(String tabName) {
        TextView textView = new TextView(getContext());
        textView.setText(tabName);
        textView.setGravity(Gravity.CENTER);
        addView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mChildCount++;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mPieceWidht = mScreenWidht / mChildCount;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int childWidth = child.getWidth();
            int childHeight2 = child.getHeight() / 2;
            int middle = (t + b) / 2;
            int left = l + mPieceWidht / 2 + mPieceWidht * i - childWidth / 2;
            child.layout(left, t, left + childWidth, b);
        }
        //绘制一像素的线
        getViewTreeObserver().addOnGlobalLayoutListener(firstTabGlobalLayoutListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        TextView child = (TextView) getChildAt(mCurrentPosition);
        int childBottom = child.getBottom();
        Pair<Float, Float> pair = getIndicatorCoordinates();
        canvas.drawRect(pair.first + mPaddingLeft,
                childBottom - mIndicatorHeight,
                pair.second + mPaddingRight,
                childBottom,
                mRectPaint);

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


    private ViewTreeObserver.OnGlobalLayoutListener firstTabGlobalLayoutListener =
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                }
            };

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
        view.setTextColor(mTabUnselectColor);
    }

    private void select(TextView view) {
        view.setTextColor(mTabSelectColor);
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
