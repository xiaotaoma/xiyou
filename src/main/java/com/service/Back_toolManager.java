package com.service;

import com.model.backstage.Back_tool;

import java.sql.SQLException;

public interface Back_toolManager {
	public void insert(Back_tool tool) throws SQLException;
}
