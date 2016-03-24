package com.service;

import com.model.backstage.Back_onlineNum;

import java.sql.SQLException;

public interface Back_onlineNumManager {
	public void insert(Back_onlineNum onlineNum) throws SQLException;
}
