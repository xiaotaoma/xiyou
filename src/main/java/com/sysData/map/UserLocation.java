package com.sysData.map;

import com.socket.battle.Node;

import java.util.List;

public class UserLocation {
	private int id;
	private int hid;//角色id
	private int x;//x
	private int y;//
	private float moveSpeed;//移动速度
	private float range;//攻击范围
	private float attSpeed;//攻击速度
	private float hp;//当前生命值
	private float att;//攻击
	private float armor;//护甲
	private float mp;//当前法力值
	private float mpMax;//总法力
	private float hpMax;//总生命值
	private int state;//1静止，2移动
	private float targetX;//目标坐标
	private float targetY;//目标坐标
	private List<Node> way;
	public float getMoveSpeed() {
		return moveSpeed;
	}
	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
	public float getRange() {
		return range;
	}
	public void setRange(float range) {
		this.range = range;
	}
	public float getAttSpeed() {
		return attSpeed;
	}
	public void setAttSpeed(float attSpeed) {
		this.attSpeed = attSpeed;
	}
	public float getHp() {
		return hp;
	}
	public void setHp(float hp) {
		this.hp = hp;
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
	public float getHpMax() {
		return hpMax;
	}
	public void setHpMax(float hpMax) {
		this.hpMax = hpMax;
	}
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
	public void setTargetX(float targetX) {
		this.targetX = targetX;
	}
	public float getTargetX() {
		return targetX;
	}
	public void setTargetY(float targetY) {
		this.targetY = targetY;
	}
	public float getTargetY() {
		return targetY;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getState() {
		return state;
	}
	public void setWay(List<Node> way) {
		this.way = way;
	}
	public List<Node> getWay() {
		return way;
	}
	
//	str=SHARED_LOCALLATAMANAGE->readString("人物成长数值.plist",IntToString(id)+".护甲").c_str();
//	a.insert(make_pair("护甲",StringToFloat(str.c_str())));
//
//	str=SHARED_LOCALLATAMANAGE->readString("人物成长数值.plist",IntToString(id)+".法力").c_str();
//	a.insert(make_pair("法力",StringToFloat(str.c_str())));
//
//	str=SHARED_LOCALLATAMANAGE->readString("人物成长数值.plist",IntToString(id)+".法力值").c_str();
//	a.insert(make_pair("法力值",StringToFloat(str.c_str())));
//
//	map<string,float> ff=SHARED_GAMEDATAMANAGE::EquipData()->getEquipAttr(id);
//	map<string,float>::iterator it=ff.begin();
//	for(;it!=ff.end();it++)
//	{
//		a[it->first]+=it->second;
//	}
//
//	a["总生命"]=a["生命"];
//	a["总法力值"]=a["法力值"];
	
	
}
