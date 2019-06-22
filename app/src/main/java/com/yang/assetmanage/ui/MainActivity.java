package com.yang.assetmanage.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;

import com.yang.assetmanage.R;
import com.yang.assetmanage.adapter.RVAdapter;
import com.yang.assetmanage.adapter.TabPagerAdapter;
import com.yang.assetmanage.db.DbUtils;
import com.yang.assetmanage.entity.Bill;
import com.yang.assetmanage.ui.fragment.AssetFragment;
import com.yang.assetmanage.ui.fragment.MyAccountFragment;
import com.yang.assetmanage.ui.fragment.PiePolylineChartFragment;
import com.yang.assetmanage.view.CustomRadioGroup;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    RecyclerView mRecyclerView;

    private RVAdapter<Bill> mAdapter;

    DbUtils mDbUtils;

    CustomRadioGroup mFooter;

    private ArrayList<Fragment> listFragment;

    private ViewPager mViewPager;

    private String[] mTabItemTitles;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_layout;
    }

    @Override
    protected boolean isNeedTitle() {
        return false;
    }

    @Override
    protected void initView() {
        mFooter = findViewById(R.id.main_footer);
        mViewPager = findViewById(R.id.main_viewpager);
        //创建tab按键
        mFooter.initItems(this,
                R.array.main_tab_item_text,
                R.array.main_tab_item_icon_unselect,
                R.array.main_tab_item_icon_select);
        //从资源文件加载title数组，为了页面滑动时，动态改变顶部title
        mTabItemTitles = getStringArrayFromResource(this, R.array.main_tab_item_text);
        listFragment = new ArrayList<>();
        listFragment.add(new PiePolylineChartFragment());
        listFragment.add(new AssetFragment());
        listFragment.add(new MyAccountFragment());
        mViewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(), listFragment, mTabItemTitles));
        mViewPager.setOffscreenPageLimit(listFragment.size());
        mViewPager.setCurrentItem(1);
        //默认消息栏
        mFooter.setCheckedIndex(1);
        initListener();

    }

    private void initListener() {

        //mViewPager滚动监听器在mFooter实现，
        mViewPager.addOnPageChangeListener(this);

        mFooter.setOnItemChangedListener(new CustomRadioGroup.OnItemChangedListener() {
            public void onItemChanged(int position) {
                mViewPager.setCurrentItem(position, false);
            }

        });
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }


    //读取资源文件数组
    public static String[] getStringArrayFromResource(Context context, int resId) {

        TypedArray typedArray = context.getResources().obtainTypedArray(resId);
        String[] array = new String[typedArray.length()];
        for (int i = 0; i < typedArray.length(); i++) {
            array[i] = typedArray.getString(i);
        }
        return array;

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mFooter.setCheckedIndex(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
