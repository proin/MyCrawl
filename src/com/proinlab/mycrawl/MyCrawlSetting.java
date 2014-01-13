package com.proinlab.mycrawl;

import com.proinlab.mycrawl.interfaces.OnVisitListener;
import com.proinlab.mycrawl.interfaces.VisitFilter;

public class MyCrawlSetting {
	private String mysql_host = "localhost";
	private String mysql_port = "3306";
	private String mysql_db_name = "crawler";
	private String mysql_seed_table = "seeds";
	private String mysql_working_table = "working";
	private String mysql_user = "root";
	private String mysql_passwd = "";
	private int max_working_size = 10000;

	private boolean continued = true;

	private long wait_time = 1000;
	private boolean seed_dependency = true;
	private OnVisitListener onVisitListener = new OnVisitListener() {
		@Override
		public void onVisit(String url, String html) {
		}
	};

	private VisitFilter visitFilter = new VisitFilter() {
		@Override
		public boolean filter(String url) {
			return true;
		}
	};

	private int max_thread = 10;

	public void setDBName(String db_name) {
		this.mysql_db_name = db_name;
	}

	public String getDBName() {
		return mysql_db_name;
	}

	public void setSeedTable(String seed_table) {
		this.mysql_seed_table = seed_table;
	}

	public String getSeedTable() {
		return mysql_seed_table;
	}

	public void setWorkingTable(String working_table) {
		this.mysql_working_table = working_table;
	}

	public String getWorkingTable() {
		return mysql_working_table;
	}

	public void setMysqlHost(String host) {
		this.mysql_host = host;
	}

	public String getMysqlHost() {
		return mysql_host;
	}

	public void setMysqlPort(String port) {
		this.mysql_port = port;
	}

	public String getMysqlPort() {
		return mysql_port;
	}

	public void setMysqlUser(String user) {
		this.mysql_user = user;
	}

	public String getMysqlUser() {
		return mysql_user;
	}

	public void setMysqlPassword(String passwd) {
		this.mysql_passwd = passwd;
	}

	public String getMysqlPassword() {
		return mysql_passwd;
	}

	// OnVisitListener
	public void setOnVisitListener(OnVisitListener onVisitListner) {
		this.onVisitListener = onVisitListner;
	}

	public OnVisitListener getOnVisitListener() {
		return onVisitListener;
	}

	// Wait Time Setting
	public void setWaitTime(long t) {
		wait_time = t;
	}

	public long getWaitTime() {
		return wait_time;
	}

	public void setSeedDependency(boolean b) {
		seed_dependency = b;
	}

	public boolean getSeedDependency() {
		return seed_dependency;
	}

	public void setMaxThread(int size) {
		this.max_thread = size;
	}

	public int getMaxThread() {
		return max_thread;
	}

	public boolean isContinue() {
		return continued;
	}

	public void setContinue(boolean b) {
		this.continued = b;
	}

	public VisitFilter getVisitFilter() {
		return visitFilter;
	}

	public void setVisitFilter(VisitFilter visitFilter) {
		this.visitFilter = visitFilter;
	}

	public void setMaxStackSize(int i) {
		this.max_working_size = i;
	}

	public int getMaxStackSize() {
		return max_working_size;
	}

}
