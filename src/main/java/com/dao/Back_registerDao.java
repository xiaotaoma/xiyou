package com.dao;

import com.model.backstage.Back_register;

import java.sql.SQLException;

public interface Back_registerDao {
	public void insert(Back_register register) throws SQLException;
}
