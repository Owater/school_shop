/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.siso.app.common.Constants;
import com.siso.app.entity.UserEntity;
import com.siso.app.ui.MyApplication;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-4-19 下午2:48:11
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-4-19 下午2:48:11 
 *
 */
public class AccountInfoUtils {
	
	public final static String key = "12345678";
	
	public static String[] getUserInfo(Context context){
		String userInfo[] = null;
		SharedPreferences userPreferences = context.getSharedPreferences(Constants.SHAREPREFER_UERINFO_NAME, Context.MODE_PRIVATE);
		String code = userPreferences.getString(Constants.SHAREPREFER_UERINFO_CODE, null);
		if(code!=null){
			String string;
			try {
				string = DESUtil.decryptDES(code, key);
				userInfo = string.split(",");
				Constants.ISLOGIN = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return userInfo;
	}
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-4-16 上午11:41:48
	 * @Decription 保存用户登录信息
	 *
	 * @param userEntity
	 */
	public static void saveSharedPreferences(Context context,UserEntity userEntity){
		SharedPreferences userPreferences = context.getSharedPreferences(Constants.SHAREPREFER_UERINFO_NAME, Context.MODE_PRIVATE);
		Editor editor = userPreferences.edit();
		
		String string = null;
		try {
			string = DESUtil.encryptDES(userEntity.getAuthCode()+","+userEntity.getId()+","
			+userEntity.getUserName()+","+userEntity.getUserPhone()+","+userEntity.getHxId()+","+userEntity.getAvatarUrl(), AccountInfoUtils.key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		editor.putString(Constants.SHAREPREFER_UERINFO_CODE, string);
		editor.putLong("timestamp", System.currentTimeMillis());
		editor.commit();
	}
	
	public static int getScId(Context context){
		SharedPreferences userPreferences = context.getSharedPreferences(Constants.SHAREPREFER_UERINFO_NAME, Context.MODE_PRIVATE);
		return userPreferences.getInt("scId", 0);
	}
	
	public static int getGuide(Context context){
		SharedPreferences userPreferences = context.getSharedPreferences(Constants.SHAREPREFER_UERINFO_GUIDE, Context.MODE_PRIVATE);
		return userPreferences.getInt("guide", 0);
	}
	
	public static void saveGuide(Context context){
		SharedPreferences userPreferences = context.getSharedPreferences(Constants.SHAREPREFER_UERINFO_GUIDE, Context.MODE_PRIVATE);
		Editor editor = userPreferences.edit();
		editor.putInt("guide", 1);
		editor.commit();
	}
	
	public static void loginOut(Context context) {
		SharedPreferences userPreferences = context.getSharedPreferences(Constants.SHAREPREFER_UERINFO_NAME, Context.MODE_PRIVATE);
		Constants.ISLOGIN = false;
		MyApplication.userInfo = null;
		userPreferences.edit().clear().commit();
	}

}

	