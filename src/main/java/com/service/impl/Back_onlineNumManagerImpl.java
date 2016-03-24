package com.service.impl;

import com.dao.Back_onlineNumDao;
import com.model.backstage.Back_onlineNum;
import com.service.Back_onlineNumManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

public class Back_onlineNumManagerImpl implements Back_onlineNumManager {
	private Back_onlineNumDao onlineNumDao;
	
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void insert(Back_onlineNum onlineNum) throws SQLException {
		this.onlineNumDao.insert(onlineNum);
	}
	public void setOnlineNumDao(Back_onlineNumDao onlineNumDao) {
		this.onlineNumDao = onlineNumDao;
	}
	public Back_onlineNumDao getOnlineNumDao() {
		return onlineNumDao;
	}
}
