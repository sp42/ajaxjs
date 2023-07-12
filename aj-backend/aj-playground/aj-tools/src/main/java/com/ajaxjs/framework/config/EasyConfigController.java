package com.ajaxjs.framework.config;

import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.web.WebHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/service/easy_config")
public class EasyConfigController {
    @Autowired
    EasyConfig config;

    @GetMapping
    @ResponseBody
    public String get() {
        return FileHelper.openAsText(config.getFilePath());
    }

    @PostMapping
    public Boolean update(HttpServletRequest req) {
        config.save(WebHelper.getRawBody(req));

        return true;
    }
}
