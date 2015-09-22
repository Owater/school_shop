/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.adapter;

import java.util.ArrayList;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.siso.app.ui.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-3-30 下午5:04:04
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-3-30 下午5:04:04
 *
 */
public class BannerAdapter extends PagerAdapter {
	
	private ArrayList<View> viewlist;
	private String imgUrl[];
	private Context context;
	
	public BannerAdapter(Context context,ArrayList<View> viewlist,String imgUrl[]){
		this.context = context;
		this.viewlist = viewlist;
		this.imgUrl = imgUrl;
	}

	@Override
	public int getCount() {
		return imgUrl.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		final View view = viewlist.get(position);
		final ImageView imageView = (ImageView)view.findViewById(R.id.item_banner_img);
//		final LinearLayout loading = (LinearLayout)view.findViewById(R.id.item_banner_loading);
		Log.i("tag", "imgUrl[position]=="+imgUrl[position]);
		if(imageView.getDrawable()==null){
			Picasso picasso = Picasso.with(imageView.getContext());
			picasso.load(imgUrl[position]).into(imageView);
//			Log.i("image", "position=="+position+"图片为空"+imgUrl[position]);
//			AsyncHttpClient client=new AsyncHttpClient();
//			client.get(imgUrl[position], new AsyncHttpResponseHandler() {
//				
//				@Override
//				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//					if(statusCode==200){
//						BitmapFactory factory=new BitmapFactory();
//						Bitmap bitmap=factory.decodeByteArray(responseBody, 0, responseBody.length);
//						imageView.setImageBitmap(bitmap);
//						loading.setVisibility(View.GONE);
//					}
//				}
//				
//				@Override
//				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//					loading.setVisibility(View.GONE);
//					Toast.makeText(context, context.getResources().getString(R.string.timeoutError), Toast.LENGTH_SHORT);
//				}
//			});
		}
		
		ViewParent vp = view.getParent();
		if (vp!=null) {
			ViewGroup parent = (ViewGroup) vp;
			parent.removeView(view);
		}
		container.addView(view);
		return view;
	}
	
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewGroup) container).removeView(viewlist.get(position));
	}

}

	