package com.siso.app.ui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rockerhieu.emojicon.EmojiconEditText;
import com.siso.app.adapter.TagSearchAdapter;
import com.siso.app.common.Constants;
import com.siso.app.entity.Position;
import com.siso.app.entity.TagInfo;
import com.siso.app.ui.photopick.PhotoPickActivity;
import com.siso.app.utils.BitmapUtils;
import com.siso.app.widget.crop.CropHelper;
import com.siso.app.widget.tag.TagView;
import com.siso.app.widget.tag.TagViewLeft;
import com.siso.app.widget.tag.TagViewRight;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MarkActivity extends Activity implements OnClickListener {
	
	private static final String TAG = "MarkActivity";
	
	private RelativeLayout markRelativeLayout;
	private TextView markTitle;
	private TextView markFirst;
	private TextView markSecond;
	private ImageView markImageView;
	private ImageView pointImg;
	private RelativeLayout addTagContainer;
	private Position tagPosition;
	private FrameLayout mark_listViewParent;
	private ListView searchListView;
	private EmojiconEditText tagTextView;
	private List<String> mListItems;
	private TagSearchAdapter tagSearchAdapter;
	private int screenWidth,screenHeight;
	final int TAG_LEFT = 0;
	final int TAG_RIGHT = 1;
	private List<TagView> tagViewList;
	private String picPath;
//	private TagInfo tagInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_mark);
		initToolbar();
		initView();
	}

	private void initToolbar() {
		markTitle = (TextView)findViewById(R.id.mark_toolbar_title);
		markFirst = (TextView)findViewById(R.id.mark_toolbar_first);
		markSecond = (TextView)findViewById(R.id.mark_toolbar_second);
		markTitle.setText("标签");
		markSecond.setOnClickListener(this);
	}
	
	
	private void initView() {
		markRelativeLayout = (RelativeLayout)findViewById(R.id.mark_relativeLayout);
		markImageView = (ImageView)findViewById(R.id.mark_imageView);
		addTagContainer = (RelativeLayout)findViewById(R.id.addTagContainer);
		pointImg = (ImageView)findViewById(R.id.brarnd_tag_point);
		mark_listViewParent = (FrameLayout)findViewById(R.id.mark_listViewParent);
		
		picPath = getIntent().getExtras().getString("Uri");
		Uri uri = Uri.parse(picPath);
		markImageView.setImageURI(uri);
		tagPosition = new Position();
		tagViewList = new ArrayList<TagView>();
		initScreen();
		tagPosition.setXplace(screenWidth/2);
		tagPosition.setYplace(screenHeight/2);
		
		initSearchTagList();
		initClick();
	}
	
	private void initScreen() {
		DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;
        screenHeight = metric.heightPixels;
	}

	private void initClick(){
		markImageView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float x = event.getX();
				float y = event.getY();
//				Log.i(TAG, "x=="+x+",,y=="+y);
				setView(Math.round(x), Math.round(y));
//				if (!point) {
//					dispTagChoose();
//				}else {
//					hideTagChoose();
//				}
				return false;
			}

		});
	}
	
	private void createTag(Position pos,TagInfo tagInfo){
		TagView tagView = null;
		if(tagInfo==null) return;
		switch (tagInfo.getOrientation()) {
		case TAG_LEFT:
			tagView = new TagViewLeft(this, null , screenWidth,screenWidth,pos,tagInfo,true);
			break;
		case TAG_RIGHT:
			tagView = new TagViewRight(this, null , screenWidth,screenWidth,pos,tagInfo,true);
			break;
		default:
			break;
		}
		if(tagView!=null) {
			addTagContainer.addView(tagView);
			tagViewList.add(tagView);
		}
		
		tagView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				addTagContainer.removeView(view);
				tagViewList.remove(view);
				int orientation= ((TagView)view).tagInfo.getOrientation();
				if(orientation==0) 
					((TagView)view).tagInfo.setOrientation(1); 
				else ((TagView)view).tagInfo.setOrientation(0);
				createTag(((TagView)view).getPosition() ,((TagView)view).tagInfo);
				Log.i(TAG, "========="+tagViewList.size());
			}
		});
//		tagViewslist.add(tagViewLeft);
		tagPosition = new Position();
	}
	
	public void setView(int left,int top){
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(left, top, 0, 0);
		pointImg.setLayoutParams(lp);
		tagPosition.setXplace(left);
		tagPosition.setYplace(top);
		if(mark_listViewParent.getVisibility()==View.VISIBLE){
			hideTagList();
		}else {
			showTagList();
		}
	}
	
	private void showTagList() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.listview_up);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.listview_up);

//        mListView.startAnimation(animation);
        mark_listViewParent.startAnimation(fadeIn);
        mark_listViewParent.setVisibility(View.VISIBLE);
    }
	
	private void hideTagList() {
        Animation animation = AnimationUtils.loadAnimation(MarkActivity.this, R.anim.listview_down);
        Animation fadeOut = AnimationUtils.loadAnimation(MarkActivity.this, R.anim.listview_down);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            	mark_listViewParent.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

//        mListView.startAnimation(animation);
        mark_listViewParent.startAnimation(fadeOut);
    }
	
	private void initSearchTagList() {
		searchListView = (ListView)findViewById(R.id.mark_tab_choose_listView);
		tagTextView = (EmojiconEditText)findViewById(R.id.search_tag_brand);
		
		String[] mStrings = { "Abbaye de Belloc",
				"\ue32dAbbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
				"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu",
				"Airag", "Airedale", "Aisy Cendre", "Abertam", "Abondance", "Ackawi",
				"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu",
				"Airag", "Airedale", "Aisy Cendre" };
		
		initSearchListener();
		AnimationSet animationSet = new AnimationSet(false);
		Animation animation = new AlphaAnimation(0,1);   //AlphaAnimation 控制渐变透明的动画效果
		animation.setDuration(300);     //动画时间毫秒数
		animationSet.addAnimation(animation);    //加入动画集合
		LayoutAnimationController controller = new LayoutAnimationController(animationSet, 1);
		
		// 设置列表内容
		mListItems = new ArrayList<String>();
		mListItems.addAll(Arrays.asList(mStrings));
		mListItems.set(0, getResources().getString(R.string.add_tag_text));
		
		tagSearchAdapter = new TagSearchAdapter(this, R.layout.item_search_tag_list, mListItems);
		searchListView.setLayoutAnimation(controller);
		searchListView.setAdapter(tagSearchAdapter);
	}
	
	private void initSearchListener(){
		tagTextView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
				mListItems.set(0, getResources().getString(R.string.add_tag_text)+str);
				tagSearchAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void beforeTextChanged(CharSequence str, int arg1, int arg2,int arg3) {
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		
		searchListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,long arg3) {
				String string =mListItems.get(position);
				if (position==0) {
					string = string.substring(getResources().getString(R.string.add_tag_text).length());
				}
				TagInfo tagInfo = new TagInfo(string, 0);
				createTag(tagPosition ,tagInfo);
				hideTagList();
			}
		});
	}
	
	private final Handler mHandler = new Handler();
	private void saveMarkImage() {
		final Bitmap croppedImage = getLayoutBitmap(markRelativeLayout);
//		File file = BitmapUtils.BitmapToFile(croppedImage, Constants.FILE_TEMP_DIR, "tt.jpg");
//		Log.i(TAG, "完成"+file.getPath());
//		Intent intent = new Intent(this,MarkSubmitActivity.class);
//		intent.putExtra("Uri", file.getPath());
//		startActivity(intent);
		
//		try {
//			InputStream is = BitmapUtils.Bitmap2ByteArrayInputStream(croppedImage);
//			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
//			byte[] buf = new byte[1024];
//			int len = 0;
//			// 将网络上的图片存储到本地
//			while ((len = is.read(buf)) > 0) {
//				bos.write(buf, 0, len);
//			}
//			is.close();
//			bos.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}catch (IOException e) {
//			e.printStackTrace();
//		}
//		Log.i(TAG, "write file to " + cacheFile.getCanonicalPath());
		
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				saveDrawableToCache(croppedImage, picPath);
				croppedImage.recycle();
			}
		});
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				saveDrawableToCache(croppedImage, urisString);
//				croppedImage.recycle();
//			}
//		}).start();
	}
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-5-2 上午1:24:07
	 * @Decription 缓存在本地
	 *
	 * @param bitmap
	 * @param filePath
	 */
	private void saveDrawableToCache(Bitmap bitmap, String filePath){
		try {
			File file = new File(filePath);
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream); 
			outStream.flush();
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-5-2 上午1:14:18
	 * @Decription 截取某个布局
	 *
	 * @param view
	 * @return
	 */
	private Bitmap getLayoutBitmap(View view){
    	view.invalidate();
    	view.setDrawingCacheEnabled(true);
//    	view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//    	view.layout(0, 0, view.getMeasuredWidth(),view.getMeasuredHeight());
//    	Log.i(TAG, "width=="+view.getMeasuredWidth()+",,,height=="+view.getMeasuredHeight());
//    	view.buildDrawingCache();
        Bitmap bitmap= view.getDrawingCache();
        return bitmap;
    }
	
	private void submit(){
		List<TagInfo> tagInfoList = new ArrayList<TagInfo>();
		for (int i = 0; i < tagViewList.size(); i++) {
			TagView tmpTagView = tagViewList.get(i);
			TagInfo tmptagInfo = tmpTagView.tagInfo;
			Position tmpPosition = tmpTagView.getPosition();
			tmptagInfo.setLabelx(round((float)tmpPosition.getXplace()/(float)screenWidth,4,BigDecimal.ROUND_HALF_UP));
			tmptagInfo.setLabely(round((float)tmpPosition.getYplace()/(float)screenHeight, 4, BigDecimal.ROUND_HALF_UP));
			tagInfoList.add(tmptagInfo);
		}
		
		Intent intent = new Intent(this,MarkSubmitActivity.class);
		intent.putExtra("Uri", picPath);
		intent.putExtra("taglist",(Serializable)tagInfoList);
		startActivity(intent);
	}
	
	private BigDecimal round(float value, int scale, int roundingMode) {
	     BigDecimal bd = new BigDecimal(value);
	     bd = bd.setScale(scale,roundingMode);
	     return bd;
	 }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mark_toolbar_second:
			saveMarkImage();
			submit();
			break;

		default:
			break;
		}
	}

}
