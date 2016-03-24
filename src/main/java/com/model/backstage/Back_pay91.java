package com.model.backstage;

/**
 * 91充值
 * @author Administrator
 *
 */
public class Back_pay91 {
	private int id;//
	private String account;//账号
	private int hid;//角色id
	private int pid;//产品id
	private int orderId;//订单号
	private long time;//时间
	private int money;//元宝
	private int rmb;//rmb（元）
	private String consumeStreamId;//
	private String originalMoney;//
	private String orderMoney;//
	private String note;//
	private int copper;//
	private String terrace;//
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
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public String getConsumeStreamId() {
		return consumeStreamId;
	}
	public void setConsumeStreamId(String consumeStreamId) {
		this.consumeStreamId = consumeStreamId;
	}
	public String getOriginalMoney() {
		return originalMoney;
	}
	public void setOriginalMoney(String originalMoney) {
		this.originalMoney = originalMoney;
	}
	public String getOrderMoney() {
		return orderMoney;
	}
	public void setOrderMoney(String orderMoney) {
		this.orderMoney = orderMoney;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public void setRmb(int rmb) {
		this.rmb = rmb;
	}
	public int getRmb() {
		return rmb;
	}
	public void setCopper(int copper) {
		this.copper = copper;
	}
	public int getCopper() {
		return copper;
	}
	public void setTerrace(String terrace) {
		this.terrace = terrace;
	}
	public String getTerrace() {
		return terrace;
	}
}
