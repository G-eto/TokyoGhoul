package com.example.tokyoghoul.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

//自定义适配器
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] title;
    List<Fragment> fragments;

    public MyFragmentPagerAdapter(FragmentManager fm, String[] title, List<Fragment> fragments) {
        super(fm);
        this.title = title;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {

        return fragments.get(position);
//        if (position == 1) {
//            return fragments.get(1);//娱乐
//        }
//        return new FirstFragment();//首页
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}