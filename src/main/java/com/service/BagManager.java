package com.service;

import com.model.Bag;

import java.sql.SQLException;

public interface BagManager {
	
	public void insert(Bag bag) throws SQLException;
	
	public void update(Bag bag) throws SQLException;
	
	public Bag getByHid(int hid) throws SQLException;
}
