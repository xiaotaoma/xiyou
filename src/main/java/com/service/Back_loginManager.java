package com.service;

import com.model.backstage.Back_login;

import java.sql.SQLException;

public interface Back_loginManager {
	public void insert(Back_login login) throws SQLException;
	
	public Back_login getByHid(int hid, int time) throws SQLException;
	
	public void updateTimes(Back_login login) throws SQLException;
	
	public void updateTatolTime(Back_login login) throws SQLException;
}
