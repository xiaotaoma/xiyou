package com.model.sys;

public class Product {
	private int id;
	private String name;
	private int moneyType;// 1 元宝， 2 铜币
	private int num;
	private int price;
	private String product_id;
	private int apple_id;
	private String description;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public int getApple_id() {
		return apple_id;
	}
	public void setApple_id(int apple_id) {
		this.apple_id = apple_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[id,").append(id).append("],");
		sb.append("[name,").append(name).append("],");
		sb.append("[price,").append(price).append("],");
		sb.append("[product_id,").append(product_id).append("],");
		sb.append("[apple_id,").append(apple_id).append("],");
		sb.append("[description,").append(description).append("]");
		return sb.toString();
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getNum() {
		return num;
	}
	public void setMoneyType(int moneyType) {
		this.moneyType = moneyType;
	}
	public int getMoneyType() {
		return moneyType;
	}

}
