package com.ajaxjs;

import java.util.List;

import com.ajaxjs.net.http.NetUtil;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

public class CSE {
	private static final int HTTP_REQUEST_TIMEOUT = 3 * 600000;

	public static void main2(String[] args) {
		String keyword = "a";

		Customsearch customsearch = null;

		try {
			customsearch = new Customsearch(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
				public void initialize(HttpRequest httpRequest) {
					try {
						// set connect and read timeouts
						httpRequest.setConnectTimeout(HTTP_REQUEST_TIMEOUT);
						httpRequest.setReadTimeout(HTTP_REQUEST_TIMEOUT);

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Result> resultList = null;
		try {
			Customsearch.Cse.List list = customsearch.cse().list(keyword);
//			list.setKey(GOOGLE_API_KEY);
			list.setCx("003205160597622947000:rdukvi3jo6a");
			Search results = list.execute();
			resultList = results.getItems();
			System.out.println(resultList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static final String ID = "010589607068358538435:p8ot8fmvira"; 
	
	public static void main(String[] args) {
		String r = NetUtil.get("https://www.googleapis.com/customsearch/v1?key=AIzaSyDDRi8Yf75G7pRx0r49DrmSfcr3jPBHMRY&cx=017576662512468239146:omuauf_lfve&q=cars");
		
		System.out.println(r);

	}
}
