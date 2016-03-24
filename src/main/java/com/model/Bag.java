package com.model;

public class Bag {
	public static final String bag = "001_0_0_0_0_0,002_0_0_0_0_0,003_0_0_0_0_0,004_0_0_0_0_0," +
			"005_0_0_0_0_0,006_0_0_0_0_0,007_0_0_0_0_0,008_0_0_0_0_0,009_0_0_0_0_0,010_0_0_0_0_0," +
			"011_0_0_0_0_0,012_0_0_0_0_0,013_0_0_0_0_0,014_0_0_0_0_0,015_0_0_0_0_0,016_0_0_0_0_0";//12格
	
	private int id;
	private int hid;
	private String data;
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
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Bag() {
		// TODO Auto-generated constructor stub
	}
	public Bag(int hid) {
		this.hid = hid;
		this.data = bag;
	}
	
	public Bag copy() {
		Bag copy = new Bag();
		copy.setData(data);
		copy.setHid(hid);
		copy.setId(id);
		return copy;
	}
}
