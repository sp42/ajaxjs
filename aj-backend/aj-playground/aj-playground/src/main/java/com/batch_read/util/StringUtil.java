package com.batch_read.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class StringUtil {

	public static Object unserializeObj(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
//			logger.error("unserializeObj error:" + e.getMessage(), e);
		}
		return null;
	}

	public static byte[] serializeObj(Object obj) {
		ByteArrayOutputStream bout = null;
		ObjectOutputStream out = null;
		byte[] bytes = null;
		try {
			bout = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bout);
			out.writeObject(obj);
			out.flush();
			bytes = bout.toByteArray();
		} catch (Exception e) {
//			logger.error("serializeObject error:" + e.getMessage(), e);
		} finally {
			try {
				if (out != null) {
					out.close();
					out = null;
				}
			} catch (Exception e) {
			}
			try {
				if (bout != null) {
					bout.close();
					bout = null;
				}
			} catch (Exception e) {
			}
		}
		return bytes;
	}

	public static String escpaeCharacters(String s) {
		String val = "";
		try {
			if (s == null || s.length() < 1) {
				return s;
			}
			StringBuilder sb = new StringBuilder(s.length() + 16);
			for (int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				switch (c) {
				case '\'':
					sb.append("′");// ´");
					break;
				case '′':
					sb.append("′");// ´");
					break;
				case '\"':
					sb.append("\"");
					break;
				case '＂':
					sb.append("\"");
					break;
				case '&':
					sb.append("＆");
					break;
				case '#':
					sb.append("＃");
					break;
				case '\\':
					sb.append('￥');
					break;

				case '>':
					sb.append('＞');
					break;
				case '<':
					sb.append('＜');
					break;
				default:
					sb.append(c);
					break;
				}
			}
			val = sb.toString();
			return val;
		} catch (Exception e) {
//			logger.error("sanitized characters error: " + e.getMessage(), e);
			return s;
		}
	}

	public static boolean isNotNullOrEmpty(String str) {
		return str != null && str.trim().length() > 0;
	}

	public static boolean isNull(Object... params) {
		if (params == null) {
			return true;
		}

		for (Object obj : params) {
			if (obj == null) {
				return true;
			}
		}
		return false;
	}

	public static String getString(Object val) {
		String rtnVal = "";
		try {
			rtnVal = (String) val;
			rtnVal = rtnVal.trim();
		} catch (Exception e) {
			rtnVal = "";
		}
		return rtnVal;
	}

	public static String nullToStr(Object val) {
		return ((val == null) ? "" : String.valueOf(val).trim());
	}

	public static int getInt(Object val) {
		int rtnVal = -1;
		String rtnValStr = "-1";
		try {
			rtnValStr = (String) val;
			rtnValStr = rtnValStr.trim();
			rtnVal = Integer.parseInt(rtnValStr);
		} catch (Exception e) {
			rtnVal = -1;
		}

		return rtnVal;
	}

	public static String convertDateToStr(Date dt) {
		String dateStr = "";
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (dt != null) {
			dateStr = format.format(dt);
		}
		return dateStr;
	}

	public static String convertDateToStr(Date dt, String formatter) {
		String dateStr = "";
		DateFormat format = new SimpleDateFormat(formatter);
		if (dt != null) {
			dateStr = format.format(dt);
		}
		return dateStr;
	}

	public static Date convertStrToDateByFormat(String dateStr) {
		String inputDateStr = "";
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			inputDateStr = dateStr;
			if (dateStr == null || dateStr.trim().length() < 1) {
				inputDateStr = "1900-01-01";
			}
			java.util.Date d = sf.parse(inputDateStr.toString().trim());
			date = new Date(d.getTime());
		} catch (Exception e) {
//			logger.error("convertStrToDateByFormat(" + dateStr + ") error:" + e.getMessage(), e);
		}
		return date;
	}

	public static Date convertStrToDateByFormat(String dateStr, String formatter) {
		String inputDateStr = "";
		SimpleDateFormat sf = new SimpleDateFormat(formatter);
		Date date = null;
		try {
			inputDateStr = dateStr;
			if (dateStr == null || dateStr.trim().length() < 1) {
				inputDateStr = "1900-01-01 01:01:01";
			}
			java.util.Date d = sf.parse(inputDateStr.toString().trim());
			date = new Date(d.getTime());
		} catch (Exception e) {
//			logger.error("convertStrToDateByFormat(" + dateStr + ") error:" + e.getMessage(), e);
		}
		return date;
	}

	public static Object deepcopy(Object src) throws Exception {
		ByteArrayOutputStream byteout = null;
		ObjectOutputStream out = null;
		ByteArrayInputStream bytein = null;
		ObjectInputStream in = null;
		Object dest = null;
		try {
			byteout = new ByteArrayOutputStream();
			out = new ObjectOutputStream(byteout);
			out.writeObject(src);

			bytein = new ByteArrayInputStream(byteout.toByteArray());

			in = new ObjectInputStream(bytein);

			dest = (Object) in.readObject();
		} catch (Exception e) {
			throw new Exception("deep copy object[" + src + "] error cause by: " + e.getMessage(), e);
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (Exception e) {
			}
			try {
				if (bytein != null) {
					bytein.close();
					bytein = null;
				}
			} catch (Exception e) {
			}
			try {
				if (out != null) {
					out.close();
					out = null;
				}
			} catch (Exception e) {
			}
			try {
				if (byteout != null) {
					byteout.close();
					byteout = null;
				}
			} catch (Exception e) {
			}
		}
		return dest;

	}

	public static Object blobToObject(Blob blob) throws Exception {
		Object obj = null;
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(blob.getBinaryStream());
			obj = in.readObject();
			return obj;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (Exception e) {
			}
		}
	}

	public static long dateSub(String dateStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		java.util.Date d = sdf.parse(dateStr);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		long currentTime = calendar.getTimeInMillis();
		calendar.setTime(d);
		long timeEnd = calendar.getTimeInMillis();
		long theDay = (timeEnd - currentTime) / (1000 * 60 * 60 * 24);
		return theDay;
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
}