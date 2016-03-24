package com.socket.battle;

import com.util.TimeUtil;

public class Skill {
	
	public Skill(int skillId,int hid,int lastTime) {
		this.skillId = skillId;
		this.hid = hid;
		this.startTime = TimeUtil.currentTime();
		this.lastTime = lastTime;
	}
	
	private int id;
	private int skillId;
	private int hid;//技能使用角色
	private int startTime;//技能开始时间
	private int lastTime;//持续时间
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSkillId() {
		return skillId;
	}
	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	public int getHid() {
		return hid;
	}
	public void setHid(int hid) {
		this.hid = hid;
	}
	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	public int getLastTime() {
		return lastTime;
	}
	public void setLastTime(int lastTime) {
		this.lastTime = lastTime;
	}
	
	
}
