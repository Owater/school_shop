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
 * @createtime : 2015-4-11 下午3:40:22
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-4-11 下午3:40:22 
 *
 */
public class UserEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String userName;
	private int sex;
	private String loginName;
	private String loginPassword;
	private String userPhone;
	private Integer unReadCommMsg;
	/**
	 * 学校
	 */
	private int schoolId;
	private String schoolName;
	/**
	 * 学号
	 */
	private String stuNumber;
	/**
	 * 授权码
	 */
	private String authCode;
	private String hxId;
	/**
	 * 头像
	 */
	private String avatarUrl;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPassword() {
		return loginPassword;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getStuNumber() {
		return stuNumber;
	}
	public void setStuNumber(String stuNumber) {
		this.stuNumber = stuNumber;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public Integer getUnReadCommMsg() {
		return unReadCommMsg;
	}
	public void setUnReadCommMsg(Integer unReadCommMsg) {
		this.unReadCommMsg = unReadCommMsg;
	}
	public int getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(int schoolId) {
		this.schoolId = schoolId;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getHxId() {
		return hxId;
	}
	public void setHxId(String hxId) {
		this.hxId = hxId;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	
}

	