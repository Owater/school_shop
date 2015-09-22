/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * description :商品对象
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-3-24 下午1:00:42
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-3-24 下午1:00:42 
 *
 */
public class GoodsEntity implements Serializable {
	
	public final static int CATALOG_SECOND = 1;//二手
	public final static int CATALOG_DBUY = 2;//代购
	public final static int CATALOG_RENT = 3;//出租
	public final static int CATALOG_ASKBUY = 4;//求购
	public final static int CATALOG_OTHER = 5;//其它

	private static final long serialVersionUID = 1L;
	private int id;
	private Date create_time;
	
	/**
	 * 商品标题
	 */
	private String goodTitle;
	private String goodName;
	private String goodDescribe;
	private String goodImages;
	private BigDecimal goodPrice;
	private String contactPhone;
	private Integer status;
	/**
	 * 是否已出售
	 * 0未出售
	 * 1已出售
	 */
	private String isSoldout;
	/**
	 * 商品类别
	 */
	private String sortCode;
	/**
	 * 浏览次数
	 */
	private Integer viewCount;
	private String userId;
	private Integer schoolId;
	private String userName;
	private String avatarUrl;
	private String hxId;
	private double distance;
	private String schoolName;
	
	private boolean isLike;
	private Integer likeCount;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
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
	public String getGoodDescribe() {
		return goodDescribe;
	}
	public void setGoodDescribe(String goodDescribe) {
		this.goodDescribe = goodDescribe;
	}
	public String getGoodImages() {
		return goodImages;
	}
	public void setGoodImages(String goodImages) {
		this.goodImages = goodImages;
	}
	public BigDecimal getGoodPrice() {
		return goodPrice;
	}
	public void setGoodPrice(BigDecimal goodPrice) {
		this.goodPrice = goodPrice;
	}
	public String getIsSoldout() {
		return isSoldout;
	}
	public void setIsSoldout(String isSoldout) {
		this.isSoldout = isSoldout;
	}
	public String getSortCode() {
		return sortCode;
	}
	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}
	public Integer getViewCount() {
		return viewCount;
	}
	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHxId() {
		return hxId;
	}
	public void setHxId(String hxId) {
		this.hxId = hxId;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public boolean isLike() {
		return isLike;
	}
	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}
	public Integer getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}
	
}