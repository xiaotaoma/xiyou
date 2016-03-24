package com.dao.impl;

import com.dao.Back_pay91Dao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.backstage.Back_pay91;

import java.sql.SQLException;

public class Back_pay91DaoImpl implements Back_pay91Dao {
	private SqlMapClient sqlMapClient;
	@Override
	public void insert(Back_pay91 pay91) throws SQLException {
		this.sqlMapClient.insert("Back_pay91.insert", pay91);
	}
	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}
	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

}
