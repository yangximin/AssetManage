package com.yang.assetmanage.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yang.assetmanage.app.MyApplication;
import com.yang.assetmanage.entity.ForgetPwd;
import com.yang.assetmanage.entity.User;

/**
 * Created by YXM
 * on 2019/5/28.
 */

public class DbUtils {

    SQLiteDatabase mSqLiteDatabase;

    public static DbUtils getInstance() {
        return SingleDbUtils.INSTANCE;
    }

    private static class SingleDbUtils {

        private static final DbUtils INSTANCE = new DbUtils();
    }

    DbUtils() {
        SQLiteDbHelper sqLiteDbHelper = new SQLiteDbHelper(MyApplication.getInstance());
        mSqLiteDatabase = sqLiteDbHelper.getWritableDatabase();
    }

    /**
     * 是否存在用户
     */
    public boolean isExistUser(String userName) {
        boolean isExist = false;
        Cursor cursor = mSqLiteDatabase.rawQuery("SELECT USER_NAME FROM user WHERE USER_NAME = ?", new String[]{userName});
        if (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("USER_NAME"));
            if (!TextUtils.isEmpty(name)) {
                isExist = true;
            }
        }
        cursor.close();
        return isExist;
    }

    /**
     * 登录
     */
    public boolean login(String userName, String pwd) {
        boolean isExist = false;
        Cursor cursor = mSqLiteDatabase.rawQuery("SELECT USER_NAME FROM user WHERE USER_NAME = ? And PASSWORD =?", new String[]{userName, pwd});
        if (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("USER_NAME"));
            if (!TextUtils.isEmpty(name)) {
                isExist = true;
            }
        }
        cursor.close();
        return isExist;
    }

    /**
     * 注册用户
     */
    public boolean insertUser(User user) {
        boolean isSuccess = false;
        ContentValues cv = new ContentValues();
        cv.put("USER_NAME", user.getName());
        cv.put("PASSWORD", user.getPassword());
        cv.put("IDCARD", user.getIdCard());
        cv.put("REGISTER_DATE", user.getRegisterDate());
        try {
            insert("USER", cv);
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    public boolean upSetPassword(ForgetPwd forgetPwd) {
        boolean isSuccess = false;
        Cursor cursor = null;
        try {
            cursor = mSqLiteDatabase.rawQuery("SELECT USER_NAME FROM user WHERE USER_NAME = ? And IDCARD =?", new String[]{forgetPwd.getName(), forgetPwd.getIdCard()});
            if (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex("_ID"));
                if (!TextUtils.isEmpty(id)) {
                    ContentValues cv = new ContentValues();
                    cv.put("PASSWORD", forgetPwd.getPwd());
                    mSqLiteDatabase.update("USER_NAME", cv, "_ID=?", new String[]{id});
                    isSuccess = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isSuccess;
    }

    /**
     * 插入数据
     *
     * @param table
     * @param contentValues
     * @throws Exception
     */
    private void insert(String table, ContentValues contentValues) throws Exception {
        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.insert(table, null, contentValues);
            mSqLiteDatabase.setTransactionSuccessful();
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

}
