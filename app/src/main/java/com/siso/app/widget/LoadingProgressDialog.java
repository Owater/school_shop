/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.widget;

import com.siso.app.ui.R.string;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-4-15 下午12:06:39
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-4-15 下午12:06:39 
 *
 */
public class LoadingProgressDialog extends ProgressDialog {
	
	public LoadingProgressDialog(Context context) {
		super(context);
	}
	
	public LoadingProgressDialog(Context context, int theme) {
		super(context, theme);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setCanceledOnTouchOutside(false);
		this.setMessage("正在加载...");
	}

}

	