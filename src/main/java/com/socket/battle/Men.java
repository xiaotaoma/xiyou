package com.socket.battle;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Men extends Member{
	private String name;
	private int tid;//徒弟id
	private BigDecimal moveSpeed;//移动速度
	private long attTime;//攻击事件
	private BigDecimal range;//攻击范围
	private BigDecimal attSpeed;//攻击速度
	private float att;//攻击
	private float armor;//护甲
	private int fali;//法力
	private int skillId1;
	private int skillId2;
	private int skillId3;
	private int skillId4;
	private int skillId5;
	private int state;//是否准备 1准备 0 没有准备
	private HashMap<Integer, Long> cd;//技能cd
	private CopyOnWriteArrayList<Buff> buffs;
	private CopyOnWriteArrayList<Skill> skills;//技能
	private int tLevel;//徒弟等级
	private int strength;//徒弟战斗力
	private float acdef;//物理免伤
	private float magicdef;//魔法免伤
	public int getSkillId1() {
		return skillId1;
	}
	public void setSkillId1(int skillId1) {
		this.skillId1 = skillId1;
	}
	public int getSkillId2() {
		return skillId2;
	}
	public void setSkillId2(int skillId2) {
		this.skillId2 = skillId2;
	}
	public int getSkillId3() {
		return skillId3;
	}
	public void setSkillId3(int skillId3) {
		this.skillId3 = skillId3;
	}
	public int getSkillId4() {
		return skillId4;
	}
	public void setSkillId4(int skillId4) {
		this.skillId4 = skillId4;
	}
	public int getSkillId5() {
		return skillId5;
	}
	public void setSkillId5(int skillId5) {
		this.skillId5 = skillId5;
	}
	
	public BigDecimal getMoveSpeed() {
		return moveSpeed;
	}
	public void setMoveSpeed(BigDecimal moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
	public BigDecimal getRange() {
		return range;
	}
	public void setRange(BigDecimal range) {
		this.range = range;
	}
	public BigDecimal getAttSpeed() {
		return attSpeed;
	}
	public void setAttSpeed(BigDecimal attSpeed) {
		this.attSpeed = attSpeed;
	}
	public float getAtt() {
		return att;
	}
	public void setAtt(float att) {
		this.att = att;
	}
	public float getArmor() {
		return armor;
	}
	public void setArmor(float armor) {
		this.armor = armor;
	}
//	public void setTargetX(int targetX) {
//		this.targetX = targetX;
//	}
//	public int getTargetX() {
//		return targetX;
//	}
//	public void setTargetY(int targetY) {
//		this.targetY = targetY;
//	}
//	public int getTargetY() {
//		return targetY;
//	}
	public void setState(int state) {
		this.state = state;
	}
	public int getState() {
		return state;
	}
//	public void setNodes(List<Node> nodes) {
//		this.nodes = nodes;
//	}
//	public List<Node> getNodes() {
//		return nodes;
//	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public int getTid() {
		return tid;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
//	public void setWalkState(int walkState) {
//		this.walkState = walkState;
//	}
//	public int getWalkState() {
//		return walkState;
//	}
	public void setCd(HashMap<Integer, Long> cd) {
		this.cd = cd;
	}
	public HashMap<Integer, Long> getCd() {
		return cd;
	}
	public void setBuffs(CopyOnWriteArrayList<Buff> buffs) {
		this.buffs = buffs;
	}
	public CopyOnWriteArrayList<Buff> getBuffs() {
		return buffs;
	}
	public void setAttTime(long attTime) {
		this.attTime = attTime;
	}
	public long getAttTime() {
		return attTime;
	}
	public void setSkills(CopyOnWriteArrayList<Skill> skills) {
		this.skills = skills;
	}
	public CopyOnWriteArrayList<Skill> getSkills() {
		return skills;
	}
	public void setFali(int fali) {
		this.fali = fali;
	}
	public int getFali() {
		return fali;
	}
	public void setStrength(int strength) {
		this.strength = strength;
	}
	public int getStrength() {
		return strength;
	}
	public void settLevel(int tLevel) {
		this.tLevel = tLevel;
	}
	public int gettLevel() {
		return tLevel;
	}
	public void setMagicdef(float magicdef) {
		this.magicdef = magicdef;
	}
	public float getMagicdef() {
		return magicdef;
	}
	public void setAcdef(float acdef) {
		this.acdef = acdef;
	}
	public float getAcdef() {
		return acdef;
	}


}
