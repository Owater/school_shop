/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * description :
 *
 * @version 1.0
 * @author zhoufeng
 * @createtime : 2015-3-26 下午4:13:41
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * zhoufeng        2015-3-26 下午4:13:41 
 *
 */
public class SharedPreferencesUtils {
	/**
	 * 
	 * @author zhoufeng
	 * @createtime 2015-3-30 下午2:59:33
	 * @Decription 向SharedPreferences添加信息
	 *
	 * @param context 上下文
	 * @param SharedPreferName SharedPreferences的名称
	 * @param type 数据的类型
	 * @param key 数据的名称
	 * @param value 数据的值
	 */
	public static void saveSharedPreferInfo(Context context, String SharedPreferName, String type, String key,
			Object value) {
		SharedPreferences userPreferences;
		userPreferences = context.getSharedPreferences(SharedPreferName, Context.MODE_PRIVATE);
		Editor editor = userPreferences.edit();

		if ("String".equals(type)) {
			editor.putString(key, (String) value);
		} else if ("Integer".equals(type)) {
			editor.putInt(key, (Integer) value);
		} else if ("Boolean".equals(type)) {
			editor.putBoolean(key, (Boolean) value);
		} else if ("Float".equals(type)) {
			editor.putFloat(key, (Float) value);
		} else if ("Long".equals(type)) {
			editor.putLong(key, (Long) value);
		}
		editor.commit();
	}
	/**
	 * 
	 * @author zhoufeng
	 * @createtime 2015-3-30 下午3:00:11
	 * @Decription 从SharedPreferences取出信息
	 *
	 * @param context 上下文
	 * @param SharedPreferName SharedPreferences的名称
	 * @param type 数据的类型
	 * @param key 数据的名称
	 * @return
	 */
	public static String getSharedPreferInfo(Context context,String SharedPreferName,String type,String key) {
		SharedPreferences userPreferences = context.getSharedPreferences(
				SharedPreferName, Context.MODE_PRIVATE);
		String string = null;
		if ("String".equals(type)) {
			string = userPreferences.getString(key, null);
		} else if ("Integer".equals(type)) {
			string = userPreferences.getString(key, null);
		} else if ("Boolean".equals(type)) {
			string = userPreferences.getString(key, null);
		} else if ("Float".equals(type)) {
			string = userPreferences.getString(key, null);
		} else if ("Long".equals(type)) {
			string = userPreferences.getString(key, null);
		}
		return string;
	}
	
	/**
	 * 
	 * @author zhoufeng
	 * @createtime 2015-3-30 下午3:00:25
	 * @Decription 清楚SharedPreferences的信息
	 *
	 * @param context 上下文
	 * @param SharedPreferName SharedPreferences的名称
	 */
	public static void clearSharePreferInfo(Context context,String SharedPreferName){
		SharedPreferences userPreferences = context.getSharedPreferences(SharedPreferName, Context.MODE_PRIVATE);
		userPreferences.edit().clear().commit();
	}
}

	