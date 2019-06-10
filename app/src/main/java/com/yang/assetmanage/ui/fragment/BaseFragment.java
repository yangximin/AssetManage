package com.yang.assetmanage.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.yang.assetmanage.R;
import com.yang.assetmanage.app.MyApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by yangximin on 2019/5/29.
 */

public abstract class BaseFragment extends Fragment {

    Context mContext;

    protected View rootView;

    private ImmersionBar mImmersionBar;

    private Toast mToast;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            pageName = savedInstanceState.getString(PAGE_NAME_KEY);
//        }
        if (getLayoutId() != 0) {
            rootView = View.inflate(mContext, getLayoutId(), null);
        }
        //初始化沉浸式
        if (immersionBarEnabled())
            initImmersionBar();
        initView();
        initData();
        EventBus.getDefault().register(this);
        return rootView;
    }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    public boolean immersionBarEnabled() {
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    /**
     * 初始化状态栏
     * ImmersionBar : https://github.com/gyf-dev/ImmersionBar
     */
    public void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        statusBarDarkFont();
    }

    /**
     * 是否保留状态栏
     */
    protected boolean isFitsSystemWindows() {
        return true;
    }

    /**
     * 状态栏深色字体
     */
    protected void statusBarDarkFont() {
        mImmersionBar
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.global_white_color)
                .fitsSystemWindows(isFitsSystemWindows())
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

    @SuppressWarnings("TypeParameterUnusedInFormals")
    public <T extends View> T findViewById(@IdRes int id) {
        return rootView.findViewById(id);
    }

    public void showMessage(String message) {
        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getInstance().getApplicationContext(), message, Toast.LENGTH_LONG);
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

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

}
