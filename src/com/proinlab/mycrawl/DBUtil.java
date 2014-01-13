package com.proinlab.mycrawl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class DBUtil {

	private Connection con;

	DBUtil(String host, String port, String db_name, String user, String password) {
		getConnection(host, port, db_name, user, password);
	}

	private void getConnection(String host, String port, String db_name, String user, String passwd) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		}

		try {
			String url = "jdbc:mysql://" + host + ":" + port + "/" + db_name + "?useUnicode=true&characterEncoding=UTF-8";
			con = DriverManager.getConnection(url, user, passwd);
		} catch (SQLException e) {
		}
	}

	void closeConnection() {
		try {
			con.close();
		} catch (SQLException e) {
		}
	}

	ResultSet executeQuery(String sql) {
		if (con == null)
			return null;
		try {
			Statement stat = con.createStatement();
			ResultSet rs = stat.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			return null;
		}
	}

	boolean execute(String query) {
		try {
			Statement stat = con.createStatement();
			stat.execute(query);
			stat.close();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
}
