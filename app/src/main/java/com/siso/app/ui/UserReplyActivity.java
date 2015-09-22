package com.siso.app.ui;

import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.siso.app.adapter.ReplyCommListAdapter;
import com.siso.app.common.URLs;
import com.siso.app.entity.DataJson;
import com.siso.app.entity.GoodsCommentEntity;
import com.siso.app.entity.GoodsEntity;
import com.siso.app.entity.UserEntity;
import com.siso.app.ui.common.BaseActionBarActivity;
import com.siso.app.utils.AccountInfoUtils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class UserReplyActivity extends BaseActionBarActivity {
	
	private Toolbar toolbar;
	private ReplyCommListAdapter replyCommListAdapter;
	private List<GoodsCommentEntity> list;
	private ListView listView;
	private int choose;
	private boolean isMe = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_reply);
		choose = getIntent().getExtras().getInt("choose");
		loadingProgressDialog.show();
		initView();
	}
	
	private void initView() {
		listView = (ListView)findViewById(R.id.user_reply_listView);
		
		initToolbar();
		getNetData();
	}

	private void initToolbar(){
		toolbar = (Toolbar)findViewById(R.id.toolbar);
		if(choose==0)toolbar.setTitle("我的回复");else toolbar.setTitle("我参与的");
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.back_btn);
	}
	
	private void getNetData() {
		RequestParams params = new RequestParams();
		params.add("userId", MyApplication.userInfo[1]);
		params.add("authCode", MyApplication.userInfo[0]);
		String url = null;
		if(choose==0) {
			url=URLs.USERRELY;
			isMe = false;
		}else {
			url=URLs.USERJOINTHEME;
			isMe = true;
		}
		networkHelper.getNetJson(url, params, new AsyncHttpResponseHandler(){
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				Gson gson = new Gson();
				DataJson dataJson = gson.fromJson(new String(responseBody), DataJson.class);
				if (dataJson.isSuccess()) {
					list = gson.fromJson(gson.toJson(dataJson.getData()), new TypeToken<List<GoodsCommentEntity>>(){}.getType());
					initAdapter();
				}else {
					showButtomToast("用户信息有误");
				}
				loadingProgressDialog.dismiss();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				loadingProgressDialog.dismiss();
			}
		});
	}
	
	private void initAdapter() {
		replyCommListAdapter = new ReplyCommListAdapter(this, R.layout.item_reply_comment, list,isMe);
		listView.setAdapter(replyCommListAdapter);
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
