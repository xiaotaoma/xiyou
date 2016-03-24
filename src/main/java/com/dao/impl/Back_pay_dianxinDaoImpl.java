package com.dao.impl;

import com.dao.Back_pay_dianxinDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.backstage.Back_pay_dianxin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class Back_pay_dianxinDaoImpl implements Back_pay_dianxinDao{
	private SqlMapClient sqlMapClient;
	@Override
	public void insert(Back_pay_dianxin pay_dianxin) throws SQLException {
		this.sqlMapClient.insert("Back_pay_dianxin.insert", pay_dianxin);
	}
	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}
	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}
	@Override
	public List<Back_pay_dianxin> getUndoneDeal(int hid) throws SQLException {
		return this.sqlMapClient.queryForList("Back_pay_dianxin.getUnDoneDeal", hid);
	}
	@Override
	public Back_pay_dianxin getByOrderId(int orderId) throws SQLException {
		return (Back_pay_dianxin) this.sqlMapClient.queryForObject("Back_pay_dianxin.getByOrderId", orderId);
	}
	@Override
	public void updateDone(HashMap<String, Integer> map) throws SQLException {
		this.sqlMapClient.update("Back_pay_dianxin.update", map);
	}

}
