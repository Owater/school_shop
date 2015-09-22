package com.siso.app.ui.photopick;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.siso.app.adapter.PickImgGridAdapter;
import com.siso.app.entity.ImageInfo;
import com.siso.app.ui.MarkActivity;
import com.siso.app.ui.R;
import com.siso.app.utils.CommonUtils;
import com.siso.app.widget.crop.CropHelper;
import com.siso.app.widget.crop.CropParams;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PhotoPickActivity extends BasePhotoCropActivity {
	
	private static final String TAG = "PhotoPickActivity";
	public static final String CameraItem = "CameraItem";
	
	String[] projection = {MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.WIDTH,
            MediaStore.Images.ImageColumns.HEIGHT,
            MediaStore.Images.ImageColumns.MINI_THUMB_MAGIC};
	
	LinkedHashMap<String, ArrayList<ImageInfo>> mFolders = new LinkedHashMap();
	ArrayList<String> mFoldersName = new ArrayList();
	final String allPhotos = "所有图片";
	
	private GridView gridView;
	private PickImgGridAdapter pickImgGridAdapter;
	private RecyclerView recyclerView;
	private GridLayoutManager layoutManager;
	private TextView mFoldName;
	private View mListViewGroup;
	private ListView mListView;
	public static CropParams mCropParams = new CropParams();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_photo_pick);
		initView();
	}

	private void initView() {
		gridView = (GridView)findViewById(R.id.photot_pick_gridView);
//		recyclerView = (RecyclerView)findViewById(R.id.photo_pick_recyclerView);
		mFoldName = (TextView) findViewById(R.id.photo_pick_foldName);
		mListViewGroup = findViewById(R.id.photo_pick_listViewParent);
		mListView = (ListView)findViewById(R.id.photo_pick_listView);
		
		String selection = "";
        String[] selectionArgs = null;
		Cursor mImageExternalCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, MediaStore.MediaColumns.DATE_ADDED + " DESC");
		CursorLoader cursorLoader = new CursorLoader(this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, MediaStore.MediaColumns.DATE_ADDED + " DESC");
		ArrayList<ImageInfo> allPhoto = new ArrayList();
        allPhoto.add(new ImageInfo(CameraItem));
        mFoldersName.add(allPhotos);
        
        while (mImageExternalCursor.moveToNext()) {
            String s0 = mImageExternalCursor.getString(0);
            String s1 = mImageExternalCursor.getString(1);
            String s2 = mImageExternalCursor.getString(2);
            int width = mImageExternalCursor.getInt(3);
            int height = mImageExternalCursor.getInt(4);
            long thumbnailId = mImageExternalCursor.getLong(5);

            String s = String.format("%s,%s,%s, %s, %s, %s", s0, s1, s2, width, height, thumbnailId);
            Log.i(TAG, "path== " + s);
            if (CommonUtils.isImageUri(s1)) {
                s1 = "file://" + s1;
            }
            ImageInfo imageInfo = new ImageInfo(s1);
            imageInfo.photoId = Long.valueOf(s0);
            imageInfo.width = width;
            imageInfo.height = height;

            ArrayList<ImageInfo> value = mFolders.get(s2);
            if (value == null) {
                value = new ArrayList<ImageInfo>();
                mFolders.put(s2, value);
                mFoldersName.add(s2);
            }
            allPhoto.add(imageInfo);
            value.add(imageInfo);
        }
        mFolders.put(allPhotos, allPhoto);
        
        mListView.setAdapter(mFoldAdapter);
        mListView.setOnItemClickListener(mOnItemClick);
        
        String folderName = mFoldersName.get(0);
        mFoldName.setText(folderName);
        mFoldName.setOnClickListener(mOnClickFoldName);
        
//        GalleryAdapter galleryAdapter = new GalleryAdapter(this, allPhoto);
//        layoutManager = new GridLayoutManager(this, getSpanCount());
//        layoutManager.setSpanSizeLookup(new HeaderSpanSizeLookup(layoutManager));
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(galleryAdapter);
        
        pickImgGridAdapter = new PickImgGridAdapter(this);
        pickImgGridAdapter.setData(mFolders.get(mFoldersName.get(0)));
        gridView.setAdapter(pickImgGridAdapter);
        gridView.setOnItemClickListener(mOnPhotoItemClick);
//        gridView.post(new Runnable() {
//			
//			@Override
//			public void run() {
//				pickImgGridAdapter.notifyDataSetChanged();
//			}
//		});
	}
	
	GridView.OnItemClickListener mOnPhotoItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Intent intent = new Intent(PhotoPickActivity.this, PhotoCropActivity.class);
//            intent.putExtra("Uri", pickImgGridAdapter.getData().get((int)id).path);
            mCropParams.uri = Uri.parse(pickImgGridAdapter.getData().get((int)id).path);
            startActivityForResult(CropHelper.buildCropFromUriIntent(mCropParams), CropHelper.REQUEST_CROP);
        }
    };
	
	ListView.OnItemClickListener mOnItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String folderName = mFoldersName.get((int) id);
            mFolders.get(folderName);
            pickImgGridAdapter.setData(mFolders.get(folderName));
            mFoldName.setText(mFoldersName.get(position));
            pickImgGridAdapter.notifyDataSetChanged();
            mFoldName.setText(folderName);
            mFoldAdapter.notifyDataSetChanged();
            hideFolderList();
        }
    };
	
	View.OnClickListener mOnClickFoldName = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListViewGroup.getVisibility() == View.VISIBLE) {
                hideFolderList();
            } else {
                showFolderList();
            }
        }
    };
    
    private void showFolderList() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.listview_up);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.listview_fade_in);

        mListView.startAnimation(animation);
        mListViewGroup.startAnimation(fadeIn);
        mListViewGroup.setVisibility(View.VISIBLE);
    }
    
    private void hideFolderList() {
        Animation animation = AnimationUtils.loadAnimation(PhotoPickActivity.this, R.anim.listview_down);
        Animation fadeOut = AnimationUtils.loadAnimation(PhotoPickActivity.this, R.anim.listview_fade_out);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mListViewGroup.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mListView.startAnimation(animation);
        mListViewGroup.startAnimation(fadeOut);
    }
    
    @Override
    public void onPhotoCropped(Uri uri) {
    	Intent intent = new Intent(this,MarkActivity.class);
    	intent.putExtra("Uri", uri.toString());
    	startActivity(intent);
    }
    
    @Override
    public CropParams getCropParams() {
    	return mCropParams;
    }
	
	private int getSpanCount() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return 3;
        } else {
            return 2;
        }
    }
	
	BaseAdapter mFoldAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return mFoldersName.size();
        }

        @Override
        public Object getItem(int position) {
            return mFoldersName.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.photopick_list_item, parent, false);
                holder = new ViewHolder();
                holder.foldIcon = (ImageView) convertView.findViewById(R.id.foldIcon);
                holder.foldName = (TextView) convertView.findViewById(R.id.foldName);
                holder.photoCount = (TextView) convertView.findViewById(R.id.photoCount);
                holder.check = convertView.findViewById(R.id.check);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String name = (String) getItem(position);
            ArrayList<ImageInfo> imageInfos = mFolders.get(name);
            String uri = imageInfos.get(0).path;
            int count = imageInfos.size();

            holder.foldName.setText(name);
            holder.photoCount.setText(String.format("%d张", count));

            // 如果是照相机，就用下一张图片
            if (uri.equals(CameraItem)) {
                if (imageInfos.size() >= 2) {
                    uri = imageInfos.get(1).path;
                }
            }

//            int width = (int) convertView.getResources().getDimension(R.dimen.pickImg_foldIcon);
            int width = 200;
            Picasso.with(holder.foldIcon.getContext()).load(uri).placeholder(R.drawable.ic_img_loading).resize(width, width).into(holder.foldIcon);
            
            if (mFoldName.getText().toString().equals(name)) {
                holder.check.setVisibility(View.VISIBLE);
            } else {
                holder.check.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }
        
    };
    
    static class ViewHolder {
        ImageView foldIcon;
        TextView foldName;
        TextView photoCount;
        View check;
    }
}
