package com.yang.assetmanage.app;

import android.app.Application;

/**
 * Created by yangximin on 2019/5/27.
 */

public class MyApplication extends Application {

    static MyApplication application;

    public static MyApplication getInstance(){
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}
