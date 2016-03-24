package com.service.impl;

import com.dao.HeroDao;
import com.main.BaseAction;
import com.model.Friend;
import com.model.Hero;
import com.service.FriendManager;
import com.service.HeroManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class HeroManagerImpl implements HeroManager {
	private HeroDao heroDao;
	public void setHeroDao(HeroDao heroDao) {
		this.heroDao = heroDao;
	}
	public HeroDao getHeroDao() {
		return heroDao;
	}
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void insert(Hero hero) throws SQLException {
		//
		this.heroDao.insert(hero);
		//好友数据,初始化
		Friend friend = new Friend();
		friend.setHid(hero.getId());
		friend.setData("");
		FriendManager friendManager = (FriendManager) BaseAction.getIntance().getBean("friendManager");
		friendManager.insert(friend);
	}
	@Override
	public List<Hero> getHeros(int accId) throws SQLException {
		return this.heroDao.getHeros(accId);
	}
	@Override
	public Hero getHeroByName(String name) throws SQLException {
		return this.heroDao.getHeroByName(name);
	}
	@Override
	public Hero getHeroById(int id) throws SQLException {
		return this.heroDao.getHeroById(id);
	}
	@Override
	public String getName(int hid) throws SQLException {
		return this.heroDao.getName(hid);
	}
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void update(Hero hero) throws SQLException {
		this.heroDao.update(hero);
	}
	@Override
	public List<Hero> getHonourRank() throws SQLException {
		return this.heroDao.getHonourRank();
	}
	@Override
	public int getAllNum() throws SQLException {
		return this.heroDao.getAllNum();
	}
	@Override
	public int todayRegisterNum(int time) throws SQLException {
		return this.heroDao.todayRegisterNum(time);
	}
	@Override
	public List<Hero> getByLoginTimes() throws SQLException {
		return this.heroDao.getByLoginTimes();
	}
	@Override
	public List<Hero> getStrengthRank() throws SQLException {
		return this.heroDao.getStrengthRank();
	}
	@Override
	public HashMap<Integer, Integer> getTreeCopper(List<Integer> list) throws SQLException {
		return this.heroDao.getTreeCopper(list);
	}
	@Override
	public HashMap<Integer, Integer> getEndCardTime(List<Integer> list)
			throws SQLException {
		return this.heroDao.getEndCardTime(list);
	}
	
	@Override
	public List<Hero> getRobRank() throws SQLException {
		return this.heroDao.getRobRank();
	}
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void giveCopper(HashMap<String, Integer> map) throws SQLException {
		this.heroDao.giveCopper(map);
	}
	
}
