package com.ajaxjs.mysql.model.alert.subscription;

import lombok.Data;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.util.HashMap;
import java.util.Map;

@Data
public class Subscription {
	// lazy, not getter/setter
	public String group; // server group
	public String host;// server host, can be null
	public String alertName; // alert name
	public Float threshold; // if required. Null use default
	public Map<String, String> params = new HashMap<>();

	public Subscription() {
	}

	// parse and set params
	public void setParams(String paramString) {
		if (paramString == null || paramString.isEmpty())
			return;
		
		JsonReader jsonReader;

		try {
			jsonReader = javax.json.Json.createReader(new java.io.ByteArrayInputStream(paramString.getBytes()));
			JsonObject jsonObject = jsonReader.readObject();
			jsonReader.close();

			String thresholdString = jsonObject.getString("threshold", null);
			if (thresholdString != null && !thresholdString.isEmpty())
				try {
					threshold = Float.parseFloat(thresholdString);
				} catch (Exception ex) {
				}
			JsonArray paramList = jsonObject.getJsonArray("params");

			if (paramList != null) {
				int mlen = paramList.size();

				for (int i = 0; i < mlen; i++) {
					JsonObject mobj = paramList.getJsonObject(i);
					params.put(mobj.getString("name"), mobj.getString("value"));
				}
			}
		} catch (Exception ex) {
			System.out.println("Error to parse alert subscription params: " + paramString);
		}

	}

	public String paramToJSON() {
		if ((this.params == null || this.params.size() == 0) && this.threshold == null)
			return null;
		StringBuilder mtrStr = new StringBuilder();
		mtrStr.append("{\r\n");
		if (this.threshold != null)
			mtrStr.append("\"threshold\": \"").append(threshold).append("\",\r\n");
		mtrStr.append("\"params\":[");
		int cnt = 0;

		for (Map.Entry<String, String> e : this.params.entrySet()) {
			if (cnt > 0)
				mtrStr.append(",\r\n");
			mtrStr.append("{\"name\": \"").append(e.getKey()).append("\",\"value\":\"").append(e.getValue()).append("\"}");
		}
		mtrStr.append("]}");

		return mtrStr.toString();
	}
}
