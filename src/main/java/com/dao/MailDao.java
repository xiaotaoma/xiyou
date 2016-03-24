package com.dao;

import com.model.Mail;

import java.sql.SQLException;
import java.util.List;

public interface MailDao {
	public void insert(Mail mail) throws SQLException;
	
	public List<MailDao> getBySenderId(int senderId) throws SQLException;
}
