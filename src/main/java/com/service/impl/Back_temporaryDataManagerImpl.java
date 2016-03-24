package com.service.impl;

import com.dao.Back_temporaryDataDao;
import com.model.backstage.Back_temporaryData;
import com.service.Back_temporaryDataManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

public class Back_temporaryDataManagerImpl implements Back_temporaryDataManager {
	private Back_temporaryDataDao temporaryDataDao;
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void insert(Back_temporaryData temporaryData) throws SQLException {
		this.temporaryDataDao.insert(temporaryData);
	}

	public void setTemporaryDataDao(Back_temporaryDataDao temporaryDataDao) {
		this.temporaryDataDao = temporaryDataDao;
	}

	public Back_temporaryDataDao getTemporaryDataDao() {
		return temporaryDataDao;
	}

	@Override
	public List<Back_temporaryData> getByFlag(int flag) throws SQLException {
		return this.temporaryDataDao.getByFlag(flag);
	}
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void truncate() throws SQLException {
		this.temporaryDataDao.truncate();
	}

}
