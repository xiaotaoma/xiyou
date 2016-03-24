package com.model.backstage;

public class Back_recharge {
	private int id;
	private int hid;//角色id
	private String account;//账号
	private String terrace;//平台
	private int zoneid;//区id
	private String orderNumber;//订单号
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getHid() {
		return hid;
	}
	public void setHid(int hid) {
		this.hid = hid;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getTerrace() {
		return terrace;
	}
	public void setTerrace(String terrace) {
		this.terrace = terrace;
	}
	public int getZoneid() {
		return zoneid;
	}
	public void setZoneid(int zoneid) {
		this.zoneid = zoneid;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
}
