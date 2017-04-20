package com.cutesys.bibleapp;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cutesys.bibleapp.SubClasses.DailyFragment;
import com.cutesys.bibleapp.SubClasses.HistoryFragment;
import com.cutesys.bibleapp.SubClasses.NotificationFragment;

public class HomeActivity extends AppCompatActivity {

    private ViewPager viewpager;
    private TabLayout tablayout;

    private ViewpagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        InitIdView();
    }
    private void InitIdView() {

        viewpager = (ViewPager)findViewById(R.id.viewpager);
        tablayout = (TabLayout)findViewById(R.id.tablayout);
        setupViewPager(viewpager);
        tablayout.setupWithViewPager(viewpager);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewpagerAdapter(getApplicationContext(),getSupportFragmentManager());
        viewpager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
    }

    public class ViewpagerAdapter extends FragmentPagerAdapter {

        private Context _context;
        private int totalPage = 3;
        private String[] titles = {"Daily Thought", "Notification",
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
                    f = new DailyFragment();
                    return f;
                case 1:
                    f = new NotificationFragment();
                    return f;
                case 2:
                    f = new HistoryFragment();
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
