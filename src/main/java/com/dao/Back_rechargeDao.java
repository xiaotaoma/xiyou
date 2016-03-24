package com.dao;

import com.model.backstage.Back_recharge;

import java.sql.SQLException;

public interface Back_rechargeDao {
	public void insert(Back_recharge recharge) throws SQLException;
}
