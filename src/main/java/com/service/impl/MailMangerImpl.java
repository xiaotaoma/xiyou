package com.service.impl;

import com.dao.MailDao;
import com.model.Mail;
import com.service.MailManager;

import java.sql.SQLException;
import java.util.List;

public class MailMangerImpl implements MailManager {
	private MailDao mailDao;
	@Override
	public void insert(Mail mail) throws SQLException {
		this.mailDao.insert(mail);
	}

	@Override
	public List<MailDao> getBySenderId(int senderId) throws SQLException {
		return this.mailDao.getBySenderId(senderId);
	}

	public void setMailDao(MailDao mailDao) {
		this.mailDao = mailDao;
	}

	public MailDao getMailDao() {
		return mailDao;
	}

}
