package com.ajaxjs.cms.website;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.net.http.HttpBasicRequest;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.web.mvc.IController;

/**
 * 网站统计
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Path("admin/website/tongji")
public class WebsiteTongJiController implements IController {

	@GET
	public String get() {
		return BaseController.jsp("/website/website_tongji");
	}

	private final static SimpleDateFormat formater = CommonUtil.simpleDateFormatFactory("yyyyMMdd");

	@GET
	@Path("getTimeTrendRpt")
	@Produces(MediaType.APPLICATION_JSON)
	public String getTimeTrendRpt() {
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DAY_OF_MONTH, -1);// 昨天
		String today = formater.format(now), yesterday = formater.format(calendar.getTime());

		Map<String, Object> body = new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("site_id", ConfigService.get("baidu_tongji.siteId"));
				put("start_date", yesterday);
				put("end_date", today);
				put("method", "overview/getTimeTrendRpt");
				put("metrics", "pv_count,visitor_count,ip_count,bounce_ratio,avg_visit_time");
			}
		};

		return getData(body);
	}

	@GET
	@Path("getCommonTrackRpt")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCommonTrackRpt() {
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DAY_OF_MONTH, -1);// 昨天
		String today = formater.format(now), yesterday = formater.format(calendar.getTime());

		Map<String, Object> body = new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("site_id", ConfigService.get("baidu_tongji.siteId"));
				put("start_date", yesterday);
				put("end_date", today);
				put("method", "overview/getCommonTrackRpt");
			}
		};

		return getData(body);
	}

	@GET
	@Path("getTrend")
	@Produces(MediaType.APPLICATION_JSON)
	public String getTrend(@QueryParam("start_date") @NotNull String start_date, @QueryParam("end_date") @NotNull String end_date) {
		Map<String, Object> body = new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("site_id", ConfigService.get("baidu_tongji.siteId"));
				put("start_date", start_date);
				put("end_date", end_date);
				put("method", "trend/time/a");
				put("metrics", "pv_count,visit_count");
				put("max_results", "0");
				put("gran", "day");
			}
		};

		return getData(body);
	}

	private static final String JSON = "{\"header\": %s, \"body\": %s}";

	/**
	 * 请求 API
	 * 
	 * @param body
	 * @return
	 */
	private static String getData(Map<String, Object> body) {
		String req = String.format(JSON, getHeader(), JsonHelper.toJson(body));
		String json = HttpBasicRequest.post("https://api.baidu.com/json/tongji/v1/ReportService/getData", req, conn -> {
			conn.addRequestProperty("Content-type", "application/json");
		});

		return "json::" + json;
	}

	/**
	 * 获取请求头
	 * 
	 * @return
	 */
	private static String getHeader() {
		Map<String, Object> header = new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("username", ConfigService.get("baidu_tongji.api_username"));
				put("password", ConfigService.get("baidu_tongji.api_password"));
				put("token", ConfigService.get("baidu_tongji.api_token"));
				put("account_type", 1);
			}
		};

		return JsonHelper.toJson(header);
	}
}
