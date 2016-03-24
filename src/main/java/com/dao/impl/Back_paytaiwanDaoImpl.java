package com.dao.impl;

import com.dao.Back_paytaiwanDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.backstage.Back_paytaiwan;

import java.sql.SQLException;

public class Back_paytaiwanDaoImpl implements Back_paytaiwanDao {
	private SqlMapClient sqlMapClient;
	@Override
	public void insert(Back_paytaiwan paytaiwan) throws SQLException {
		this.sqlMapClient.insert("Back_paytaiwan.insert", paytaiwan);
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

}
