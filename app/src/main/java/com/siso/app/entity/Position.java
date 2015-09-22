package com.siso.app.entity;

public class Position {
	
	private int xplace;
	private int yplace;
	public Position() {}
	public Position(int xplace, int yplace) {
		super();
		this.xplace = xplace;
		this.yplace = yplace;
	}
	public int getXplace() {
		return xplace;
	}
	public void setXplace(int xplace) {
		this.xplace = xplace;
	}
	public int getYplace() {
		return yplace;
	}
	public void setYplace(int yplace) {
		this.yplace = yplace;
	}
	

}
