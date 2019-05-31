package com.yang.assetmanage.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * TabLayout + ViewPager组合时用的适配器
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> list;
    private String[] titles;

    /**
     * 构造方法
     */
    public TabPagerAdapter(FragmentManager fm, List<Fragment> list, String[] titles) {
        super(fm);
        this.list = list;
        this.titles = titles;
    }

    /**
     * 返回显示的Fragment总数
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * 返回要显示的Fragment的某个实例
     */
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    /**
     * 返回每个Tab的标题
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    /**
     * 如果动态加载、删除fragment时
     */
//    @Override
//    public int getItemPosition(Object object) {
//        return PagerAdapter.POSITION_NONE;
//    }
}
