package com.siso.app.ui;

import java.io.Serializable;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.siso.app.common.Constants;
import com.siso.app.common.URLs;
import com.siso.app.entity.DataJson;
import com.siso.app.entity.UserEntity;
import com.siso.app.ui.common.BaseActionBarActivity;
import com.siso.app.utils.QiniuUploadUitls;
import com.siso.app.utils.QiniuUploadUitls.QiniuUploadUitlsListener;
import com.siso.app.widget.ChoosePicDialog;
import com.siso.app.widget.ChoosePicDialog.ChoosePicListener;
import com.siso.app.widget.CircleImageView;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserDetailActivity extends BaseActionBarActivity {
	
	private UserEntity userEntity;
	private TextView userNameTextView;
	private CircleImageView userAvatar;
	private ChoosePicDialog choosePicDialog;
	private final int CROP_REQUEST_CODE = 2;
	public final static int RESULT_EDIT = 13;
	private Bitmap avatar;
	private LinearLayout userGoods;
	private LinearLayout userShare;
	private LinearLayout likeView;
	private ImageView sex;
	private TextView userSchool;
	private TextView goodsTextView;
	private TextView shareTextView;
	private TextView findTextView;
	private TextView likeTextView;
	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_detail);
		initToolbar(getStringByRId(R.string.title_activity_user_detail));
		toolbar.setBackgroundColor(Color.TRANSPARENT);
		initQiniuToken();
		initView();
	}
	
	private void initQiniuToken(){
		MarketPubActivity.qiniuToken = null;
		networkHelper.getNetString(URLs.QINIU_TOKEN, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				MarketPubActivity.qiniuToken = new String(responseBody);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
			}
		});
	}
	
	private void initView() {
		userNameTextView = (TextView)findViewById(R.id.user_detail_name);
		userAvatar = (CircleImageView)findViewById(R.id.user_detail_avatar);
		userGoods = (LinearLayout)findViewById(R.id.userdetail_goods);
		userShare = (LinearLayout)findViewById(R.id.userdetail_share);
		likeView = (LinearLayout)findViewById(R.id.userdetail_like);
		goodsTextView = (TextView)findViewById(R.id.userdetail_goods_text);
		shareTextView = (TextView)findViewById(R.id.userdetail_share_text);
		findTextView = (TextView)findViewById(R.id.userdetail_find_text);
		likeTextView = (TextView)findViewById(R.id.userdetail_like_text);
		sex = (ImageView)findViewById(R.id.sex);
		userSchool = (TextView)findViewById(R.id.userdetail_school);
		
		choosePicDialog =  new ChoosePicDialog(this, R.style.ForwardDialog, R.layout.choose_pic_dialog);
		userAvatar.setOnClickListener(this);
		userGoods.setOnClickListener(this);
		userShare.setOnClickListener(this);
		likeView.setOnClickListener(this);
		choosePicDialog.setOnClickListener(new ChoosePicListener() {
			
			@Override
			public void startChooseActivity(int choose) {
				choosePicDialog.dismiss();
				if (choose==Constants.TAKEPHOTO) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent, Constants.TAKEPHOTO);
				}else if(choose==Constants.CHOOSE_PIC) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");
					startActivityForResult(intent, Constants.TAKEPHOTO);
				}
			}
		});
		userId = getIntent().getExtras().getString("userId");
		getNetData(userId);
	}
	
	private void getNetData(String userId) {
		loadingProgressDialog.show();
		RequestParams params = new RequestParams();
		String url = URLs.USERINFO_GENER;
		if(userId.equals("")){
			userId = MyApplication.userInfo[1];
			params.add("authCode", MyApplication.userInfo[0]);
			url = URLs.USERINFO;
			isMenuOpen = true;
		}else {
			params.add("code", "code");
			if(!userId.equals(MyApplication.userInfo[1])){
				goodsTextView.setText(getStringByRId(R.string.other_goods));
				shareTextView.setText(getStringByRId(R.string.other_share));
				findTextView.setText(getStringByRId(R.string.other_find));
				likeTextView.setText(getStringByRId(R.string.other_like));
			}
		}
		params.add("userId", userId);
		Log.i("tag", "url=="+url);
		networkHelper.getNetJson(url, params, new AsyncHttpResponseHandler(){
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				Gson gson = new Gson();
				DataJson dataJson = gson.fromJson(new String(responseBody), DataJson.class);
				if (dataJson.isSuccess()) {
					userEntity = gson.fromJson(gson.toJson(dataJson.getData()), UserEntity.class);
					if(userEntity!=null){
						setUserMsg();
					}else {
						showButtomToast(getStringByRId(R.string.getUserInfo_fail));
						finish();
					}
				}else {
					loadingProgressDialog.dismiss();
					showButtomToast(getStringByRId(R.string.account_error));
//					finish();
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				loadingProgressDialog.dismiss();
				showButtomToast(getStringByRId(R.string.timeoutError));
			}
		});
	}
	
	private void setUserMsg(){
		loadingProgressDialog.dismiss();
		userNameTextView.setText(userEntity.getUserName());
		if(userEntity.getSex()==0) 
			sex.setImageDrawable(getResources().getDrawable(R.drawable.ic_sex_boy));
		else if(userEntity.getSex()==1) sex.setImageDrawable(getResources().getDrawable(R.drawable.ic_sex_girl));
		else sex.setImageDrawable(getResources().getDrawable(R.drawable.privary_sex));
		userSchool.setText(userEntity.getSchoolName());
		Picasso picasso = Picasso.with(userAvatar.getContext());
		picasso.load(userEntity.getAvatarUrl()).placeholder(R.drawable.ic_avatar).into(userAvatar);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_detail_avatar:
			choosePicDialog.show();
			break;
		case R.id.userdetail_goods:
			startMyActivity(UserPubGoodsActivity.class,"userGoods");
			break;
		case R.id.userdetail_share:
			startMyActivity(UserPubGoodsActivity.class,"userShare");
			break;
		case R.id.userdetail_like:
			startMyActivity(UserPubGoodsActivity.class,"like");
			break;
		default:
			break;
		}
	}
	
	private <T> void startMyActivity(Class<T> activityClass,String type){
		if(MyApplication.userInfo!=null){
			Intent intent = new Intent(this,activityClass);
			intent.putExtra("type", type);
			if(userId.equals(""))
				intent.putExtra("isMe", true);
			else 
				intent.putExtra("isMe", false);
			intent.putExtra("otherUserId", userEntity.getId());
			startActivity(intent);
		}else {
			showButtomToast(getStringByRId(R.string.has_no_login));
		}
	}

	boolean isMenuOpen = false;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (isMenuOpen) 
			getMenuInflater().inflate(R.menu.user_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.user_detail_edit:
			Intent intent = new Intent(this,UserDetailEditActivity.class);
			intent.putExtra("userEntity", (Serializable)userEntity);
			startActivityForResult(intent, RESULT_EDIT);
            return true;
        default:
            return super.onOptionsItemSelected(item);
         }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case RESULT_EDIT:
				getNetData("");
				break;
			case Constants.CHOOSE_PIC:
				cropImage(data.getData());
				break;
			case Constants.TAKEPHOTO:
				cropImage(data.getData());
				break;
			case CROP_REQUEST_CODE:
				if(data!=null)
				{
					avatar = data.getExtras().getParcelable("data");
					upLoadImage();
				}
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-5-7 下午8:12:37
	 * @Decription 上传头像
	 *
	 * @param bitmap
	 */
	private void upLoadImage(){
		
		if(MarketPubActivity.qiniuToken==null){
			showButtomToast(getStringByRId(R.string.tip_qiniutoken_error));
			return;
		}
		
		loadingProgressDialog.show();
		QiniuUploadUitls.getInstance().uploadImage(avatar,new QiniuUploadUitlsListener(){

			@Override
			public void onSucess(String fileUrl) {
				UserEntity userEntity = new UserEntity();
				userEntity.setId(MyApplication.userInfo[1]);
				userEntity.setAuthCode(MyApplication.userInfo[0]);
				userEntity.setAvatarUrl(fileUrl);
				networkHelper.postData(URLs.USER_AVATAR, userEntity);
				showButtomToast(getStringByRId(R.string.userdetail_avatar_change));
			}

			@Override
			public void onError(int errorCode, String msg) {
				loadingProgressDialog.dismiss();
			}

			@Override
			public void onProgress(final int progress) {
				runOnUiThread(new Runnable() {
		            public void run() {
		            	loadingProgressDialog.setMessage("正在上传... "+progress+"%");
		            }
		        });
			}
		});
	}
	
	@Override
	public void onRequestSuccess(String response) {
		super.onRequestSuccess(response);
		if(avatar!=null) userAvatar.setImageBitmap(avatar);
	}
	
	private void cropImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

}
