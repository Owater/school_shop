/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.ui.common;

import java.lang.reflect.Method;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.siso.app.ui.R;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-3-26 下午6:33:32
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-3-26 下午6:33:32 
 *
 */
public class FootUpdate {
	
	View mLayout;
	TextView mClick;
	View mLoading;
	
	public void initfooter(Object listView,LayoutInflater inflater,final LoadMore loadMore) {
		View v = inflater.inflate(R.layout.listview_footer, null, false);

        // 为了防止触发listview的onListItemClick事件
        mLayout = v.findViewById(R.id.listview_footer_layout);
        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        mClick = (TextView) v.findViewById(R.id.footer_textView);
        mClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                loadMore.loadMore();
            }
        });

        mLoading = v.findViewById(R.id.footer_progressBar);
        try {
            Method method = listView.getClass().getMethod("addFooterView", View.class);
            method.invoke(listView, v);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        mLayout.setVisibility(View.GONE);
//        showLoading();
	}
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-3-26 下午7:54:30
	 * @Decription 正在加载中
	 *
	 */
	public void showLoading() {
        show(true, true,false,null);
    }
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-3-26 下午7:54:42
	 * @Decription 加载失败
	 *
	 */
	public void showFail(String text) {
        show(true, false,true,text);
    }

	/**
	 * 
	 * @author Owater
	 * @createtime 2015-3-26 下午7:54:52
	 * @Decription 关闭
	 *
	 */
    public void dismiss() {
        show(false, true,false,null);
    }
    
    public void showDone(String text) {
        show(true, false,false,text);
    }
	
	private void show(boolean show, boolean loading,boolean isClickable,String text) {
        if (mLayout == null) {
            return;
        }

        if (show) {
            mLayout.setVisibility(View.VISIBLE);
            mLayout.setPadding(0, 0, 0, 0);
            if (loading) {
                mClick.setVisibility(View.INVISIBLE);
                mLoading.setVisibility(View.VISIBLE);
            } else {
                mClick.setVisibility(View.VISIBLE);
                if(text!=null) mClick.setText(text);
                mClick.setClickable(isClickable);
                mLoading.setVisibility(View.INVISIBLE);
            }
        } else {
            mLayout.setVisibility(View.INVISIBLE);
            mLayout.setPadding(0, -mLayout.getHeight(), 0, 0);
        }
    }
	
	public static interface LoadMore {
        public void loadMore();
    }

}

	