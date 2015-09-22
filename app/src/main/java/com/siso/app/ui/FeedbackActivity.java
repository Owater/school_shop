package com.siso.app.ui;

import com.google.gson.Gson;
import com.siso.app.common.URLs;
import com.siso.app.entity.DataJson;
import com.siso.app.entity.FeedBackEntity;
import com.siso.app.ui.common.BaseActionBarActivity;
import com.siso.app.utils.AccountInfoUtils;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class FeedbackActivity extends BaseActionBarActivity {
	
	EditText titleEditText;
	EditText contentEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initView();
	}
	
	private void initView() {
		titleEditText = (EditText) findViewById(R.id.feedback_title);
		contentEditText = (EditText) findViewById(R.id.feedback_content);
		
		initToolbar(getStringByRId(R.string.title_activity_feedback));
	}

	private void submit(){
		String title = titleEditText.getText().toString();
		String content = contentEditText.getText().toString();
		if(title!=null){
			if(content!=null){
				FeedBackEntity feedBackEntity = new FeedBackEntity();
				feedBackEntity.setTitle(title);
				feedBackEntity.setContent(content);
				if(MyApplication.userInfo[1]!=null) feedBackEntity.setUserId(MyApplication.userInfo[1]);
				loadingProgressDialog.show();
				networkHelper.postData(URLs.USER_FEEDBACK, feedBackEntity);
			}else {
				showButtomToast("内容不能为空");
			}
		}else {
			showButtomToast("标题不能为空");
		}
	}
	
	@Override
	public void onRequestSuccess(String response) {
		super.onRequestSuccess(response);
		Gson gson = new Gson();
		DataJson dataJson = gson.fromJson(response, DataJson.class);
		if (dataJson.isSuccess()) {
			showButtomToast(dataJson.getMsg());
		}else {
			showButtomToast(dataJson.getMsg());
		}
	}
	
	@Override
	public void onRequestFail(String response) {
		super.onRequestFail(response);
		showButtomToast("提交失败");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.feedback,menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_finish:
			submit();
            return true;
        default:
            return super.onOptionsItemSelected(item);
         }
	}
	
}
