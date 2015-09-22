/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.adapter;

import java.util.List;

import com.siso.app.adapter.MarketListAdapter.ViewHolder;
import com.siso.app.entity.GoodsEntity;
import com.siso.app.entity.SchoolEntity;
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
 * @createtime : 2015-4-16 下午8:56:25
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-4-16 下午8:56:25 
 *
 */
public class SchoolListAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater listContainer;
	private int itemViewResource;
	private List<SchoolEntity> listItems;
	static class ViewHolder{
        public TextView content;
	}
	
	public SchoolListAdapter(Context context, List<SchoolEntity> data,int resource){
		this.context = context;
		this.listContainer = LayoutInflater.from(context);
		this.itemViewResource = resource;
		this.listItems = data;
	}
	
	@Override
	public int getCount() {
		return listItems.size();
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
			convertView = listContainer.inflate(this.itemViewResource, null);
			listItemView = new ViewHolder();
			listItemView.content = (TextView)convertView.findViewById(R.id.item_school_name);
			convertView.setTag(listItemView);
		}else {
			listItemView = (ViewHolder)convertView.getTag();
		}
		listItemView.content.setText(listItems.get(position).getSchoolName());
		return convertView;
	}

}

	