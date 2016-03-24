package com.service;

import com.model.backstage.Back_record;

import java.sql.SQLException;

public interface Back_recordManager {
	public void insert(Back_record record) throws SQLException;
}
