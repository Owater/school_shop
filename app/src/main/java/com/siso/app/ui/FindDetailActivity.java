package com.siso.app.ui;

import com.siso.app.adapter.FindDetailAdapter;
import com.siso.app.entity.FindListJsonEntity;
import com.siso.app.ui.common.BaseActionBarActivity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

public class FindDetailActivity extends BaseActionBarActivity {
	
	private RecyclerView recyclerView;
	private FindListJsonEntity findJsonEntity;
	private FindDetailAdapter findDetailAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_detail);
		initView();
	}

	private void initView() {
		recyclerView = (RecyclerView) findViewById(R.id.find_detail_recyclerView);
		initToolbar(getStringByRId(R.string.title_activity_find_detail));
		
		findJsonEntity = (FindListJsonEntity)getIntent().getSerializableExtra("entity");
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		
		findDetailAdapter = new FindDetailAdapter(this, findJsonEntity);
		recyclerView.setAdapter(findDetailAdapter);
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
