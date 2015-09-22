package com.siso.app.ui;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.siso.app.adapter.GoodsDetailAdapter;
import com.siso.app.common.URLs;
import com.siso.app.entity.GoodsCommentEntity;
import com.siso.app.entity.GoodsEntity;
import com.siso.app.ui.common.BaseActionBarActivity;
import com.siso.app.utils.CommonUtils;
import com.siso.app.utils.StringUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;

public class GoodsDetailActivity extends BaseActionBarActivity implements EmojiconGridFragment.OnEmojiconClickedListener, 
EmojiconsFragment.OnEmojiconBackspaceClickedListener,OnClickListener {
	
	private static final String TAG = "GoodsDetailActivity";
	
	private GoodsDetailAdapter goodsDetailAdapter;
	private ListView listView;
	private EmojiconEditText mEditEmojicon;
	private FrameLayout emojicons;
	private boolean openemoj = false;
	private ImageButton emojbtn;
	private GoodsEntity goodsEntity;
	private Button sendbtn;
	private LinkedList<GoodsCommentEntity> commentLists;
	private GoodsCommentEntity tmpComment;
	/**
	 * toolbar滚动时颜色变化
	 */
	private int[] primary = new int[3];
	/**
	 * 服务器返回json
	 */
	private String jsonData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_detail);
		initView();
	}
	
	private void initView() {
		listView = (ListView)findViewById(R.id.goodsdetail_listview);
		
		initToolbar(getStringByRId(R.string.title_activity_goods_detail));
		toolbar.setBackgroundColor(Color.TRANSPARENT);
		
		goodsEntity = (GoodsEntity)getIntent().getSerializableExtra("entity");
		commentLists = new LinkedList<GoodsCommentEntity>();
		
	    initRGB();
		
		goodsDetailAdapter = new GoodsDetailAdapter(this, goodsEntity,commentLists);
		listView.setAdapter(goodsDetailAdapter);
		initEmojicon();
		loadData();
		listView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(emojicons.getVisibility()==View.VISIBLE) emojicons.setVisibility(View.GONE);
				return false;
			}
		});
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int scrolled = getScrollY();
				int toolbarHeight = toolbar.getHeight();
				int transHeight = CommonUtils.dip2px(GoodsDetailActivity.this, 250)-toolbarHeight;
				if (scrolled<transHeight) {
					toolbar.setBackgroundColor(color(((float)scrolled) / transHeight));
				}else {
					toolbar.setBackgroundColor(color(1));
				}
			}
		});
	}
	
	private void initRGB() {
		int color = getResources().getColor(R.color.statusbar_bg);
		primary[0] = Color.red(color);
	    primary[1] = Color.green(color);
	    primary[2] = Color.blue(color);
	}

	private int color(float alpha) {
        return Color.argb((int) (alpha * 0xff), primary[0], primary[1], primary[2]);
    }
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-5-14 下午3:54:33
	 * @Decription 计算Listview滚动的位置，前提是每个item高度固定
	 *
	 * @return
	 */
	private int getScrollY() {
	    View c = listView.getChildAt(0);
	    if (c == null) {
	        return 0;
	    }
	    int firstVisiblePosition = listView.getFirstVisiblePosition();
	    int top = c.getTop();
	    return -top + firstVisiblePosition * c.getHeight() ;
	}
	
	private void initEmojicon() {
		sendbtn = (Button)findViewById(R.id.comment_send_btn);
		mEditEmojicon = (EmojiconEditText) findViewById(R.id.editEmojicon);
		emojicons = (FrameLayout)findViewById(R.id.emojicons);
		emojbtn = (ImageButton)findViewById(R.id.icon_emoji_btn);
		emojbtn.setOnClickListener(this);
		sendbtn.setOnClickListener(this);
        setEmojiconFragment(false);
	}
	
	private void loadData(){
		stringRequest = new StringRequest(URLs.MARKET_COMMENTLIST_URL+goodsEntity.getId(), 
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						jsonData = response;
						parseJson();
						goodsDetailAdapter.notifyDataSetChanged();
					}
				} , new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
		stringRequest.setTag(stringRequestTag);
		requestQueue.add(stringRequest);
	}
	
	private void parseJson(){
		if (jsonData!=null&&jsonData.length()>10) {
//	        Gson gson = new GsonBuilder().registerTypeAdapter(java.util.Date.class, new DateSerializer()).setDateFormat(DateFormat.LONG).create();
//			List<GoodsComment> tmplist = gson.fromJson(jsonData, new TypeToken<List<GoodsComment>>(){}.getType());
			Gson gson = new Gson();
			List<GoodsCommentEntity> tmplist = gson.fromJson(jsonData, new TypeToken<List<GoodsCommentEntity>>(){}.getType());
			for (GoodsCommentEntity tmp:tmplist) {
				commentLists.add(tmp);
			}
		}
	}
	
	private void publish() {
		loadingProgressDialog.show();
		tmpComment = new GoodsCommentEntity();
		tmpComment.setComment(StringUtil.string2Unicode(mEditEmojicon.getText().toString()));
		tmpComment.setGoodId(goodsEntity.getId());
		tmpComment.setUserId(MyApplication.userInfo[1]);
		tmpComment.setUserName(MyApplication.userInfo[2]);
		networkHelper.postData(URLs.MARKET_ADDCOMMENT_URL, tmpComment);
	}
	
	private void setEmojiconFragment(boolean useSystemDefault) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.icon_emoji_btn:
			if(openemoj){
				emojicons.setVisibility(View.GONE);
				openemoj = false;
			}else {
				emojicons.setVisibility(View.VISIBLE);
				openemoj = true;
			}
			break;
		case R.id.comment_send_btn:
			if (MyApplication.userInfo!=null) {
				String commString = mEditEmojicon.getText().toString();
				if(commString.length()<=0){
					showButtomToast(getStringByRId(R.string.comment_is_null));
					return;
				}
				publish();
			}else {
				showButtomToast(getStringByRId(R.string.has_no_login));
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		if (requestQueue!=null) requestQueue.cancelAll(stringRequestTag);
		loadingProgressDialog.dismiss();
		if(emojicons.getVisibility()==View.VISIBLE){
			emojicons.setVisibility(View.GONE);
		}else {
			finish();
		}
	}
	
	/**
	 * @param emojicon
	 */
	@Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(mEditEmojicon, emojicon);
    }
	
	@Override
	public void onEmojiconBackspaceClicked(View v) {
		EmojiconsFragment.backspace(mEditEmojicon);
	}
	
	@Override
	public void onRequestSuccess(String response) {
		super.onRequestSuccess(response);
		showButtomToast(getString(R.string.add_comment_success));
		mEditEmojicon.setText("");
		tmpComment.setCreate_time(new Date());
		commentLists.addFirst(tmpComment);
		goodsDetailAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onRequestFail(String response) {
		super.onRequestFail(response);
		showButtomToast(getString(R.string.add_comment_fail));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
            finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
         }
	}

}
