package com.dao;

import com.model.backstage.Back_record;

import java.sql.SQLException;

public interface Back_recordDao {
	public void insert(Back_record record) throws SQLException;
}
