package com.dao.impl;

import com.dao.MailDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.Mail;

import java.sql.SQLException;
import java.util.List;

public class MailDaoImpl implements MailDao {
	private SqlMapClient sqlMapClient;
	@Override
	public void insert(Mail mail) throws SQLException {
		this.sqlMapClient.insert("",mail);
	}

	@Override
	public List<MailDao> getBySenderId(int senderId) throws SQLException {
		return this.sqlMapClient.queryForList("", senderId);
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

}
