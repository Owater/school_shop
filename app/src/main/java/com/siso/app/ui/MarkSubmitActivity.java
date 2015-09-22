package com.siso.app.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.siso.app.common.URLs;
import com.siso.app.entity.FindListJsonEntity;
import com.siso.app.entity.TagInfo;
import com.siso.app.ui.common.BaseActionBarActivity;
import com.siso.app.utils.AccountInfoUtils;
import com.siso.app.utils.Bimp;
import com.siso.app.utils.BitmapUtils;
import com.siso.app.utils.QiniuUploadUitls;
import com.siso.app.utils.QiniuUploadUitls.QiniuUploadUitlsListener;
import com.siso.app.widget.crop.CropHelper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MarkSubmitActivity extends BaseActionBarActivity {
	
	private static final String TAG = "MarkActivity";
	
	private ImageView markImageView;
	private List<TagInfo> tagInfoList;
	private String userInfo[];
	private EditText despEditText;
	private Uri imgUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_mark_submit);
		initView();
	}

	private void initView() {
		initQiniuToken();
		initToolbar();
		markImageView = (ImageView)findViewById(R.id.mark_submit_imageView);
		despEditText = (EditText)findViewById(R.id.mark_submit_editext);
		
		tagInfoList = new ArrayList<TagInfo>();
		tagInfoList = (List<TagInfo>)getIntent().getSerializableExtra("taglist");
		String urisString = getIntent().getExtras().getString("Uri");
		imgUri = Uri.parse(urisString);
		markImageView.setImageURI(imgUri);
		userInfo = AccountInfoUtils.getUserInfo(this);
	}
	
	private void initToolbar(){
		toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.back_btn);
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.feedback,menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
            finish();
            return true;
		case R.id.action_finish:
			submit();
            return true;
        default:
            return super.onOptionsItemSelected(item);
         }
	}

	private void submit() {
		String string = despEditText.getText().toString();
		if(string.length()<=0){
			showButtomToast(getStringByRId(R.string.mark_submit_tip));
			return;
		}else if (MarketPubActivity.qiniuToken==null) {
			showButtomToast(getStringByRId(R.string.tip_qiniutoken_error));
			return;
		}
		loadingProgressDialog.show();
		uploadImage(BitmapUtils.decodeUriToBitmap(this, imgUri));
	}
	
	private void uploadImage(Bitmap bitmap){
		QiniuUploadUitls.getInstance().uploadImage(bitmap,new QiniuUploadUitlsListener(){

			@Override
			public void onSucess(String fileUrl) {
				postData(fileUrl);
			}

			@Override
			public void onError(int errorCode, String msg) {
			}

			@Override
			public void onProgress(int progress) {
			}
		});
	}
	
	private void postData(String fileUrl) {
		FindListJsonEntity findJsonEntity = new FindListJsonEntity();
		String string = despEditText.getText().toString();
		findJsonEntity.setDesp(string);
		findJsonEntity.setUserId("4028b8814cd09869014cd09a301d0000");
		findJsonEntity.setFindLabelEntities(tagInfoList);
		findJsonEntity.setImgUrl(fileUrl);
		networkHelper.postData(URLs.MARK_PUB, findJsonEntity);
	}
	
	@Override
	public void onRequestSuccess(String response) {
		super.onRequestSuccess(response);
		showButtomToast("成功发布");
		Log.i(TAG, response);
	}
	
	@Override
	public void onRequestFail(String response) {
		super.onRequestFail(response);
	}
	
}
