/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.widget;

import com.siso.app.common.Constants;
import com.siso.app.ui.MarketPubActivity;
import com.siso.app.ui.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 
 * description :相片弹出选择
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-3-23 下午2:54:29
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-3-23 下午2:54:29
 *
 */
public class ChoosePicDialog extends Dialog implements android.view.View.OnClickListener{
	private Context context;
	private int layout;
	private ChoosePicListener choosePicListener;
	private Button cameraBtn,albumBtn;

	public ChoosePicDialog(Context context) {
		super(context);
	}

	public ChoosePicDialog(Context context, int theme,int layout) {
		super(context, theme);
		this.context = context;
		this.layout = layout;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout);
		
		cameraBtn = (Button)findViewById(R.id.choose_dialog_camera);
		albumBtn = (Button)findViewById(R.id.choose_dialog_album);
		cameraBtn.setOnClickListener(this);
		albumBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.choose_dialog_camera:
//			MarketPubActivity.CHOOSE_PIC=MarketPubActivity.CHOOSE_PIC_CAMERA;
			choosePicListener.startChooseActivity(Constants.TAKEPHOTO);
			break;
		case R.id.choose_dialog_album:
//			MarketPubActivity.CHOOSE_PIC=MarketPubActivity.CHOOSE_PIC_ALBUM;
			choosePicListener.startChooseActivity(Constants.CHOOSE_PIC);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-3-23 下午7:51:34
	 * @Decription 注册监听器
	 *
	 * @param choosePicListener
	 */
	public void setOnClickListener(ChoosePicListener choosePicListener){
		this.choosePicListener = choosePicListener;
	}
	
	/**
	 * 
	 * description : 回调
	 *
	 * @version 1.0
	 * @author Owater
	 * @createtime : 2015-3-23 下午7:45:55
	 * 
	 * 修改历史:
	 * 修改人                                          修改时间                                                  修改内容
	 * --------------- ------------------- -----------------------------------
	 * Owater        2015-3-23 下午7:45:55
	 *
	 */
	public static interface ChoosePicListener{
		/**
		 * 
		 * @author Owater
		 * @createtime 2015-3-23 下午7:48:01
		 * @Decription 启动拍照或者相册
		 *
		 */
		public void startChooseActivity(int choose);
	}
}
