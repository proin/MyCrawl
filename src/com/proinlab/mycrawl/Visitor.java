package com.proinlab.mycrawl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Visitor implements Runnable {

	private MyCrawlSetting setting;

	private ArrayList<HashMap<String, String>> seedlist;
	private DBUtil db;
	private HttpUtil http = new HttpUtil();
	private int working_size = 0;

	Visitor(MyCrawlSetting setting, ArrayList<HashMap<String, String>> seedlist) {
		this.seedlist = seedlist;
		this.setting = setting;
		db = new DBUtil(setting.getMysqlHost(), setting.getMysqlPort(), "", setting.getMysqlUser(), setting.getMysqlPassword());
		db.execute("USE " + setting.getDBName());
	}

	@Override
	public void run() {
		try {
			if (seedlist.size() > 0) {
				int index = 0;
				while (true) {
					visit(seedlist.get(index));
					index = (index + 1) % seedlist.size();
				}
			}
		} catch (OutOfMemoryError e) {
			sleep(10000);
			run();
		} catch (Exception e) {
			run();
		}
	}

	private void sleep(long t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException e1) {
		}
	}

	private void visit(HashMap<String, String> seed) {
		String id = null; // visiting list id of database
		String url = null; // visiting list url of database
		String html = null;
		try {
			// get url, id from database
			ResultSet rs = db.executeQuery("SELECT count(*) FROM " + setting.getWorkingTable() + " WHERE seed_id = " + seed.get("id"));
			rs.next();
			working_size = rs.getInt("count(*)");
			rs.close();
			rs = null;

			rs = db.executeQuery("SELECT id,url FROM " + setting.getWorkingTable() + " WHERE seed_id = " + seed.get("id") + " AND stat = 0 LIMIT 1;");
			if (rs == null || rs.next() == false) {
				// if data not exists
				db.execute("DELETE FROM " + setting.getWorkingTable() + " WHERE seed_id = '" + seed.get("id") + "'");
				db.execute("INSERT INTO " + setting.getWorkingTable() + "(seed_id,url,stat) VALUES('" + seed.get("id") + "','" + seed.get("url") + "',0)");
			} else {
				id = rs.getString("id");
				url = rs.getString("url");
				html = http.httpGet(url, seed.get("encoding"));
				if (html != null)
					saveChildURL(seed, url, html);

				// delete from list
				db.execute("DELETE FROM " + setting.getWorkingTable() + " WHERE id = " + id);
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			db.execute("DELETE FROM " + setting.getWorkingTable() + " WHERE id = " + id);
			db.execute("INSERT INTO " + setting.getWorkingTable() + "(seed_id,url,stat) VALUES('" + seed.get("id") + "','" + url + "',0)");
		}

		if (url != null && html != null && setting.getOnVisitListener() != null)
			setting.getOnVisitListener().onVisit(url, html);

		System.gc();
		sleep(setting.getWaitTime());
	}

	private void saveChildURL(HashMap<String, String> seed, String url, String html) {
		Pattern nonValidPattern = Pattern.compile("<a[^>]*href=[\"']?([^>\"']+)[\"']?[^>]*>");
		StringBuffer out = new StringBuffer();
		Matcher matcher = nonValidPattern.matcher(html);
		while (matcher.find()) {
			try {
				String child = new HtmlUtil().HrefToUrl(url, matcher.group(1));
				if (child != null)
					if (setting.getSeedDependency() == false || child.contains(seed.get("url").substring(seed.get("url").indexOf(".")))) {
						boolean b = true;
						int limit = setting.getMaxStackSize() / new Seeds(setting).length();
						if (setting.getMaxStackSize() > 0 && working_size > limit)
							return;

						try {
							b = setting.getVisitFilter().filter(child);
						} catch (Exception e) {
						}

						try {
							ResultSet rs = db.executeQuery("SELECT id FROM " + setting.getWorkingTable() + " WHERE url='" + child + "' AND stat = 0 LIMIT 1");
							if (rs == null || rs.next() == false)
								b = true;
							else
								b = false;
							rs.close();
							rs = null;
						} catch (SQLException e) {
						}

						if (b) {
							db.execute("INSERT INTO " + setting.getWorkingTable() + "(seed_id,url,stat) VALUES('" + seed.get("id") + "','" + child + "',0)");
							working_size++;
						}
					}
			} catch (Exception e) {
			}
		}
		matcher.appendTail(out);
	}

}