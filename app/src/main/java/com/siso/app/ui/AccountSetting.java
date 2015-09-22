package com.siso.app.ui;

import com.siso.app.ui.common.BaseActionBarActivity;
import com.siso.app.utils.AccountInfoUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AccountSetting extends BaseActionBarActivity {
	
	private Toolbar toolbar;
	private RelativeLayout passwordSetting;
	private TextView phoneTextView;
	private String userInfo[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_setting);
		initToolbar();
		initView();
	}
	
	private void initView() {
		passwordSetting = (RelativeLayout)findViewById(R.id.acount_setting_passwordSetting);
		phoneTextView = (TextView)findViewById(R.id.account_setting_phone);
		
		userInfo = AccountInfoUtils.getUserInfo(this);
		phoneTextView.setText(userInfo[3]);
		
		passwordSetting.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AccountSetting.this,SetPasswordActivity.class));
			}
		});
	}

	private void initToolbar(){
		toolbar = (Toolbar)findViewById(R.id.toolbar);
		toolbar.setTitle("账号设置");
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.back_btn);
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
