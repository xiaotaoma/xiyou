package com.model;

public class Announcement {
	private int id;
	private int time;//公告时间
	private String content;//公告内容
	private String name;//发出公告人
	private int canuse;//是否可用,显示
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setCanuse(int canuse) {
		this.canuse = canuse;
	}
	public int getCanuse() {
		return canuse;
	}
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis()/1000);
	}
}
