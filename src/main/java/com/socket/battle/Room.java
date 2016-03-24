package com.socket.battle;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Room {
	private int id;
	private int leader;
	private String leaderName;
	private int leaderTid;//房主徒弟id
	private int leaderTLevel;//房主徒弟等级
	private int leaderStrength;//
	private ConcurrentHashMap<Integer, Men> members;//角色
	private ConcurrentHashMap<Integer, Call> callMonsters;//召唤物
	private int mapId;
	private Set<Integer> load;
	private boolean battle = false;//是否在战斗中
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLeader() {
		return leader;
	}
	public void setLeader(int leader) {
		this.leader = leader;
	}
	public ConcurrentHashMap<Integer, Men> getMembers() {
		return members;
	}
	public void setMembers(ConcurrentHashMap<Integer, Men> members) {
		this.members = members;
	}
	
	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}
	public String getLeaderName() {
		return leaderName;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	public int getMapId() {
		return mapId;
	}
	private static AtomicInteger roomId = new AtomicInteger(0);
	public static int getRoomId() {
		roomId.getAndIncrement();//值加1
		return roomId.intValue();
	}
	public void setLoad(Set<Integer> load) {
		this.load = load;
	}
	public Set<Integer> getLoad() {
		return load;
	}
	public void setLeaderStrength(int leaderStrength) {
		this.leaderStrength = leaderStrength;
	}
	public int getLeaderStrength() {
		return leaderStrength;
	}
	public void setLeaderTLevel(int leaderTLevel) {
		this.leaderTLevel = leaderTLevel;
	}
	public int getLeaderTLevel() {
		return leaderTLevel;
	}
	public void setLeaderTid(int leaderTid) {
		this.leaderTid = leaderTid;
	}
	public int getLeaderTid() {
		return leaderTid;
	}
	
	public Room() {
		
	}
	public void setCallMonsters(ConcurrentHashMap<Integer, Call> callMonsters) {
		this.callMonsters = callMonsters;
	}
	public ConcurrentHashMap<Integer, Call> getCallMonsters() {
		return callMonsters;
	}
	public void setBattle(boolean battle) {
		this.battle = battle;
	}
	public boolean isBattle() {
		return battle;
	}
}
