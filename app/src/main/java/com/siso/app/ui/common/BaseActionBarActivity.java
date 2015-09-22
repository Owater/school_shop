/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.ui.common;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.siso.app.chat.applib.controller.HXSDKHelper;
import com.siso.app.common.SystemBarTintManager;
import com.siso.app.ui.R;
import com.siso.app.ui.R.color;
import com.siso.app.ui.R.id;
import com.siso.app.ui.R.string;
import com.siso.app.utils.NetworkCallback;
import com.siso.app.utils.NetworkHelper;
import com.siso.app.widget.LoadingProgressDialog;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-3-14 下午5:39:35
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-3-14 下午5:39:35 
 *
 */
public class BaseActionBarActivity extends UmengActivity
		implements NetworkCallback,OnClickListener {
	
	/**
	 * 沉侵式状态栏管理
	 */
	protected SystemBarTintManager mTintManager;
	/**
	 * 网络请求
	 */
	protected RequestQueue requestQueue;
	protected StringRequest stringRequest;
	protected final String stringRequestTag = "stringRequestTag";
	protected NetworkHelper networkHelper;
//	protected ImageButton back_btn;
	protected LoadingProgressDialog loadingProgressDialog;
	protected Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBeforeSetContentLayout();
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
//        if (hasReturnBtn()) {
//        	back_btn = (ImageButton)findViewById(R.id.mytoolbar_backbtn);
//        	title = (TextView)findViewById(R.id.mytoolbar_title);
//        	back_btn.setOnClickListener(this);
//		}
        requestQueue = Volley.newRequestQueue(this);
        networkHelper = new NetworkHelper(this,this);
        loadingProgressDialog = new LoadingProgressDialog(this);
    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    
    protected void initToolbar(String title){
		toolbar = (Toolbar)findViewById(R.id.toolbar);
		toolbar.setTitle(title);
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.back_btn);
	}
    
    protected void showToastError(String error){
		String result = NetworkHelper.parseError(error);
		if (result.equals(NetworkHelper.NoConnectionError)) {
			showButtomToast(getResources().getString(R.string.noConnectionError));
		}else if (result.equals(NetworkHelper.TimeoutError)) {
			showButtomToast(getResources().getString(R.string.timeoutError));
		}
	}
	
	protected void showButtomToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
	
	protected void onBeforeSetContentLayout() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
	        setTranslucentStatus(true);
	    }
	    mTintManager = new SystemBarTintManager(this);
	    mTintManager.setStatusBarTintEnabled(true);
	    mTintManager.setStatusBarTintResource(R.color.statusbar_bg);
    }
	
	protected int getLayoutId() {
        return 0;
    }
	
	protected String getStringByRId(int id){
		return getResources().getString(id);
	}
	
	protected boolean hasMyToolBar() {
        return true;
    }
	
//	protected boolean hasReturnBtn() {
//        return false;
//    }
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (requestQueue!=null) requestQueue.cancelAll(stringRequestTag);
		loadingProgressDialog.dismiss();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		requestQueue.cancelAll(stringRequestTag);
	}
	
	@Override
	public void onRequestSuccess(String response) {
		loadingProgressDialog.dismiss();
	}

	@Override
	public void onRequestFail(String response) {
		loadingProgressDialog.dismiss();
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onResume() {
		super.onResume();
		// onresume时，取消notification显示
		HXSDKHelper.getInstance().getNotifier().reset();
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
    
}

	