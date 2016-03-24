package com.model.sys;

public class SysCall {	
	private int sysId;//、id
	private String name;//名字
	private float demage;//继承自身伤害
	private float attSpeed;//攻击速度
	private float moveSpeed;//移动速度
	private float range;//攻击范围
	private int attType;//攻击类型
	private int penetrate;//是否穿透
	private int lastTime;//持续时间
	private int skillId;//技能id
	public int getSysId() {
		return sysId;
	}
	public void setSysId(int sysId) {
		this.sysId = sysId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getAttSpeed() {
		return attSpeed;
	}
	public void setAttSpeed(float attSpeed) {
		this.attSpeed = attSpeed;
	}
	public float getMoveSpeed() {
		return moveSpeed;
	}
	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
	public float getRange() {
		return range;
	}
	public void setRange(float range) {
		this.range = range;
	}
	public int getAttType() {
		return attType;
	}
	public void setAttType(int attType) {
		this.attType = attType;
	}
	public int getPenetrate() {
		return penetrate;
	}
	public void setPenetrate(int penetrate) {
		this.penetrate = penetrate;
	}
	public int getLastTime() {
		return lastTime;
	}
	public void setLastTime(int lastTime) {
		this.lastTime = lastTime;
	}
	public int getSkillId() {
		return skillId;
	}
	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	public void setDemage(float demage) {
		this.demage = demage;
	}
	public float getDemage() {
		return demage;
	}
}
