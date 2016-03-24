package com.model;

import com.util.TimeUtil;

public class Login {
	private String account;
	private String code;//
	private int time;
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public Login(String code,String account) {
		this.code = code;
		this.account = account;
		this.time = TimeUtil.currentTime();
	}
}
