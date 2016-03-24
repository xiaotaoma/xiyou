package com.dao.impl;

import com.dao.HeroDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.Hero;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class HeroDaoImpl implements HeroDao {
	private SqlMapClient sqlMapClient;
	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}
	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}
	@Override
	public void insert(Hero hero) throws SQLException {
		this.sqlMapClient.insert("Hero.insert", hero);
	}
	@Override
	public List<Hero> getHeros(int accId) throws SQLException {
		return this.sqlMapClient.queryForList("Hero.getByAccId", accId);
	}
	@Override
	public Hero getHeroByName(String name) throws SQLException {
		return (Hero) this.sqlMapClient.queryForObject("Hero.getHeroByName", name);
	}
	@Override
	public Hero getHeroById(int id) throws SQLException {
		return (Hero) this.sqlMapClient.queryForObject("Hero.getById", id);
	}
	@Override
	public String getName(int hid) throws SQLException {
		return (String) this.sqlMapClient.queryForObject("Hero.getName", hid);
	}
	@Override
	public void update(Hero hero) throws SQLException {
		this.sqlMapClient.update("Hero.update", hero);
	}
	@Override
	public List<Hero> getHonourRank() throws SQLException {
		return this.sqlMapClient.queryForList("Hero.honourRank");
	}
	@Override
	public int getAllNum() throws SQLException {
		return (Integer) this.sqlMapClient.queryForObject("Hero.getAllNum");
	}
	
	@Override
	public int todayRegisterNum(int time) throws SQLException {
		return (Integer) this.sqlMapClient.queryForObject("Hero.todayRegisterNum", time);
	}
	
	@Override
	public List<Hero> getByLoginTimes() throws SQLException {
		return this.sqlMapClient.queryForList("Hero.getByLoginTimes");
	}
	@Override
	public List<Hero> getStrengthRank() throws SQLException {
		return this.sqlMapClient.queryForList("Hero.getStrengthRank");
	}
	@Override
	public HashMap<Integer, Integer> getTreeCopper(List<Integer> list) throws SQLException {
		/**
		 * query，parameter ，key , value
		 */
		return (HashMap<Integer, Integer>) this.sqlMapClient.queryForMap("Hero.getTreeCopper", list, "id", "treeCopper");
	}
	
	@Override
	public HashMap<Integer, Integer> getEndCardTime(List<Integer> list)
			throws SQLException {
		return (HashMap<Integer, Integer>) this.sqlMapClient.queryForMap("Hero.getEndCardTime", list, "id", "endCardTime");
	}
	@Override
	public List<Hero> getRobRank() throws SQLException {
		return this.sqlMapClient.queryForList("Hero.getRobRank");
	}
	@Override
	public void giveCopper(HashMap<String, Integer> map) throws SQLException {
		this.sqlMapClient.update("Hero.giveCopper", map);
	}
}
