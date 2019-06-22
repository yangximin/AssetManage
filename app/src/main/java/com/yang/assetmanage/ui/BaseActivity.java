package com.yang.assetmanage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.yang.assetmanage.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by YXM
 * on 2019/5/28.
 */

public abstract class BaseActivity extends AppCompatActivity {


    private View mContentView;

    public ImmersionBar mImmersionBar;

    private Toast mToast;

    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加子布局
        setContentView(getLayoutId());
        //初始化沉浸式
        if (isImmersionBarEnabled())
            initImmersionBar();
        EventBus.getDefault().register(this);
        initView();
        initData();

    }

    @Override
    public void setContentView(int layoutResID) {
        if (layoutResID == 0) return;
        // 初始化标题栏
        if (isNeedTitle()) {
            mContentView = getContentView(layoutResID);
        } else {
            mContentView = LayoutInflater.from(this).inflate(layoutResID, null);
        }
        super.setContentView(mContentView);
    }

    private View getContentView(int layoutResID) {
        //填充根布局
        CoordinatorLayout rootView = (CoordinatorLayout) LayoutInflater.from(this).inflate(
                R.layout.base_layout_main, null);
        FrameLayout frameLayout = rootView.findViewById(R.id.base_layout_fl);
        //初始化标题栏
        initTitleBar(rootView);
        //填充子布局
        View contentView = View.inflate(this, layoutResID, null);
        //把子布局添加到根布局中
        frameLayout.addView(contentView);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //设置子布局在根布局中为标题栏垂直底下
//        lp.addRule(RelativeLayout.BELOW, R.id.rel_title_bar);
        contentView.setLayoutParams(lp);
        return rootView;
    }

    private void initTitleBar(View rootView) {
         toolbar = rootView.findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        //标题栏根布局
//        mTitleBar = rootView.findViewById(R.id.rel_title_bar);
//        //返回
//        mLeftBtn = rootView.findViewById(R.id.title_left_iv);
//        //标题
//        mTitleTv = rootView.findViewById(R.id.title_center_tv);
//
//        mLeftBtn.setOnClickListener(
//                new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
    }

    protected void setTitle(String title) {
        toolbar.setTitle(title);
    }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 加载布局
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 初始化 数据
     */
    protected abstract void initData();

    public void toActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivity(intent);

    }

    protected boolean isNeedTitle() {
        return true;
    }

    /**
     * 初始化状态栏
     * ImmersionBar : https://github.com/gyf-dev/ImmersionBar
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        statusBarDarkFont();
    }

    /**
     * 状态栏深色字体
     */
    protected void statusBarDarkFont() {
        mImmersionBar
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.component_transparent_color)
                .fitsSystemWindows(true)
                .keyboardEnable(true)
                .init();
    }

    /**
     * 状态栏亮色字体
     */
    protected void statusBarLightFont() {
        mImmersionBar
                .statusBarDarkFont(false)
                .statusBarColor(R.color.global_float_transparent)
                .fitsSystemWindows(false)
                .keyboardEnable(true)
                .init();
    }

    @Override
    protected void onDestroy() {
        if (mImmersionBar != null)
            mImmersionBar.destroy();
        super.onDestroy();
    }

    public void showMessage(String message) {
        if (this.isFinishing()) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        }
        if (!TextUtils.isEmpty(message)) {
            mToast.setText(message);
        }
        mToast.show();
    }

    /**
     * 接收订阅的事件
     * 子类实现该方法取值
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBaseEvent(Object event) {

    }
}
