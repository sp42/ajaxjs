package com.ajaxjs.mysql.manager;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Data;

@Data
public class UserReport {
	public static final char fieldSeparator = '\u0001';
	public static final char pairSeparator = '\u0002';
	public static final char kvSeparator = '\u0003';

	private int id;// report id, user specific
	private String type;// report type
	private String dbname;// db/cluster name
	private String dbhost;// db hostname, default to all
	private String created_timestamp;// yyyyMMddHHmmssSSS in java
	private String completed_timestamp;// yyyyMMddHHmmssSSS in java
	private String format;// html or text
	private String filename;// file name will be type dependent
	private LinkedHashMap<String, String> parameters = new java.util.LinkedHashMap<>();

	public void addParameter(String name, String value) {
		parameters.put(name, value);
	}

	public String encodeLine() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.id).append(fieldSeparator);
		sb.append(this.type).append(fieldSeparator);
		sb.append(this.dbname).append(fieldSeparator);
		sb.append(this.dbhost).append(fieldSeparator);
		sb.append(this.created_timestamp).append(fieldSeparator);
		sb.append(this.format).append(fieldSeparator);
		sb.append(this.filename).append(fieldSeparator);

		boolean first = true;
		for (Map.Entry<String, String> e : this.parameters.entrySet()) {
			if (!first)
				sb.append(pairSeparator);
			else
				first = false;
			sb.append(e.getKey()).append(kvSeparator).append(e.getValue());
		}

		sb.append(fieldSeparator);
		sb.append(this.completed_timestamp);

		return sb.toString();
	}

	public static UserReport decodeLine(String line) {
		if (line == null || line.trim().length() == 0)
			return null;

		String[] tokens = line.trim().split("\\u0001");
		UserReport urp = new UserReport();

		try {
			int idx = 0;
			urp.setId(Integer.parseInt(tokens[idx++]));
			urp.setType(tokens[idx++]);
			urp.setDbname(tokens[idx++]);
			urp.setDbhost(tokens[idx++]);
			urp.setCreated_timestamp(tokens[idx++]);
			urp.setFormat(tokens[idx++]);
			urp.setFilename(tokens[idx++]);
			String p = tokens[idx++];

			if (p != null) {
				String[] pairs = p.split("\\u0002");
				if (pairs != null) {
					for (String pair : pairs) {
						String[] kv = pair.split("\\u0003");

						if (kv != null && kv.length == 2)
							urp.addParameter(kv[0], kv[1]);

					}
				}
			}
			urp.setCompleted_timestamp(tokens[idx++]);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return urp;
	}

}