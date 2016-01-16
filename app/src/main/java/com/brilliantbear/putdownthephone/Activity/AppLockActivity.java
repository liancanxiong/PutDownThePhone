package com.brilliantbear.putdownthephone.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.brilliantbear.putdownthephone.R;
import com.brilliantbear.putdownthephone.fragment.LockFragment;
import com.brilliantbear.putdownthephone.fragment.UnlockFragment;

import java.util.ArrayList;
import java.util.List;

public class AppLockActivity extends AppCompatActivity {

    private List<Fragment> mFragments;
    private List<String> mTitles;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private UnlockFragment unlockFragment;
    private LockFragment lockFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);

        initView();

        initData();

    }


    private void initData() {


        unlockFragment = new UnlockFragment();
        lockFragment = new LockFragment();

        mFragments = new ArrayList<>();
        mFragments.add(unlockFragment);
        mFragments.add(lockFragment);

        mTitles = new ArrayList<>();
        mTitles.add("未加锁");
        mTitles.add("已加锁");

        tabLayout.addTab(tabLayout.newTab().setText(mTitles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitles.get(1)));

        AppLockAdapter mAdapter = new AppLockAdapter(getSupportFragmentManager(), mFragments, mTitles);
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("应用锁");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tab);

        viewPager = (ViewPager) findViewById(R.id.view_pager);

    }


    private class AppLockAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;
        private List<String> titles;

        public AppLockAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
