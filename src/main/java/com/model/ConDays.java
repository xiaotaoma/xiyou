package com.model;

import com.util.TimeUtil;

public class ConDays {
	private int id;//
	private int copper;//奖励铜币
	private int money;//奖励元宝
	private int time;//达成时间
	private int get;//是否领取   1 已经领取过,0没有领取
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCopper() {
		return copper;
	}
	public void setCopper(int copper) {
		this.copper = copper;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
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
		sb.append(id).append("_").append(copper).append("_").append(money).append("_").append(time).append("_").append(getGet());
		return sb.toString();
	}
	public ConDays(String s) {
		String[] split = s.split("_");
		this.id = Integer.parseInt(split[0]);
		this.copper = Integer.parseInt(split[1]);
		this.money = Integer.parseInt(split[2]);
		this.time = Integer.parseInt(split[3]);
		this.setGet(Integer.parseInt(split[4]));
	}
	public ConDays(int days) {
		switch (days) {
		case 3:this.id = 20002;this.copper=30000;this.money = 0;this.time=TimeUtil.currentTime();this.setGet(0); break;
		case 5:this.id = 20003;this.copper=80000;this.money = 0;this.time=TimeUtil.currentTime();this.setGet(0); break;
		case 8:this.id = 20004;this.copper=180000;this.money = 0;this.time=TimeUtil.currentTime();this.setGet(0); break;
		case 16:this.id = 20005;this.copper=0;this.money = 666;this.time=TimeUtil.currentTime();this.setGet(0); break;
		case 30:this.id = 20006;this.copper=0;this.money = 1588;this.time=TimeUtil.currentTime();this.setGet(0); break;
		default:
			break;
		}
	}
	public void setGet(int get) {
		this.get = get;
	}
	public int getGet() {
		return get;
	}
	
	public static void init(Hero hero) {
		new ConDays(3);
		
	}
}
