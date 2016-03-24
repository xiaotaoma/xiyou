package com.model.sys;

import java.util.List;

public class ArenaMonster {
	private int wave;//
	private List<Integer> monsters;
	private List<Integer> boss;
	private int monsterNum;//
	private List<Integer> list;//并列数
	private int hide;
	private int relife;
	private float coefficient;
	private int bossCoeffi;
	private int copper;
	private int score;
	public int getWave() {
		return wave;
	}
	public void setWave(int wave) {
		this.wave = wave;
	}
	public List<Integer> getMonsters() {
		return monsters;
	}
	public void setMonsters(List<Integer> monsters) {
		this.monsters = monsters;
	}
	public List<Integer> getBoss() {
		return boss;
	}
	public void setBoss(List<Integer> boss) {
		this.boss = boss;
	}
	public int getMonsterNum() {
		return monsterNum;
	}
	public void setMonsterNum(int monsterNum) {
		this.monsterNum = monsterNum;
	}
	public List<Integer> getList() {
		return list;
	}
	public void setList(List<Integer> list) {
		this.list = list;
	}
	public int getHide() {
		return hide;
	}
	public void setHide(int hide) {
		this.hide = hide;
	}
	public int getRelife() {
		return relife;
	}
	public void setRelife(int relife) {
		this.relife = relife;
	}
	public void setCoefficient(float coefficient) {
		this.coefficient = coefficient;
	}
	public float getCoefficient() {
		return coefficient;
	}
	public void setBossCoeffi(int bossCoeffi) {
		this.bossCoeffi = bossCoeffi;
	}
	public int getBossCoeffi() {
		return bossCoeffi;
	}
	public void setCopper(int copper) {
		this.copper = copper;
	}
	public int getCopper() {
		return copper;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getScore() {
		return score;
	}
	
}
