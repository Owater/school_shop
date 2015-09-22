package com.siso.app.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.siso.app.adapter.ImgGridAdapter;
import com.siso.app.adapter.ImgGridAdapter.UpdateGridAdapterListener;
import com.siso.app.common.Constants;
import com.siso.app.common.URLs;
import com.siso.app.entity.GoodsEntity;
import com.siso.app.entity.ImageItem;
import com.siso.app.ui.common.BaseActionBarActivity;
import com.siso.app.utils.Bimp;
import com.siso.app.utils.BitmapUtils;
import com.siso.app.utils.AccountInfoUtils;
import com.siso.app.utils.NetworkHelper;
import com.siso.app.utils.QiniuUploadUitls;
import com.siso.app.utils.QiniuUploadUitls.QiniuUploadUitlsListener;
import com.siso.app.utils.UploadUtil;
import com.siso.app.utils.UploadUtil.OnUploadProcessListener;
import com.siso.app.widget.ChoosePicDialog;
import com.siso.app.widget.ChoosePicDialog.ChoosePicListener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class MarketPubActivity extends BaseActionBarActivity implements OnUploadProcessListener,UpdateGridAdapterListener {
	
	private static final String TAG = "MarketPubActivity";
	
	/**
	 * 帖子标题
	 */
	private EditText marketPubTitle;
	/**
	 * 商品标题
	 */
	private EditText marketPubName;
	/**
	 * 商品价格
	 */
	private EditText marketPubPrice;
	/**
	 * 商品描述
	 */
	private EditText marketPubDescribe;
	/**
	 * 联系人
	 */
	private EditText marketPubContactPhone;
	/**
	 * 商品图片
	 */
	private GridView imgGridview;
	/**
	 * 相机或相册选择框
	 */
	private ChoosePicDialog choosePicDialog;
	
	/**
	 * 图片选择标识
	 */
	public static int CHOOSE_PIC=0;
	/**
	 * 拍照
	 */
//	public static final int CHOOSE_PIC_CAMERA = 0x001;
	/**
	 * 相册
	 */
//	public static final int CHOOSE_PIC_ALBUM = 0x002;
	private Uri imageUri;
	
	private ImgGridAdapter imgGridAdapter;
	public static Bitmap folderBitmap;
	private UploadUtil uploadUtil;
	
	private Spinner spinner;
	/**
	 * 七牛token
	 */
	public static String qiniuToken;
	/**
	 * 上传图片的名称(放在服务器的名称)
	 */
	private String imageUrl;
	private int schoolId;
	/**
	 * 上传图片的数量
	 */
	private int imgNum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_market_pub);
		initQiniuToken();
		initView();
	}
	
	private void initView() {
		marketPubTitle = (EditText) findViewById(R.id.market_pub_title);
		marketPubName = (EditText) findViewById(R.id.market_pub_name);
		marketPubPrice = (EditText) findViewById(R.id.market_pub_price);
		marketPubDescribe = (EditText) findViewById(R.id.market_pub_describe);
		marketPubContactPhone = (EditText) findViewById(R.id.market_pub_contactPhone);
		imgGridview = (GridView)findViewById(R.id.market_ImgGridview);
		
		initToolbar(getStringByRId(R.string.title_activity_market_pub));
		imgGridAdapter = new ImgGridAdapter(this);
//		imgGridAdapter.update();
		imgGridview.setAdapter(imgGridAdapter);
		
		schoolId = AccountInfoUtils.getScId(this);
		
		/**
		 * 图片上传监听
		 */
		uploadUtil = UploadUtil.getInstance();//初始化
		uploadUtil.setOnUploadProcessListener(this); // 设置监听器监听上传状态
		
		setupSpinner();
		initClick();
	}
	
	private String filename; //图片名称 
	private void initImgUri() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date(System.currentTimeMillis());
		File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		filename = format.format(date);
		File outputImage = new File(path,filename+".jpg");
		try {
            if(outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
        }
		imageUri = Uri.fromFile(outputImage);
	}

	private void initClick() {
		/**
		 * 发布图片列表
		 */
		choosePicDialog =  new ChoosePicDialog(this, R.style.ForwardDialog, R.layout.choose_pic_dialog);
		/**
		 * 监听用户点击选择
		 */
		choosePicDialog.setOnClickListener(new ChoosePicListener() {
			
			@Override
			public void startChooseActivity(int choose) {
//				Log.i(TAG,"MarketPubActivity.CHOOSE_PIC="+MarketPubActivity.CHOOSE_PIC);
				choosePicDialog.dismiss();
				if (choose==Constants.TAKEPHOTO) {
					initImgUri();
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(intent, Constants.TAKEPHOTO);
				}else if(choose==Constants.CHOOSE_PIC) {
					Intent intent = new Intent(MarketPubActivity.this,ImageFileActivity.class);
					startActivityForResult(intent, 100);
				}
				
			}
		});
		
		imgGridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == Bimp.tempSelectBitmap.size()) {
					choosePicDialog.show();
				}
			}
		});
		
	}
	
	private void initQiniuToken(){
		networkHelper.getNetString(URLs.QINIU_TOKEN, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				qiniuToken = new String(responseBody);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
			}
		});
	}
	
	private void setupSpinner() {
		spinner = (Spinner) findViewById(R.id.market_pub_category_spinner);

		spinner.setAdapter(ArrayAdapter.createFromResource(
                this, R.array.goods_category_tabs,
                android.R.layout.simple_spinner_dropdown_item
        ));

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

	/**
	 * 
	 * @author Owater
	 * @createtime 2015-3-23 下午2:18:32
	 * @Decription 发布商品
	 *
	 */
	private void publishGoods(){
		if (isEditText(marketPubTitle)) {
			showButtomToast(getStringByRId(R.string.tip_pubgoods_title));
		}else if(isEditText(marketPubName)) {
			showButtomToast(getStringByRId(R.string.tip_pubgoods_name));
		}else if(isEditText(marketPubPrice)) {
			showButtomToast(getStringByRId(R.string.tip_pubgoods_price));
		}else if (qiniuToken==null) {
			showButtomToast(getStringByRId(R.string.tip_qiniutoken_error));
		}else {
			//重置图片数量，从0开始
			imgNum = 0;
			/**
			 * 上传图片
			 */
			loadingProgressDialog.show();
			if (Bimp.tempSelectBitmap.size()==0){
				postGoodsData();
			}else {
				for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
//				String picTempName = "tmp"+".jpg";
//				File tmpFile = BitmapUtils.BitmapToFile(Bimp.tempSelectBitmap.get(i).getBitmap(),Constants.FILE_TEMP_DIR, picTempName);
//				files.add(tmpFile);
					uploadImage(Bimp.tempSelectBitmap.get(i).getBitmap());
//				qiniu(files.get(i));
//				Bitmap bitmap = Bimp.tempSelectBitmap.get(i).getBitmap();
//				int width = bitmap.getWidth();
//				int height = bitmap.getHeight();
//				String pathString = Bimp.tempSelectBitmap.get(i).getImagePath();
//				Log.i("img", "width===="+width+"height=="+height+",,,,pathString=="+pathString);
				}
			}
		}
	}
	
	private void uploadImage(Bitmap bitmap){
		QiniuUploadUitls.getInstance().uploadImage(bitmap,new QiniuUploadUitlsListener(){

			@Override
			public void onSucess(String fileUrl) {
				if (imageUrl==null) imageUrl=fileUrl;
				else imageUrl+=";"+fileUrl;
				imgNum++;
				if (imgNum>=Bimp.tempSelectBitmap.size()) postGoodsData();
			}

			@Override
			public void onError(int errorCode, String msg) {
			}

			@Override
			public void onProgress(int progress) {
			}
		});
	}
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-4-17 下午8:24:27
	 * @Decription 提交商品数据
	 *
	 */
	private void postGoodsData(){
		GoodsEntity goodsEntity = new GoodsEntity();
		goodsEntity.setSortCode((spinner.getSelectedItemPosition()+1)+"");
		goodsEntity.setGoodTitle(marketPubTitle.getText().toString());
		goodsEntity.setGoodName(marketPubName.getText().toString());
		goodsEntity.setGoodPrice(new BigDecimal(marketPubPrice.getText().toString()));
		goodsEntity.setGoodDescribe(marketPubDescribe.getText().toString());
		goodsEntity.setContactPhone(marketPubContactPhone.getText().toString());
		goodsEntity.setGoodImages(imageUrl);
		goodsEntity.setUserId(MyApplication.userInfo[1]);
		goodsEntity.setSchoolId(schoolId);
		networkHelper.postData(URLs.MARKET_PUBLISH_URL, goodsEntity);
	}
	
	private void qiniu(File data){
		String key="aaa";
		String token=qiniuToken;
		UploadManager uploadManager = new UploadManager();
		uploadManager.put(data, key, token,new UpCompletionHandler() {
			
			@Override
			public void complete(String key, ResponseInfo info, JSONObject response) {
				Log.i("qiniu", "key==="+key);
			}
		}, null);
	}
	
	public boolean isEditText(EditText view){
		if (view.getText().toString().length()>0) {
			return false;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==Constants.TAKEPHOTO) {
			if (Bimp.tempSelectBitmap.size() < Constants.UPLOAD_PIC_NUMS && resultCode == RESULT_OK) {
				Bitmap tmpBitmap = BitmapUtils.decodeUriToBitmap(this, imageUri);
				Log.i("img", "photo之后bm.getWidth()="+tmpBitmap.getWidth()+"bm.getHeight()=="+tmpBitmap.getHeight()+"imgFilePath=="+imageUri);
//				String fileName = String.valueOf(System.currentTimeMillis());
//				Bitmap bm = (Bitmap) data.getExtras().get("data");
//				FileUtils.saveBitmap(bm, fileName);
				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(tmpBitmap);
				Bimp.tempSelectBitmap.add(takePhoto);
			}
			imgGridAdapter.notifyDataSetChanged();
//			if (data != null) {
//				Uri uri = data.getData();
//				if (uri != null) {
//					Bitmap photo = BitmapUtils.decodeUriToBitmap(this, uri);
////					Bitmap smallPhoto = BitmapUtils.compressImage(photo, 190, 100);
//					String picTempName = "tmp.jpg";
//					File tempFile = BitmapUtils.BitmapToFile(Bimp.tempSelectBitmap.get(0).getBitmap(),Constants.FILE_TEMP_DIR, picTempName);
//
//					Map<String, String> params = new HashMap<String, String>();
//					params.put("test", "test");
//					uploadUtil.uploadFile(tempFile.getAbsolutePath(),"fileKey", URLs.MARKET_PUB_URL, params);
//				}
//			}
		}else if (resultCode==Constants.CHOOSE_PIC) {
			imgGridAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onUploadDone(int responseCode, String message) {
//		showButtomToast();
//		Log.i(TAG, getStringByRId(R.string.market_pub_success));
	}

	@Override
	public void onUploadProcess(int uploadSize) {
	}

	@Override
	public void initUpload(int fileSize) {
	}

	/**
	 * 
	 * 重写方法: updateImgGridData|描述:更新图片gridview
	 * 
	 * @see com.siso.app.adapter.ImgGridAdapter.UpdateGridAdapterListener#updateImgGridData()
	 */
	@Override
	public void updateImgGridData() {
//		imgGridAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onRequestSuccess(String response) {
		super.onRequestSuccess(response);
		showButtomToast(getStringByRId(R.string.market_pub_success));
		finish();
	}
	
	@Override
	public void onRequestFail(String response) {
		super.onRequestFail(response);
		if (response.equals(NetworkHelper.NoFileError)) {
			showButtomToast(getStringByRId(R.string.market_pub_fail));
		}else if (response.equals(NetworkHelper.NoConnectionError)) {
			showButtomToast(getStringByRId(R.string.noConnectionError));
		}else if (response.equals(NetworkHelper.TimeoutError)) {
			showButtomToast(getStringByRId(R.string.timeoutError));
		}
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Bimp.tempSelectBitmap.clear();
//		Bimp.tempSelectBitmap = null;
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
			publishGoods();
            return true;
        default:
            return super.onOptionsItemSelected(item);
         }
	}
}
