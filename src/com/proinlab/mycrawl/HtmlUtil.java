package com.proinlab.mycrawl;

class HtmlUtil {

	String HrefToUrl(String parent, String url) {
		String result = url;
		if (!url.contains("//")) {
			String[] folder;
			String prefix;
			if (parent.contains("//")) {
				prefix = parent.substring(0, parent.indexOf("//") + 2).toLowerCase();
				folder = parent.substring(parent.indexOf("//") + 2).split("/");
			} else {
				folder = parent.split("/");
				prefix = "http://";
			}

			if (!prefix.equals("http://") && !prefix.equals("https://"))
				return null;

			int count_back = 0;
			while (result.contains("..")) {
				result = result.substring(result.indexOf("..") + 2);
				count_back++;
			}

			if (result.startsWith("."))
				result = result.substring(1);

			if (folder.length - 2 > count_back) {
				for (int i = folder.length - 2 - count_back; i >= 0; i--) {
					if (result.startsWith("/"))
						result = folder[i] + result;
					else
						result = folder[i] + "/" + result;

				}
				result = prefix + result;
			}
		}
		if (result.endsWith("/"))
			result = result.substring(0, result.length() - 2);
		return result;
	}
}
