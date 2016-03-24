package com.model.backstage;

public class Back_pay_dianxin {
	private int id;//订单号
	private int zoneid;//区id
	private String account;//账号
	private int hid;//角色id
	private int time;//时间 毫秒
	private String serialno;//业务流水号 
	private int money;//充值金额，元
	private String payType;//aidou:爱豆支付; szf:神州付;	alipay:支付宝;xmobo:掌中付;sms:短信
	private int copper;//充值铜币
	private int orderId;//订单号
	private int give;//是否发放 0：没有发放 ，1：已经发放
	private int pid;//productId
	private int giveTime;//发放时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getZoneid() {
		return zoneid;
	}
	public void setZoneid(int zoneid) {
		this.zoneid = zoneid;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getSerialno() {
		return serialno;
	}
	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public int getCopper() {
		return copper;
	}
	public void setCopper(int copper) {
		this.copper = copper;
	}
	public void setHid(int hid) {
		this.hid = hid;
	}
	public int getHid() {
		return hid;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getMoney() {
		return money;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setGive(int give) {
		this.give = give;
	}
	public int getGive() {
		return give;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getPid() {
		return pid;
	}
	public void setGiveTime(int giveTime) {
		this.giveTime = giveTime;
	}
	public int getGiveTime() {
		return giveTime;
	}
	
}	
