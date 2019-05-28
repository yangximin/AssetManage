package com.yang.assetmanage.app;

import android.app.Application;

import com.yang.assetmanage.db.DbUtils;

/**
 * Created by YXM
 * on 2019/5/28.
 */

public class MyApplication extends Application {

    static MyApplication mApplication;


    public static MyApplication getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        DbUtils.getInstance();
    }
}
