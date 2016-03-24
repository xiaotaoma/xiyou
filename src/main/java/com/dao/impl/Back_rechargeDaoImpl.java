package com.dao.impl;

import com.dao.Back_rechargeDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.backstage.Back_recharge;

import java.sql.SQLException;

public class Back_rechargeDaoImpl implements Back_rechargeDao {
	private SqlMapClient sqlMapClient;
	@Override
	public void insert(Back_recharge recharge) throws SQLException {
		this.sqlMapClient.insert("Back_recharge.insert", recharge);
	}
	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}
	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

}
