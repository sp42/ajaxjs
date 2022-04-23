package com.ajaxjs.util.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.util.WebHelper;
import com.ajaxjs.util.io.FileHelper;

@Controller
@RequestMapping("/service/easy_config")
public class EasyConfigController {
	@Autowired
	private EasyConfig config;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
	@ResponseBody
	public String get() {
		String jsonStr = FileHelper.openAsText(config.getFilePath());
		return jsonStr;
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
	@ResponseBody
	public String update(HttpServletRequest req) {
		String jsonStr = WebHelper.getRawBody(req);
		config.save(jsonStr);

		return BaseController.jsonOk("更新配置成功");
	}
}
