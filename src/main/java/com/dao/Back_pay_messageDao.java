package com.dao;

import com.model.backstage.Back_pay_message;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface Back_pay_messageDao {
	public void insert(Back_pay_message pay_message) throws SQLException;
	
	public List<Back_pay_message> getUndone(int hid) throws SQLException;
	
	public void update(HashMap<String, Object> map) throws SQLException;
	
	public Back_pay_message getByOrderId(String orderId) throws SQLException;
}
