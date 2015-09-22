/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.ui.common;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-3-26 下午5:14:56
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-3-26 下午5:14:56 
 *
 */
public class RefreshBaseFragment extends BaseFragment {
	protected final int STATE_NONE = 0;
	protected final int STATE_REFRESH = 1;
	protected final int STATE_LOADMORE = 2;
	protected final int STATE_DONE = 3;
	protected int mState = STATE_NONE;
	
	protected SwipeRefreshLayout swipeRefreshLayout;
	protected FootUpdate mfooUpdate = new FootUpdate();
	private View mView;

    protected void setRefreshing(boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

    protected boolean isRefreshing() {
        return swipeRefreshLayout.isRefreshing();
    }

    protected void init(View mView) {
    	this.mView=mView;
        initSwipeLayout(swipeRefreshLayout);
    }
    
    private void initSwipeLayout(SwipeRefreshLayout swipeLayout) {
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.transparent,
                android.R.color.holo_blue_light,
                android.R.color.transparent
        );
    }

}

	