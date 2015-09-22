/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.siso.app.entity.ShareEntity;
import com.siso.app.ui.R;
import com.siso.app.ui.WebViewActivity;
import com.siso.app.utils.AsynImageLoader;
import com.siso.app.widget.CircleImageView;
import com.siso.app.widget.viewpagerindicator.CirclePageIndicator;
import com.siso.app.widget.viewpagerindicator.PageIndicator;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-5-4 下午8:14:06
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-5-4 下午8:14:06 
 *
 */
public class ShareDetailAdapter extends RecyclerView.Adapter<ShareDetailAdapter.ViewHolder> implements OnClickListener{
	
	private Context context;
	private ShareEntity shareEntity;
	private final LayoutInflater inflater;
	private OnItemClickListener listener;
	private final int VIEW_TYPE_BANNER = 0;
	private final int VIEW_TYPE_DESP = 1;
	
	public ShareDetailAdapter(Context context, ShareEntity shareEntity,OnItemClickListener listener) {
		this.context = context;
		this.shareEntity = shareEntity;
		this.inflater = LayoutInflater.from(context);
		this.listener = listener;
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
		public CircleImageView avatar;
		public TextView userName;
		public TextView time;
		public TextView desp;
		public TextView goodsName;
		public TextView goodsPrice;
		public ViewPager viewPager;
		public PageIndicator mIndicator;
		private View itemView;
		public Button button;

		public ViewHolder(View itemView) {
			super(itemView);
			this.itemView=itemView;
			this.avatar = (CircleImageView) itemView.findViewById(R.id.item_share_detail_avatar);
			this.userName = (TextView) itemView.findViewById(R.id.item_share_detail_uname);
			this.desp = (TextView) itemView.findViewById(R.id.item_share_detail_desp);
			this.viewPager = (ViewPager)itemView.findViewById(R.id.item_goods_detail_viewpager);
			this.mIndicator = (CirclePageIndicator) itemView.findViewById(R.id.indicator);
			this.goodsName = (TextView)itemView.findViewById(R.id.item_share_detail_title);
			this.goodsPrice = (TextView)itemView.findViewById(R.id.item_share_detail_price);
			this.time = (TextView)itemView.findViewById(R.id.item_share_detail_time);
			this.button = (Button)itemView.findViewById(R.id.item_share_detail_go);
		}
	}
	
	public static interface OnItemClickListener {
        public void onItemClick(View view,ShareEntity shareEntity);
    }
	
	@Override
	public int getItemCount() {
		return 2;
	}
	
	@Override
	public int getItemViewType(int position) {
		return position == 0 ? VIEW_TYPE_BANNER : VIEW_TYPE_DESP;
	}
	
	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
//		Picasso picasso = Picasso.with(holder.imageView.getContext());
//        picasso.load(shareEntity.getImgUrl()).placeholder(R.drawable.ic_img_loading).into(holder.imageView);
		switch (getItemViewType(position)) {
		case VIEW_TYPE_BANNER:
			initBanner(holder);
			break;
		case VIEW_TYPE_DESP:
			holder.userName.setText(shareEntity.getUserName());
			holder.desp.setText(shareEntity.getShareReason());
			holder.goodsName.setText(shareEntity.getTitle());
			holder.goodsPrice.setText(shareEntity.getGoodsPrice()+"");
			Date date = shareEntity.getCreate_time();
			SimpleDateFormat sdf=new SimpleDateFormat("MM-dd-yyyy");
			if(date!=null) holder.time.setText(sdf.format(date));
			Picasso picasso = Picasso.with(holder.avatar.getContext());
	        picasso.load(shareEntity.getAvatarUrl()).placeholder(R.drawable.ic_img_loading).into(holder.avatar);
	        holder.button.setOnClickListener(this);
			break;
		default:
			break;
		}
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
		case VIEW_TYPE_BANNER:
			return new ViewHolder(inflater.inflate(R.layout.item_goods_detail_banner,parent,false));
		case VIEW_TYPE_DESP:
			View view = inflater.inflate(R.layout.item_share_detail,parent,false);
	        ViewHolder viewHolder = new ViewHolder(view);
	        view.setOnClickListener(this);
	        return viewHolder;
		default:
			return null;
		}
	}
	
	@Override
	public void onClick(View v) {
		if(listener!=null){
			listener.onItemClick(v, (ShareEntity)v.getTag());
		}
		if(v.getId()==R.id.item_share_detail_go){
			Intent intent = new Intent(context,WebViewActivity.class);
			intent.putExtra("url", shareEntity.getGoodsUrl());
			context.startActivity(intent);
		}
	}
	
	private void initBanner(ViewHolder viewHolder) {
		ArrayList<View> viewlist = new ArrayList<View>();
		
		viewHolder.viewPager = (ViewPager)viewHolder.itemView.findViewById(R.id.item_goods_detail_viewpager);
		viewHolder.mIndicator = (CirclePageIndicator) viewHolder.itemView.findViewById(R.id.indicator);
		
		String imgUrl[] = shareEntity.getImgUrl().split(";");
		for (int i = 0; i < imgUrl.length; i++) {
			View view = viewHolder.itemView.inflate(context, R.layout.item_banner_view, null);
			viewlist.add(view);
		}
		BannerAdapter bannerAdapter = new BannerAdapter(context,viewlist,imgUrl);
		viewHolder.viewPager.setAdapter(bannerAdapter);
		viewHolder.mIndicator.setViewPager(viewHolder.viewPager);
	}

}

	