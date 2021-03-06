package com.model.sys;

public class ProductDianxin {
	private int id;//编号	
	private String good;//商品
	private int price;//价格		
	private String productName;//商品名称
	private int moneyType;//货币类型
	private int num;//货币数量
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGood() {
		return good;
	}
	public void setGood(String good) {
		this.good = good;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(int moneyType) {
		this.moneyType = moneyType;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[id=").append(id).append("],");
		sb.append("[good=").append(good).append("],");
		sb.append("[price=").append(price).append("],");
		sb.append("[productName=").append(productName).append("],");
		sb.append("[moneyType=").append(moneyType).append("],");
		sb.append("[num=").append(num).append("]");
		return sb.toString();
	}

}
