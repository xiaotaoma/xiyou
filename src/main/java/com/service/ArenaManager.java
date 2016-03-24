package com.service;

import com.model.Arena;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ArenaManager {
	public Arena getByHid(int hid) throws SQLException;
	
	public void insert(Arena arena) throws SQLException;
	
	public void update(Arena arena) throws SQLException;
	
	public int getRank(int id) throws SQLException;
	
	public List<Arena> getSamll(Map<String, Integer> map) throws SQLException;
	
	public List<Arena> get100(int num) throws SQLException;
	
	public List<Arena> getList(HashMap<String, Integer> map) throws SQLException;
	
	public int getRankByHid(int hid) throws SQLException;
	
	public int getlastRank() throws SQLException;
	
	public List<Arena> getMatch(int strength) throws SQLException;
	
	public void updateStrength(Map<String, Integer> map) throws SQLException;
	
	public void updateRetinue(Map<String, Integer> map) throws SQLException;
	
	public String getAchievement(int hid) throws SQLException;
	
	public void updateAchievement(Map<String, Object> map) throws SQLException;
	/**
	 * 获取看到的100个人,排名从start开始，到end结束
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public List<Arena> getRankList(int start, int end) throws SQLException;
	/**
	 * 
	 * @param p1 小
	 * @param p2 大
	 * @return
	 * @throws SQLException
	 */
	public List<Arena> getList(int p1, int p2) throws SQLException;
	
	public void updateRank(List<Arena> list) throws SQLException;
	
	public List<Arena> onlineArena(List list) throws SQLException;
	
	public Integer getMaxRank() throws SQLException;
	
	public void update(Arena arena, Arena arena2) throws SQLException;
	
	public Integer getCountByPrestige(int prestige) throws SQLException;
	
	public List<Arena> getAll() throws SQLException;
	
	public List<Arena> getWaveRank() throws SQLException;
}
