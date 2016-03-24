package com.service.impl;

import com.dao.Back_pay_messageDao;
import com.model.backstage.Back_pay_message;
import com.service.Back_pay_messageManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class Back_pay_messageManagerImpl implements Back_pay_messageManager {
	private Back_pay_messageDao pay_messageDao;
	
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void insert(Back_pay_message pay_message) throws SQLException {
		this.pay_messageDao.insert(pay_message);
	}
	public void setPay_messageDao(Back_pay_messageDao pay_messageDao) {
		this.pay_messageDao = pay_messageDao;
	}
	public Back_pay_messageDao getPay_messageDao() {
		return pay_messageDao;
	}
	@Override
	public List<Back_pay_message> getUndone(int hid) throws SQLException {
		return this.pay_messageDao.getUndone(hid);
	}
	
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void update(int giveTime,String orderId,int isdone) throws SQLException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("giveTime", giveTime);
		map.put("orderId", orderId);
		map.put("isdone", isdone);
		this.pay_messageDao.update(map);
	}
	@Override
	public Back_pay_message getByOrderId(String orderId) throws SQLException {
		return this.pay_messageDao.getByOrderId(orderId);
	}
}
