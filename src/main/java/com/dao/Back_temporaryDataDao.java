package com.dao;

import com.model.backstage.Back_temporaryData;

import java.sql.SQLException;
import java.util.List;

public interface Back_temporaryDataDao {
	public void insert(Back_temporaryData temporaryData) throws SQLException;
	
	public List<Back_temporaryData> getByFlag(int flag) throws SQLException;
	
	public void truncate()throws SQLException;
}
