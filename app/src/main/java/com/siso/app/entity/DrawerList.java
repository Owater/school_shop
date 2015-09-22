/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.entity;

import java.io.Serializable;

/**
 * description : 侧滑列表对象
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-3-13 下午11:34:46
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-3-13 下午11:34:46 
 *
 */
public class DrawerList implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String text;
	private int imgId;//图片id
	
	public DrawerList(String text, int imgId) {
		super();
		this.text = text;
		this.imgId = imgId;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getImgId() {
		return imgId;
	}
	public void setImgId(int imgId) {
		this.imgId = imgId;
	}
	
}

	