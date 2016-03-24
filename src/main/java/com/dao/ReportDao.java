package com.dao;

import com.model.Report;

import java.sql.SQLException;
import java.util.List;

public interface ReportDao {
	public void insert(Report report) throws SQLException;
	
	public List<Report> getReports(int hid) throws SQLException;
	
	public List<Report> getBobedReports(int hid) throws SQLException;
}
