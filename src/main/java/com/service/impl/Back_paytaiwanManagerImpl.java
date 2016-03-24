package com.service.impl;

import com.dao.Back_paytaiwanDao;
import com.model.backstage.Back_paytaiwan;
import com.service.Back_paytaiwanManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

public class Back_paytaiwanManagerImpl implements Back_paytaiwanManager {
	private Back_paytaiwanDao paytaiwanDao;
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void insert(Back_paytaiwan paytaiwan) throws SQLException {
		this.paytaiwanDao.insert(paytaiwan);
	}

	public void setPaytaiwanDao(Back_paytaiwanDao paytaiwanDao) {
		this.paytaiwanDao = paytaiwanDao;
	}

	public Back_paytaiwanDao getPaytaiwanDao() {
		return paytaiwanDao;
	}
	
}
