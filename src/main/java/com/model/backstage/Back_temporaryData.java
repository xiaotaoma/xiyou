package com.model.backstage;

/**
 * 临时数据记录
 * 服务器关闭时记录缓存数据到数据库
 * 服务器开启时读取数据到缓存
 * @author Administrator
 *
 */
public class Back_temporaryData {
	public static final int FLAG_ONEDAYOLD = 1;
	public static final int FLAG_MONEYMAP = 2;
	private int id;
	private int flag;//后台数据表编号
	private String data;//
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
}
