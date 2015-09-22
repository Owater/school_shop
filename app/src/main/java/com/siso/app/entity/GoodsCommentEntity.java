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
 * @createtime : 2015-3-31 下午2:05:39
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-3-31 下午2:05:39 
 *
 */
public class GoodsCommentEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;//主键
	private Date create_time;//创建时间
	/**
	 * 评论
	 */
	private String comment;
	/**
	 * 用户信息
	 */
	private String userName;
	private String userId;
	private String avatarUrl;
	/**
	 * 主题
	 */
	private Integer goodId;
	private String goodTitle;
	private String goodName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public Integer getGoodId() {
		return goodId;
	}
	public void setGoodId(Integer goodId) {
		this.goodId = goodId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getGoodTitle() {
		return goodTitle;
	}
	public void setGoodTitle(String goodTitle) {
		this.goodTitle = goodTitle;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	
}

	