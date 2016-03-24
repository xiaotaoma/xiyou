package com.model.backstage;

/**
 * 注册数据统计
 * @author Administrator
 *
 */
public class Back_register {
	private int id;
	private int time;//时间戳
	private int register;//总注册人数
	private int newRehister;//新增注册人数
	private int oneDayLogin;//前一天注册角色今天登陆数
	private int threeDayLogin;//前三天注册角色今天登陆
	private int sevenDayLogin;//前七天注册角色今天登陆
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getRegister() {
		return register;
	}
	public void setRegister(int register) {
		this.register = register;
	}
	public int getNewRehister() {
		return newRehister;
	}
	public void setNewRehister(int newRehister) {
		this.newRehister = newRehister;
	}
	public int getOneDayLogin() {
		return oneDayLogin;
	}
	public void setOneDayLogin(int oneDayLogin) {
		this.oneDayLogin = oneDayLogin;
	}
	public void setSevenDayLogin(int sevenDayLogin) {
		this.sevenDayLogin = sevenDayLogin;
	}
	public int getSevenDayLogin() {
		return sevenDayLogin;
	}
	public void setThreeDayLogin(int threeDayLogin) {
		this.threeDayLogin = threeDayLogin;
	}
	public int getThreeDayLogin() {
		return threeDayLogin;
	}
}
