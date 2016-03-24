package com.service;

import com.model.Account;

import java.sql.SQLException;
import java.util.List;

public interface AccountManager {
	public void insert(Account account) throws SQLException;
	
	public List<Account> getByAccount(String account) throws SQLException;
	
	public void update(Account account) throws SQLException;
}
