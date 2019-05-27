package com.yang.assetmanage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yangximin on 2019/5/27.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;//定义数据库版本号
    private static final String DBNAME = "Asset.db"; //定义数据库名

    public DBHelper(Context context) {//定义构造函数
        //参数 上下文 数据库名称 cosor工厂 版本号
        super(context, DBNAME, null, VERSION);//重写基类的构造函数
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//创建数据库
        //用户表
//        db.execSQL("create table ceshi (id varchar(10) primary key, name varchar(200),age varchar(10),date varchar(10))");
        db.execSQL("CREATE TABLE USER(\n" +
                "   _ID INT PRIMARY KEY    NOT NULL,\n" +
                "   NAME           TEXT    NOT NULL,\n" +
                "   PASSWORD       TEXT    NOT NULL,\n" +
                "   IDCARD         INT     NOT NULL,\n" +
                "   REGISTER_DATA  TEXT    NOT NULL\n" +
                ")");
      //财务表
        db.execSQL("CREATE TABLE ASSET(\n" +
                "   BILL_ID        INT PRIMARY KEY   NOT NULL,\n" +
                "   USER_ID        INT    NOT NULL,\n" +
                "   MONEY          FLOAT  NOT NULL,\n" +
                "   MONEY_TYPE     INT    NOT NULL,\n" +
                "   CRETE_DATA     TEXT   NOT NULL,\n" +
                "   MEMBER         TEXT   NOT NULL\n" +
                ")");
//        //账单表
        db.execSQL("CREATE TABLE BILL(\n" +
                "   _ID            INT PRIMARY KEY   NOT NULL,\n" +
                "   BILL_NAME      STRING NOT NULL\n" +
                ")");

//        "   REGISTER_DATA        CHAR(50),\n" +
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//更新数据库
        //本方法主要用于更新数据库 通过对当前版本的判断 实现数据库的更新
    }
}