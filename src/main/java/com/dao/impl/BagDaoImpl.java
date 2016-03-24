package com.dao.impl;

import com.dao.BagDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.Bag;

import java.sql.SQLException;

public class BagDaoImpl implements BagDao {
	private SqlMapClient sqlMapClient;
	@Override
	public void insert(Bag bag) throws SQLException {
		this.sqlMapClient.insert("Bag.insert", bag);
	}

	@Override
	public void update(Bag bag) throws SQLException {
		this.sqlMapClient.update("Bag.update", bag);
	}

	@Override
	public Bag getByHid(int hid) throws SQLException {
		return (Bag) this.sqlMapClient.queryForObject("Bag.getByHid", hid);
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

}
