package com.dao;

import com.model.Friend;

import java.sql.SQLException;

public interface FriendDao {
	public void insert(Friend friend) throws SQLException;
	
	public Friend getByHid(int hid) throws SQLException;
	
	public void update(Friend friend) throws SQLException;
}
