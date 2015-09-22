package com.siso.app.utils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.siso.app.ui.MarketPubActivity;

public class QiniuUploadUitls {

	private static final String fileName = "temp.jpg";

	private static final String tempJpeg = Environment.getExternalStorageDirectory().getPath() + "/" + fileName;

	private int maxWidth = 350;
	private int maxHeight = 500;
	/**
	 * 七牛token
	 */
	private String qiniuToken;

	public interface QiniuUploadUitlsListener {
		public void onSucess(String fileUrl);
		public void onError(int errorCode, String msg);
		public void onProgress(int progress);
	}

	private QiniuUploadUitls() {

	}

	private static QiniuUploadUitls qiniuUploadUitls = null;

	private UploadManager uploadManager = new UploadManager();

	public static QiniuUploadUitls getInstance() {
		if (qiniuUploadUitls == null) {
			qiniuUploadUitls = new QiniuUploadUitls();
		}
		return qiniuUploadUitls;
	}

	public boolean saveBitmapToJpegFile(Bitmap bitmap, String filePath) {
		return saveBitmapToJpegFile(bitmap, filePath, 70);
	}

	public boolean saveBitmapToJpegFile(Bitmap bitmap, String filePath, int quality) {
		try {
			FileOutputStream fileOutStr = new FileOutputStream(filePath);
			BufferedOutputStream bufOutStr = new BufferedOutputStream(fileOutStr);
			resizeBitmap(bitmap).compress(CompressFormat.JPEG, quality, bufOutStr);
			bufOutStr.flush();
			bufOutStr.close();
		} catch (Exception exception) {
			return false;
		}
		return true;
	}

	/**
	 * 缩小图片
	 * 
	 * @param bitmap
	 * @return
	 */
	public Bitmap resizeBitmap(Bitmap bitmap) {
		if (bitmap != null) {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			if (width > maxWidth) {
				int pWidth = maxWidth;
				int pHeight = maxWidth * height / width;
				Bitmap result = Bitmap.createScaledBitmap(bitmap, pWidth, pHeight, false);
				bitmap.recycle();
				return result;
			}
			if (height > maxHeight) {
				int pHeight = maxHeight;
				int pWidth = maxHeight * width / height;
				Bitmap result = Bitmap.createScaledBitmap(bitmap, pWidth, pHeight, false);
				bitmap.recycle();
				return result;
			}
		}
		return bitmap;
	}

	public void uploadImage(Bitmap bitmap, QiniuUploadUitlsListener listener) {
		saveBitmapToJpegFile(bitmap, tempJpeg);
		uploadImage(tempJpeg, listener);
	}

	public void uploadImage(String filePath, final QiniuUploadUitlsListener listener) {
		final String fileUrlUUID = UUIDGenerator.getUUID();
//		String token = "bu9hpABH3Ymc7IwVEytEp5rXeCy2BwpGiWxV55k5:Zbga7qX6sqM_kS4aIowerFy6o18=:eyJzY29wZSI6Im93YXRlcnRlc3QiLCJkZWFkbGluZSI6MTQyODYzMzI5OH0=";
//		if (token == null) {
//			if (listener != null) {
//				listener.onError(-1, "token is null");
//			}
//			return;
//		}
		uploadManager.put(filePath, fileUrlUUID, MarketPubActivity.qiniuToken, new UpCompletionHandler() {
			public void complete(String key, ResponseInfo info, JSONObject response) {
				if (info != null && info.statusCode == 200) {// 上传成功
//					String fileRealUrl = getRealUrl(fileUrlUUID);
					if (listener != null) {
						listener.onSucess(fileUrlUUID);
					}
				} else {
					if (listener != null) {
						listener.onError(info.statusCode, info.error);
					}
				}
			}
		}, new UploadOptions(null, null, false, new UpProgressHandler() {
			public void progress(String key, double percent) {
				if (listener != null) {
					listener.onProgress((int) (percent * 100));
				}
			}
		}, null));

	}

}
