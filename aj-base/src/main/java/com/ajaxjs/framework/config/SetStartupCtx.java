package com.ajaxjs.framework.config;

import com.ajaxjs.Version;
import com.ajaxjs.framework.Application;
import com.ajaxjs.framework.IComponent;
import com.ajaxjs.net.http.Tools;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 设置环境变量
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class SetStartupCtx implements IComponent {
	private static final LogHelper LOGGER = LogHelper.getLog(SetStartupCtx.class);

	static {
		// 可不用注册组件，仅仅是登记事件
		Application.onServletStartUp.add(ctx -> {
			LOGGER.info("设置环境变量");

			String ctxPath = ctx.getContextPath();
			ctx.setAttribute("ctx", ctxPath);
			ctx.setAttribute("isDebuging", Version.isDebug);
			ctx.setAttribute("commonAsset", ctxPath + "/asset/common"); // 静态资源目录
			ctx.setAttribute("commonAssetIcon", ConfigService.get("forDelevelopers.commonAssetIcon")); // 静态资源图标目录
			ctx.setAttribute("ajaxjs_ui_output", "https://ajaxjs.nos-eastchina1.126.net");

			// 开发阶段，ajaxjsui 指定了前端 js 所在的位置，通常是另外一个项目同时运行着，例如当前是本机 8080 端口的 ajaxjs-js。
			if (Version.isDebug)
				ctx.setAttribute("developing_js_url", "http://" + Tools.getIp() + ":8888");

			ctx.setAttribute("aj_static_resource", ctx.getAttribute(Version.isDebug ? "developing_js_url" : "ajaxjs_ui_output"));
		});
	}
}
