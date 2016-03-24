package com.dao.impl;

import com.dao.Back_registerDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.backstage.Back_register;

import java.sql.SQLException;

public class Back_registerDaoImpl implements Back_registerDao {
	
	@Override
	public void insert(Back_register register) throws SQLException{
		this.sqlMapClient.insert("Back_register.insert", register);
	}
	
	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

	private SqlMapClient sqlMapClient;
}
