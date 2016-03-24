package com.dao;

import com.model.LeaveMessage;

import java.sql.SQLException;
import java.util.List;

public interface LeaveMessageDao {
	public void insert(LeaveMessage leaveMessage) throws SQLException;
	
	public void delete(String s) throws SQLException;
	
	public List<LeaveMessage> getByReceiverId(int receiverId) throws SQLException;
}
