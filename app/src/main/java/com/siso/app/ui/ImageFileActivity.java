package com.siso.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.siso.app.adapter.FolderAdapter;
import com.siso.app.common.Constants;
import com.siso.app.entity.ImageBucket;
import com.siso.app.entity.ImageItem;
import com.siso.app.ui.common.BaseActionBarActivity;
import com.siso.app.utils.AlbumHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ImageFileActivity extends BaseActionBarActivity {
	
	private static final String TAG = "ImageFileActivity";
	
	public static List<ImageBucket> contentList;
	private ArrayList<ImageItem> dataList;
	private AlbumHelper helper;
	private FolderAdapter folderAdapter;
	
	/**
	 * 图片文件夹列表
	 */
	private GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_file);
		initView();
	}

	private void initView() {
		gridView = (GridView)findViewById(R.id.imageFile_act_fileGridView);
		
		initToolbar(getResources().getString(R.string.title_activity_picture_file));
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		contentList = helper.getImagesBucketList(false);
		folderAdapter = new FolderAdapter(this,contentList);
		gridView.setAdapter(folderAdapter);
		dataList = new ArrayList<ImageItem>();
		for(int i = 0; i<contentList.size();i++){
			dataList.addAll(contentList.get(i).imageList);
		}
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				ShowFilePhotoActivity.dataList = (ArrayList<ImageItem>) ImageFileActivity.contentList.get(position).imageList;
				Intent intent = new Intent();
				String folderName = ImageFileActivity.contentList.get(position).bucketName;
				intent.putExtra("folderName", folderName);
				intent.setClass(ImageFileActivity.this, ShowFilePhotoActivity.class);
				startActivityForResult(intent, 100);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==Constants.CHOOSE_PIC) {
			setResult(Constants.CHOOSE_PIC);
			this.finish();
		}
	}
	
	
}
