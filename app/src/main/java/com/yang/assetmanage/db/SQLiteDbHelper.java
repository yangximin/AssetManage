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
                "   _ID       INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                "   BILL_ID       INTEGER       NOT NULL,\n" +
                "   USER_ID       INTEGER      NOT NULL,\n" +
                "   MONEY         TEXT     NOT NULL,\n" +
                "   MONEY_TYPE    INT      NOT NULL,\n" +
                "   MONEY_TYPE_ID INT      NOT NULL,\n" +
                "   CRETE_DATA    TEXT     NOT NULL,\n" +
                "   MEMBER        TEXT     NOT NULL,\n" +
                "   REMARK        TEXT     NOT NULL,\n" +
                "FOREIGN KEY (BILL_ID) REFERENCES BILL(_ID) ON DELETE CASCADE" +
                ");");
        //账本表
        db.execSQL("CREATE TABLE BILL(\n" +
                "   _ID           INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                "   USER_ID       INTEGER      NOT NULL,\n" +
                "   BILL_NAME     TEXT     NOT NULL\n" +
                ");");
        //字典表
        db.execSQL("CREATE TABLE DICT(\n" +
                "   _ID           INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                "   TYPE       INTEGER      NOT NULL,\n" +
                "   NAME       TEXT      NOT NULL\n" +
                ");");
        //插入日常账本
        db.execSQL("INSERT INTO BILL VALUES (1, 'NORMAL', '日常账本');");
        //插入字典
        db.execSQL("INSERT INTO DICT VALUES (0,1, '无成员')," +
                "(1,1, '本人')," +
                "(2,1, '老公')," +
                "(3, 1,'老婆')," +
                "(4,1, '子女')," +
                "(5,1, '父母')," +
                "(6,1, '父母');");
        db.execSQL("INSERT INTO DICT VALUES (7, 2,'零食')," +
                "(8, 2,'衣服饰品')," +
                "(9, 2,'行车交通')," +
                "(10, 2,'居家物业')," +
                "(11, 2,'交流通讯')," +
                "(12, 2,'学习进修')," +
                "(13, 2,'医疗保险')," +
                "(14, 2,'金融保险')," +
                "(15, 2,'其他');");
        db.execSQL("INSERT INTO DICT VALUES (16, 3,'工资收入')," +
                "(17, 3,'红包收入')," +
                "(18, 3,'奖金收入')," +
                "(19, 3,'经营收入')," +
                "(20, 3,'转移收入')," +
                "(21, 3,'理财收入')," +
                "(22, 3,'转账收入')," +
                "(23, 3,'财产收入')," +
                "(24, 3,'其他');");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}