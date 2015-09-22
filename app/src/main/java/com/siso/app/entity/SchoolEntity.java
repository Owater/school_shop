/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-4-16 下午4:34:59
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-4-16 下午4:34:59 
 *
 */
public class SchoolEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String schoolName;
	private String address;
	/**
	 * 纬度
	 */
	private BigDecimal latitude;
	/**
	 * 经度
	 */
	private BigDecimal lontitude;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public BigDecimal getLatitude() {
		return latitude;
	}
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	public BigDecimal getLontitude() {
		return lontitude;
	}
	public void setLontitude(BigDecimal lontitude) {
		this.lontitude = lontitude;
	}

}

	