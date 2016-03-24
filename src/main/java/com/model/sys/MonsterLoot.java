package com.model.sys;

public class MonsterLoot {
	private int mapId;
	private Loot easy;//简单难度掉率
	private Loot normal;//普通难度掉落
	private Loot hard;//困难难度掉落
	public int getMapId() {
		return mapId;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	public Loot getEasy() {
		return easy;
	}
	public void setEasy(Loot easy) {
		this.easy = easy;
	}
	public Loot getNormal() {
		return normal;
	}
	public void setNormal(Loot normal) {
		this.normal = normal;
	}
	public Loot getHard() {
		return hard;
	}
	public void setHard(Loot hard) {
		this.hard = hard;
	}
	
}
