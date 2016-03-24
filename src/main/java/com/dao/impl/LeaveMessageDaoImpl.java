package com.dao.impl;

import com.dao.LeaveMessageDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.LeaveMessage;

import java.sql.SQLException;
import java.util.List;

public class LeaveMessageDaoImpl implements LeaveMessageDao{
	private SqlMapClient sqlMapClient;
	@Override
	public void insert(LeaveMessage leaveMessage) throws SQLException {
		this.sqlMapClient.insert("LeaveMessage.insert",leaveMessage);
	}

	@Override
	public void delete(String s) throws SQLException {
		this.sqlMapClient.delete("LeaveMessage.delete", s);
	}

	@Override
	public List<LeaveMessage> getByReceiverId(int receiverId)
			throws SQLException {
		return this.sqlMapClient.queryForList("LeaveMessage.getByReceicerId", receiverId);
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}
	
}
