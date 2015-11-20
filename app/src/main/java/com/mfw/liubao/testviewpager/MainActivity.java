package com.mfw.liubao.testviewpager;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TabView mLinearLayout;
    PagerAdapter mAdapter;
    ArrayList<View> viewContainter = new ArrayList<>();
    private int height;
    private int width;
    private ViewPager mViewPager;
    private String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLinearLayout = (TabView) findViewById(R.id.linear);
        TextView text = (TextView) findViewById(R.id.text);
        viewContainter.add(getLayoutInflater().inflate(R.layout.tab1, null));
        viewContainter.add(getLayoutInflater().inflate(R.layout.tab2, null));
        viewContainter.add(getLayoutInflater().inflate(R.layout.tab3, null));
        viewContainter.add(getLayoutInflater().inflate(R.layout.tab4, null));

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewContainter.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = viewContainter.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewContainter.get(position));
            }
        });
        mLinearLayout.setViewPager(mViewPager);
        mLinearLayout.addTab("1");
        mLinearLayout.addTab("2sadf");
        mLinearLayout.addTab("3dd");
        mLinearLayout.addTab("4asdfasd");

    }
}
