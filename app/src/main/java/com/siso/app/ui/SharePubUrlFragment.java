/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.siso.app.common.URLs;
import com.siso.app.entity.DataJson;
import com.siso.app.entity.ShareEntity;
import com.siso.app.ui.common.BaseFragment;
import com.siso.app.utils.AccountInfoUtils;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-4-27 下午6:09:06
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-4-27 下午6:09:06 
 *
 */
public class SharePubUrlFragment extends BaseFragment implements OnClickListener {
	
	private View mView;
	private RelativeLayout share_pub_link_view;
	private Button getBtn;
	private EditText linkEditText;
	private TextWatcher textWatcher;
	private ImageView clearImageView;
	private SharePubUrlCallbacks sharePubUrlCallbacks;
	private ShareEntity shareEntity;
	private RelativeLayout errorView;
	private TextView errorTextView;
	private Button knowBtn;
	
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mView = (View) inflater.inflate(R.layout.fragment_pub_url, null);
		initView();
		return mView;
	}

	private void initView() {
		getBtn = (Button)mView.findViewById(R.id.share_pub_get);
		share_pub_link_view  = (RelativeLayout)mView.findViewById(R.id.share_pub_link_view);
		linkEditText = (EditText) (EditText)mView.findViewById(R.id.share_pub_link_editText);
		clearImageView = (ImageView)mView.findViewById(R.id.share_pub_edit_clear);
		errorView = (RelativeLayout)mView.findViewById(R.id.pub_url_error_view);
		errorTextView = (TextView)mView.findViewById(R.id.pub_url_error_text);
		knowBtn = (Button)mView.findViewById(R.id.share_pub_I_know);
		
		textWatcher = new TextWatcher() {
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) share_pub_link_view.getLayoutParams();
				if (linkEditText.getText().toString().trim().length()>0) {
					params.height = 70;
				}else {
					params.height = LinearLayout.LayoutParams.MATCH_PARENT;
				}
			}
		};
		getBtn.setOnClickListener(this);
		linkEditText.addTextChangedListener(textWatcher);
		clearImageView.setOnClickListener(this);
		knowBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share_pub_get:
			shareEntity = new ShareEntity();
			shareEntity.setGoodsUrl(linkEditText.getText().toString());
			shareEntity.setUserId(MyApplication.userInfo[1]);
			loadingProgressDialog.show();
			networkHelper.postData(URLs.SHARE_GOODS, shareEntity);
			break;
		case R.id.share_pub_edit_clear:
			linkEditText.setText("");
			break;
		case R.id.share_pub_I_know:
			errorView.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onRequestSuccess(String response) {
		super.onRequestSuccess(response);
		Gson gson = new Gson();
		DataJson dataJson = gson.fromJson(response, DataJson.class);
		if (dataJson.isSuccess()) {
			ShareEntity tmpEntity = gson.fromJson(gson.toJson(dataJson.getData()), ShareEntity.class);
			sharePubUrlCallbacks.changeFragment(1,tmpEntity);
		}else {
			errorView.setVisibility(View.VISIBLE);
			errorTextView.setText(getStringByRId(R.string.share_pub_url_error)+"\n\n"+linkEditText.getText().toString());
		}
	}
	
	@Override
	public void onRequestFail(String response) {
		super.onRequestFail(response);
		showButtomToast(getStringByRId(R.string.timeoutError));
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            sharePubUrlCallbacks = (SharePubUrlCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement SharePubUrlCallbacks.");
        }
    }
	
	public static interface SharePubUrlCallbacks{
		void changeFragment(int position,ShareEntity shareEntity);
	}

}

	