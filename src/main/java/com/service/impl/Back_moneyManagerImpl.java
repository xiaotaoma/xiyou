package com.service.impl;

import com.dao.Back_moneyDao;
import com.model.backstage.Back_money;
import com.service.Back_moneyManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

public class Back_moneyManagerImpl implements Back_moneyManager {
	private Back_moneyDao moneyDao;
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void insert(Back_money money) throws SQLException {
		this.moneyDao.insert(money);
	}
	public void setMoneyDao(Back_moneyDao moneyDao) {
		this.moneyDao = moneyDao;
	}
	public Back_moneyDao getMoneyDao() {
		return moneyDao;
	}

}
