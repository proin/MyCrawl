package com.proinlab.mycrawl;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

class HttpUtil {

	String httpGet(String url, String encoding) throws IOException, ClientProtocolException {
		HttpClient httpclient = new DefaultHttpClient();
		String result = "";
		try {
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, encoding);

			Pattern nonValidPattern = Pattern.compile("<meta[^>]*charset=[\"']?([^>\"']+)[\"']?[^>]*>");
			StringBuffer out = new StringBuffer();
			Matcher matcher = nonValidPattern.matcher(result);
			String enc = null;
			while (matcher.find())
				enc = matcher.group(1);
			matcher.appendTail(out);

			if (enc != null && enc.length() != 0 && !enc.equals(encoding))
				return httpGet(url, enc);

			httpget.abort();
			httpclient.getConnectionManager().shutdown();
			httpclient = null;
		} catch (ClientProtocolException e) {
			throw new ClientProtocolException();
		} catch (IOException e) {
			throw new IOException();
		} catch (Exception e) {
			return null;
		}
		return result;
	}

}
