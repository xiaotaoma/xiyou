package com.dao.impl;

import com.dao.FriendDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.Friend;

import java.sql.SQLException;

public class FriendDaoImpl implements FriendDao {
	private SqlMapClient sqlMapClient;
	@Override
	public void insert(Friend friend) throws SQLException {
		this.sqlMapClient.insert("Friend.insert", friend);
	}

	@Override
	public Friend getByHid(int hid) throws SQLException {
		return (Friend) this.sqlMapClient.queryForObject("Friend.getFriendByHid", hid);
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

	@Override
	public void update(Friend friend) throws SQLException {
		this.sqlMapClient.update("Friend.update", friend);
	}

}
