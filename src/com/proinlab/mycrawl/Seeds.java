package com.proinlab.mycrawl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

class Seeds {

	private MyCrawlSetting setting;
	private DBUtil db;

	Seeds(MyCrawlSetting setting) {
		this.setting = setting;
		db = new DBUtil(setting.getMysqlHost(), setting.getMysqlPort(), "", setting.getMysqlUser(), setting.getMysqlPassword());
		db.execute("USE " + setting.getDBName());
	}

	void addSeed(String url, String enc) {
		try {
			ResultSet rs = db.executeQuery("SELECT * FROM " + setting.getSeedTable() + " WHERE url = '" + url + "'");
			if (rs == null || !rs.next())
				db.execute("INSERT INTO " + setting.getSeedTable() + "(url,encoding) VALUES('" + url + "','" + enc + "')");
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
		}
	}

	void addSeed(String[] urls, String[] encs) {
		for (int i = 0; i < urls.length; i++)
			addSeed(urls[i], encs[i]);
	}

	ArrayList<HashMap<String, String>> getSeeds() {
		ArrayList<HashMap<String, String>> seeds = new ArrayList<HashMap<String, String>>();
		try {
			ResultSet rs = db.executeQuery("SELECT * FROM " + setting.getSeedTable());
			if (rs != null) {
				while (rs.next()) {
					HashMap<String, String> info = new HashMap<String, String>();
					info.put("id", rs.getString("id"));
					info.put("url", rs.getString("url"));
					info.put("encoding", rs.getString("encoding"));
					seeds.add(info);
				}
				rs.close();
			}
		} catch (SQLException e) {
		}
		return seeds;
	}

	int length() {
		try {
			ResultSet rs = db.executeQuery("SELECT count(*) FROM " + setting.getSeedTable());
			if (rs != null) {
				rs.next();
				int result = rs.getInt("count(*)");
				rs.close();
				return result;
			}
		} catch (SQLException e) {
		}
		return 0;
	}
}
