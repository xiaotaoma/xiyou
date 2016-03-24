package com.model;

public class Arena {
	public Arena() {
		// TODO Auto-generated constructor stub
	}
	private int id;
	private int hid;//角色id
	private String name;
	private int rank;//排名
	private int strength;//战斗力
	private int prestige;//威望
	private int retinue;//随从
	private int times;//可挑战次数
	private int time;//挑战时间
	private String achievement;//最好成绩[mapId,wave][mapid,wave]
	private int score;
	private int wave;//
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
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getStrength() {
		return strength;
	}
	public void setStrength(int strength) {
		this.strength = strength;
	}
	public int getPrestige() {
		return prestige;
	}
	public void setPrestige(int prestige) {
		this.prestige = prestige;
	}
	public int getRetinue() {
		return retinue;
	}
	public void setRetinue(int retinue) {
		this.retinue = retinue;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getTime() {
		return time;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setAchievement(String achievement) {
		this.achievement = achievement;
	}
	public String getAchievement() {
		return achievement;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("id:").append(id).append(" hid:").append(hid);
		sb.append(" name:").append(name);
		sb.append(" rank:").append(rank);
		sb.append(" strength:").append(strength);
		sb.append(" prestige:").append(prestige);
		sb.append(" retinue:").append(retinue);
		sb.append(" times:").append(times);
		sb.append(" time:").append(time);
		sb.append(" achievement:").append(achievement);
		return sb.toString();
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getScore() {
		return score;
	}
	public void setWave(int wave) {
		this.wave = wave;
	}
	public int getWave() {
		return wave;
	}
}
