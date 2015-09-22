/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.widget;

import com.siso.app.adapter.CustomDialogAdapter;
import com.siso.app.entity.GoodsEntity;
import com.siso.app.ui.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-5-11 下午3:55:44
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-5-11 下午3:55:44 
 *
 */
public class CustomDialog extends Dialog {
	
	private Context context;
	private String title;
	private int arrId;
	private ListView listView;
	private TextView titleTextView;
	private CustomDialogListener listener;
	private Object object;
	private String data[];
	private CustomDialogAdapter adapter;
	
	public CustomDialog(Context context, int theme,String title,int arrId) {
		super(context, theme);
		this.context = context;
		this.title = title;
		this.arrId = arrId;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_dialog);
		initView();
	}

	private void initView() {
		listView = (ListView)findViewById(R.id.custom_dialog_listview);
		titleTextView = (TextView)findViewById(R.id.custom_dialog_title);
		titleTextView.setText(title);
		
		data = context.getResources().getStringArray(arrId);
		adapter = new CustomDialogAdapter(context, data);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					listener.onChoose(position,object);
				} catch (Exception e) {
					Log.e("CustomDialog", "Activity must be implements CustomDialogListener.");
				}
			}
		});
	}
	
	public void setData(int position,String content){
		data[position]=content;
		adapter.notifyDataSetChanged();
	}
	
	public void show(Object object) {
		super.show();
		this.object=object;
		if(((GoodsEntity)object).getStatus()==1){
			setData(1, "下架");
		}else {
			setData(1, "上架");
		}
	}
	
	public void setOnClickListener(CustomDialogListener listener){
		this.listener = listener;
	}
	
	public static interface CustomDialogListener{
		public void onChoose(int position,Object object);
	}

}

	