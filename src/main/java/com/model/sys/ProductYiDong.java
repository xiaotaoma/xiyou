package com.model.sys;

public class ProductYiDong {
	private int id;
	private String payCode;//计费代码
	private String name;
	private String price;//价格
	private String dec;//描述
	private int moneyType;//货币类型，2 铜币  ， 1 元宝
	private int moneyNum;//货币数量
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getDec() {
		return dec;
	}
	public void setDec(String dec) {
		this.dec = dec;
	}
	public int getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(int moneyType) {
		this.moneyType = moneyType;
	}
	public int getMoneyNum() {
		return moneyNum;
	}
	public void setMoneyNum(int moneyNum) {
		this.moneyNum = moneyNum;
	}
	
}
