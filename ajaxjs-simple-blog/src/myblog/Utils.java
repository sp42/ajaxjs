package myblog;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.util.io.StreamUtil;
import com.ajaxjs.util.map.MapHelper;

public class Utils {
	static int max = 200;

	public static String substr(String text) {
		return text.substring(0, text.length() > max ? max : text.length());
	}

	/**
	 * 获取 PUT 请求所提交的内容。 Servlet 不能获取 PUT 请求内容，所以这里写一个方法
	 * 
	 * @return 参数、值集合
	 */
	public static Map<String, Object> getPutRequestData(HttpServletRequest request) {
		String params = null;

		try {
			params = new StreamUtil().setIn(request.getInputStream()).byteStream2stringStream().close().getContent();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		if (params == null)
			return null;
		return MapHelper.toMap(params.split("&"), true);
	}
}
