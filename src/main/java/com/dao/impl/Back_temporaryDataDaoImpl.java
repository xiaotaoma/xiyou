package com.dao.impl;

import com.dao.Back_temporaryDataDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.backstage.Back_temporaryData;

import java.sql.SQLException;
import java.util.List;

public class Back_temporaryDataDaoImpl implements Back_temporaryDataDao {
	private SqlMapClient sqlMapClient;
	@Override
	public void insert(Back_temporaryData temporaryData) throws SQLException {
		this.sqlMapClient.insert("Back_temporaryData.insert", temporaryData);
	}
	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}
	@Override
	public List<Back_temporaryData> getByFlag(int flag) throws SQLException {
		return this.sqlMapClient.queryForList("Back_temporaryData.getByFlag", flag);
	}
	@Override
	public void truncate() throws SQLException {
		this.sqlMapClient.update("Back_temporaryData.truncate");
	}

}
