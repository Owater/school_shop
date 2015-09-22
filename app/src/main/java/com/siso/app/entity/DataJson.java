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
 * @createtime : 2015年4月15日 下午4:30:20
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015年4月15日 下午4:30:20 
 *
 */
public class DataJson implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private boolean success;
	private String msg;
	private Object data;
	private int code;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}

	