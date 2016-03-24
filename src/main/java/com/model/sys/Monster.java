package com.model.sys;

import java.util.List;

public class Monster {
	private int id;//id
	private String name;//关卡名称
	private String hard;//关卡难度
	private int wave;//波数
	private List<String> monsters;//怪物id
	private int waveNum;//每波数量
	private int addWaveNum;//每10递增数量
	private List<Integer> mixWaves;//混合波数
	private List<Integer> parallelNum;//并行数
	private float conffie;//属性系数
	private float addConffie;//递增系数
	private int hide;//是否隐藏0表示无；1表示有；	2表示随机
	private int relife;//是否再生0表示无；1表示有；	2表示随机.
	private String content;//最后生成内容
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
	public int getWave() {
		return wave;
	}
	public void setWave(int wave) {
		this.wave = wave;
	}
	public List<String> getMonsters() {
		return monsters;
	}
	public void setMonsters(List<String> monsters) {
		this.monsters = monsters;
	}
	public int getWaveNum() {
		return waveNum;
	}
	public void setWaveNum(int waveNum) {
		this.waveNum = waveNum;
	}
	public int getAddWaveNum() {
		return addWaveNum;
	}
	public void setAddWaveNum(int addWaveNum) {
		this.addWaveNum = addWaveNum;
	}
	public List<Integer> getMixWaves() {
		return mixWaves;
	}
	public void setMixWaves(List<Integer> mixWaves) {
		this.mixWaves = mixWaves;
	}
	public List<Integer> getParallelNum() {
		return parallelNum;
	}
	public void setParallelNum(List<Integer> parallelNum) {
		this.parallelNum = parallelNum;
	}
	public float getConffie() {
		return conffie;
	}
	public void setConffie(float conffie) {
		this.conffie = conffie;
	}
	public float getAddConffie() {
		return addConffie;
	}
	public void setAddConffie(float addConffie) {
		this.addConffie = addConffie;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
