/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.siso.app.adapter.ShareImgListAdapter;
import com.siso.app.common.URLs;
import com.siso.app.entity.ShareEntity;
import com.siso.app.ui.common.BaseFragment;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-4-27 下午8:05:47
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-4-27 下午8:05:47 
 *
 */
public class SharePubEditFragment extends BaseFragment implements OnClickListener {
	
	private View mView;
	private RecyclerView recyclerView;
	private ShareImgListAdapter shareImgListAdapter;
	private ShareEntity shareEntity;
	private EditText nameEditText;
	private TextView priceTextView;
	private Button pubBtn;
	private EditText reasonEditText;
	
	@Override
	public void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		Bundle bundle = this.getArguments();
		shareEntity = (ShareEntity) bundle.getSerializable("entity");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mView = (View) inflater.inflate(R.layout.fragment_share_pub_edit, null);
		initView();
		return mView;
	}

	private void initView() {
		recyclerView = (RecyclerView)mView.findViewById(R.id.share_pub_edit_pics);
		nameEditText = (EditText)mView.findViewById(R.id.share_pub_name);
		priceTextView = (TextView)mView.findViewById(R.id.share_pub_goods_price);
		pubBtn = (Button)mView.findViewById(R.id.share_edit_pubBtn);
		reasonEditText = (EditText)mView.findViewById(R.id.share_edit_reason);
		
		nameEditText.setText(shareEntity.getTitle());
		priceTextView.setText("￥"+shareEntity.getGoodsPrice());
		initPics();
		pubBtn.setOnClickListener(this);
	}

	private void initPics() {
		String imgUrlString=shareEntity.getImgUrl();
		if(imgUrlString==null) return;
		String imgUrls[] = imgUrlString.split(";");
		
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < imgUrls.length; i++) {
			list.add(imgUrls[i]);
		}
		shareImgListAdapter = new ShareImgListAdapter(getActivity(),list);
		//设置布局管理器
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.setAdapter(shareImgListAdapter);
	}
	
	private void submit(){
		String title = nameEditText.getText().toString();
		if(title.length()<=0) {
			showButtomToast(getStringByRId(R.string.share_pub_name_tip));
			return;
		}
		shareEntity.setTitle(title);
		shareEntity.setShareReason(reasonEditText.getText().toString());
		shareEntity.setUserId(MyApplication.userInfo[1]);
		loadingProgressDialog.show();
		networkHelper.postData(URLs.SHARE_ADD, shareEntity);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share_edit_pubBtn:
			submit();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onRequestSuccess(String response) {
		super.onRequestSuccess(response);
		showButtomToast(getStringByRId(R.string.market_pub_success));
		getActivity().finish();
	}

}

	