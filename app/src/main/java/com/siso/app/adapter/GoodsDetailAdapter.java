/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.rockerhieu.emojicon.EmojiconTextView;
import com.siso.app.chat.ui.ChatActivity;
import com.siso.app.entity.GoodsCommentEntity;
import com.siso.app.entity.GoodsEntity;
import com.siso.app.ui.MyApplication;
import com.siso.app.ui.R;
import com.siso.app.ui.UserDetailActivity;
import com.siso.app.utils.CommonUtils;
import com.siso.app.utils.StringUtil;
import com.siso.app.widget.CircleImageView;
import com.siso.app.widget.viewpagerindicator.CirclePageIndicator;
import com.siso.app.widget.viewpagerindicator.PageIndicator;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-3-30 下午2:16:24
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-3-30 下午2:16:24 
 *
 */
public class GoodsDetailAdapter extends BaseAdapter implements OnClickListener {
	
	private Context context;
	private LayoutInflater listContainer;
	private GoodsEntity entity;
	private List<GoodsCommentEntity> list;
	class ViewHolder{
		public TextView titleTextView;
		public TextView nameTextView;
		public TextView priceTextView;
		public TextView timeTextView;
		public TextView despTextView;
		public CircleImageView avatar;
        public EmojiconTextView content;
        public ImageButton chat;
        public ImageButton call;
        public ViewPager viewPager;
        public PageIndicator mIndicator;
	}
	
	public GoodsDetailAdapter(Context context, GoodsEntity data,List<GoodsCommentEntity> list){
		this.context = context;
		this.listContainer = LayoutInflater.from(context);
		this.entity = data;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size()+2;
	}
	
	@Override
	public int getViewTypeCount() {
		return 3;
	}
	
	@Override
	public int getItemViewType(int position) {
		if (position==0) {
			return 0;
		}else if (position==1) {
			return 1;
		}else {
			return 2;
		}
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
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			switch (position) {
			case 0:
				convertView = listContainer.inflate(R.layout.item_goods_detail_banner, null);
				initBanner(convertView,viewHolder);
				break;
			case 1:
				convertView = listContainer.inflate(R.layout.item_goods_detail_desp, null);
				viewHolder.titleTextView = (TextView)convertView.findViewById(R.id.item_goods_detail_desp_title);
				viewHolder.priceTextView = (TextView)convertView.findViewById(R.id.item_goods_detail_desp_price);
				viewHolder.despTextView = (TextView)convertView.findViewById(R.id.item_goods_detail_desp);
				viewHolder.nameTextView = (TextView)convertView.findViewById(R.id.item_goods_detail_username);
				viewHolder.timeTextView = (TextView)convertView.findViewById(R.id.item_goods_detail_time);
				viewHolder.chat = (ImageButton)convertView.findViewById(R.id.item_goods_detail_chat);
				viewHolder.call = (ImageButton)convertView.findViewById(R.id.item_goods_detail_call);
				viewHolder.avatar = (CircleImageView)convertView.findViewById(R.id.item_goods_desp_avatar);
				break;
			default:
				convertView = listContainer.inflate(R.layout.item_goods_detail_comment, null);
				viewHolder.nameTextView = (TextView)convertView.findViewById(R.id.item_goods_detail_name);
				viewHolder.content = (EmojiconTextView)convertView.findViewById(R.id.item_goods_detail_comm);
				viewHolder.timeTextView = (TextView)convertView.findViewById(R.id.item_goods_detail_time);
				viewHolder.avatar = (CircleImageView)convertView.findViewById(R.id.item_goods_detail_img);
				break;
			}
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		if (position==1) {
			Date date = entity.getCreate_time();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
			viewHolder.titleTextView.setText(entity.getGoodName());
			viewHolder.priceTextView.setText("￥ "+entity.getGoodPrice());
			viewHolder.despTextView.setText(entity.getGoodDescribe());
			viewHolder.nameTextView.setText(entity.getUserName());
			if(date!=null)viewHolder.timeTextView.setText(sdf.format(date));
			
			Picasso picasso = Picasso.with(viewHolder.avatar.getContext());
	        picasso.load(entity.getAvatarUrl()).placeholder(R.drawable.ic_avatar).into(viewHolder.avatar);
	        
			viewHolder.chat.setOnClickListener(this);
			viewHolder.call.setOnClickListener(this);
			viewHolder.avatar.setOnClickListener(this);
			viewHolder.nameTextView.setOnClickListener(this);
		}else {
			if (viewHolder.content!=null) {
				final GoodsCommentEntity tmpCommentEntity = list.get(position-2);
				Date date = tmpCommentEntity.getCreate_time();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(date!=null)viewHolder.timeTextView.setText(sdf.format(date));
				viewHolder.content.setText(StringUtil.unicode2String(tmpCommentEntity.getComment()));
				viewHolder.nameTextView.setText(tmpCommentEntity.getUserName());
				
				Picasso picasso = Picasso.with(viewHolder.avatar.getContext());
		        picasso.load(tmpCommentEntity.getAvatarUrl()).placeholder(R.drawable.ic_avatar).into(viewHolder.avatar);
				viewHolder.avatar.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						startUserDetailActivity(tmpCommentEntity.getUserId());
					}
				});
				viewHolder.nameTextView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						startUserDetailActivity(tmpCommentEntity.getUserId());
					}
				});
			}
		}
		return convertView;
	}
	
	private void chat(){
		Intent intent = new Intent(context,ChatActivity.class);
		intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
		intent.putExtra("userId", entity.getHxId());
		intent.putExtra("userName", entity.getUserName());
		intent.putExtra("buserAvatar", entity.getAvatarUrl());
		context.startActivity(intent);
	}

	/**
	 * 
	 * @author Owater
	 * @createtime 2015-3-30 下午2:44:30
	 * @Decription 初始化图片轮播
	 *
	 */
	private void initBanner(View convertView,ViewHolder viewHolder) {
		
		ArrayList<View> viewlist = new ArrayList<View>();
		viewHolder.viewPager = (ViewPager) convertView.findViewById(R.id.item_goods_detail_viewpager);
		viewHolder.mIndicator = (CirclePageIndicator) convertView.findViewById(R.id.indicator);

		if (entity.getGoodImages()!=null){
			String imgUrl[] = CommonUtils.getImgArr(entity.getGoodImages());
			for (int i = 0; i < imgUrl.length; i++) {
				View view = convertView.inflate(context, R.layout.item_banner_view, null);
				viewlist.add(view);
			}
			BannerAdapter bannerAdapter = new BannerAdapter(context,viewlist,imgUrl);
			viewHolder.viewPager.setAdapter(bannerAdapter);
			viewHolder.mIndicator.setViewPager(viewHolder.viewPager);
		}

//		listItemView.content = (TextView)convertView.findViewById(R.id.item_goods_detail_viewpager_text);
//		listItemView.content.setText("viewpager");
//		convertView.setTag(listItemView);
	}
	
	private void startUserDetailActivity(){
		if (MyApplication.userInfo==null) {
			Toast.makeText(context, context.getString(R.string.has_no_login), Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(context,UserDetailActivity.class);
		intent.putExtra("userId", entity.getUserId());
		context.startActivity(intent);
	}
	
	private void startUserDetailActivity(String userId){
		if (MyApplication.userInfo==null) {
			Toast.makeText(context, context.getString(R.string.has_no_login), Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(context,UserDetailActivity.class);
		intent.putExtra("userId", userId);
		context.startActivity(intent);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.item_goods_detail_call:
			if(entity.getContactPhone()!=null){
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DIAL);
				Uri uri = Uri.parse("tel:"+entity.getContactPhone());
				intent.setData(uri);
				context.startActivity(intent);
			}else {
				Toast.makeText(context, context.getResources().getString(R.string.tip_goodsdetail_contact_phone), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.item_goods_detail_chat:
			if (MyApplication.userInfo!=null) {
				chat();
			}else {
				Toast.makeText(context, context.getResources().getString(R.string.has_no_login), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.item_goods_detail_username:
			startUserDetailActivity();
			break;
		case R.id.item_goods_desp_avatar:
			startUserDetailActivity();
			break;
		default:
			break;
		}
	}
	
}

	