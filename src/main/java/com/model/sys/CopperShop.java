package com.model.sys;

public class CopperShop {
	private int id;
	private int moneyType;//货币类型
	private int price;//价格
	private String name;//商品名乘
	private String context;//描述
	private int copper;//兑换获得铜币
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(int moneyType) {
		this.moneyType = moneyType;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public int getCopper() {
		return copper;
	}
	public void setCopper(int copper) {
		this.copper = copper;
	}
	
}
