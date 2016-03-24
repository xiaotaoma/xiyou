package com.model.backstage;

import com.util.SysUtil;

import java.util.List;

/**
 * 服务器元宝统计
 * @author Administrator
 *
 */
public class Back_money {
	public static final int TYPE_ADD=1;
	public static final int TYPE_REDUCE=2;
	
	private int id;
	private int type;//增加或者减少
	private int num;//增加或者减少的数量
	private int reason;//增加或者减少项目
	private int time;//记录时间
	
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
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getReason() {
		return reason;
	}
	public void setReason(int reason) {
		this.reason = reason;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(type).append("_").append(num).append("_").append(reason).append("_");
		return sb.toString();
	}
	
	public Back_money(String s) {
		List<Integer> list = SysUtil.splitGetInt(s, "_");
		this.type = list.get(0);
		this.num = list.get(1);
		this.reason = list.get(2);
	}
	public Back_money() {
		
	}
	public Back_money(int num,int type,int reason) {
		this.num = num;
		this.type = type;
		this.reason = reason;
	}
	
}
