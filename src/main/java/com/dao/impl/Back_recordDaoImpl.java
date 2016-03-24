package com.dao.impl;

import com.dao.Back_recordDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.backstage.Back_record;

import java.sql.SQLException;

public class Back_recordDaoImpl implements Back_recordDao{
	private SqlMapClient sqlMapClient;
	
	@Override
	public void insert(Back_record record) throws SQLException {
		this.sqlMapClient.insert("Back_record.insert",record);
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

}
