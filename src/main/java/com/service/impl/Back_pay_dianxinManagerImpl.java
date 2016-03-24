package com.service.impl;

import com.dao.Back_pay_dianxinDao;
import com.model.backstage.Back_pay_dianxin;
import com.service.Back_pay_dianxinManager;
import com.util.TimeUtil;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class Back_pay_dianxinManagerImpl implements Back_pay_dianxinManager {
	private Back_pay_dianxinDao dianxinDao;
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void insert(Back_pay_dianxin pay_dianxin) throws SQLException {
		this.dianxinDao.insert(pay_dianxin);
	}
	public void setDianxinDao(Back_pay_dianxinDao dianxinDao) {
		this.dianxinDao = dianxinDao;
	}
	public Back_pay_dianxinDao getDianxinDao() {
		return dianxinDao;
	}
	@Override
	public List<Back_pay_dianxin> getUndoneDeal(int hid) throws SQLException {
		return this.dianxinDao.getUndoneDeal(hid);
	}
	@Override
	public Back_pay_dianxin getByOrderId(int orderId) throws SQLException {
		return this.dianxinDao.getByOrderId(orderId);
	}
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void updateDone(int orderId) throws SQLException {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("giveTime", TimeUtil.currentTime());
		map.put("orderId", orderId);
		this.dianxinDao.updateDone(map);
	}
}
