package com.proinlab.mycrawl;

import java.util.ArrayList;
import java.util.HashMap;

public class MyCrawl {

	private Seeds seeds = null;
	private MyCrawlSetting setting = new MyCrawlSetting();

	// Seed Setting
	public void addSeed(String url, String enc) {
		if (seeds != null && seeds.length() <= 20)
			seeds.addSeed(url, enc);
	}

	public void addSeed(String[] urls, String[] encs) {
		if (seeds != null && seeds.length() <= 20)
			seeds.addSeed(urls, encs);
	}

	public void setSetting(MyCrawlSetting setting) {
		this.setting = setting;
		DBUtil db = new DBUtil(setting.getMysqlHost(), setting.getMysqlPort(), "", setting.getMysqlUser(), setting.getMysqlPassword());

		db.execute("CREATE DATABASE " + setting.getDBName());
		db.execute("USE " + setting.getDBName());

		if (!setting.isContinue()) {
			db.execute("DROP TABLE " + setting.getWorkingTable());
			db.execute("DROP TABLE " + setting.getSeedTable());
		}

		db.execute("CREATE TABLE " + setting.getSeedTable() + "(id INTEGER PRIMARY KEY AUTO_INCREMENT ,url VARCHAR(200) NOT NULL, encoding VARCHAR(10) NOT NULL);");
		db.execute("CREATE TABLE " + setting.getWorkingTable()
				+ "(id INTEGER PRIMARY KEY AUTO_INCREMENT, seed_id INTEGER NOT NULL, url VARCHAR(400) NOT NULL, stat INTEGER NOT NULL, index (seed_id), foreign key(seed_id) references "
				+ setting.getSeedTable() + "(id) on delete cascade on update cascade)");
		db.closeConnection();
		seeds = new Seeds(setting);
	}

	public void start() {
		if (setting == null)
			return;
		ArrayList<HashMap<String, String>> seedList = seeds.getSeeds();
		int thread_count = seedList.size();
		if (thread_count > setting.getMaxThread())
			thread_count = setting.getMaxThread();
		for (int i = 0; i < thread_count; i++) {
			ArrayList<HashMap<String, String>> thread_seeds = new ArrayList<HashMap<String, String>>();
			int c = 0;
			while (c != seedList.size()) {
				if (c % thread_count == i)
					thread_seeds.add(seedList.get(c));
				c++;
			}
			Visitor visitor = new Visitor(setting, thread_seeds);
			new Thread(visitor).start();
		}
	}
}
