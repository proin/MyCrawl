# MyCrawl
## Overview
**MyCrawl** is a Web Crawler. This crawler written by java and using MySQL Database.

## Dependencies
to use this library, you need some external library belong to `lib/` which includes `Apache HttpClient`, `JDBC`.

## Usage
You can collect web data using by this library. `MyCrawl` class is main collector. set MyCrawlSetting class to this class. You can order some process when visit each web page by adding OnVisitListener.

This is parts of `SampleCrawler.java`.

```java
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
```
