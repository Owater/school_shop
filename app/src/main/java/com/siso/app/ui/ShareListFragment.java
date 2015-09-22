package com.siso.app.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.siso.app.adapter.ShareListAdapter;
import com.siso.app.common.URLs;
import com.siso.app.entity.ShareEntity;
import com.siso.app.ui.common.RefreshBaseFragment;
import com.siso.app.widget.FloatingActionButton;
import com.siso.app.widget.ShowHideOnScroll;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class ShareListFragment extends RefreshBaseFragment implements ShareListAdapter.OnItemClickListener,OnClickListener {
	
	private View mView;
	private FloatingActionButton pubBtn;
	private RecyclerView recyclerView;
	private ShareListAdapter shareListAdapter;
	private List<ShareEntity> dataList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void init() {
		super.init(mView);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mView = (View) inflater.inflate(R.layout.fragment_share_list, null);
		initView();
		return mView;
	}

	private void initView() {
		pubBtn = (FloatingActionButton) mView.findViewById(R.id.share_list_floatButton);
		
		swipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.share_list_swipeRefreshLayout);
		
		init();
		setUpRecyclerView();
		loadData();
		pubBtn.setColor(getResources().getColor(R.color.statusbar_bg));
		pubBtn.setOnClickListener(this);
		recyclerView.setOnTouchListener(new ShowHideOnScroll(pubBtn));
	}
	
	private void setUpRecyclerView(){
		recyclerView = (RecyclerView) mView.findViewById(R.id.share_list_recyclerView);
		
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		dataList = new ArrayList<ShareEntity>();
		shareListAdapter = new ShareListAdapter(getActivity(), dataList,this);
		recyclerView.setAdapter(shareListAdapter);
	}
	
	private void loadData() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(true);
			}
		});
		stringRequest = new StringRequest(URLs.SHARE_LIST, new Response.Listener<String>() {

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
		if(jsonData!=null){
			if (jsonData.length()>5) {
				Gson gson = new Gson();
				List<ShareEntity> tmpList = gson.fromJson(jsonData, new TypeToken<List<ShareEntity>>(){}.getType());
				dataList.clear();
				dataList.addAll(tmpList);
				shareListAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onItemClick(View view, ShareEntity shareEntity) {
		Intent intent = new Intent(getActivity(),ShareDetailActivity.class);
		intent.putExtra("entity", (Serializable)shareEntity);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share_list_floatButton:
			if(MyApplication.userInfo!=null){
				startActivity(new Intent(getActivity(),SharePubActivity.class));
			}else {
				showButtomToast(getStringByRId(R.string.has_no_login));
			}
			break;
		default:
			break;
		}
	}
}
