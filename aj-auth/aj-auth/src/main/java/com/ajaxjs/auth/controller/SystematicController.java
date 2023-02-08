package com.ajaxjs.auth.controller;

import com.ajaxjs.auth.service.system.ISystematicService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Frank Cheung frank@ajaxjs.com
 * @date 2023/1/17 23:36
 */
@RestController
@RequestMapping("/admin/system")
public interface SystematicController extends ISystematicService {
}
