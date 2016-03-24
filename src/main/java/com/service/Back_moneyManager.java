package com.service;

import com.model.backstage.Back_money;

import java.sql.SQLException;

public interface Back_moneyManager {
	public void insert(Back_money money) throws SQLException;
}
