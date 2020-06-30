package com.ajaxjs.framework.config;

import com.ajaxjs.Version;
import com.ajaxjs.framework.Application;
import com.ajaxjs.net.http.Tools;
import com.ajaxjs.util.logger.LogHelper;

public class SetStartupCtx {
	private static final LogHelper LOGGER = LogHelper.getLog(SetStartupCtx.class);

	static {
		System.out.println("------------------------");
		// 可不用注册组件，仅仅是登记事件
		Application.onServletStartUp.add(ctx -> {
			LOGGER.info("设置环境变量");

			// 设置全局环境变量
			String ctxPath = ctx.getContextPath();
			ctx.setAttribute("ctx", ctxPath);
			ctx.setAttribute("isDebuging", Version.isDebug);
			ctx.setAttribute("commonAsset", ctxPath + "/asset/common"); // 静态资源目录
			ctx.setAttribute("commonAssetIcon", ctxPath + "/asset/common/icon"); // 静态资源图标目录
			ctx.setAttribute("ajaxjs_ui_output", "https://ajaxjs.nos-eastchina1.126.net");

			// 开发阶段，ajaxjsui 指定了前端 js 所在的位置，通常是另外一个项目同时运行着，例如当前是本机 8080 端口的 ajaxjs-js。
			if (Version.isDebug)
				ctx.setAttribute("developing_js_url", "http://" + Tools.getIp() + ":8080/ajaxjs-js");
		});
	}
}
