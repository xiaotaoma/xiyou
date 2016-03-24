package com.model.sys;

public class Task {
	private int id;
	private String name;
	private int loop;//循环，每天重置
	private String desc;//
	private String awardDesc;//
	private String award;//奖励
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
	public int getLoop() {
		return loop;
	}
	public void setLoop(int loop) {
		this.loop = loop;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getAwardDesc() {
		return awardDesc;
	}
	public void setAwardDesc(String awardDesc) {
		this.awardDesc = awardDesc;
	}
	public String getAward() {
		return award;
	}
	public void setAward(String award) {
		this.award = award;
	}
	
}
