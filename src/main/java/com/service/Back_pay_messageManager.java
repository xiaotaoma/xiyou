package com.service;

import com.model.backstage.Back_pay_message;

import java.sql.SQLException;
import java.util.List;

public interface Back_pay_messageManager {
	public void insert(Back_pay_message pay_message) throws SQLException; 
	
	public List<Back_pay_message> getUndone(int hid) throws SQLException;
	
	public void update(int giveTime, String orderId, int isdone) throws SQLException;
	
	public Back_pay_message getByOrderId(String orderId) throws SQLException;
}
