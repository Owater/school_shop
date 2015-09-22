package com.siso.app.ui;

import java.io.Serializable;
import java.util.HashMap;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.google.gson.Gson;
import com.siso.app.common.Constants;
import com.siso.app.common.URLs;
import com.siso.app.entity.DataJson;
import com.siso.app.entity.UserEntity;
import com.siso.app.ui.common.BaseActionBarActivity;
import com.siso.app.ui.login.RegisterActivity_;
import com.siso.app.utils.AccountInfoUtils;
import com.siso.app.utils.PBEUtil;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-4-11 下午2:04:58
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-4-11 下午2:04:58
 *
 */
public class LoginActivity extends BaseActionBarActivity {
	
	private static final String TAG = "LoginActivity";
	
	public final static int LOGIN_SUCCESS = 111;
	public final static int LOGIN_FAIL = 112;
	public final static int REGISTER_SUCCESS = 114;
	private EditText unameEditText;
	private EditText pwdEditText;
	private Button registerBtn;
	private Button loginBtn;
	private Toolbar toolbar;
	private UserEntity userEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}
	
	private void initView() {
		unameEditText = (EditText)findViewById(R.id.login_username);
		pwdEditText = (EditText)findViewById(R.id.login_password);
		registerBtn = (Button)findViewById(R.id.register_button_tips);
		loginBtn = (Button)findViewById(R.id.login);
		
		initToolbar();
		registerBtn.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
	}

	private void initToolbar(){
		toolbar = (Toolbar)findViewById(R.id.toolbar);
		toolbar.setTitle("登录");
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.back_btn);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_login;
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
	
	@Override
	public void onRequestSuccess(String response) {
		Gson gson = new Gson();
		DataJson dataJson = gson.fromJson(response, DataJson.class);
		if (dataJson.isSuccess()) {
			userEntity = gson.fromJson(gson.toJson(dataJson.getData()), UserEntity.class);
			loginHx();
		}else {
			showButtomToast(getStringByRId(R.string.account_fail));
		}
	}
	
	String currentUsername;
	String currentPassword;
	private void loginHx(){
		currentUsername = userEntity.getHxId();
		currentPassword = pwdEditText.getText().toString();
		// 调用sdk登陆方法登陆聊天服务器
		EMChatManager.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

			@Override
			public void onSuccess() {
				loginSuccess();
//				if (!progressShow) {
//					return;
//				}
				// 登陆成功，保存用户名密码
//				MyApplication.getInstance().setUserName(currentUsername);
//				MyApplication.getInstance().setPassword(currentPassword);
				//更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
//				boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(MyApplication.currentUserNick.trim());
//				if (!updatenick) {
//					Log.e("LoginActivity", "update current user nick fail");
//				}
//				if (!LoginActivity.this.isFinishing())
//					pd.dismiss();
			}

			@Override
			public void onProgress(int progress, String status) {
				loadingProgressDialog.setMessage(getStringByRId(R.string.logining));
			}

			@Override
			public void onError(final int code, final String message) {
				loadingProgressDialog.dismiss();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showButtomToast(getStringByRId(R.string.login_fail));
					}
				});
			}
		});
	}
	
	private void loginSuccess(){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showButtomToast("成功登录");
			}
		});
		Intent intent = new Intent();
		intent.putExtra("entity", (Serializable)userEntity);
		AccountInfoUtils.saveSharedPreferences(this, userEntity);
		MyApplication.userInfo = AccountInfoUtils.getUserInfo(this);
		Constants.ISLOGIN = true;
		setResult(LoginActivity.LOGIN_SUCCESS, intent);
		loadingProgressDialog.dismiss();
		finish();
	}
	
	String userPhone;
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.register_button_tips:
			//初始化短信sdk
//			SMSSDK.initSDK(this, Constants.APPKEY, Constants.APPSECRET);
			startActivity(new Intent(this, RegisterActivity_.class));
			// 打开注册页面
//			RegisterPage registerPage = new RegisterPage();
//			//验证短信的回调函数
//			registerPage.setRegisterCallback(new EventHandler() {
//				public void afterEvent(int event, int result, Object data) {
//					// 解析注册结果
//					if (result == SMSSDK.RESULT_COMPLETE) {
//						HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
//						String country = (String) phoneMap.get("country");
//						userPhone = (String) phoneMap.get("phone");
//						Intent intent = new Intent(LoginActivity.this,RegirsterActivity.class);
//						Bundle bundle = new Bundle();
//						bundle.putString("userPhone", userPhone);
//						intent.putExtras(bundle);
//						startActivityForResult(intent,1);
//					}
//				}
//			});
//			registerPage.show(this);
			break;
		case R.id.login:
			UserEntity userEntity = new UserEntity();
			userEntity.setUserPhone(unameEditText.getText().toString());
			userEntity.setLoginPassword(PBEUtil.encrypt(pwdEditText.getText().toString(), PBEUtil.passwordkey, PBEUtil.getStaticSalt()));
			loadingProgressDialog.show();
			networkHelper.postData(URLs.LOGIN, userEntity);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK){
			if(resultCode==REGISTER_SUCCESS){
				if(userPhone!=null){
					unameEditText.setText(userPhone);
				}
			}
		}
	}
}
