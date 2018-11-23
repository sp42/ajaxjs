package com.ajaxjs.cms.controller;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.jaxrs2.integration.ServletOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.OpenApiContextLocator;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.getContextIdFromServletConfig;

public class ApiDocController extends HttpServlet {
	private static final long serialVersionUID = 5041371253005960693L;

	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_YAML = "application/yaml";
	public static final String ACCEPT_HEADER = "Accept";

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String ctxId = getContextIdFromServletConfig(config);
		
		try {
			new ServletOpenApiContextBuilder<>().servletConfig(config).ctxId(ctxId).buildContext(true);
		} catch (OpenApiConfigurationException e) {
			e.printStackTrace();
		}
	}

	// TODO move to own servlet non jaxrs project and reference from there
	// TODO cleanup and errors
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");

		String ctxId = getContextIdFromServletConfig(getServletConfig());
		OpenApiContext ctx = OpenApiContextLocator.getInstance().getOpenApiContext(ctxId);
		OpenAPI oas = ctx.read();

		String type = "json";

		String acceptHeader = req.getHeader(ACCEPT_HEADER);
		if (!StringUtils.isBlank(acceptHeader) && acceptHeader.toLowerCase().contains(APPLICATION_YAML)) {
			type = "yaml";
		} else {
			// check URL:
			if (req.getRequestURL().toString().toLowerCase().endsWith("yaml")) {
				type = "yaml";
			}
		}

		boolean pretty = false;
		if (ctx.getOpenApiConfiguration() != null && Boolean.TRUE.equals(ctx.getOpenApiConfiguration().isPrettyPrint())) {
			pretty = true;
		}

		resp.setStatus(200);

		try(PrintWriter pw = resp.getWriter();) {
			if (type.equalsIgnoreCase("yaml")) {
				resp.setContentType(APPLICATION_YAML);
				pw.write(pretty ? Yaml.pretty(oas) : Yaml.mapper().writeValueAsString(oas));
			} else {
				resp.setContentType(APPLICATION_JSON);
				pw.write(pretty ? Json.pretty(oas) : Json.mapper().writeValueAsString(oas));
			}
		}
	}
}
