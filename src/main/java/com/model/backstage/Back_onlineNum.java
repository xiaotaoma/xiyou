package com.model.backstage;
/**
 * 在线人数，每5分钟统计一次
 * @author Administrator
 *
 */
public class Back_onlineNum {
	private int id;
	private int num;
	private int time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	
}
