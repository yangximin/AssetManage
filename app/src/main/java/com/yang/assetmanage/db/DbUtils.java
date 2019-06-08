package com.yang.assetmanage.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yang.assetmanage.app.MyApplication;
import com.yang.assetmanage.entity.Asset;
import com.yang.assetmanage.entity.Bill;
import com.yang.assetmanage.entity.Dicts;
import com.yang.assetmanage.entity.ForgetPwd;
import com.yang.assetmanage.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YXM
 * on 2019/5/28.
 */

public class DbUtils {

    private SQLiteDatabase mSqLiteDatabase;

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
    public User login(String userName, String pwd) {
        User user = null;
        Cursor cursor = mSqLiteDatabase.rawQuery("SELECT * FROM user WHERE USER_NAME = ? And PASSWORD =?", new String[]{userName, pwd});
        if (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("USER_NAME"));
            if (!TextUtils.isEmpty(name)) {
                String id = cursor.getString(cursor.getColumnIndex("_ID"));
                user = new User();
                user.setName(name);
                user.setId(id);
            }
        }
        cursor.close();
        return user;
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

    /**
     * 更新密码
     */
    public boolean upSetPassword(ForgetPwd forgetPwd) {
        boolean isSuccess = false;
        Cursor cursor = null;
        try {
            cursor = mSqLiteDatabase.rawQuery("SELECT * FROM user WHERE USER_NAME = ? And IDCARD =?", new String[]{forgetPwd.getName(), forgetPwd.getIdCard()});
            if (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex("_ID"));
                if (!TextUtils.isEmpty(id)) {
                    ContentValues cv = new ContentValues();
                    cv.put("PASSWORD", forgetPwd.getPwd());
                    mSqLiteDatabase.update("user", cv, "_ID=?", new String[]{id});
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
     * 查询表单
     */
    public List<Bill> selectBill(String userId) {
        Cursor cursor = null;
        List<Bill> bills = new ArrayList<>();
        try {
            cursor = mSqLiteDatabase.rawQuery("SELECT * FROM BILL WHERE USER_ID = ? OR _ID =?", new String[]{userId, "1"});
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex("_ID"));
                String name = cursor.getString(cursor.getColumnIndex("BILL_NAME"));
                if (!TextUtils.isEmpty(id)) {
                    Bill bill = new Bill();
                    bill.setBillName(name);
                    bill.setId(id);
                    bills.add(bill);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            bills = null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return bills;
    }
/**
 *   "   BILL_ID       INTEGER       NOT NULL,\n" +
 "   USER_ID       INTEGER      NOT NULL,\n" +
 "   MONEY         TEXT     NOT NULL,\n" +
 "   MONEY_TYPE    INT      NOT NULL,\n" +
 "   CRETE_DATA    TEXT     NOT NULL,\n" +
 "   MEMBER        TEXT     NOT NULL,\n" +
 "   REMARK        TEXT     NOT NULL\n" +
 */
    /**
     * @param billId
     * @return
     */
    public List<Asset> getAssetList(String billId) {
        Cursor cursor = null;
        List<Asset> assets = new ArrayList<>();
        try {
            cursor = mSqLiteDatabase.rawQuery("SELECT * FROM ASSET WHERE BILL_ID = ?", new String[]{billId});
            while (cursor.moveToNext()) {
                String money = cursor.getString(cursor.getColumnIndex("MONEY"));
                String type = cursor.getString(cursor.getColumnIndex("MONEY_TYPE"));
                String date = cursor.getString(cursor.getColumnIndex("CRETE_DATA"));
                String member = cursor.getString(cursor.getColumnIndex("MEMBER"));
                String remark = cursor.getString(cursor.getColumnIndex("REMARK"));
                Asset asset = new Asset();
                asset.setMoney(money+"元");
                asset.setMoneyName(getDictName(type));
                asset.setCreteData(date);
                asset.setMemberName(getDictName(member));
                asset.setRemark(remark);
                assets.add(asset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return assets;
    }

    public String getDictName(String id) {
        String name = null;
        Cursor cursor = null;
        try {
            cursor = mSqLiteDatabase.rawQuery("SELECT * FROM DICT WHERE _ID = ?", new String[]{id});
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex("NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return name;
    }

    public List<Dicts> getDictList(String type) {
        Cursor cursor = null;
        List<Dicts> dicts = new ArrayList<>();
        try {
            cursor = mSqLiteDatabase.rawQuery("SELECT * FROM DICT WHERE TYPE = ?", new String[]{type});
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex("_ID"));
                String name = cursor.getString(cursor.getColumnIndex("NAME"));
                dicts.add(new Dicts(id, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
            dicts = null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return dicts;
    }

    /**
     * 插入表单
     */
    public boolean insertBill(String userId, String billName) {
        boolean isSuccess = false;
        Cursor cursor = null;
        try {
            cursor = mSqLiteDatabase.rawQuery("SELECT * FROM BILL WHERE USER_ID = ? And BILL_NAME =?", new String[]{userId, billName});
            if (cursor.moveToNext()) {
                return false;
            }
            ContentValues cv = new ContentValues();
            cv.put("BILL_NAME", billName);
            cv.put("USER_ID", userId);
            insert("BILL", cv);
            isSuccess = true;
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
    public void insert(String table, ContentValues contentValues) throws Exception {
        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.insert(table, null, contentValues);
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("插入异常");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

}
