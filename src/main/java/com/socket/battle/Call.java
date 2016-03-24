package com.socket.battle;

import com.util.TimeUtil;

import java.util.concurrent.atomic.AtomicInteger;

public class Call extends Member{
	public static AtomicInteger callId = new AtomicInteger();
	
	/**
	 * 
	 * @param sysId
	 * @param id
	 * @param hid
	 * @param x
	 * @param y
	 * @param lastTime
	 */
	public Call(int sysId,int hid,int x,int y,int lastTime,Float att) {
		setHid(callId.decrementAndGet());
		this.sysId = sysId;
		this.father = hid;
		setX(x);
		setY(y);
		this.startTime = TimeUtil.currentTime();
		this.lastTime = lastTime;
		this.setAtt(att);
	}
	private int father;
	private int sysId;
	private float att;
	private int startTime;
	private int lastTime;
	private long attTime;
	public int getSysId() {
		return sysId;
	}
	public void setSysId(int sysId) {
		this.sysId = sysId;
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
	public void setFather(int father) {
		this.father = father;
	}
	public int getFather() {
		return father;
	}
	public void setAttTime(long attTime) {
		this.attTime = attTime;
	}
	public long getAttTime() {
		return attTime;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("hid:").append(getHid()).append(",").append("sysId=").append(sysId).append(",").append("father=").append(father);
		sb.append("lastTime:").append(lastTime).append(",att").append(att);
		return sb.toString();
	}
	public void setAtt(float att) {
		this.att = att;
	}
	public float getAtt() {
		return att;
	}
}
