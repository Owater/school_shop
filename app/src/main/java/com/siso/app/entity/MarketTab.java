/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.entity;

import com.siso.app.ui.MarketListFragment;
import com.siso.app.ui.R;

/**
 * description : viewpager切换
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-3-29 下午10:14:00
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-3-29 下午10:14:00 
 *
 */
public enum MarketTab {
	SECOND(0,GoodsEntity.CATALOG_SECOND,R.string.title_marketListFragment_second,MarketListFragment.class),
	DBUY(1,GoodsEntity.CATALOG_SECOND,R.string.title_marketListFragment_dbuy,MarketListFragment.class),
	RENT(2,GoodsEntity.CATALOG_SECOND,R.string.title_marketListFragment_rent,MarketListFragment.class),
	ASKBUY(3,GoodsEntity.CATALOG_SECOND,R.string.title_marketListFragment_askbuy,MarketListFragment.class),
	OTHER(4,GoodsEntity.CATALOG_SECOND,R.string.title_marketListFragment_other,MarketListFragment.class);
	
    private int idx;
    private int catalog;
    private int title;
    private Class<?> clz;
    
	private MarketTab(int idx, int catalog, int title, Class<?> clz) {
		this.idx = idx;
		this.catalog = catalog;
		this.title = title;
		this.clz = clz;
	}
	
	public static MarketTab getTabByIdx(int idx) {
        for (MarketTab t : values()) {
            if (t.getIdx() == idx)
                return t;
        }
        return SECOND;
    }

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getCatalog() {
		return catalog;
	}

	public void setCatalog(int catalog) {
		this.catalog = catalog;
	}

	public int getTitle() {
		return title;
	}

	public void setTitle(int title) {
		this.title = title;
	}

	public Class<?> getClz() {
		return clz;
	}

	public void setClz(Class<?> clz) {
		this.clz = clz;
	}
	
}

	