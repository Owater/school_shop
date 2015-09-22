/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.rockerhieu.emojicon.EmojiconTextView;
import com.siso.app.adapter.SchoolListAdapter.ViewHolder;
import com.siso.app.entity.GoodsCommentEntity;
import com.siso.app.ui.MyApplication;
import com.siso.app.ui.R;
import com.siso.app.utils.StringUtil;
import com.siso.app.widget.CircleImageView;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.util.Log;
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
 * @createtime : 2015-4-20 下午5:06:00
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-4-20 下午5:06:00 
 *
 */
public class ReplyCommListAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater listContainer;
	private int itemViewResource;
	private List<GoodsCommentEntity> list;
	private boolean isMe;
	static class ViewHolder{
		public TextView userNameTextView;
		public TextView timeTextView;
        public EmojiconTextView comment;
        public TextView themeTextView;
        public CircleImageView avatar;
	}
	
	public ReplyCommListAdapter(Context context,int itemViewResource, List<GoodsCommentEntity> list,boolean isMe) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context);
		this.itemViewResource = itemViewResource;
		this.list = list;
		this.isMe = isMe;
	}
	
	@Override
	public int getCount() {
		return list.size();
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
			convertView = listContainer.inflate(this.itemViewResource, null);
			viewHolder = new ViewHolder();
			viewHolder.userNameTextView = (TextView)convertView.findViewById(R.id.item_reply_comment_name);
			viewHolder.timeTextView = (TextView)convertView.findViewById(R.id.item_reply_comment_time);
			viewHolder.comment = (EmojiconTextView)convertView.findViewById(R.id.item_reply_comment_comment);
			viewHolder.themeTextView = (TextView)convertView.findViewById(R.id.item_reply_comment_theme);
			viewHolder.avatar = (CircleImageView)convertView.findViewById(R.id.item_reply_comment_avatar);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		Date date = list.get(position).getCreate_time();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if(date!=null)viewHolder.timeTextView.setText(sdf.format(date));
		viewHolder.userNameTextView.setText(list.get(position).getUserName());
		viewHolder.comment.setText(StringUtil.unicode2String(list.get(position).getComment()));
		viewHolder.themeTextView.setText("评论主题内容: "+list.get(position).getGoodTitle());
		
		String avatarUrl;
		if(isMe) avatarUrl = MyApplication.userInfo[5];else avatarUrl=list.get(position).getAvatarUrl();
		Picasso picasso = Picasso.with(viewHolder.avatar.getContext());
        picasso.load(avatarUrl).placeholder(R.drawable.ic_avatar).into(viewHolder.avatar);
		return convertView;
	}

}

	