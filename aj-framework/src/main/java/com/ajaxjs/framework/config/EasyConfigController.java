package com.ajaxjs.framework.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ajaxjs.web.WebHelper;
import com.ajaxjs.framework.spring.easy_controller.anno.ControllerMethod;
import com.ajaxjs.util.io.FileHelper;

@Controller
@RequestMapping("/service/easy_config")
public class EasyConfigController {
	@Autowired
	private EasyConfig config;

	@GetMapping
	@ResponseBody
	public String get() {
		String jsonStr = FileHelper.openAsText(config.getFilePath());
		return jsonStr;
	}

	@PostMapping
	@ControllerMethod("更新配置")
	public Boolean update(HttpServletRequest req) {
		String jsonStr = WebHelper.getRawBody(req);
		config.save(jsonStr);

		return true;
	}
}
