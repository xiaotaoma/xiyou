package com.dao.impl;

import com.dao.Back_loginDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.backstage.Back_login;

import java.sql.SQLException;
import java.util.HashMap;

public class Back_loginDaoImpl implements Back_loginDao {
	private SqlMapClient sqlMapClient;
	@Override
	public void insert(Back_login login) throws SQLException {
		this.sqlMapClient.insert("Back_login.insert", login);
	}

	@Override
	public Back_login getByHid(HashMap<String, Integer> map)
			throws SQLException {
		return (Back_login) this.sqlMapClient.queryForObject("Back_login.getByHid", map);
	}

	@Override
	public void updateTimes(Back_login login) throws SQLException {
		this.sqlMapClient.update("Back_login.updateTimes", login);
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

	@Override
	public void updateTatolTime(Back_login login) throws SQLException {
		this.sqlMapClient.update("Back_login.updateTatolTime", login);
	}

}
