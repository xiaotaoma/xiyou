package com.service.impl;

import com.dao.AccountDao;
import com.model.Account;
import com.service.AccountManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

public class AccountManagerImpl implements AccountManager {
	private AccountDao accountDao;
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void insert(Account account) throws SQLException {
		this.accountDao.insert(account);
	}
	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}
	public AccountDao getAccountDao() {
		return accountDao;
	}
	@Override
	public List<Account> getByAccount(String account) throws SQLException {
		return this.accountDao.getByAccount(account);
	}
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void update(Account account) throws SQLException {
		this.accountDao.update(account);
	}
	
}
