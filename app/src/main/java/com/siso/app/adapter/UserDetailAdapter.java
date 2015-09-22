/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.adapter;

import com.siso.app.ui.R;

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
 * @createtime : 2015-4-22 上午1:25:57
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-4-22 上午1:25:57 
 *
 */
public class UserDetailAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater listContainer;
	private int itemViewResource;
	String[] user_info_list_first;
	String[] user_info_list_second;
	static class ViewHolder{
		TextView first;
        TextView second;
	}
	
	public UserDetailAdapter(Context context,String[] user_info_list_first,String[] user_info_list_second,int resource){
		this.context = context;
		this.listContainer = LayoutInflater.from(context);
		this.itemViewResource = resource;
		this.user_info_list_first = user_info_list_first;
		this.user_info_list_second = user_info_list_second;
	}
	
	@Override
	public int getCount() {
		return user_info_list_first.length;
	}

	@Override
	public Object getItem(int position) {
		return user_info_list_second[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			convertView = listContainer.inflate(this.itemViewResource, null);
			viewHolder = new ViewHolder();
			viewHolder.first = (TextView)convertView.findViewById(R.id.user_detail_first);
			viewHolder.second = (TextView)convertView.findViewById(R.id.user_detail_second);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.first.setText(user_info_list_first[position]);
		String seondString = user_info_list_second[position];
        if (seondString==null) {
            seondString = "未填写";
        }
        viewHolder.second.setText(seondString);
		return convertView;
	}

}

	