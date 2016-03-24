package com.model;

public class Mail {
	private int id;
	private int type;//类型
	private int receiverId;//接受角色id
	private int time;//发送时间
	private int money;//元宝
	private int copper;//铜币
	private int remove;//是否删除
	private int activityId;//活动id
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getCopper() {
		return copper;
	}
	public void setCopper(int copper) {
		this.copper = copper;
	}
	public int getRemove() {
		return remove;
	}
	public void setRemove(int remove) {
		this.remove = remove;
	}
	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	public int getActivityId() {
		return activityId;
	}
}
