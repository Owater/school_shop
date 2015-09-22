/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.utils;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-3-31 下午3:38:25
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-3-31 下午3:38:25 
 *
 */
public class StringUtil {
	
	public static String string2Unicode(String string) {
	    StringBuffer unicode = new StringBuffer();
	    for (int i = 0; i < string.length(); i++) {
	        char c = string.charAt(i);// 取出每一个字符
	        unicode.append("\\u" + Integer.toHexString(c));// 转换为unicode
	    }
	    return unicode.toString();
	}
	
	public static String unicode2String(String unicode) {
	    StringBuffer string = new StringBuffer();
	    String[] hex = unicode.split("\\\\u");
	    for (int i = 1; i < hex.length; i++) {
	        int data = Integer.parseInt(hex[i], 16);// 转换出每一个代码点
	        string.append((char) data);// 追加成string
	    }
	    return string.toString();
	}

}

	