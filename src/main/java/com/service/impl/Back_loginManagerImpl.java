package com.service.impl;

import com.dao.Back_loginDao;
import com.model.backstage.Back_login;
import com.service.Back_loginManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;

public class Back_loginManagerImpl implements Back_loginManager {
	private Back_loginDao loginDao;
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void insert(Back_login login) throws SQLException {
		this.loginDao.insert(login);
	}

	@Override
	public Back_login getByHid(int hid,int time)
			throws SQLException {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("hid", hid);
		map.put("time", time);
		return this.loginDao.getByHid(map);
	}
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void updateTimes(Back_login login) throws SQLException {
		this.loginDao.updateTimes(login);
	}

	public void setLoginDao(Back_loginDao loginDao) {
		this.loginDao = loginDao;
	}

	public Back_loginDao getLoginDao() {
		return loginDao;
	}
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void updateTatolTime(Back_login login) throws SQLException {
		this.loginDao.updateTatolTime(login);
	}

}
