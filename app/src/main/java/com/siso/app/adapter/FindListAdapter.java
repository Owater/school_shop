/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.adapter;

import java.util.List;

import com.siso.app.entity.FindListJsonEntity;
import com.siso.app.entity.Position;
import com.siso.app.entity.TagInfo;
import com.siso.app.ui.MyApplication;
import com.siso.app.ui.R;
import com.siso.app.widget.CircleImageView;
import com.siso.app.widget.tag.TagView;
import com.siso.app.widget.tag.TagViewLeft;
import com.siso.app.widget.tag.TagViewRight;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-5-3 下午1:46:40
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-5-3 下午1:46:40 
 *
 */
public class FindListAdapter extends RecyclerView.Adapter<FindListAdapter.ViewHolder> implements OnClickListener{
	
	private final LayoutInflater inflater;
	private List<FindListJsonEntity> list;
	private Context context;
	private int screenWidth;
	private int screenHeight;
	private OnItemClickListener listener;
	
	public FindListAdapter(Context context,List<FindListJsonEntity> list,OnItemClickListener listener) {
		this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.screenWidth = MyApplication.screenWidth;
        this.screenHeight = MyApplication.screenHeight;
        this.listener = listener;
    }

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public CircleImageView avatar;
		public TextView userName;
		public TextView time;
		public ImageView imageView;
		public TextView desp;
		public RelativeLayout addTagContainer;
		public ViewHolder(View itemView) {
			super(itemView);
			this.avatar = (CircleImageView)itemView.findViewById(R.id.item_find_list_avatar);
			this.userName = (TextView)itemView.findViewById(R.id.item_find_list_uname);
			this.imageView = (ImageView)itemView.findViewById(R.id.item_find_list_img);
			this.desp = (TextView)itemView.findViewById(R.id.item_find_list_desp);
			this.addTagContainer = (RelativeLayout)itemView.findViewById(R.id.find_list_addTagContainer);
		}
	}
	
	public static interface OnItemClickListener {
        public void onItemClick(View view,FindListJsonEntity findJsonEntity);
    }
	
	@Override
	public int getItemCount() {
		return list.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		FindListJsonEntity tmpEntity = list.get(position);
		holder.desp.setText(tmpEntity.getDesp());
		holder.userName.setText(tmpEntity.getUserName());
		List<TagInfo> tagInfoList = tmpEntity.getFindLabelEntities();
		
		//添加标签
		int orientation = 0;
		/**
		 * 这里需要把之前的布局移除removeAllViews，再进行添加，因为onBindViewHolder会被销毁
		 * 然后又被重新加载，但是动态添加的布局却没有被销毁，也许我这种处理方法不够好，可能是个bug，需要改进
		 */
		holder.addTagContainer.removeAllViews();
		for (int i = 0; i < tagInfoList.size(); i++) {
			TagInfo tmpTagInfo = tagInfoList.get(i);
			orientation = tagInfoList.get(i).getOrientation();
			TagView tagView = null;
			Position pos = new Position();
			pos.setXplace((int)(screenWidth*tmpTagInfo.getLabelx().floatValue()));
			pos.setYplace((int)(screenHeight*tmpTagInfo.getLabely().floatValue()));
//			Log.i("tag", "x=="+(int)tmpTagInfo.getLabelx().floatValue()+",,y=="+(int)tmpTagInfo.getLabelx().floatValue()+",,tmpTagInfo.getLabelx()="+tmpTagInfo.getLabelx()+",screenWidth="+screenWidth);
			if(orientation==0){
				tagView = new TagViewLeft(context, null, screenWidth, screenHeight, pos, tmpTagInfo,false);
			}else {
				tagView = new TagViewRight(context, null, screenWidth, screenHeight, pos, tmpTagInfo,false);
			}
			tagView.tagInfo = tagInfoList.get(i);
			holder.addTagContainer.addView(tagView);
		}
		holder.itemView.setTag(tmpEntity);
		
		Picasso picasso = Picasso.with(holder.imageView.getContext());
        picasso.load(list.get(position).getImgUrl()).placeholder(R.drawable.ic_img_loading).into(holder.imageView);
        
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.item_find_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
	}

	@Override
	public void onClick(View v) {
		if(listener!=null){
			listener.onItemClick(v, (FindListJsonEntity)v.getTag());
		}
	}
}

	