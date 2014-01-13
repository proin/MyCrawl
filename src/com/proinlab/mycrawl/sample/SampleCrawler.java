package com.proinlab.mycrawl.sample;
import com.proinlab.mycrawl.MyCrawl;
import com.proinlab.mycrawl.MyCrawlSetting;
import com.proinlab.mycrawl.interfaces.OnVisitListener;

public class SampleCrawler {
	public static void main(String[] args) {
		MyCrawl crawler = new MyCrawl();

		MyCrawlSetting setting = new MyCrawlSetting();
		setting.setDBName("crawler");
		setting.setSeedTable("crawler_seed");
		setting.setWorkingTable("crawler_work");
		setting.setWaitTime(2500);
		setting.setMaxThread(20);
		setting.setContinue(false);
		setting.setOnVisitListener(new OnVisitListener() {
			@Override
			public void onVisit(String url, String html) {
				System.out.println(url);
			}
		});
		crawler.setSetting(setting);

		crawler.addSeed("http://www.zdnet.co.kr", "utf-8");
		crawler.start();
	}
}
