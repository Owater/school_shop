/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.utils;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-3-31 上午9:47:01
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-3-31 上午9:47:01 
 *
 */
public interface NetworkCallback {
	
	public void onRequestSuccess(String response);
	
	public void onRequestFail(String response);
	
}

	