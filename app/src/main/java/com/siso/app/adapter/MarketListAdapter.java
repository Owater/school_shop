/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.adapter;

import java.util.List;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.siso.app.common.URLs;
import com.siso.app.entity.GoodsEntity;
import com.siso.app.ui.MyApplication;
import com.siso.app.ui.R;
import com.siso.app.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
public class MarketListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater listContainer;
	private int itemViewResource;
	private List<GoodsEntity> listItems;
	private int type;
	static class ViewHolder{
		public TextView priceTextView;
        public TextView content;
        public TextView username;
        public ImageView imageView;
        public LinearLayout linearLayout;
        public TextView schoolname;
        public TextView distance;
        public LinearLayout likeButton;
        public TextView likeCount;
        public TextView viewCount;
        public ImageButton likeImageButton;
	}
	
	public MarketListAdapter(Context context, List<GoodsEntity> data,int resource,int type){
		this.context = context;
		this.listContainer = LayoutInflater.from(context);
		this.itemViewResource = resource;
		this.listItems = data;
		this.type = type;
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
			convertView = listContainer.inflate(this.itemViewResource, null,false);
			listItemView = new ViewHolder();
			listItemView.content = (TextView)convertView.findViewById(R.id.marketList_title);
			listItemView.priceTextView = (TextView)convertView.findViewById(R.id.marketList_price);
			listItemView.username = (TextView)convertView.findViewById(R.id.marketList_username);
			listItemView.imageView = (ImageView)convertView.findViewById(R.id.marketList_img);
			listItemView.linearLayout = (LinearLayout)convertView.findViewById(R.id.nearby);
			listItemView.likeButton = (LinearLayout)convertView.findViewById(R.id.item_goods_like);
			listItemView.likeCount = (TextView)convertView.findViewById(R.id.item_goods_like_text);
			listItemView.viewCount = (TextView)convertView.findViewById(R.id.item_goods_viewcount);
			listItemView.likeImageButton = (ImageButton)convertView.findViewById(R.id.item_goods_like_btn);
			if(type==1){
				listItemView.username.setVisibility(View.GONE);
				listItemView.linearLayout.setVisibility(View.VISIBLE);
				listItemView.schoolname = (TextView)convertView.findViewById(R.id.marketList_schoolname);
				listItemView.distance = (TextView)convertView.findViewById(R.id.marketList_distance);
			}
			convertView.setTag(listItemView);
		}else {
			listItemView = (ViewHolder)convertView.getTag();
		}
		
		final GoodsEntity tmpGoodsEntity = listItems.get(position);
		listItemView.content.setText(tmpGoodsEntity.getGoodTitle());
		listItemView.priceTextView.setText("￥"+tmpGoodsEntity.getGoodPrice());
		listItemView.username.setText(tmpGoodsEntity.getUserName());
		if(type==1){
			listItemView.schoolname.setText("来自["+tmpGoodsEntity.getSchoolName()+"]");
			listItemView.distance.setText(tmpGoodsEntity.getDistance()+"Km");
		}
		if(tmpGoodsEntity.isLike()){
			listItemView.likeImageButton.setBackgroundResource(R.drawable.liked);
		}
		listItemView.likeCount.setText(tmpGoodsEntity.getLikeCount()+"");
		listItemView.viewCount.setText(tmpGoodsEntity.getViewCount() + "");
		
		listItemView.likeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				if (MyApplication.userInfo == null) {
					Toast.makeText(context, context.getResources().getString(R.string.has_no_login), Toast.LENGTH_SHORT).show();
					return;
				}

				ImageButton imageButton = (ImageButton) view.findViewById(R.id.item_goods_like_btn);
				TextView likeCount = (TextView) view.findViewById(R.id.item_goods_like_text);
				if (tmpGoodsEntity.isLike()) {
					postData(tmpGoodsEntity.getId(), "delete");
					tmpGoodsEntity.setLike(false);
					imageButton.setBackgroundResource(R.drawable.like);
					String textString = likeCount.getText().toString();
					if (textString != null) {
						likeCount.setText(Integer.parseInt(textString) - 1 + "");
					}
				} else {
					postData(tmpGoodsEntity.getId(), "add");
					tmpGoodsEntity.setLike(true);
					imageButton.setBackgroundResource(R.drawable.liked);
					String textString = likeCount.getText().toString();
					if (textString != null) {
						likeCount.setText(Integer.parseInt(textString) + 1 + "");
					}
				}

			}
		});

		if (tmpGoodsEntity.getGoodImages()!=null){
			Picasso picasso = Picasso.with(listItemView.imageView.getContext());
			picasso.load(CommonUtils.getFirstImg(tmpGoodsEntity.getGoodImages())).placeholder(R.drawable.ic_img_loading).into(listItemView.imageView);
		}else {
			listItemView.imageView.setBackgroundResource(R.drawable.ic_img_loading);
		}

		return convertView;
	}
	
	private void postData(int goodId,String type){
		AsyncHttpClient client =new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("userId", MyApplication.userInfo[1]);
		params.add("authCode", MyApplication.userInfo[0]);
		params.add("type", type);
		params.add("goodId", goodId+"");
		client.get(URLs.GOODS_LIKE, params , new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Toast.makeText(context, context.getResources().getString(R.string.timeoutError), Toast.LENGTH_SHORT).show();
			}
		});
	}
	
}

	