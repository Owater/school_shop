/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.adapter;

import java.util.ArrayList;

import com.siso.app.entity.ImageInfo;
import com.siso.app.ui.R;
import com.siso.app.ui.photopick.PhotoPickActivity;
import com.siso.app.widget.CameraPreview;
import com.siso.app.widget.crop.CropHelper;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-4-28 下午9:42:53
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-4-28 下午9:42:53 
 *
 */
public class PickImgGridAdapter extends BaseAdapter {
	
	private Context context;//运行上下文
	private LayoutInflater inflater;
	private ArrayList<ImageInfo> mData;
	private final int TYPE_CAMERA = 0;
    private final int TYPE_PHOTO = 1;
    
    static class ViewHolder {
        ImageView imgView;
    }
    
    static class GridCameraHolder {
        CameraPreview cameraPreview;
    }
	
	public PickImgGridAdapter(Context context) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}
	
	public void setData(ArrayList<ImageInfo> data) {
        mData = data;
    }
	
	public ArrayList<ImageInfo> getData() {
        return mData;
    }

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
    public int getViewTypeCount() {
        return 2;
    }
	
	@Override
    public int getItemViewType(int position) {
        ImageInfo imageInfo = (ImageInfo) getItem(position);
        if (imageInfo.path.equals(PhotoPickActivity.CameraItem)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PHOTO;
        }
    }

	long lastTime;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int type = getItemViewType(position);
		int width = context.getResources().getDisplayMetrics().widthPixels / 3;
		if (type == TYPE_PHOTO) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_photopick_gridlist,parent, false);
				holder = new ViewHolder();
				holder.imgView = (ImageView)convertView.findViewById(R.id.item_photo_img);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			Picasso picasso = Picasso.with(holder.imgView.getContext());
	        picasso.load(mData.get(position).path).resize(width, width).placeholder(R.drawable.ic_img_loading).into(holder.imgView);
			
		} else {
            final GridCameraHolder cameraHolder;
            if (convertView == null) {

                lastTime = System.currentTimeMillis();

                convertView = inflater.inflate(R.layout.photopick_gridlist_item_camera, parent, false);

                ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
                layoutParams.height = width;
                layoutParams.width = width;

                cameraHolder = new GridCameraHolder();
//                cameraHolder.cameraPreview = (CameraPreview) convertView.findViewById(R.id.cameraPreview);
//                cameraHolder.cameraPreview.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        cameraHolder.cameraPreview.stopAndReleaseCamera();
//                        camera();
//                    }
//                });
            }
        }
//		try {
//			holder.imgView.setImageBitmap(Bimp.revitionImageSize(mData.get(position).path));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return convertView;
	}
	
	public void camera() {
        Intent intent = CropHelper.buildCaptureIntent(PhotoPickActivity.mCropParams.uri);
        ((PhotoPickActivity)context).startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
    }

}

	