/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.entity;

import java.io.Serializable;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-4-28 下午9:44:33
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-4-28 下午9:44:33 
 *
 */
public class ImageInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String path;
	public long photoId;
	public int width;
	public int height;
    
    public ImageInfo(String path) {
        this.path = path;
    }
    
}

	