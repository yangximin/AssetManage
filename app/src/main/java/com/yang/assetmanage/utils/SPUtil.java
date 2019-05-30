/* 
 * @Title:  a.java 
 * @Description:  sp帮助类
 * @version:  V1.0 
 * @data:  2015-1-21 下午5:50:25 
 * @Copyright:  Zac Co., Ltd. Copyright 2003-2014,  All rights reserved 
 */
package com.yang.assetmanage.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * sp帮助类
 */
public class SPUtil {

    // 存储的sharedpreferences文件名
    private static final String FILE_NAME = "ahw_sp";

    /**
     * 保存数据到文件
     *
     * @param context
     * @param key
     * @param data
     */
    public static void saveData(Context context, String key, Object data) {
        if (data == null) {
            return;
        }
        String type = data.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        }

        editor.commit();
    }

    public static void saveObjData(Context context, String key, Object data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        //创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //创建字节对象输出流
        ObjectOutputStream out = null;
        try {
            //然后通过将字对象进行64转码，写入key值为key的sp中
            out = new ObjectOutputStream(baos);
            out.writeObject(data);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor.putString(key, objectVal);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static Object getObjData(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String objectVal = sharedPreferences.getString(key, null);
        if (TextUtils.isEmpty(objectVal)) {
            return null;
        }
        byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
        //一样通过读取字节流，创建字节流输入流，写入对象并作强制转换
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bais.close();
                if (ois != null) {
                    ois.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 从文件中读取数据
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static Object getData(Context context, String key, Object defValue) {

        String type = defValue.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        // defValue为为默认值，如果当前获取不到数据就返回它
        if ("Integer".equals(type)) {
            return sharedPreferences.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return sharedPreferences.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return sharedPreferences.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            return sharedPreferences.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return sharedPreferences.getLong(key, (Long) defValue);
        }
        return null;
    }

}
