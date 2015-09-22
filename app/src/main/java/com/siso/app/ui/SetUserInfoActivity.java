package com.siso.app.ui;

import com.siso.app.ui.common.BaseActionBarActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class SetUserInfoActivity extends BaseActionBarActivity {
	
	private EditText editText;
	private Toolbar toolbar;
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_user_info);
		initView();
	}
	
	private void initView() {
		editText = (EditText) findViewById(R.id.set_user_infovalue);
		
		title = getIntent().getExtras().getString("title");
		String value = getIntent().getExtras().getString("value");
		
		initToolbar();
		String hintFormat = "请输入%s";
		editText.setHint(String.format(hintFormat, title));
		editText.setText(value);
		editText.requestFocus();
	}
	
	private void initToolbar(){
		toolbar = (Toolbar)findViewById(R.id.toolbar);
		toolbar.setTitle(title);
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.back_btn);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.set_password,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
            finish();
            return true;
		case R.id.setpwd_submit:
			
			String string = editText.getText().toString();
			if (string.length()>0) {
				Intent data = new Intent();
				data.putExtra("value", string);
				setResult(UserDetailEditActivity.USERINFO_REturn_NAME, data);
	            finish();
			}else {
				showButtomToast(title+"不能为空");
			}
			
            return true;
        default:
            return super.onOptionsItemSelected(item);
         }
	}
	
}
