/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.adapter;

import com.siso.app.ui.R;
import com.siso.app.ui.R.string;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-5-11 下午4:41:13
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-5-11 下午4:41:13 
 *
 */
public class CustomDialogAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater listContainer;
	private String[] listItems;
	
	static class ViewHolder{
        public TextView content;
	}
	
	public CustomDialogAdapter(Context context, String[] data){
		this.context = context;
		this.listContainer = LayoutInflater.from(context);
		this.listItems = data;
	}
	
	public void setData(int position,String content){
		listItems[position]=content;
	}
	
	@Override
	public int getCount() {
		return listItems.length;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder listItemView = null;
		if(convertView == null){
			if(position==listItems.length-1){
				convertView = listContainer.inflate(R.layout.item_custom_dialog_last, null);
			}else {
				convertView = listContainer.inflate(R.layout.item_custom_dialog, null);
			}
			listItemView = new ViewHolder();
			listItemView.content = (TextView)convertView.findViewById(R.id.item_custom_dialog_text);
			convertView.setTag(listItemView);
		}else {
			listItemView = (ViewHolder)convertView.getTag();
		}
		listItemView.content.setText(listItems[position]);
		return convertView;
	}

}

	