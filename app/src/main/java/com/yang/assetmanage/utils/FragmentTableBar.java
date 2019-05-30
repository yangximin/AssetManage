/* 
 * @Title:  FragmentTablebar.java 
 * @Description:  fragmenttablebar管理控制类
 * @version:  V1.0 
 * @data:  2015-1-27 下午1:54:00 
 * @Copyright:  Zac Co., Ltd. Copyright 2003-2014,  All rights reserved 
 */
package com.yang.assetmanage.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

/**
 * FragmentTableBar管理控制类
 *
 * @author yxm
 */
public class FragmentTableBar {


    // 管理器
    private FragmentManager fragmentManager;
    // 类引用
    private FragmentActivity fragmentActivity;

    public final static String ARGUMENT = "fragmentArgument";
    /**
     * 当前fragment的tag
     */
    private String currentTag = "";

    /**
     * fragment的布局Id
     */
    private int layoutId;

    /**
     * @param fragmentActivity
     */
    public FragmentTableBar(FragmentActivity fragmentActivity, int layoutId) {
        super();
        this.fragmentActivity = fragmentActivity;
        this.fragmentManager = fragmentActivity.getSupportFragmentManager();
        this.layoutId = layoutId;
    }

    /**
     * fragment嵌套fragment构造
     *
     * @param fragmentActivity
     */
    public FragmentTableBar(FragmentActivity fragmentActivity, Fragment fragment, int layoutId) {
        super();
        this.fragmentActivity = fragmentActivity;
        this.fragmentManager = fragment.getChildFragmentManager();
        this.layoutId = layoutId;
    }

    /**
     * 切换fragment
     *
     * @param tag fragment的Tag
     */
    public Fragment switchFragment(String tag) {
        if (!TextUtils.equals(currentTag, tag)) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment currFragment = fragmentManager.findFragmentByTag(currentTag);
            Fragment fragment = fragmentManager.findFragmentByTag(tag);
            if (currFragment != null) {
                transaction.detach(currFragment);
            }
            currentTag = tag;
            if (fragment == null) {
                // 设置动画，可以自定义
                //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragment = Fragment.instantiate(fragmentActivity, tag);
                transaction.add(layoutId, fragment, tag);
            } else {
                transaction.attach(fragment);
            }
            // transaction.commit();
            if (fragmentActivity.isFinishing()) {// 判断activity是否关闭
                return null;
            }
            transaction.commitAllowingStateLoss();
            return fragment;
        }
        return null;
    }

    public String getCurrentTag() {
        return currentTag;
    }

    public Fragment getFragment(String tag) {
        return fragmentManager.findFragmentByTag(tag);
    }
}

