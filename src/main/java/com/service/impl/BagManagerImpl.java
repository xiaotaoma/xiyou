package com.service.impl;

import com.dao.BagDao;
import com.model.Bag;
import com.service.BagManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

public class BagManagerImpl implements BagManager {
	private BagDao bagDao;
	
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void insert(Bag bag) throws SQLException {
		this.bagDao.insert(bag);
	}
	
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void update(Bag bag) throws SQLException {
		this.bagDao.update(bag);
	}

	@Override
	public Bag getByHid(int hid) throws SQLException {
		return this.bagDao.getByHid(hid);
	}

	public void setBagDao(BagDao bagDao) {
		this.bagDao = bagDao;
	}

	public BagDao getBagDao() {
		return bagDao;
	}

}
