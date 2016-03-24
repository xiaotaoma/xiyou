package com.model;

public class Account {
	private int id;
	private String account;
	private int registerTime;
	private String createIp;
	private String terrace;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(int registerTime) {
		this.registerTime = registerTime;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAccount() {
		return account;
	}
	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}
	public String getCreateIp() {
		return createIp;
	}
	public void setTerrace(String terrace) {
		this.terrace = terrace;
	}
	public String getTerrace() {
		return terrace;
	}
	
}
