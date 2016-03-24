package com.model.sys;

public class Shop {
	private int id;//
	private int toolId;//道具id
	private int moneyType;//价格类型1 元宝	2 铜币
	private int price;//价格
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getToolId() {
		return toolId;
	}
	public void setToolId(int toolId) {
		this.toolId = toolId;
	}
	/**
	 * 1 元宝	2 铜币
	 * @return
	 */
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
	
}
