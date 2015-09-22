/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-4-24 下午1:59:24
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-4-24 下午1:59:24 
 *
 */
public class FeedBackEntity implements Serializable {
	
	 /** 
	  * serialVersionUID
	  */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Date create_time;//创建时间
	private String title;
	private String content;
	private String userId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}

	