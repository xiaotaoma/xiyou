package com.dao;

import com.model.backstage.Back_pay_dianxin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface Back_pay_dianxinDao {
	public void insert(Back_pay_dianxin pay_dianxin) throws SQLException;
	
	public List<Back_pay_dianxin> getUndoneDeal(int hid) throws SQLException;
	
	public Back_pay_dianxin getByOrderId(int orderId) throws SQLException;
	
	public void updateDone(HashMap<String, Integer> map) throws SQLException;
}
