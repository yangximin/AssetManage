package com.yang.assetmanage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by YXM
 * on 2019/5/28.
 */

public class SQLiteDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "asset.db";

    public static final int DB_VERSION = 1;


    public SQLiteDbHelper(Context context) {
        // 传递数据库名与版本号给父类
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // 在这里通过 db.execSQL 函数执行 SQL 语句创建所需要的表
        // 创建 students 表
        //用户表
        db.execSQL("CREATE TABLE USER(\n" +
                "   _ID           INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                "   USER_NAME     TEXT     NOT NULL,\n" +
                "   PASSWORD      TEXT     NOT NULL,\n" +
                "   IDCARD        INT      NOT NULL,\n" +
                "   REGISTER_DATE TEXT     NOT NULL\n" +
                ");");
        //收支表
        db.execSQL("CREATE TABLE ASSET(\n" +
                "   BILL_ID       INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                "   USER_ID       INTEGER      NOT NULL,\n" +
                "   MONEY         TEXT     NOT NULL,\n" +
                "   MONEY_TYPE    INT      NOT NULL,\n" +
                "   CRETE_DATA    TEXT     NOT NULL,\n" +
                "   MEMBER        TEXT     NOT NULL\n" +
                ");");
        //账本表
        db.execSQL("CREATE TABLE BILL(\n" +
                "   _ID           INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                "   USER_ID       INTEGER      NOT NULL,\n" +
                "   BILL_NAME     TEXT     NOT NULL\n" +
                ");");
        //插入日常账本
        db.execSQL("INSERT INTO BILL VALUES (1, 'NORMAL', '日常账本');");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}