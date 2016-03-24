package com.dao.impl;

import com.dao.Back_onlineNumDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.backstage.Back_onlineNum;

import java.sql.SQLException;

public class Back_onlineNumDaoImpl implements Back_onlineNumDao {
	private SqlMapClient sqlMapClient;
	@Override
	public void insert(Back_onlineNum onlineNum) throws SQLException {
		this.sqlMapClient.insert("Back_onlineNum.insert" , onlineNum);
	}
	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}
	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

}
