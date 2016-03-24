package com.service.impl;

import com.dao.Back_pay91Dao;
import com.model.backstage.Back_pay91;
import com.service.Back_pay91Manager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

public class Back_pay91ManagerImpl implements Back_pay91Manager {
	private Back_pay91Dao pay91Dao;
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void insert(Back_pay91 pay91) throws SQLException {
		this.pay91Dao.insert(pay91);
	}
	public void setPay91Dao(Back_pay91Dao pay91Dao) {
		this.pay91Dao = pay91Dao;
	}
	public Back_pay91Dao getPay91Dao() {
		return pay91Dao;
	}

}
