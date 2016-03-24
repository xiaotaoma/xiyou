package com.dao.impl;

import com.dao.AccountDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.Account;

import java.sql.SQLException;
import java.util.List;

public class AccountDaoImpl implements AccountDao {
	private SqlMapClient sqlMapClient;
	@Override
	public void insert(Account account) throws SQLException {
		this.sqlMapClient.insert("Account.insertAccount", account);
	}
	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}
	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}
	@Override
	public List<Account> getByAccount(String account) throws SQLException {
		return this.sqlMapClient.queryForList("Account.getByAccount", account);
	}
	@Override
	public void update(Account account) throws SQLException {
		this.sqlMapClient.update("Account.update", account);
	}

}
