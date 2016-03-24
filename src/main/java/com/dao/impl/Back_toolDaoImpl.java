package com.dao.impl;

import com.dao.Back_toolDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.backstage.Back_tool;

import java.sql.SQLException;

public class Back_toolDaoImpl implements Back_toolDao {
	private SqlMapClient sqlMapClient;
	@Override
	public void insert(Back_tool tool) throws SQLException {
		this.sqlMapClient.insert("Back_tool.insert", tool);	
	}
	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}
	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

}
