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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * description :
 * 
 * @version 1.0
 * @author Owater
 * @createtime : 2015-5-4 上午10:45:09
 * 
 *             修改历史: 修改人 修改时间 修改内容 --------------- -------------------
 *             ----------------------------------- Owater 2015-5-4 上午10:45:09
 * 
 */
public class FindDetailAdapter extends
		RecyclerView.Adapter<FindDetailAdapter.ViewHolder> {

	private Context context;
	private FindListJsonEntity findJsonEntity;
	private final LayoutInflater inflater;
	private int screenWidth;
	private int screenHeight;

	public FindDetailAdapter(Context context, FindListJsonEntity findJsonEntity) {
		this.context = context;
		this.findJsonEntity = findJsonEntity;
		this.inflater = LayoutInflater.from(context);
		this.screenWidth = MyApplication.screenWidth;
        this.screenHeight = MyApplication.screenHeight;
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
			this.avatar = (CircleImageView) itemView
					.findViewById(R.id.item_find_detail_avatar);
			this.userName = (TextView) itemView
					.findViewById(R.id.item_find_detail_uname);
			this.imageView = (ImageView) itemView
					.findViewById(R.id.item_find_detail_img);
			this.desp = (TextView) itemView
					.findViewById(R.id.item_find_detail_desp);
			this.addTagContainer = (RelativeLayout) itemView
					.findViewById(R.id.find_detail_addTagContainer);
		}
	}

	@Override
	public int getItemCount() {
		return 1;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.desp.setText(findJsonEntity.getDesp());
		holder.userName.setText(findJsonEntity.getUserName());
		List<TagInfo> tagInfoList = findJsonEntity.getFindLabelEntities();
		// 添加标签
		int orientation = 0;
		holder.addTagContainer.removeAllViews();
		for (int i = 0; i < tagInfoList.size(); i++) {
			TagInfo tmpTagInfo = tagInfoList.get(i);
			orientation = tagInfoList.get(i).getOrientation();
			TagView tagView = null;
			Position pos = new Position();
			pos.setXplace((int) (screenWidth * tmpTagInfo.getLabelx()
					.floatValue()));
			pos.setYplace((int) (screenHeight * tmpTagInfo.getLabely()
					.floatValue()));
			// Log.i("tag",
			// "x=="+(int)tmpTagInfo.getLabelx().floatValue()+",,y=="+(int)tmpTagInfo.getLabelx().floatValue()+",,tmpTagInfo.getLabelx()="+tmpTagInfo.getLabelx()+",screenWidth="+screenWidth);
			if (orientation == 0) {
				tagView = new TagViewLeft(context, null, screenWidth,
						screenHeight, pos, tmpTagInfo, false);
			} else {
				tagView = new TagViewRight(context, null, screenWidth,
						screenHeight, pos, tmpTagInfo, false);
			}
			tagView.tagInfo = tagInfoList.get(i);
			holder.addTagContainer.addView(tagView);
		}

		Picasso picasso = Picasso.with(holder.imageView.getContext());
		picasso.load(findJsonEntity.getImgUrl())
				.placeholder(R.drawable.ic_img_loading).into(holder.imageView);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.item_find_detail, parent, false);
		ViewHolder viewHolder = new ViewHolder(view);
		return viewHolder;
	}

}
