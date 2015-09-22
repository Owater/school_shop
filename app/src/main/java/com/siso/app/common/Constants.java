/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Environment;

/**
 * description :常量
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-3-20 上午10:28:59
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-3-20 上午10:28:59 
 *
 */
public class Constants {
	
	/**
	 * 编码格式
	 */
	public static final String ENCODING = "UTF-8";
	
	/**
	 * Share-SDK appkey
	 */
	public static final String APPKEY = "6360c8ed4a9c";
	
	/**
	 * Share-SDK appsecret
	 */
	public static final String APPSECRET = "bd3f34340d94d8f402514eee49a01a7f";
	
	/**
	 * 相册选择返回标识
	 */
	public static final int CHOOSE_PIC = 11;
	/**
	 * 相册选择返回标识
	 */
	public static final int TAKEPHOTO = 12;
	
	/**
	 * 限制上传图片个数
	 */
	public static final int UPLOAD_PIC_NUMS = 4;
	
	/**
	 * 图片缓存路径
	 */
	public static final String FILE_TEMP_DIR = Environment.getExternalStorageDirectory()+ File.separator + "siso";

	/**
	 * 网络异常的文字
	 */
	public static final String INTENT_ERROR = "与服务器断开连接，请重新检查一下网络";
	
	/**
	 * 加密的key值
	 */
	public static final String KEY = "siso";
	/**
	 * 
	 */
	public static final String SHAREPREFER_UERINFO_GUIDE = "xyyg_guide";
	public static final String SHAREPREFER_UERINFO_NAME = "xyyg_user";
	public static final String SHAREPREFER_UERINFO_CODE = "xyyg_code";
	public static int SCID;
	public static boolean ISLOGIN = false;
	
	/**
	 * Chat
	 */
	public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
	public static final String GROUP_USERNAME = "item_groups";
	public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
	public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";
	public static final String ACCOUNT_REMOVED = "account_removed";
}

	