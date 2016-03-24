package com.service.impl;

import com.dao.LeaveMessageDao;
import com.model.LeaveMessage;
import com.service.LeaveMessageManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

public class LeaveMessageManagerImpl implements LeaveMessageManager {
	private LeaveMessageDao leaveMessageDao;
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void insert(LeaveMessage leaveMessage) throws SQLException {
		this.leaveMessageDao.insert(leaveMessage);
	}
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void delete(String s) throws SQLException {
		this.leaveMessageDao.delete(s);
	}

	@Override
	public List<LeaveMessage> getByReceiverId(int receiverId)
			throws SQLException {
		return this.leaveMessageDao.getByReceiverId(receiverId);
	}

	public void setLeaveMessageDao(LeaveMessageDao leaveMessageDao) {
		this.leaveMessageDao = leaveMessageDao;
	}

	public LeaveMessageDao getLeaveMessageDao() {
		return leaveMessageDao;
	}

}
