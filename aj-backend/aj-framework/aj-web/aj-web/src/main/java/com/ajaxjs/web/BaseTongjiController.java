/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.web;

import com.ajaxjs.net.http.Post;
import com.ajaxjs.net.http.SetConnection;
import com.ajaxjs.util.date.DateUtil;
import com.ajaxjs.util.map.JsonHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 百度统计
 *
 * @author Frank Cheung
 */
@RestController
@RequestMapping("${website.service_root:/service}/baidu_tongji")
public class BaseTongjiController {
    @Value("${website.baidu_tongji.api_username}")
    String api_username;

    @Value("${website.baidu_tongji.api_password}")
    String api_password;

    @Value("${website.baidu_tongji.api_token}")
    String api_token;

    @Value("${website.baidu_tongji.siteId}")
    String siteId;

    private static final String API = "https://api.baidu.com/json/tongji/v1/ReportService/getData";

    /**
     * 请求 API
     *
     * @param body
     * @return
     */
    private String getData(Map<String, Object> body) {
        // 生成请求头
        Map<String, Object> header = new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;

            {
                put("username", api_username);
                put("password", api_password);
                put("token", api_token);
                put("account_type", 1);
            }
        };

        String _header = JsonHelper.toJson(header);
        String params = String.format("{\"header\": %s, \"body\": %s}", _header, JsonHelper.toJson(body));

        return Post.post(API, params, SetConnection.SET_JSON).toString();
    }

    private final static SimpleDateFormat FORMATER = DateUtil.simpleDateFormatFactory("yyyyMMdd");

    @GetMapping("/getTimeTrendRpt")
    public String getTimeTrendRpt() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, -1);// 昨天
        String today = FORMATER.format(now), yesterday = FORMATER.format(calendar.getTime());

        return getData(new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;

            {
                put("site_id", siteId);
                put("start_date", yesterday);
                put("end_date", today);
                put("method", "overview/getTimeTrendRpt");
                put("metrics", "pv_count,visitor_count,ip_count,bounce_ratio,avg_visit_time");
            }
        });
    }

    @GetMapping("/getCommonTrackRpt")
    public String getCommonTrackRpt() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, -1);// 昨天
        String today = FORMATER.format(now), yesterday = FORMATER.format(calendar.getTime());

        return getData(new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;

            {
                put("site_id", siteId);
                put("start_date", yesterday);
                put("end_date", today);
                put("method", "overview/getCommonTrackRpt");
            }
        });
    }

    @GetMapping("getTrend")
    public String getTrend(String start_date, String end_date) {
        return getData(new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;

            {
                put("site_id", siteId);
                put("start_date", start_date);
                put("end_date", end_date);
                put("method", "trend/time/a");
                put("metrics", "pv_count,visit_count");
                put("max_results", "0");
                put("gran", "day");
            }
        });
    }
}
