/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.adapter;

import java.util.List;

import com.siso.app.ui.R;
import com.siso.app.utils.AsynImageLoader;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-4-28 上午1:20:29
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-4-28 上午1:20:29 
 *
 */
public class ShareImgListAdapter extends RecyclerView.Adapter<ShareImgListAdapter.ViewHolder> {
	
	private final LayoutInflater inflater;
	private List<String> list;
	
	public ShareImgListAdapter(Context context,List<String> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
		public ImageView imageView;
		public ViewHolder(View itemView) {
			super(itemView);
			this.imageView = (ImageView)itemView.findViewById(R.id.item_share_pub_img);
		}
	}

	@Override
	public int getItemCount() {
//		Log.i("tag", "size=="+list.size());
		return list.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
//		AsynImageLoader asynImageLoader = AsynImageLoader.getInstance();
//		asynImageLoader.loadDrawableFromNet(holder.imageView, list.get(position));
		/**
		 * Picasso加载有问题，重复加载两次之后，图片就不能正常显示了，所以改用其它方法
		 */
		Picasso.with(holder.imageView.getContext()).load(list.get(position)).placeholder(R.drawable.ic_img_loading).into(holder.imageView);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share_pub_img_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
	}

}

	