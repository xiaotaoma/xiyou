package com.model;

public class Report {
	public static final int TYPE_ARENA = 1;
	public static final int TYPE_BATTLE = 2;
	
	private int id;
	private int hid;//获胜方
	private int thid;//失败方
	private int time;//
	private String name;//获胜方
	private String tName;//失败方
	private int descId;//描述id
	private int retinue;//获胜方徒弟id
	private int tRetinue;//失败方徒弟id
	private int type;//竞技场或徒弟pk
	private int strength1;//战斗力
	private int strength2;//战斗力
	private int robCopper;//抢夺的铜币
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
	public int getThid() {
		return thid;
	}
	public void setThid(int thid) {
		this.thid = thid;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String gettName() {
		return tName;
	}
	public void settName(String tName) {
		this.tName = tName;
	}
	public void setDescId(int descId) {
		this.descId = descId;
	}
	public int getDescId() {
		return descId;
	}
	public void setRetinue(int retinue) {
		this.retinue = retinue;
	}
	public int getRetinue() {
		return retinue;
	}
	public void settRetinue(int tRetinue) {
		this.tRetinue = tRetinue;
	}
	public int gettRetinue() {
		return tRetinue;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}
	public Report() {
		// TODO Auto-generated constructor stub
	}
	public void setStrength1(int strength1) {
		this.strength1 = strength1;
	}
	public int getStrength1() {
		return strength1;
	}
	public void setStrength2(int strength2) {
		this.strength2 = strength2;
	}
	public int getStrength2() {
		return strength2;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("id=").append(id);
		sb.append(",type=").append(type);
		sb.append(",Hid=").append(hid);
		sb.append(",Name=").append(name);
		sb.append(",DescId=").append(descId);
		sb.append(",Retinue=").append(retinue);
		sb.append(",Strength=").append(strength1);
		return sb.toString();
	}
	public void setRobCopper(int robCopper) {
		this.robCopper = robCopper;
	}
	public int getRobCopper() {
		return robCopper;
	}
}
