package com.model.sys;

import java.util.List;

public class BarriersScence {
	private int id;
	private String name;//关卡名称
	private String hard;//难度
	private List<String> easy;//简单
	private List<String> ordinary;//普通
	private List<String> difficult;//困难
	private int interval;//下一波间隔
	private String relifeInterval;//野生复活时间
	private int vigor;//精力
	private int evil;//初始恶灵
	private int exp;//经验
	private int copper;//铜币
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
	public String getHard() {
		return hard;
	}
	public void setHard(String hard) {
		this.hard = hard;
	}
	public List<String> getEasy() {
		return easy;
	}
	public void setEasy(List<String> easy) {
		this.easy = easy;
	}
	public List<String> getOrdinary() {
		return ordinary;
	}
	public void setOrdinary(List<String> ordinary) {
		this.ordinary = ordinary;
	}
	public List<String> getDifficult() {
		return difficult;
	}
	public void setDifficult(List<String> difficult) {
		this.difficult = difficult;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	public String getRelifeInterval() {
		return relifeInterval;
	}
	public void setRelifeInterval(String relifeInterval) {
		this.relifeInterval = relifeInterval;
	}
	public int getVigor() {
		return vigor;
	}
	public void setVigor(int vigor) {
		this.vigor = vigor;
	}
	public int getEvil() {
		return evil;
	}
	public void setEvil(int evil) {
		this.evil = evil;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public int getCopper() {
		return copper;
	}
	public void setCopper(int copper) {
		this.copper = copper;
	}
	
}
