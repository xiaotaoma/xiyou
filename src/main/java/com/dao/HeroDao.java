package com.dao;

import com.model.Hero;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface HeroDao {
	public void insert(Hero hero) throws SQLException;
	
	public List<Hero> getHeros(int accId) throws SQLException;
	
	public Hero getHeroByName(String name) throws SQLException;
	
	public Hero getHeroById(int id) throws SQLException;
	
	public String getName(int hid) throws SQLException;
 	
	public void update(Hero hero) throws SQLException;
	
	public List<Hero> getHonourRank() throws SQLException;
	
	public int getAllNum() throws SQLException;
	
	public int todayRegisterNum(int time) throws SQLException;
	
	public List<Hero> getByLoginTimes() throws SQLException;
	
	public List<Hero> getStrengthRank() throws SQLException;
	
	public HashMap<Integer, Integer> getTreeCopper(List<Integer> list) throws SQLException;
	
	public HashMap<Integer, Integer> getEndCardTime(List<Integer> list) throws SQLException;
	
	public List<Hero> getRobRank() throws SQLException;
	
	public void giveCopper(HashMap<String, Integer> map) throws SQLException;
	
}
