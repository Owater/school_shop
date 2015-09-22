package com.siso.app.ui;

import java.io.Serializable;

import com.siso.app.entity.ShareEntity;
import com.siso.app.ui.SharePubUrlFragment.SharePubUrlCallbacks;
import com.siso.app.ui.common.BaseActionBarActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

public class SharePubActivity extends BaseActionBarActivity implements SharePubUrlCallbacks {
	
	int currentPos = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_pub);
		initToolbar(getStringByRId(R.string.title_activity_share_pub));
		initView();
	}

	private void initView() {
		changeFragment(currentPos,null);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
            exit();
            return true;
		default:
            return super.onOptionsItemSelected(item);
		}
	}
	
	private void exit(){
		if(currentPos==0) finish();else changeFragment(0,null);
	}

	@Override
	public void changeFragment(int position,ShareEntity shareEntity) {
		Fragment fragment = null;
		currentPos = position;
		switch (position) {
		case 0:
			fragment = new SharePubUrlFragment();
			break;
		case 1:
			fragment = new SharePubEditFragment();
			break;
		default:
			break;
		}
		
		if(shareEntity!=null){
			Bundle bundle = new Bundle();
			bundle.putSerializable("entity", (Serializable)shareEntity);
			fragment.setArguments(bundle);
		}
		
		if(fragment!=null){
        	getSupportFragmentManager().beginTransaction().replace(R.id.share_pub_container,fragment,"SHARE_FRAGMENT").commit();
        }
	}
	
	@Override
	public void onBackPressed() {
		exit();
	}
}
