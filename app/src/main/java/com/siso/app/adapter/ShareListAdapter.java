/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.adapter;

import java.util.List;

import com.siso.app.adapter.FindListAdapter.ViewHolder;
import com.siso.app.entity.ShareEntity;
import com.siso.app.ui.R;
import com.siso.app.widget.CircleImageView;
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
 * @createtime : 2015-5-4 下午5:37:58
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-5-4 下午5:37:58 
 *
 */
public class ShareListAdapter extends RecyclerView.Adapter<ShareListAdapter.ViewHolder> implements OnClickListener {
	
	private final LayoutInflater inflater;
	private Context context;
	private OnItemClickListener listener;
	private List<ShareEntity> list;
	
	public ShareListAdapter(Context context,List<ShareEntity> list,OnItemClickListener listener){
		this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.listener = listener;
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
		public CircleImageView avatar;
		public TextView userName;
		public TextView time;
		public ImageView imageView;
		public TextView desp;
		public ViewHolder(View itemView) {
			super(itemView);
			this.imageView = (ImageView)itemView.findViewById(R.id.item_find_share_img);
			this.avatar = (CircleImageView)itemView.findViewById(R.id.item_find_share_avatar);
			this.userName = (TextView)itemView.findViewById(R.id.item_find_share_uname);
			this.desp = (TextView)itemView.findViewById(R.id.item_find_share_desp);
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
		ShareEntity tmpEntity = list.get(position);
		holder.desp.setText(tmpEntity.getShareReason());
		String imgUrl[] = tmpEntity.getImgUrl().split(";");
		holder.userName.setText(tmpEntity.getUserName());
		
		Picasso.with(holder.imageView.getContext()).load(imgUrl[0]).placeholder(R.drawable.ic_img_loading).into(holder.imageView);
		Picasso.with(holder.avatar.getContext()).load(tmpEntity.getAvatarUrl()).placeholder(R.drawable.ic_avatar).into(holder.avatar);
        holder.itemView.setTag(tmpEntity);
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.item_share_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
	}

	@Override
	public void onClick(View v) {
		if(listener!=null){
			listener.onItemClick(v, (ShareEntity)v.getTag());
		}
	}

}

	