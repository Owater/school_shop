package com.siso.app.ui;

import com.siso.app.adapter.ShareDetailAdapter;
import com.siso.app.entity.ShareEntity;
import com.siso.app.ui.common.BaseActionBarActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ShareDetailActivity extends BaseActionBarActivity implements ShareDetailAdapter.OnItemClickListener {
	
	private RecyclerView recyclerView;
	private ShareEntity shareEntity;
	private ShareDetailAdapter shareDetailAdapter;
	/**
	 * toolbar滚动时颜色变化
	 */
	private int[] primary = new int[3];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_detail);
		initView();
	}

	private void initView() {
		shareEntity = (ShareEntity) getIntent().getSerializableExtra("entity");
		initToolbar(getStringByRId(R.string.title_activity_share_detail));
		toolbar.setBackgroundColor(Color.TRANSPARENT);
		setUpRecyclerView();
	}
	
	private void setUpRecyclerView(){
		recyclerView = (RecyclerView) findViewById(R.id.share_detail_recyclerView);
		
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		shareDetailAdapter = new ShareDetailAdapter(this, shareEntity, this);
		recyclerView.setAdapter(shareDetailAdapter);
	}

	@Override
	public void onItemClick(View view, ShareEntity shareEntity) {
	}
}
