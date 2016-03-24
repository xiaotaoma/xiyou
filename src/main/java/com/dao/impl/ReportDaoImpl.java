package com.dao.impl;

import com.dao.ReportDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.Report;

import java.sql.SQLException;
import java.util.List;

public class ReportDaoImpl implements ReportDao {
	private SqlMapClient sqlMapClient;
	
	@Override
	public void insert(Report report) throws SQLException {
		this.sqlMapClient.insert("Report.insert", report);
	}

	@Override
	public List<Report> getReports(int hid) throws SQLException {
		return this.sqlMapClient.queryForList("Report.getReports", hid);
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}
	@Override
	public List<Report> getBobedReports(int hid) throws SQLException {
		return this.sqlMapClient.queryForList("Report.getBobedReports", hid);
	}
}
