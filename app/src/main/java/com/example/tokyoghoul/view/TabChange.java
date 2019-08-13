package com.example.tokyoghoul.view;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import static com.example.tokyoghoul.activity.MainActivity.ackViewId;
import static com.example.tokyoghoul.activity.MainActivity.addMenu;
import static com.example.tokyoghoul.activity.MainActivity.searchView;
import static com.example.tokyoghoul.view.MyRoleFragment.opopSearch;

public class TabChange {
    private String[] Title;
    private int[] selectTabRes;
    private int[] unSelectTabRes;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public TabChange(String[] Title, int[] selectTabRes, int[] unSelectTabRes,
                        TabLayout tabLayout, ViewPager viewPager){
        this.Title = Title;
        this.selectTabRes = selectTabRes;
        this.unSelectTabRes = unSelectTabRes;
        this.tabLayout = tabLayout;
        this.viewPager = viewPager;
    }
    public void initView() {
      /*  //设置方式一：
        //获取底部的单个Tab
        tabAtOne = tabLayout.getTabAt(0);
        tabAttwo = tabLayout.getTabAt(1);
        tabAtthree = tabLayout.getTabAt(2);
        tabAtfour = tabLayout.getTabAt(3);

        //设置Tab图片
        tabAtOne.setIcon(R.drawable.i8live_menu_home_press);
        tabAttwo.setIcon(R.drawable.i8live_menu_information_normal);
        tabAtthree.setIcon(R.drawable.i8live_menu_game_normal);
        tabAtfour.setIcon(R.drawable.i8live_menu_personl_normal);*/

        //设置方式二：
        for (int i = 0; i < Title.length; i++) {
            if (i == 0) {
                tabLayout.getTabAt(0).setIcon(selectTabRes[0]);
            } else {
                tabLayout.getTabAt(i).setIcon(unSelectTabRes[i]);
            }
        }

    }

    public void initData() {

    }

    public void initListener(final String[] Title) {
        //TabLayout切换时导航栏图片处理
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {//选中图片操作

                for (int i = 0; i < Title.length; i++) {
                    if (tab == tabLayout.getTabAt(i)) {
                        if(i == 0){
                            searchView.setVisible(true);
                            addMenu.setVisible(false);
                        }else {
                            searchView.setVisible(false);
                            addMenu.setVisible(true);
                        }
                        ackViewId = i;
                        opopSearch = i;
                        tabLayout.getTabAt(i).setIcon(selectTabRes[i]);
                        viewPager.setCurrentItem(i);
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {//未选中图片操作

                for (int i = 0; i < Title.length; i++) {
                    if (tab == tabLayout.getTabAt(i)) {
                        tabLayout.getTabAt(i).setIcon(unSelectTabRes[i]);
                    }
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
