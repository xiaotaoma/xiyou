package com.service.impl;

import com.dao.ArenaDao;
import com.model.Arena;
import com.service.ArenaManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArenaManagerImpl implements ArenaManager {
	private Logger logger = LoggerFactory.getLogger(ArenaManagerImpl.class);
	
	private ArenaDao arenaDao;
	@Override
	public Arena getByHid(int hid) throws SQLException {
		return this.arenaDao.getByHid(hid);
	}
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void insert(Arena arena) throws SQLException {
		this.arenaDao.insert(arena);
	}

	public void setArenaDao(ArenaDao arenaDao) {
		this.arenaDao = arenaDao;
	}

	public ArenaDao getArenaDao() {
		return arenaDao;
	}
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void update(Arena arena) throws SQLException {
		this.arenaDao.update(arena);
	}

	@Override
	public int getRank(int id) throws SQLException {
		return this.arenaDao.getRank(id);
	}
	@Override
	public List<Arena> getSamll(Map<String, Integer> map) throws SQLException {
		return this.arenaDao.getSamll(map);
	}
	/**
	 * 查询竞技场前100名
	 */
	@Override
	public List<Arena> get100(int num) throws SQLException {
		return this.arenaDao.get100(num);
	}
	@Override
	public List<Arena> getList(HashMap<String, Integer> map) throws SQLException {
		return this.arenaDao.getList(map);
	}
	@Override
	public int getRankByHid(int hid) throws SQLException {
		return this.arenaDao.getRankByHid(hid);
	}
	@Override
	public int getlastRank() throws SQLException {
		return this.arenaDao.getlastRank();
	}
	
	@Override
	public List<Arena> getMatch(int strength) throws SQLException {
		return this.arenaDao.getMatch(strength);
	}
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void updateStrength(Map<String, Integer> map) throws SQLException {
		this.arenaDao.updateStrength(map);
	}
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void updateRetinue(Map<String, Integer> map) throws SQLException {
		this.arenaDao.updateRetinue(map);
	}
	
	@Override
	public String getAchievement(int hid) throws SQLException {
		return this.arenaDao.getAchievement(hid);
	}
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void updateAchievement(Map<String, Object> map) throws SQLException {
		this.arenaDao.updateAchievement(map);
	}
	
	@Override
	public List<Arena> getRankList(int start, int end)
			throws SQLException {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("start", start);
		map.put("end", end);
		return this.arenaDao.getRankList(map);
	}
	@Override
	public List<Arena> getList(int p1, int p2) throws SQLException {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("p1", p1);
		map.put("p2", p2);
		return this.arenaDao.getList(map);
	}
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void updateRank(List<Arena> list) throws SQLException {
		if (list!=null) {
			for (Arena arena : list) {
				this.arenaDao.update(arena);
				logger.info("updateRank:"+arena.toString());
			}
		}
	}
	@Override
	public List<Arena> onlineArena(List list) throws SQLException {
		return this.arenaDao.onlineArena(list);
	}
	@Override
	public Integer getMaxRank() throws SQLException {
		return this.arenaDao.getMaxRank();
	}
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void update(Arena arena, Arena arena2) throws SQLException {
		this.arenaDao.update(arena2);
		this.arenaDao.update(arena);
	}
	@Override
	public Integer getCountByPrestige(int prestige) throws SQLException {
		return this.arenaDao.getCountByPrestige(prestige);
	}
	
	@Override
	public List<Arena> getAll() throws SQLException {
		return this.arenaDao.getAll();
	}
	@Override
	public List<Arena> getWaveRank() throws SQLException {
		return this.arenaDao.getWaveRank();
	}
}
