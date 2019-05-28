package com.yang.assetmanage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.yang.assetmanage.R;

/**
 * Created by YXM
 * on 2019/5/28.
 */

public abstract class BaseActivity extends FragmentActivity {


    private View mContentView;

    public ImmersionBar mImmersionBar;

    private Toast mToast;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加子布局
        setContentView(getLayoutId());
        //初始化沉浸式
        initImmersionBar();
        initView();
        initData();

    }

    @Override
    public void setContentView(int layoutResID) {
        mContentView = LayoutInflater.from(this).inflate(layoutResID, null);
        super.setContentView(mContentView);
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

    protected void toActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivity(intent);

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
                .statusBarColor(R.color.global_white_color)
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
}
