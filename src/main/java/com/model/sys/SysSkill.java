package com.model.sys;

public class SysSkill {
	private int id;//技能id
	private String name;
	private int level;//等级
	private int demage;//伤害
	private String effect;//影响属性
	private int lastTime;//持续时间
	private float attSpeed;//攻击速度
	private String zhaohuan;//召唤物
	private int special;//特殊效果
	private int costMP;//消耗
	private float cd;//技能cd
	private int effectsNums;//攻击数量
	private Float range;//施法范围
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getDemage() {
		return demage;
	}
	public void setDemage(int demage) {
		this.demage = demage;
	}
	public String getEffect() {
		return effect;
	}
	public void setEffect(String effect) {
		this.effect = effect;
	}
	public int getLastTime() {
		return lastTime;
	}
	public void setLastTime(int lastTime) {
		this.lastTime = lastTime;
	}
	public float getAttSpeed() {
		return attSpeed;
	}
	public void setAttSpeed(float attSpeed) {
		this.attSpeed = attSpeed;
	}
	public String getZhaohuan() {
		return zhaohuan;
	}
	public void setZhaohuan(String zhaohuan) {
		this.zhaohuan = zhaohuan;
	}
	public int getSpecial() {
		return special;
	}
	public void setSpecial(int special) {
		this.special = special;
	}
	public int getEffectsNums() {
		return effectsNums;
	}
	public void setEffectsNums(int effectsNums) {
		this.effectsNums = effectsNums;
	}
	public Float getRange() {
		return range;
	}
	public void setRange(Float range) {
		this.range = range;
	}
	public void setCostMP(int costMP) {
		this.costMP = costMP;
	}
	public int getCostMP() {
		return costMP;
	}
	public void setCd(float cd) {
		this.cd = cd;
	}
	public float getCd() {
		return cd;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
}
