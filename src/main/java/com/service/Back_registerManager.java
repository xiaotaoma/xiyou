package com.service;

import com.model.backstage.Back_register;

import java.sql.SQLException;

public interface Back_registerManager {
	public void insert(Back_register register) throws SQLException;
}
