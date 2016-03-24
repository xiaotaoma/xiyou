package com.socket.battle;

public class Member {
	private int hid;
	private int x;
	private int y;
	private float hp;
	private float hpMax;
	private float mp;
	private float mpMax;
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public float getHp() {
		return hp;
	}
	public void setHp(float hp) {
		this.hp = hp;
	}
	public float getHpMax() {
		return hpMax;
	}
	public void setHpMax(float hpMax) {
		this.hpMax = hpMax;
	}
	public float getMp() {
		return mp;
	}
	public void setMp(float mp) {
		this.mp = mp;
	}
	public float getMpMax() {
		return mpMax;
	}
	public void setMpMax(float mpMax) {
		this.mpMax = mpMax;
	}
	public void setHid(int hid) {
		this.hid = hid;
	}
	public int getHid() {
		return hid;
	}
	
}
