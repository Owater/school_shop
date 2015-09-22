package com.siso.app.adapter;

import java.util.ArrayList;
import java.util.List;

import com.siso.app.entity.ImageBucket;
import com.siso.app.entity.ImageItem;
import com.siso.app.ui.ImageFileActivity;
import com.siso.app.ui.R;
import com.siso.app.ui.ShowFilePhotoActivity;
import com.siso.app.utils.BitmapCache;
import com.siso.app.utils.BitmapCache.ImageCallback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FolderAdapter extends BaseAdapter {

	private Context mContext;
	private Intent mIntent;
	private DisplayMetrics dm;
	private List<ImageBucket> contentList;
	BitmapCache cache;
	final String TAG = getClass().getSimpleName();
	
	public FolderAdapter(Context c,List<ImageBucket> contentList) {
		cache = new BitmapCache();
		this.contentList = contentList;
		init(c);
	}

	// 初始化
	public void init(Context c) {
		mContext = c;
		mIntent = ((Activity) mContext).getIntent();
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
	}

	

	@Override
	public int getCount() {
		return contentList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	ImageCallback callback = new ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap,
				Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				if (url != null && url.equals((String) imageView.getTag())) {
					((ImageView) imageView).setImageBitmap(bitmap);
				} else {
					Log.e(TAG, "callback, bmp not match");
				}
			} else {
				Log.e(TAG, "callback, bmp null");
			}
		}
	};

	private class ViewHolder {
		//
		public ImageView backImage;
		// 封面
		public ImageView imageView;
		public ImageView choose_back;
		// 文件夹名称
		public TextView folderName;
		// 文件夹里面的图片数量
		public TextView fileNum;
	}
	ViewHolder holder = null;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_camera_select_folder, null);
			holder = new ViewHolder();
			holder.backImage = (ImageView) convertView.findViewById(R.id.folder_file_back);
			holder.imageView = (ImageView) convertView.findViewById(R.id.folder_file_image);
			holder.choose_back = (ImageView) convertView.findViewById(R.id.folder_choose_back);
			holder.folderName = (TextView) convertView.findViewById(R.id.folder_name);
			holder.fileNum = (TextView) convertView.findViewById(R.id.folder_filenum);
			holder.imageView.setAdjustViewBounds(true);
			holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		String path;
		if (contentList.get(position).imageList != null) {
			
			//path = photoAbsolutePathList.get(position);
			//封面图片路径
			path = contentList.get(position).imageList.get(0).imagePath;
			// 给folderName设置值为文件夹名称
			//holder.folderName.setText(fileNameList.get(position));
			holder.folderName.setText(contentList.get(position).bucketName);
			
			// 给fileNum设置文件夹内图片数量
			holder.fileNum.setText("" + contentList.get(position).count);
			
		} else
			path = "android_hybrid_camera_default";
		if (path.contains("android_hybrid_camera_default"))
			holder.imageView.setImageResource(R.drawable.camera_no_pictures);
		else {
//			holder.imageView.setImageBitmap( AlbumActivity.contentList.get(position).imageList.get(0).getBitmap());
			final ImageItem item = contentList.get(position).imageList.get(0);
			holder.imageView.setTag(item.imagePath);
			cache.displayBmp(holder.imageView, item.thumbnailPath, item.imagePath,callback);
		}
		// 为封面添加监听
//		holder.imageView.setOnClickListener(new ImageViewClickListener(position, mIntent,holder.choose_back));
		
		return convertView;
	}

	// 为每一个文件夹构建的监听器
	private class ImageViewClickListener implements OnClickListener {
		private int position;
		private Intent intent;
		private ImageView choose_back;
		public ImageViewClickListener(int position, Intent intent,ImageView choose_back) {
			this.position = position;
			this.intent = intent;
			this.choose_back = choose_back;
		}
		
		public void onClick(View v) {
			ShowFilePhotoActivity.dataList = (ArrayList<ImageItem>) ImageFileActivity.contentList.get(position).imageList;
			Intent intent = new Intent();
			String folderName = ImageFileActivity.contentList.get(position).bucketName;
			intent.putExtra("folderName", folderName);
			intent.setClass(mContext, ShowFilePhotoActivity.class);
//			mContext.startActivityForResult(intent, 100);
//			choose_back.setVisibility(v.VISIBLE);
		}
	}

	public int dipToPx(int dip) {
		return (int) (dip * dm.density + 0.5f);
	}

}
