/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015年5月2日 下午4:56:40
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015年5月2日 下午4:56:40 
 *
 */
public class FindListJsonEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Date create_time;
	private String imgUrl;
	private String desp;
	private Integer labelId;
	private String userId;
	private String userName;
	private List<TagInfo> findLabelEntities;
	
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
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getDesp() {
		return desp;
	}
	public void setDesp(String desp) {
		this.desp = desp;
	}
	public Integer getLabelId() {
		return labelId;
	}
	public void setLabelId(Integer labelId) {
		this.labelId = labelId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<TagInfo> getFindLabelEntities() {
		return findLabelEntities;
	}
	public void setFindLabelEntities(List<TagInfo> findLabelEntities) {
		this.findLabelEntities = findLabelEntities;
	}
}

	