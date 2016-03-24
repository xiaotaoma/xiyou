package com.dao.impl;

import com.dao.Back_moneyDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.backstage.Back_money;

import java.sql.SQLException;

public class Back_moneyDaoImpl implements Back_moneyDao {
	private SqlMapClient sqlMapClient;
	@Override
	public void insert(Back_money money) throws SQLException {
		this.sqlMapClient.insert("Back_money.insert", money);
	}
	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}
	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

}
