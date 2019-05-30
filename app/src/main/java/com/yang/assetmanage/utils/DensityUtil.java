/* 
 * @Title:  DensityUtil.java 
 * @Description:  分辨率处理类
 * @version:  V1.0 
 * @data:  2014-8-5 下午4:26:34 
 * @Copyright:  Zac Co., Ltd. Copyright 2003-2014,  All rights reserved 
 */
package com.yang.assetmanage.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;

/**
 * 
 * 分辨率处理类
 * 
 * @author hanli
 * @data: 2014-10-29 下午1:47:28
 */
public class DensityUtil {

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * px转sp
	 * 
	 * @param context 上下文
	 * @param pxVal 像素
	 * @return
	 */
	public static float px2sp(Context context, float pxVal) {
		return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
	}



	/**
	 * 将sp值转换为px值，保证文字大小不变
	 *
	 * @param spValue （DisplayMetrics类中属性scaledDensity）
	 *
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取系统通知栏的高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
//			L.printStackTrace(e);
		}
		return statusBarHeight;
	}

	/**
	 * 获取屏幕的分辨率
	 * 
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getDisplayMetrics(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}

}
