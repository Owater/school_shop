/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.adapter;

import java.util.List;
import com.siso.app.entity.DrawerList;
import com.siso.app.ui.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-3-13 下午9:24:37
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-3-13 下午9:24:37 
 *
 */
public class NavigationDrawerAdapter extends BaseAdapter {

	private Context context;//运行上下文
	private LayoutInflater listContainer;//视图容器
	private int itemViewResource;//自定义项视图源
	private List<DrawerList> listItems;//数据集合
	static class ListItemView{				//自定义控件集合  
        public TextView content;
        public ImageView iconImageView;
	}
	
	public NavigationDrawerAdapter(Context context, List<DrawerList> data,int resource){
		this.context = context;
		this.listContainer = LayoutInflater.from(context);	//创建视图容器并设置上下文
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
		//自定义视图
		ListItemView listItemView = null;
		if(convertView == null){
			//获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);
			listItemView = new ListItemView();
			//获取控件对象
			listItemView.content = (TextView)convertView.findViewById(R.id.drawer_list_text);
			listItemView.iconImageView = (ImageView)convertView.findViewById(R.id.drawer_list_icon);
			//设置控件集到convertView
			convertView.setTag(listItemView);
		}else {
			listItemView = (ListItemView)convertView.getTag();
		}
		listItemView.content.setText(listItems.get(position).getText());
		listItemView.iconImageView.setImageDrawable(convertView.getResources().getDrawable(listItems.get(position).getImgId()));
		return convertView;
	}
	
}

	