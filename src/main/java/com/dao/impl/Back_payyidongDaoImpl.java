package com.dao.impl;

import com.dao.Back_payyidongDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.backstage.Back_payyidong;

import java.sql.SQLException;

public class Back_payyidongDaoImpl implements Back_payyidongDao {
	private SqlMapClient sqlMapClient;
	@Override
	public void insert(Back_payyidong payyidong) throws SQLException {
		this.sqlMapClient.insert("Back_payyidong.insert", payyidong);
	}
	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}
	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

}
