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
 * @createtime : 2015-4-19 下午2:30:25
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-4-19 下午2:30:25 
 *
 */
public class UserMsgEntity  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer ID;
	private Integer unReadCommNum;
	private Integer allNums;
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public Integer getUnReadCommNum() {
		return unReadCommNum;
	}
	public void setUnReadCommNum(Integer unReadCommNum) {
		this.unReadCommNum = unReadCommNum;
	}
	public Integer getAllNums() {
		return allNums;
	}
	public void setAllNums(Integer allNums) {
		this.allNums = allNums;
	}

}

	