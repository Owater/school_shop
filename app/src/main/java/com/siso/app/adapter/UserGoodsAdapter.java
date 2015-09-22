/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.adapter;

import java.util.List;

import com.siso.app.entity.FindListJsonEntity;
import com.siso.app.entity.GoodsEntity;
import com.siso.app.ui.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @param <T>
 * @createtime : 2015-5-9 下午12:49:09
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-5-9 下午12:49:09 
 *
 */
public class UserGoodsAdapter extends RecyclerView.Adapter<UserGoodsAdapter.ViewHolder> implements OnClickListener,OnLongClickListener {
	
	private final LayoutInflater inflater;
	private List<GoodsEntity> list;
	private Context context;
	private OnItemClickListener listener;
	private boolean isMe;
	
	public UserGoodsAdapter(Context context,List<GoodsEntity> list,boolean isMe){
		this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.isMe = isMe;
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
        public void onItemClick(View view,GoodsEntity goodsEntity);
        public void onItemLongClick(View view,GoodsEntity goodsEntity);
    }
	
	@Override
	public int getItemCount() {
		return list.size();
	}
	
	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		GoodsEntity goodsEntity = list.get(position);
		
		holder.price.setText("￥"+goodsEntity.getGoodPrice());
		holder.title.setText(goodsEntity.getGoodName());
		holder.desp.setText(goodsEntity.getGoodDescribe());
		
		if(goodsEntity.getGoodImages()!=null){
			String imgUrls[] = goodsEntity.getGoodImages().split(";");
			Log.i("tag", "imgUrls[0]===="+imgUrls[0]);
			Picasso picasso = Picasso.with(holder.imageView.getContext());
	        picasso.load(imgUrls[0]).placeholder(R.drawable.ic_img_loading).into(holder.imageView);
		}
		holder.itemView.setTag(goodsEntity);
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.item_user_pub_goods,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        if(isMe)
        	view.setOnLongClickListener(this);
        return viewHolder;
	}
	
	public void setOnItemClickListener(OnItemClickListener onItemClickListener){
		if(onItemClickListener!=null){
			this.listener = onItemClickListener;
		}
	}

	@Override
	public void onClick(View view) {
		if (listener!=null) {
			listener.onItemClick(view, (GoodsEntity)view.getTag());
		}
	}

	@Override
	public boolean onLongClick(View view) {
		if (listener!=null) {
			listener.onItemLongClick(view, (GoodsEntity)view.getTag());
		}
		return true;
	}
	
}

	