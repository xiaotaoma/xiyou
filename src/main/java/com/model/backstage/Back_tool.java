package com.model.backstage;

public class Back_tool {
	public static final int TYPE_ADD = 1;
	public static final int TYPE_REDUCE = 2;
	
	public static final int REASON_GIFT = 1;//赠送
	public static final int REASON_BACKDOOR = 2;//后门
	public static final int REASON_BUYFROMSHOP = 3;//商店购买
	public static final int REASON_USETOOL = 4;//使用物品获得
	public static final int REASON_USE = 5;//使用物品
	public static final int REASON_LOOT = 6;//掉落
	
	private int time;//
	private int hid;//角色id
	private int type;//类型
	private int reason;//原因
	private int toolId;//道具id
	private int hasNum;//变化前数量
	private int leftNum;//变化后数量
	private int num;//变化数量
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getHid() {
		return hid;
	}
	public void setHid(int hid) {
		this.hid = hid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getReason() {
		return reason;
	}
	public void setReason(int reason) {
		this.reason = reason;
	}
	public int getToolId() {
		return toolId;
	}
	public void setToolId(int toolId) {
		this.toolId = toolId;
	}
	public int getHasNum() {
		return hasNum;
	}
	public void setHasNum(int hasNum) {
		this.hasNum = hasNum;
	}
	public int getLeftNum() {
		return leftNum;
	}
	public void setLeftNum(int leftNum) {
		this.leftNum = leftNum;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
}
