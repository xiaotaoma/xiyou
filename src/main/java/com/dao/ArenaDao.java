package com.dao;

import com.model.Arena;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ArenaDao {
	public Arena getByHid(int hid) throws SQLException;
	
	public void insert(Arena arena) throws SQLException;
	
	public void update(Arena arena) throws SQLException;
	
	public int getRank(int strength) throws SQLException;
	
	public List<Arena> getSamll(Map<String, Integer> map) throws SQLException;
	
	public List<Arena> get100(int num) throws SQLException;
	
	public List<Arena> getList(HashMap<String, Integer> map)throws SQLException;
	
	public int getRankByHid(int hid) throws SQLException;
	
	public int getlastRank() throws SQLException;
	
	public List<Arena> getMatch(int strength) throws SQLException;
	
	public void updateStrength(Map<String, Integer> map) throws SQLException;
	
	public void updateRetinue(Map<String, Integer> map) throws SQLException;
	
	public String getAchievement(int hid) throws SQLException;
	
	public void updateAchievement(Map<String, Object> map) throws SQLException;
	
	public List<Arena> getRankList(Map<String, Integer> map) throws SQLException;
	
	public List<Arena> getList(Map<String, Integer> map) throws SQLException;
	
	public void updateRank(Arena arena) throws SQLException;
	
	public List<Arena> onlineArena(List list) throws SQLException;
	   
	public Integer getMaxRank() throws SQLException;
	
	public Integer getCountByPrestige(int prestige) throws SQLException;
	
	public List<Arena> getAll() throws SQLException;
	
	public List<Arena> getWaveRank() throws SQLException;
}
