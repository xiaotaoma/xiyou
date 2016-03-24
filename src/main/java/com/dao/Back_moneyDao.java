package com.dao;

import com.model.backstage.Back_money;

import java.sql.SQLException;

public interface Back_moneyDao {
	public void insert(Back_money money) throws SQLException;
}
