package com.model.backstage;

public class Back_paytaiwan {
	private int id;
	private String account;//账号
	private int hid;//角色id
	private int time;//收到时间
	private int amount;//充值金额(单位：元)
	private int offer;// 额外赠送元宝（单件：个）
	private String system_account;//渠道商用户唯一帐号	string
	private int user_id;//渠道商用户ID	int
	private String orderId;//渠道商订单号String
	private int money;//充值元宝数
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public int getHid() {
		return hid;
	}
	public void setHid(int hid) {
		this.hid = hid;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getOffer() {
		return offer;
	}
	public void setOffer(int offer) {
		this.offer = offer;
	}
	public String getSystem_account() {
		return system_account;
	}
	public void setSystem_account(String system_account) {
		this.system_account = system_account;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	
	
}
