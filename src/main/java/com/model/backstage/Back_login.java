package com.model.backstage;
/**
 * 每日登陆统计
 * @author Administrator
 *
 */
public class Back_login {
	private int id;
	private int time;
	private int hid;
	private int times;//登陆次数
	private int totalTime;//在线总时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public int getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}
	public void setHid(int hid) {
		this.hid = hid;
	}
	public int getHid() {
		return hid;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getTime() {
		return time;
	}
	public Back_login(int time,int hid) {
		this.time = time;
		this.hid = hid;
	}
	public Back_login() {
		// TODO Auto-generated constructor stub
	}
}
