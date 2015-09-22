package com.siso.app.ui;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.siso.app.adapter.UserGoodsAdapter;
import com.siso.app.adapter.UserShareAdapter;
import com.siso.app.common.URLs;
import com.siso.app.entity.DataJson;
import com.siso.app.entity.GoodsEntity;
import com.siso.app.entity.ShareEntity;
import com.siso.app.ui.common.BaseActionBarActivity;
import com.siso.app.widget.CustomDialog;
import com.siso.app.widget.CustomDialog.CustomDialogListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class UserPubGoodsActivity extends BaseActionBarActivity implements CustomDialogListener {
	
	private RecyclerView recyclerView;
	private UserGoodsAdapter userGoodsAdapter;
	private UserShareAdapter userShareAdapter;
	private String type;
	private boolean isMe = false;
	private String otherUserId = null;
	private List<GoodsEntity> goodsList;
	private List<ShareEntity> shareList;
	private CustomDialog customDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_pub_goods);
		initView();
	}

	private void initView() {
		type = getIntent().getExtras().getString("type");
		isMe = getIntent().getBooleanExtra("isMe", false);
		otherUserId = getIntent().getExtras().getString("otherUserId");
		if(type.equals("userGoods")){
			if(isMe)
				initToolbar(getStringByRId(R.string.title_activity_user_pub_goods));
			else initToolbar(getStringByRId(R.string.other_goods));
		}else if (type.equals("userShare")) {
			if(isMe)
				initToolbar(getStringByRId(R.string.title_activity_user_pub_share));
			else initToolbar(getStringByRId(R.string.other_share));
		}else if (type.equals("like")) {
			if(isMe)
				initToolbar(getStringByRId(R.string.title_activity_user_like));
			else initToolbar(getStringByRId(R.string.other_like));
		}
		customDialog = new CustomDialog(this, R.style.ForwardDialog, 
				getResources().getString(R.string.change_goods_status), R.array.goods_status_change);
		customDialog.setOnClickListener(this);
		initRecyclerView();
		loadData();
	}

	private void loadData() {
		loadingProgressDialog.show();
		RequestParams params = new RequestParams();
		if (!isMe) {
			params.add("otherUserId", otherUserId);
		}
		params.add("userId", MyApplication.userInfo[1]);
		params.add("authCode", MyApplication.userInfo[0]);
		params.add("type", type);
		networkHelper.getNetJson(URLs.USER_PUB_GOODS, params, new AsyncHttpResponseHandler(){
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				Gson gson = new Gson();
				DataJson dataJson = gson.fromJson(new String(responseBody), DataJson.class);
				if(dataJson.isSuccess()){
					 if(type.equals("userGoods")||type.equals("like")){
						 goodsList = gson.fromJson(gson.toJson(dataJson.getData()), new TypeToken<List<GoodsEntity>>(){}.getType());
						 if(goodsList!=null) initAdapter(); else showButtomToast(getStringByRId(R.string.dataError));
					 }else if (type.equals("userShare")) {
						 shareList = gson.fromJson(gson.toJson(dataJson.getData()), new TypeToken<List<ShareEntity>>(){}.getType());
						 if(shareList!=null) initAdapter(); else showButtomToast(getStringByRId(R.string.dataError));
					}
				}else {
					showButtomToast(getStringByRId(R.string.dataError));
				}
				loadingProgressDialog.dismiss();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				loadingProgressDialog.dismiss();
			}
		});
	}
	
	UserGoodsAdapter.OnItemClickListener goodsClickListener = new UserGoodsAdapter.OnItemClickListener() {
		
		@Override
		public void onItemClick(View view, GoodsEntity goodsEntity) {
			Intent intent = new Intent(UserPubGoodsActivity.this,GoodsDetailActivity.class);
			intent.putExtra("entity", (Serializable)goodsEntity);
			startActivity(intent);
		}

		@Override
		public void onItemLongClick(View view, GoodsEntity goodsEntity) {
			customDialog.show(goodsEntity);
		}
	};
	
	UserShareAdapter.OnItemClickListener shareClickListener = new UserShareAdapter.OnItemClickListener() {
		
		@Override
		public void onItemClick(View view, ShareEntity shareEntity) {
			Intent intent = new Intent(UserPubGoodsActivity.this,ShareDetailActivity.class);
			intent.putExtra("entity", (Serializable)shareEntity);
			startActivity(intent);
		}
	};
	
	private void initAdapter() {
		if(type.equals("userGoods")||type.equals("like")){
			userGoodsAdapter = new UserGoodsAdapter(this, goodsList,isMe);
			recyclerView.setAdapter(userGoodsAdapter);
			userGoodsAdapter.setOnItemClickListener(goodsClickListener);
		}else if (type.equals("userShare")) {
			userShareAdapter = new UserShareAdapter(this, shareList,shareClickListener);
			recyclerView.setAdapter(userShareAdapter);
		}
	}

	private void initRecyclerView() {
		recyclerView = (RecyclerView)findViewById(R.id.user_pub_recyclerView);
		GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
		recyclerView.setLayoutManager(gridLayoutManager);
	}

	@Override
	public void onChoose(int position,Object object) {
		customDialog.dismiss();
		String type = null;
		if (position==0) {
			type="price";
			setPriceAlertDialog(object);
		}else if (position==1) {
			type="out";
			String url = URLs.CHANGE_GOODS_STATUS+"?userId="+MyApplication.userInfo[1]
					+"&authCode="+MyApplication.userInfo[0]+"&type="+type;
			networkHelper.postData(url, (GoodsEntity)object);
		}else if (position==2) {
			type="delete";
			String url = URLs.CHANGE_GOODS_STATUS+"?userId="+MyApplication.userInfo[1]
					+"&authCode="+MyApplication.userInfo[0]+"&type="+type;
			networkHelper.postData(url, (GoodsEntity)object);
		}
		
	}
	
	private void setPriceAlertDialog(final Object object){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater li = LayoutInflater.from(this);
        View v1 = li.inflate(R.layout.dialog_input, null);
        final EditText input = (EditText) v1.findViewById(R.id.value);
        input.setText(((GoodsEntity)object).getGoodPrice()+"");
        builder.setTitle("修改商品价格").setView(v1).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String price = input.getText().toString();
				if(price.equals("")){
					showButtomToast(getStringByRId(R.string.tip_pubgoods_price));
				}else {
					String url = URLs.CHANGE_GOODS_STATUS+"?userId="+MyApplication.userInfo[1]
							+"&authCode="+MyApplication.userInfo[0]+"&type=price";
					((GoodsEntity)object).setGoodPrice(new BigDecimal(price));
					networkHelper.postData(url, (GoodsEntity)object);
				}
			}
		}).setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        input.requestFocus();
	}
	
	@Override
	public void onRequestSuccess(String response) {
		super.onRequestSuccess(response);
		showButtomToast(getStringByRId(R.string.change_goods_success));
		loadData();
	}
	
	@Override
	public void onRequestFail(String response) {
		super.onRequestFail(response);
		showButtomToast(getStringByRId(R.string.change_goods_fail));
	}
	
}
