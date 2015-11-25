package com.mfw.liubao.testviewpager;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String LOG_TAG = MainActivity.class.getSimpleName();
    private TabLayout mTabView;
    ArrayList<View> viewContainter = new ArrayList<>();
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTabView = (TabLayout) findViewById(R.id.linear);
        viewContainter.add(getLayoutInflater().inflate(R.layout.tab1, null));
        viewContainter.add(getLayoutInflater().inflate(R.layout.tab2, null));
        viewContainter.add(getLayoutInflater().inflate(R.layout.tab3, null));
        viewContainter.add(getLayoutInflater().inflate(R.layout.tab4, null));
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        final String[] titles ={"sssdf", "saa", "sadddddddddsf", "sassssssdwwf", "sadwwf", "sadwwf"};
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

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        mTabView.setViewPager(mViewPager);
    }

}
