package com.cutesys.bibleapp;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewpager;
    private TabLayout tablayout;
    private TextView mTitle;
    private ImageView icon;

    private ViewpagerAdapter adapter;

    private int ActionPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitIdView();
    }
    private void InitIdView() {

       /* icon = (ImageView) rootView.findViewById(R.id.icon);
        icon.setVisibility(View.GONE);*/
        viewpager = (ViewPager)findViewById(R.id.viewpager);
        tablayout = (TabLayout)findViewById(R.id.tablayout);
        mTitle = ((TextView)findViewById(R.id.mTitle));
        setupViewPager(viewpager);
        tablayout.setupWithViewPager(viewpager);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewpagerAdapter(getApplicationContext(),getSupportFragmentManager());
        viewpager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
        if(ActionPage == 0){
            viewpager.setCurrentItem(0);
        } else if(ActionPage == 1){
            viewpager.setCurrentItem(1);
        } else if(ActionPage == 2){
            viewpager.setCurrentItem(2);
        } else if(ActionPage == 3){
            viewpager.setCurrentItem(3);
        }
    }

    public class ViewpagerAdapter extends FragmentPagerAdapter {

        private Context _context;
        private int totalPage = 3;
        private String[] titles = {"Daily Verse", "Notification",
                "History"};

        public ViewpagerAdapter(Context applicationContext,
                                FragmentManager fragmentManager) {
            super(fragmentManager);
            _context = applicationContext;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = new Fragment();
            switch (position) {
                case 0:
                    f = new Activity_Daily();
                    return f;
                case 1:
                    f = new Activity_Daily();
                    return f;
                case 2:
                    f = new Activity_Daily();
                    return f;
                case 3:
                    f = new Activity_Daily();
                    return f;
            }
            return f;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return totalPage;
        }
    }
}
