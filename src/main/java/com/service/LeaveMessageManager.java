package com.service;

import com.model.LeaveMessage;

import java.sql.SQLException;
import java.util.List;

public interface LeaveMessageManager {

	public void insert(LeaveMessage leaveMessage) throws SQLException;
	
	public void delete(String s) throws SQLException;
	
	public List<LeaveMessage> getByReceiverId(int receiverId) throws SQLException;

}
