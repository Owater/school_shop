/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.ui.find;

import java.io.Serializable;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.siso.app.adapter.FindListAdapter;
import com.siso.app.common.URLs;
import com.siso.app.entity.FindListJsonEntity;
import com.siso.app.entity.GoodsEntity;
import com.siso.app.ui.FindDetailActivity;
import com.siso.app.ui.R;
import com.siso.app.ui.SharePubActivity;
import com.siso.app.ui.common.BaseFragment;
import com.siso.app.ui.common.RefreshBaseFragment;
import com.siso.app.ui.photopick.PhotoPickActivity;
import com.siso.app.widget.FloatingActionButton;
import com.siso.app.widget.ShowHideOnScroll;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-4-28 下午4:05:09
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-4-28 下午4:05:09 
 *
 */
public class FindListFragment extends RefreshBaseFragment implements OnClickListener,FindListAdapter.OnItemClickListener {
	
	private View mView;
	private FloatingActionButton pubBtn;
	private RecyclerView recyclerView;
	private FindListAdapter findListAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mView = (View) inflater.inflate(R.layout.fragment_find_list, null);
		initView();
		return mView;
	}
	
	protected void init() {
		super.init(mView);
	}

	private void initView() {
		pubBtn = (FloatingActionButton) mView.findViewById(R.id.find_list_floatButton);
		recyclerView = (RecyclerView) mView.findViewById(R.id.find_list_recyclerView);
		swipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.find_list_swipeRefreshLayout);
		
		init();
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		loadData();
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
			}
		});
		
		pubBtn.setColor(getResources().getColor(R.color.statusbar_bg));
		pubBtn.setOnClickListener(this);
		recyclerView.setOnTouchListener(new ShowHideOnScroll(pubBtn));
	}

	private void loadData() {
		stringRequest = new StringRequest(URLs.MARK_LIST, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				parseJson(response);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				showToastError(error.toString());
			}
		});
		
		stringRequest.setTag(stringRequestTag);
		requestQueue.add(stringRequest);
	}
	
	private void parseJson(String jsonData) {
//		Log.i("tag", jsonData);
		if(jsonData!=null){
			if (jsonData.length()>5) {
				Gson gson = new Gson();
				List<FindListJsonEntity> tmpList = gson.fromJson(jsonData, new TypeToken<List<FindListJsonEntity>>(){}.getType());
				findListAdapter = new FindListAdapter(getActivity(), tmpList,this);
				recyclerView.setAdapter(findListAdapter);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.find_list_floatButton:
			startActivity(new Intent(getActivity(),PhotoPickActivity.class));
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(View view, FindListJsonEntity findJsonEntity) {
//		Log.i("tag", findJsonEntity.getDesp());
		Intent intent = new Intent(getActivity(),FindDetailActivity.class);
		intent.putExtra("entity", (Serializable)findJsonEntity);
		startActivity(intent);
	}

}

	