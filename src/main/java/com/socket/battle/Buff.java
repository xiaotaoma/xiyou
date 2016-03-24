package com.socket.battle;

import com.util.TimeUtil;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 技能buff效果，只有攻击力增加有效
 * @author Administrator
 *
 */
public class Buff {
	/**
	 * 定身
	 */
	public static final int NOMOVE = 1;//
	/**
	 * 烧伤
	 */
	public static final int BURN = 2;//
	/**
	 * 攻击力增加
	 */
	public static final int UPATT = 3;//
	/**
	 *  眩晕
	 */
	public static final int DIZZINESS = 4;//
	/**
	 * 减速
	 */
	public static final int SPEEDDOWN = 5;//减速
	/**
	 * 减甲
	 */
	public static final int REDUCEARMOR = 6;
	/**
	 * 变大
	 */
	public static final int BIGGER = 7;//
	/**
	 * 沙漠
	 */
	public static final int DESERT = 8;
	
	/**
	 * 
	 * @param effect 效果分类（暂时只有3）
	 * @param effectValue  效果值
	 * @param lastTime 持续时间
	 * @param 持续伤害百分比*100
	 */
	public Buff(int effect,int effectValue,int lastTime,int per) {
		this.effect = effect;
		this.effectValue = effectValue;
		this.startTime = TimeUtil.currentTime();
		this.lastTime = lastTime;
		this.per = per;
	}
	/**
	 * 
	 * @param effect 效果分类（暂时只有3）
	 * @param effectValue  效果值
	 * @param lastTime 持续时间
	 * @param 持续伤害百分比*100
	 */
	public Buff(int effect,int effectValue,int lastTime) {
		this.effect = effect;
		this.effectValue = effectValue;
		this.startTime = TimeUtil.currentTime();
		this.lastTime = lastTime;
	}
	/**
	 * 
	 * @param effect 效果分类（暂时只有3）
	 * @param effectValue  效果值
	 * @param lastTime 持续时间
	 * @param 持续伤害百分比*100
	 */
	public Buff(int effect,int effectValue,CopyOnWriteArrayList<Float> burnDemage) {
		this.effect = effect;
		this.effectValue = effectValue;
		this.startTime = TimeUtil.currentTime();
		this.burnDemage = burnDemage;
	}
	private int effect;//效果
	private int effectValue;//效果值
	private int startTime;//开始时间
	private int lastTime;//持续时间
	private int per;//伤害百分比
	private CopyOnWriteArrayList<Float> burnDemage;//烧伤伤害
	public int getEffect() {
		return effect;
	}
	public void setEffect(int effect) {
		this.effect = effect;
	}
	public int getEffectValue() {
		return effectValue;
	}
	public void setEffectValue(int effectValue) {
		this.effectValue = effectValue;
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
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("效果：").append(effect).append(" startTime").append(TimeUtil.formatTime(startTime))
		.append(" lastTime").append(lastTime);
		return sb.toString();
	}
	public void setPer(int per) {
		this.per = per;
	}
	public int getPer() {
		return per;
	}
	public void setBurnDemage(CopyOnWriteArrayList<Float> burnDemage) {
		this.burnDemage = burnDemage;
	}
	public CopyOnWriteArrayList<Float> getBurnDemage() {
		return burnDemage;
	}
}
