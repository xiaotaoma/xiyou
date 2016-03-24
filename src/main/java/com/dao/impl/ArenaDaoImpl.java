package com.dao.impl;

import com.dao.ArenaDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.Arena;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArenaDaoImpl implements ArenaDao {
	private SqlMapClient sqlMapClient;
	
	@Override
	public Arena getByHid(int hid) throws SQLException {
		return (Arena) this.sqlMapClient.queryForObject("Arena.getByHid", hid);
	}

	@Override
	public void insert(Arena arena) throws SQLException {
		this.sqlMapClient.insert("Arena.insert", arena);
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

	@Override
	public void update(Arena arena) throws SQLException {
		this.sqlMapClient.update("Arena.update",arena);
	}

	@Override
	public int getRank(int strength) throws SQLException {
		return (Integer) this.sqlMapClient.queryForObject("Arena.getRank", strength);
	}

	@Override
	public List<Arena> getSamll(Map<String, Integer> map) throws SQLException {
		return this.sqlMapClient.queryForList("Arena.getSmall", map);
	}

	@Override
	public List<Arena> get100(int num) throws SQLException {
		return this.sqlMapClient.queryForList("Arena.get100",num);
	}

	@Override
	public List<Arena> getList(HashMap<String, Integer> map) throws SQLException {
		return this.sqlMapClient.queryForList("Arena.getBig", map);
	}

	@Override
	public int getRankByHid(int hid) throws SQLException {
		return (Integer) this.sqlMapClient.queryForObject("Arena.getRankByHid", hid);
	}

	@Override
	public int getlastRank() throws SQLException {
		return (Integer) this.sqlMapClient.queryForObject("Arena.getlastRank");
	}

	@Override
	public List<Arena> getMatch(int strength) throws SQLException {
		return this.sqlMapClient.queryForList("Arena.getMatch", strength);
	}

	@Override
	public void updateStrength(Map<String, Integer> map) throws SQLException {
		this.sqlMapClient.update("Arena.updateStrength", map);
	}

	@Override
	public void updateRetinue(Map<String, Integer> map) throws SQLException {
		this.sqlMapClient.update("Arena.updateRetinue",map);
	}

	@Override
	public String getAchievement(int hid) throws SQLException {
		return (String) this.sqlMapClient.queryForObject("Arena.getAchievement", hid);
	}

	@Override
	public void updateAchievement(Map<String, Object> map) throws SQLException {
		this.sqlMapClient.update("Arena.updateAchievement", map);
	}

	@Override
	public List<Arena> getRankList(Map<String, Integer> map)
			throws SQLException {
		return this.sqlMapClient.queryForList("Arena.getRankList", map);
	}

	@Override
	public List<Arena> getList(Map<String, Integer> map) throws SQLException {
		return this.sqlMapClient.queryForList("Arena.getList", map);
	}
	@Override
	public void updateRank(Arena arena) throws SQLException {
		this.sqlMapClient.update("Arena.updateRank", arena);
	}
	
	@Override
	public List<Arena> onlineArena(List list) throws SQLException {
		return this.sqlMapClient.queryForList("Arena.onlineArena", list);
	}
	
	@Override
	public Integer getMaxRank() throws SQLException {
		return (Integer) this.sqlMapClient.queryForObject("Arena.getMaxRank");
	}
	
	@Override
	public Integer getCountByPrestige(int prestige) throws SQLException {
		return (Integer) this.sqlMapClient.queryForObject("Arena.getCountByPrestige", prestige);
	}
	@Override
	public List<Arena> getAll() throws SQLException {
		// TODO Auto-generated method stub
		return this.sqlMapClient.queryForList("Arena.getAll");
	}

	@Override
	public List<Arena> getWaveRank() throws SQLException {
		return this.sqlMapClient.queryForList("Arena.getWaveRank");
	}
}
