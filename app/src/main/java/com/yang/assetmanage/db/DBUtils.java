package com.yang.assetmanage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.yang.assetmanage.app.MyApplication;

/**
 * Created by yangximin on 2019/5/27.
 */

public class DBUtils {

    DBHelper dbHelper;
    SQLiteDatabase mSQLiteDatabase;


    public static DBUtils getInstance() {
        return SingleDBUtil.dbUtils;
    }

    private static class SingleDBUtil {
        private static DBUtils dbUtils = new DBUtils(MyApplication.getInstance());
    }

    DBUtils(Context context) {
        dbHelper = new DBHelper(context);
        mSQLiteDatabase = dbHelper.getWritableDatabase();
    }
}
