package com.service;

import com.model.Friend;

import java.sql.SQLException;

public interface FriendManager {
	public void insert(Friend friend) throws SQLException;
	
	public Friend getByHid(int hid) throws SQLException;
	
	public void update(Friend friend) throws SQLException;
	
}
