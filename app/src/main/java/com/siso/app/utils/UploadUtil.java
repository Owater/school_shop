package com.siso.app.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.util.Log;

public class UploadUtil {
	private static UploadUtil uploadUtil;
	private static final String BOUNDARY =  UUID.randomUUID().toString(); // 边界标识 随机生成
	private static final String PREFIX = "--";
	private static final String LINE_END = "\r\n";
	private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型

	/**
	 * 单例模式获取上传工具类
	 * @return
	 */
	public static UploadUtil getInstance() {
		if (null == uploadUtil) {
			uploadUtil = new UploadUtil();
		}
		return uploadUtil;
	}

	private static final String TAG = "UploadUtil";
	private int readTimeOut = 20 * 1000; // 读取超时
	private int connectTimeout = 20 * 1000; // 超时时间
	/***
	 * 请求使用多长时间
	 */
	private static int requestTime = 0;
	
	private static final String CHARSET = "utf-8"; // 设置编码

	/***
	 * 上传成功
	 */
	public static final int UPLOAD_SUCCESS_CODE = 1;
	/**
	 * 文件不存在
	 */
	public static final int UPLOAD_FILE_NOT_EXISTS_CODE = 2;
	/**
	 * 服务器出错
	 */
	public static final int UPLOAD_SERVER_ERROR_CODE = 3;
	protected static final int WHAT_TO_UPLOAD = 1;
	protected static final int WHAT_UPLOAD_DONE = 2;
	
	/**
	 * android上传文件到服务器
	 * 
	 * @param filePath
	 *            需要上传的文件的路径
	 * @param fileKey
	 *            在网页上<input type=file name=xxx/> xxx就是这里的fileKey
	 * @param RequestURL
	 *            请求的URL
	 */
	public void uploadFile(String filePath, String fileKey, String RequestURL,
			Map<String, String> param) {
		if (filePath == null) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,"文件不存在");
			return;
		}
		try {
			File file = new File(filePath);
			uploadFile(file, fileKey, RequestURL, param);
		} catch (Exception e) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,"文件不存在");
			e.printStackTrace();
			return;
		}
	}

	/**
	 * android上传文件到服务器
	 * 
	 * @param file
	 *            需要上传的文件
	 * @param fileKey
	 *            在网页上<input type=file name=xxx/> xxx就是这里的fileKey
	 * @param RequestURL
	 *            请求的URL
	 */
	public void uploadFile(final File file, final String fileKey,
			final String RequestURL, final Map<String, String> param) {
		if (file == null || (!file.exists())) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,"文件不存在");
			return;
		}

		Log.i(TAG, "请求的URL=" + RequestURL);
		Log.i(TAG, "请求的fileName=" + file.getName());
		Log.i(TAG, "请求的fileKey=" + fileKey);
		new Thread(new Runnable() {  //开启线程上传文件
			@Override
			public void run() {
				toUploadFile(file, fileKey, RequestURL, param);
			}
		}).start();
		
	}

	private void toUploadFile(File file, String fileKey, String RequestURL,
			Map<String, String> param) {
		String result = null;
		requestTime= 0;
		
		long requestTime = System.currentTimeMillis();
		long responseTime = 0;

		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(readTimeOut);
			conn.setConnectTimeout(connectTimeout);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
//			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			/**
			 * 当文件不为空，把文件包装并且上传
			 */
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			StringBuffer sb = null;
			String params = "";
			
			/***
			 * 以下是用于上传参数
			 */
			if (param != null && param.size() > 0) {
				Iterator<String> it = param.keySet().iterator();
				while (it.hasNext()) {
					sb = null;
					sb = new StringBuffer();
					String key = it.next();
					String value = param.get(key);
					sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
					sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
					sb.append(value).append(LINE_END);
					params = sb.toString();
					Log.i(TAG, key+"="+params+"##");
					dos.write(params.getBytes());
//					dos.flush();
				}
			}
			
			sb = null;
			params = null;
			sb = new StringBuffer();
			/**
			 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
			 * filename是文件的名字，包含后缀名的 比如:abc.png
			 */
			if (file != null) {
				sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
				sb.append("Content-Disposition:form-data; name=\"" + fileKey
						+ "\"; filename=\"" + file.getName() + "\"" + LINE_END);
				sb.append("Content-Type:image/jpeg" + LINE_END); // 这里配置的Content-type很重要的
																	// ，用于服务器端辨别文件的类型的
				sb.append(LINE_END);
				params = sb.toString();
				sb = null;

				Log.i(TAG, file.getName() + "=" + params + "##");
				dos.write(params.getBytes());
				/** 上传文件 */
				InputStream is = new FileInputStream(file);
				Log.i(TAG, "##文件长度"+(file.length()/1024));
				onUploadProcessListener.initUpload((int) file.length());
				byte[] bytes = new byte[1024];
				int len = 0;
				int curLen = 0;
				while ((len = is.read(bytes)) != -1) {
					curLen += len;
					dos.write(bytes, 0, len);
					onUploadProcessListener.onUploadProcess(curLen);
				}
				is.close();

				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				dos.flush();
			}
//			
//			dos.write(tempOutputStream.toByteArray());
			/**
			 * 获取响应码 200=成功 当响应成功，获取响应的流
			 */
			int res = conn.getResponseCode();
			responseTime = System.currentTimeMillis();
			this.requestTime = (int) ((responseTime-requestTime)/1000);
			Log.e(TAG, "response code:" + res);
			if (res == 200) {
				Log.e(TAG, "request success");
				InputStream input = conn.getInputStream();
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while ((ss = input.read()) != -1) {
					sb1.append((char) ss);
				}
				result = sb1.toString();
				Log.e(TAG, "result : " + result);
				sendMessage(UPLOAD_SUCCESS_CODE, "上传结果："
						+ result);
				return;
			} else {
				Log.e(TAG, "request error");
				sendMessage(UPLOAD_SERVER_ERROR_CODE,"上传失败：code=" + res);
				return;
			}
		} catch (MalformedURLException e) {
			sendMessage(UPLOAD_SERVER_ERROR_CODE,"上传失败：error=" + e.getMessage());
			e.printStackTrace();
			return;
		} catch (IOException e) {
			sendMessage(UPLOAD_SERVER_ERROR_CODE,"上传失败：error=" + e.getMessage());
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-3-17 下午11:44:11
	 * @Decription 多文件上传
	 *
	 * @param files 多文件
	 * @param fileKey
	 * @param RequestURL
	 * @param param
	 */
//	public void uploadFiles(final List<File> files,final String fileKey,final String RequestURL,final Map<String, String> param) {
//		
//		for(File tmp:files){
//			if (tmp == null || (!tmp.exists())) {
//				sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,"文件不存在");
//				return;
//			}
//		}
//		new Thread(new Runnable() {  //开启线程上传文件
//			@Override
//			public void run() {
//				toUploadFiles(files, fileKey, RequestURL, param);
//			}
//		}).start();
//	}
	
	public void toUploadFiles(final List<File> files, String fileKey, String RequestURL,Map<String, String> param) {
		String result = null;
		requestTime= 0;
		
		long requestTime = System.currentTimeMillis();
		long responseTime = 0;

		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(readTimeOut);
			conn.setConnectTimeout(connectTimeout);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
			
			/**
			 * 当文件不为空，把文件包装并且上传
			 */
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			StringBuffer sb = null;
			String params = "";
			
			/***
			 * 以下是用于上传参数
			 */
			if (param != null && param.size() > 0) {
				Iterator<String> it = param.keySet().iterator();
				while (it.hasNext()) {
					sb = null;
					sb = new StringBuffer();
					String key = it.next();
					String value = param.get(key);
					sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
					sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
					sb.append(value).append(LINE_END);
					params = sb.toString();
					Log.i(TAG, key+"="+params+"##");
					dos.write(params.getBytes());
				}
			}
			
			if (files != null) {
				InputStream is =null;
				Log.i(TAG, "files.size()=="+files.size());
				int i=0;
				for(File file:files){
					i++;
					params = null;
					sb = new StringBuffer();
					sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
					sb.append("Content-Disposition:form-data; name=\"" + fileKey+ "\"; filename=\"" + file.getName() + "\"" + LINE_END);
					sb.append("Content-Type:image/jpeg" + LINE_END); // 这里配置的Content-type很重要的，用于服务器端辨别文件的类型的
					sb.append(LINE_END);
					params = sb.toString();
					sb = null;

					Log.i(TAG, file.getName() + "=" + params + "##");
					dos.write(params.getBytes());
					/** 上传文件 */
					is = null;
					is = new FileInputStream(file);
//					Log.i(TAG, "##文件长度"+(file.length()/1024));
//					onUploadProcessListener.initUpload((int) file.length());
					byte[] bytes = new byte[1024];
					int len = 0;
					int curLen = 0;
					while ((len = is.read(bytes)) != -1) {
						curLen += len;
						dos.write(bytes, 0, len);
//						onUploadProcessListener.onUploadProcess(curLen);
					}
					
					dos.write(LINE_END.getBytes());
				}
				
				/**
				 * 结束标识
				 */
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
				dos.write(end_data);
				Log.i(TAG,"i=="+i);
				if (is!=null) is.close();
				dos.flush();
				dos.close();
			}

			/**
			 * 获取响应码 200=成功 当响应成功，获取响应的流
			 */
			int res = conn.getResponseCode();
			responseTime = System.currentTimeMillis();
			this.requestTime = (int) ((responseTime-requestTime)/1000);
			Log.e(TAG, "response code:" + res);
			if (res == 200) {
				Log.e(TAG, "request success");
				InputStream input = conn.getInputStream();
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while ((ss = input.read()) != -1) {
					sb1.append((char) ss);
				}
				result = sb1.toString();
				Log.e(TAG, "result : " + result);
//				sendMessage(UPLOAD_SUCCESS_CODE, "上传结果："
//						+ result);
				return;
			} else {
				Log.e(TAG, "request error");
//				sendMessage(UPLOAD_SERVER_ERROR_CODE,"上传失败：code=" + res);
				return;
			}
		} catch (MalformedURLException e) {
//			sendMessage(UPLOAD_SERVER_ERROR_CODE,"上传失败：error=" + e.getMessage());
			e.printStackTrace();
			return;
		} catch (IOException e) {
//			sendMessage(UPLOAD_SERVER_ERROR_CODE,"上传失败：error=" + e.getMessage());
			e.printStackTrace();
			return;
		}
	}
	
	public void uploadFiles(final File file, final String fileKey,final String RequestURL, final Map<String, String> param) {
		if (file == null || (!file.exists())) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,"文件不存在");
			return;
		}

		Log.i(TAG, "请求的URL=" + RequestURL);
		Log.i(TAG, "请求的fileName=" + file.getName());
		Log.i(TAG, "请求的fileKey=" + fileKey);
		new Thread(new Runnable() {  //开启线程上传文件
			@Override
			public void run() {
				toUploadFile(file, fileKey, RequestURL, param);
			}
		}).start();
	}

	/**
	 * 发送上传结果
	 * @param responseCode
	 * @param responseMessage
	 */
	private void sendMessage(int responseCode,String responseMessage)
	{
//		onUploadProcessListener.onUploadDone(responseCode, responseMessage);
	}
	
	/**
	 * 下面是一个自定义的回调函数，用到回调上传文件是否完成
	 * 
	 * @author shimingzheng
	 * 
	 */
	public static interface OnUploadProcessListener {
		/**
		 * 上传响应
		 * @param responseCode
		 * @param message
		 */
		void onUploadDone(int responseCode, String message);
		/**
		 * 上传中
		 * @param uploadSize
		 */
		void onUploadProcess(int uploadSize);
		/**
		 * 准备上传
		 * @param fileSize
		 */
		void initUpload(int fileSize);
	}
	private OnUploadProcessListener onUploadProcessListener;
	
	

	public void setOnUploadProcessListener(
			OnUploadProcessListener onUploadProcessListener) {
		this.onUploadProcessListener = onUploadProcessListener;
	}

	public int getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	/**
	 * 获取上传使用的时间
	 * @return
	 */
	public static int getRequestTime() {
		return requestTime;
	}
	
	public static interface uploadProcessListener{
		
	}
	
//	public void upload() {
//		ByteArrayOutputStream stream = new ByteArrayOutputStream();
//		String picPath="";
//		Bitmap upbitmap = new BitmapFactory().decodeFile(picPath);
//		upbitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
//		byte[] b = stream.toByteArray();
//		// 将图片流以字符串形式存储下来
//		String file = new String(Base64Coder.encodeLines(b));
//		HttpClient client = new DefaultHttpClient();
//		// 设置上传参数
//		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
//		formparams.add(new BasicNameValuePair("contract", file));
//		formparams.add(new BasicNameValuePair("buyMoney", "213"));
//		HttpPost post = new HttpPost(URLs.FEEDBACK_URL);
//		UrlEncodedFormEntity entity;
//		try {
//			entity = new UrlEncodedFormEntity(formparams, "UTF-8");
//			post.addHeader("Accept",
//					"text/javascript, text/html, application/xml, text/xml");
//			post.addHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
//			post.addHeader("Accept-Encoding", "gzip,deflate,sdch");
//			post.addHeader("Connection", "Keep-Alive");
//			post.addHeader("Cache-Control", "no-cache");
//			post.addHeader("Content-Type", "application/x-www-form-urlencoded");
//			post.setEntity(entity);
//			HttpResponse response = client.execute(post);
//			System.out.println(response.getStatusLine().getStatusCode());
//			HttpEntity e = response.getEntity();
//			System.out.println(EntityUtils.toString(e));
//			if (200 == response.getStatusLine().getStatusCode()) {
//				System.out.println("上传完成");
//			} else {
//				System.out.println("上传失败");
//			}
//			client.getConnectionManager().shutdown();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	
}
