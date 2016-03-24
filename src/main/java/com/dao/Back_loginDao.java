package com.dao;

import com.model.backstage.Back_login;

import java.sql.SQLException;
import java.util.HashMap;

public interface Back_loginDao {
	public void insert(Back_login login) throws SQLException;
	
	public Back_login getByHid(HashMap<String, Integer> map) throws SQLException;
	
	public void updateTimes(Back_login login) throws SQLException;
	
	public void updateTatolTime(Back_login login) throws SQLException;
}
