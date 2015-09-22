package com.siso.app.ui;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.siso.app.common.URLs;
import com.siso.app.entity.DataJson;
import com.siso.app.ui.common.BaseActionBarActivity;
import com.siso.app.utils.AccountInfoUtils;
import com.siso.app.utils.DESUtil;
import com.siso.app.utils.PBEUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class SetPasswordActivity extends BaseActionBarActivity {
	
	private Toolbar toolbar;
	TextView oldPassword;
	TextView newPassword;
	TextView confirmPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_password);
		initToolbar();
		initView();
	}
	
	private void initView() {
		oldPassword = (TextView)findViewById(R.id.set_oldPassword);
		newPassword = (TextView)findViewById(R.id.set_newPassword);
		confirmPassword = (TextView)findViewById(R.id.set_confirmPassword);
		
	}

	private void initToolbar(){
		toolbar = (Toolbar)findViewById(R.id.toolbar);
		toolbar.setTitle("修改密码");
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.back_btn);
	}
	
	public void postData(){
		RequestParams params = new RequestParams();
		params.put("authCode", MyApplication.userInfo[0]);
		params.put("userId", MyApplication.userInfo[1]);
		params.put("phone", MyApplication.userInfo[3]);
		try {
            String oldPwd = oldPassword.getText().toString();
            String newPwd = newPassword.getText().toString();
            String confirmPwd = confirmPassword.getText().toString();
            if (passwordFormatError(oldPwd, newPwd, confirmPwd)) {
                return;
            }
            params.put("oldPassword", PBEUtil.encrypt(oldPwd, PBEUtil.passwordkey, PBEUtil.getStaticSalt()));
            params.put("newPassword", PBEUtil.encrypt(newPwd, PBEUtil.passwordkey,PBEUtil.getStaticSalt()));
            networkHelper.getNetJson(URLs.USER_RESETPWD, params, new AsyncHttpResponseHandler(){
            	
            	@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            		Gson gson = new Gson();
    				DataJson dataJson = gson.fromJson(new String(responseBody), DataJson.class);
    				if (dataJson.isSuccess()) {
    					showButtomToast("密码修改成功");
    					popDialog();
    				}else {
    					showButtomToast(dataJson.getMsg());
					}
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
				}

            });
        } catch (Exception e) {
        	showButtomToast(e.toString());
        }
	}
	
	boolean passwordFormatError(String oldPwd, String newPwd, String confirmPwd) {
        if (TextUtils.isEmpty(oldPwd)) {
            showButtomToast("当前密码不能为空");
            return true;
        } else if (TextUtils.isEmpty(newPwd)) {
        	showButtomToast("新密码不能为空");
            return true;
        } else if (TextUtils.isEmpty(confirmPwd)) {
        	showButtomToast("确认密码不能为空");
            return true;
        } else if (!newPwd.equals(confirmPwd)) {
        	showButtomToast("两次密码输入不一致");
            return true;
        } else if (newPwd.length() < 6) {
        	showButtomToast("密码不能少于6位");
            return true;
        } else if (newPwd.length() > 32) {
        	showButtomToast("密码不能大于32位");
            return true;
        }
        return false;
    }
	
	private void popDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("修改密码后需要重新登录")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(new Intent(SetPasswordActivity.this, LoginActivity.class));
                    }
                })
                .setCancelable(false)
                .show();
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
            postData();
            return true;
        default:
            return super.onOptionsItemSelected(item);
         }
	}
}
