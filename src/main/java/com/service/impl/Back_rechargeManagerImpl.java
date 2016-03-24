package com.service.impl;

import com.dao.Back_rechargeDao;
import com.model.backstage.Back_recharge;
import com.service.Back_rechargeManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

public class Back_rechargeManagerImpl implements Back_rechargeManager {
	private Back_rechargeDao rechargeDao;
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void insert(Back_recharge recharge) throws SQLException {
		this.rechargeDao.insert(recharge);
	}
	public void setRechargeDao(Back_rechargeDao rechargeDao) {
		this.rechargeDao = rechargeDao;
	}
	public Back_rechargeDao getRechargeDao() {
		return rechargeDao;
	}
}
