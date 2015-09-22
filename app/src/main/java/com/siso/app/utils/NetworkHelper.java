/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.utils;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-3-27 下午11:06:52
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-3-27 下午11:06:52 
 *
 */
public class NetworkHelper {
	
	/**
	 * 网络请求超时
	 */
	public static final String TimeoutError = "TimeoutError";
	/**
	 * 没有网络连接
	 */
	public static final String NoConnectionError = "NoConnectionError";
	/**
	 * 没有文件
	 */
	public static final String NoFileError = "NoFileError";
	/**
	 * 请求成功
	 */
	public static final String SUCCESS = "success";
	/**
	 * 网络请求回调
	 */
	private NetworkCallback networkCallback;
	private Context context;
	private static AsyncHttpClient client =new AsyncHttpClient();
	
	public NetworkHelper(){}
	
	public NetworkHelper(Context context,NetworkCallback networkCallback) {
        this.networkCallback = networkCallback;
        this.context = context;
    }
	
	/**
	* 判断网络是否连接
	* @param context
	* @return
	*/
	public static boolean isNetworkConnected(Context context){
		if(context!=null){
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if(mNetworkInfo!=null){
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-3-27 下午11:25:28
	 * @Decription 解析volley返回的错误
	 *
	 * @param error
	 * @return
	 */
	public static String parseError(String error){
		if (error.contains(TimeoutError)) return TimeoutError;
		else if(error.contains(NoConnectionError)) return NoConnectionError;
		return "";
	}
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-4-18 下午9:01:35
	 * @Decription 
	 *
	 * @param stringRequest
	 * @param requestQueue
	 * @param stringRequestTag
	 * @param url
	 */
	public void loadData(StringRequest stringRequest,RequestQueue requestQueue,String stringRequestTag,String url){
		stringRequest = new StringRequest(url, 
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
//						networkCallback.onRequestSuccess(response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
//						networkCallback.onRequestFail(error);
					}
				});
		stringRequest.setTag(stringRequestTag);
		requestQueue.add(stringRequest);
	}
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-4-11 下午3:47:07
	 * @Decription 以实体形式post
	 *
	 * @param url
	 * @param object实体
	 */
	public void postData(String url, Object object){
		new MyAsyncTask(url, object).execute();
	}
	
	public void uploadFileAndParams(final List<File> files,final String fileKey,final String RequestURL,final Map<String, String> params){
		if (isNetworkConnected(context)) {
//			boolean run = true;
//			for(File tmp:files){
//				if (tmp == null || (!tmp.exists())) {
//					Log.i("Network", "文件不存在");
//					networkCallback.onRequestFail(NoFileError);
//					run = false;
//					break;
//				}
//			}
			new UploadAsyncTask(files, fileKey, RequestURL, params).execute();
		}else {
			networkCallback.onRequestFail(NoConnectionError);
		}
	}
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-4-17 下午5:38:03
	 * @Decription 获取网络string对象
	 *
	 * @param url
	 * @param res
	 */
	public void getNetString(String url,AsyncHttpResponseHandler res){
		client.get(url, res);
	}
	
	public void getNetJson(String url,RequestParams params,AsyncHttpResponseHandler res){
		client.get(url,params,res);
	}
	
	private class MyAsyncTask extends AsyncTask<Void, Integer, Boolean> {
		
		private Object object;
		private String url;
		private String response;

		public MyAsyncTask(String url, Object object) {
			this.object = object;
			this.url = url;
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			if (object!=null&&url!=null) {
				response = HttpUtils.requestGson(url, object);
			}else {
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				networkCallback.onRequestSuccess(response);
			}else {
				networkCallback.onRequestFail(NoConnectionError);
			}
		}

	}
	
	private class UploadAsyncTask extends AsyncTask<Void, Integer, Boolean> {
		
		private List<File> files;
		private String fileKey;
		private String RequestURL;
		private Map<String, String> params;

		public UploadAsyncTask(List<File> files,String fileKey,String RequestURL,Map<String, String> params) {
			this.files = files;
			this.fileKey = fileKey;
			this.RequestURL = RequestURL;
			this.params = params;
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			new UploadUtil().toUploadFiles(files, fileKey, RequestURL, params);
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				networkCallback.onRequestSuccess(SUCCESS);
			}else {
				networkCallback.onRequestFail(NoConnectionError);
			}
		}

	}
	
}

	