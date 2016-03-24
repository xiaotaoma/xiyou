package com.service;

import com.model.backstage.Back_recharge;

import java.sql.SQLException;

public interface Back_rechargeManager {
	public void insert(Back_recharge recharge) throws SQLException;
}
