package com.service.impl;

import com.dao.Back_toolDao;
import com.model.backstage.Back_tool;
import com.service.Back_toolManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

public class Back_toolManagerImpl implements Back_toolManager {
	private Back_toolDao toolDao;
	
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void insert(Back_tool tool) throws SQLException {
		this.toolDao.insert(tool);
	}
	public void setToolDao(Back_toolDao toolDao) {
		this.toolDao = toolDao;
	}
	public Back_toolDao getToolDao() {
		return toolDao;
	}
	
}
