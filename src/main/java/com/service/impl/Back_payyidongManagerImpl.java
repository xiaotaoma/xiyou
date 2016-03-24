package com.service.impl;

import com.dao.Back_payyidongDao;
import com.model.backstage.Back_payyidong;
import com.service.Back_payyidongManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

public class Back_payyidongManagerImpl implements Back_payyidongManager {
	private Back_payyidongDao payyidongDao;
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void insert(Back_payyidong payyidong) throws SQLException {
		this.payyidongDao.insert(payyidong);
	}
	public Back_payyidongDao getPayyidongDao() {
		return payyidongDao;
	}
	public void setPayyidongDao(Back_payyidongDao payyidongDao) {
		this.payyidongDao = payyidongDao;
	}
	
}
