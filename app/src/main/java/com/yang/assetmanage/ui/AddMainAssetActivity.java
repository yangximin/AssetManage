package com.yang.assetmanage.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.yang.assetmanage.R;
import com.yang.assetmanage.entity.TabEntity;
import com.yang.assetmanage.ui.fragment.AddExpendRecordFragment;
import com.yang.assetmanage.ui.fragment.AddIncomeRecordFragment;
import com.yang.assetmanage.ui.fragment.BaseFragment;
import com.yang.assetmanage.view.CommonTabLayout;
import com.yang.assetmanage.view.CustomTabEntity;
import com.yang.assetmanage.view.OnTabSelectListener;

import java.util.ArrayList;

/**
 * Created by YXM
 * on 2019/5/30.
 */

public class AddMainAssetActivity extends BaseActivity {
    /**
     * Fragment适配器
     */
    private FragmentPagerAdapter tabFragmentAdapter;
    private ViewPager tabUserVPager;
    private CommonTabLayout tabTitleLayout;

    private AddExpendRecordFragment mAddExpendRecordFragment;

    private ArrayList<BaseFragment> tabFragments = new ArrayList<>();

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_asset_layout;
    }

    @Override
    protected void initView() {
        tabTitleLayout = findViewById(R.id.tabTitleLayout);
        tabUserVPager = findViewById(R.id.ed_contacts_user_viewpager);
        tabTitleLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                tabUserVPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

    }

    @Override
    protected void initData() {
        AddExpendRecordFragment expendRecordFragment = new AddExpendRecordFragment();
        AddIncomeRecordFragment incomeRecordFragment = new AddIncomeRecordFragment();
        tabFragments.add(expendRecordFragment);
        tabFragments.add(incomeRecordFragment);
        mTabEntities.add(new TabEntity("支出"));
        mTabEntities.add(new TabEntity("收入"));

        tabTitleLayout.setTabData(mTabEntities);
        tabFragmentAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return tabFragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return tabFragments.get(arg0);
            }
        };
        tabUserVPager.setAdapter(tabFragmentAdapter);
        tabUserVPager.addOnPageChangeListener(new MyOnPageChangeListener());
    }


    /**
     * 页面滑动监听
     */
    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            //((RadioButton)tabTitleLayout.getChildAt(position)).setChecked(true);
            tabTitleLayout.setCurrentTab(position);
            //mCurrIndex = position;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
