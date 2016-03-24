package com.service.impl;

import com.dao.Back_registerDao;
import com.model.backstage.Back_register;
import com.service.Back_registerManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

public class Back_registerManagerImpl implements Back_registerManager {
	private Back_registerDao registerDao;
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void insert(Back_register register) throws SQLException {
		this.registerDao.insert(register);
	}
	public void setRegisterDao(Back_registerDao registerDao) {
		this.registerDao = registerDao;
	}
	public Back_registerDao getRegisterDao() {
		return registerDao;
	}
	

}
