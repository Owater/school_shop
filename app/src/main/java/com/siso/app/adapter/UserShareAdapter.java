/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.adapter;

import java.util.List;

import com.siso.app.entity.ShareEntity;
import com.siso.app.ui.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-5-9 下午5:21:48
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-5-9 下午5:21:48 
 *
 */
public class UserShareAdapter extends RecyclerView.Adapter<UserShareAdapter.ViewHolder> implements OnClickListener {
	
	private final LayoutInflater inflater;
	private List<ShareEntity> list;
	private Context context;
	private OnItemClickListener listener;
	
	public UserShareAdapter(Context context,List<ShareEntity> list,OnItemClickListener listener){
		this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.listener = listener;
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
		public ImageView imageView;
		public TextView price;
		public TextView title;
		public TextView desp;
		public ViewHolder(View itemView) {
			super(itemView);
			this.price = (TextView)itemView.findViewById(R.id.item_user_pub_price);
			this.title = (TextView)itemView.findViewById(R.id.item_user_pub_title);
			this.desp = (TextView)itemView.findViewById(R.id.item_user_pub_desp);
			this.imageView = (ImageView)itemView.findViewById(R.id.item_user_pub_img);
		}
	}
	
	public static interface OnItemClickListener {
        public void onItemClick(View view,ShareEntity shareEntity);
    }
	
	@Override
	public int getItemCount() {
		return list.size();
	}
	
	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		ShareEntity shareEntity = list.get(position);
		
		holder.price.setText("￥"+shareEntity.getGoodsPrice());
		holder.title.setText(shareEntity.getTitle());
		holder.desp.setText(shareEntity.getShareReason());
		
		if(shareEntity.getImgUrl()!=null){
			String imgUrls[] = shareEntity.getImgUrl().split(";");
			Picasso picasso = Picasso.with(holder.imageView.getContext());
	        picasso.load(imgUrls[0]).placeholder(R.drawable.ic_img_loading).into(holder.imageView);
		}
		holder.itemView.setTag(shareEntity);
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.item_user_pub_goods,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
	}

	@Override
	public void onClick(View view) {
		if (listener!=null) {
			listener.onItemClick(view, (ShareEntity)view.getTag());
		}
	}

}

	