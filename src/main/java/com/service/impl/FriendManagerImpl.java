package com.service.impl;

import com.dao.FriendDao;
import com.model.Friend;
import com.service.FriendManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

public class FriendManagerImpl implements FriendManager {
	private FriendDao friendDao;
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void insert(Friend friend) throws SQLException {
		this.friendDao.insert(friend);
	}

	@Override
	public Friend getByHid(int hid) throws SQLException {
		return this.friendDao.getByHid(hid);
	}

	public void setFriendDao(FriendDao friendDao) {
		this.friendDao = friendDao;
	}

	public FriendDao getFriendDao() {
		return friendDao;
	}
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void update(Friend friend) throws SQLException {
		this.friendDao.update(friend);
	}
}
