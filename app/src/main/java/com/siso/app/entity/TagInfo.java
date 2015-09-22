package com.siso.app.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-4-30 下午5:44:48
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-4-30 下午5:44:48
 *
 */
public class TagInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer labelId;
	private String title;
	private String link;
	private BigDecimal labelx;
	private BigDecimal labely;
	private Integer type;
	public int orientation; //0代表左边 , 1代表右边
	
	public TagInfo(String title,int orientation) {
		this.title = title;
		this.orientation = orientation;
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLabelId() {
		return labelId;
	}
	public void setLabelId(Integer labelId) {
		this.labelId = labelId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public BigDecimal getLabelx() {
		return labelx;
	}
	public void setLabelx(BigDecimal labelx) {
		this.labelx = labelx;
	}
	public BigDecimal getLabely() {
		return labely;
	}
	public void setLabely(BigDecimal labely) {
		this.labely = labely;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public int getOrientation() {
		return orientation;
	}
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	
	
}
