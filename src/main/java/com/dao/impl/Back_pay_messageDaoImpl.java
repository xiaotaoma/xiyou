package com.dao.impl;

import com.dao.Back_pay_messageDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.backstage.Back_pay_message;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class Back_pay_messageDaoImpl implements Back_pay_messageDao{
	private SqlMapClient sqlMapClient;
	@Override
	public void insert(Back_pay_message pay_message) throws SQLException {
		this.sqlMapClient.insert("Back_pay_message.insert", pay_message);
	}
	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}
	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}
	@Override
	public List<Back_pay_message> getUndone(int hid) throws SQLException {
		return this.sqlMapClient.queryForList("Back_pay_message.selectUndoneDeal", hid);
	}
	@Override
	public void update(HashMap<String, Object> map) throws SQLException {
		this.sqlMapClient.update("Back_pay_message.updateDone", map);
	}
	@Override
	public Back_pay_message getByOrderId(String orderId) throws SQLException {
		return (Back_pay_message) this.sqlMapClient.queryForObject("Back_pay_message.getByOrderId", orderId);
	}
	
}
