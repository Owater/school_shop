/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-4-29 上午12:39:26
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-4-29 上午12:39:26 
 *
 */
public class CommonUtils {
	
	private static String splitString = ";";
	
	public static boolean isImageUri(String s1) {
        s1 = s1.toLowerCase();
        return s1.endsWith(".png")
                || s1.endsWith(".jpg")
                || s1.endsWith(".jpeg")
                || s1.endsWith(".bmp")
                || s1.endsWith(".gif");
    }
	
	public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
     }
	
	public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
	}
	
	public static String getFirstImg(String imgUrl){
		String img[] = imgUrl.split(splitString);
		return img[0];
	}
	
	public static String[] getImgArr(String imgUrl){
		String img[] = imgUrl.split(splitString);
		return img;
	}
	
}

	