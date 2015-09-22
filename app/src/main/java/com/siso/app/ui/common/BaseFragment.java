/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.ui.common;

import java.io.UnsupportedEncodingException;

import com.android.volley.Cache.Entry;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.loopj.android.http.RequestParams;
import com.siso.app.common.Constants;
import com.siso.app.common.network.NetworkImpl;
import com.siso.app.ui.R;
import com.siso.app.utils.NetworkCallback;
import com.siso.app.utils.NetworkHelper;
import com.siso.app.widget.LoadingProgressDialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Toast;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-3-26 下午5:08:56
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-3-26 下午5:08:56 
 *
 */
public class BaseFragment extends Fragment
		implements NetworkCallback,com.siso.app.common.network.NetworkCallback {
	
	protected LayoutInflater mInflater;
	/**
	 * 网络请求
	 */
	protected RequestQueue requestQueue;
	protected StringRequest stringRequest;
	protected final String stringRequestTag = "stringRequestTag";
	protected NetworkHelper networkHelper;
	protected LoadingProgressDialog loadingProgressDialog;
	private NetworkImpl networkImpl;

	@Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        mInflater = LayoutInflater.from(getActivity());
        requestQueue = Volley.newRequestQueue(getActivity());
        networkHelper = new NetworkHelper(getActivity(),this);
		networkImpl = new NetworkImpl(getActivity(),this);
        loadingProgressDialog = new LoadingProgressDialog(getActivity());
    }
	
	protected void showToastError(String error){
		String result = NetworkHelper.parseError(error);
		if (result.equals(NetworkHelper.NoConnectionError)) {
			showButtomToast(getResources().getString(R.string.noConnectionError));
		}else if (result.equals(NetworkHelper.TimeoutError)) {
			showButtomToast(getActivity().getResources().getString(R.string.timeoutError));
		}
	}
	
	protected void showButtomToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

	protected void showMiddleToast(String msg) {
		Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	protected String getStringByRId(int id){
		return getResources().getString(id);
	}
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-3-27 下午11:56:42
	 * @Decription 获取缓存
	 *
	 * @param url
	 * @return
	 */
	protected String getCache(String url){
		Entry entry = requestQueue.getCache().get(url);
		String jsonData = null;
		if(entry!=null){
			try {
				jsonData = new String(entry.data,Constants.ENCODING);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return jsonData;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (requestQueue!=null) requestQueue.cancelAll(stringRequestTag);
		loadingProgressDialog.dismiss();
	}

	@Override
	public void onRequestSuccess(String response) {
		loadingProgressDialog.dismiss();
	}

	@Override
	public void onRequestFail(String response) {
		loadingProgressDialog.dismiss();
	}

	protected void postNetwork(String url, RequestParams params, final String tag) {
		networkImpl.loadData(url, params, tag, NetworkImpl.Request.Post);
	}

	@Override
	public void parseJson(int code, String msg, String tag, Object data) {
	}

	@Override
	public void getNetwork(String uri, String tag) {
	}
}