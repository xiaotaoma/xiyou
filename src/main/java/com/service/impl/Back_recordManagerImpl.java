package com.service.impl;

import com.dao.Back_recordDao;
import com.model.backstage.Back_record;
import com.service.Back_recordManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

public class Back_recordManagerImpl implements Back_recordManager {
	private Back_recordDao recordDao;
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void insert(Back_record record) throws SQLException {
		this.recordDao.insert(record);
	}
	public void setRecordDao(Back_recordDao recordDao) {
		this.recordDao = recordDao;
	}
	public Back_recordDao getRecordDao() {
		return recordDao;
	}

}
