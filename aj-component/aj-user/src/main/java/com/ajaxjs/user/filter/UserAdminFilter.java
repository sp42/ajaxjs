package com.ajaxjs.user.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ajaxjs.user.role.RightConstant;
import com.ajaxjs.user.role.RoleService;
import com.ajaxjs.util.Encode;

/**
 * 进入后台一切资源的拦截器。
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class UserAdminFilter {
	/**
	 * 提示的 HTML
	 */
	// @formatter:off
	private static final String NO_ACCESS = "<title>禁止访问，非法权限</title><meta charset=\"utf-8\" />"
			+ "<div style=\"height: 100%%; display: flex; justify-content: center; align-items: center;\"><table>" 
			+ "		<tr>"
			+ "			<td align=\"center\">" 
			+ "				<svg width=\"150px\" viewBox=\"0 0 1000 1000\"> "
			+ "					<g transform=\"translate(0.000000,511.000000) scale(0.100000,-0.100000)\">"
			+ "						<path fill=\"#ea8010\" d=\"M4154,4950.8c-964.7-162-1888.9-648-2618-1377.1C586,2623.7,100,1452.8,100,108.8C100-1445,807-2870,"
			+ "2044.1-3801.5c869-651.7,1863.1-983.1,2953-983.1c2131.9,0,3987.7,1340.3,4657.8,3361.7c500.8,1520.7,224.6,3199.8-743.8,4488.5c-751.1,997.8-1874.2,"
			+ "1679-3100.3,1885.2C5383.8,5024.4,4584.8,5020.7,4154,4950.8z M5604.7,3739.4c1472.8-257.8,2603.2-1314.5,2975.1-2780c114.2-449.2,110.5-1248.2-3.7-1704.8c-84.7-338.8-261.4-758.5-460.3-1086.2l-114.2-191.5L5431.7,"
			+ "543.3L2865.3,3109.7l99.4,70c176.8,117.8,655.4,349.8,887.4,430.8C4385.9,3790.9,5037.7,3838.8,5604.7,"
			+ "3739.4z M6996.5-2973.1c-243-165.7-806.3-408.7-1137.7-493.4c-460.3-114.1-1255.6-117.8-1708.5-3.7C2405-3028.3,1237.8-1474.5,1326.1,278.2c36.8,651.7,206.2,1207.7,552.3,1778.4l114.1,191.5L4562.7-318.3l2566.4-2566.4L6996.5-2973.1z\" />"
			+ "					</g>" 
			+ "				</svg></td></tr>" 
			+ "		<tr><td>"
			+ "			<br />禁止访问，非法权限。Authentication Required <a href=\"%s/admin/login/?lastUrl=%s\">登 录</a></td>"
			+ "	</tr></table></div>";
	// @formatter:on

	public static final BiFunction<HttpServletRequest, HttpServletResponse, Boolean> CHECK_ADMIN = (req, resp) -> {
		String ctxPath = req.getContextPath(), uri = req.getRequestURI();

		if (uri.startsWith(ctxPath + "/admin")) {
			HttpSession session = req.getSession();

			if (uri.equals(ctxPath + "/admin/login/")) {
				// 后台登录，允许直接访问
				return true;
			} else if (!RoleService.check(session, RightConstant.ADMIN_SYSTEM_ALLOW_ENTNER)) {
				String lastUrl = req.getRequestURL().toString();
				lastUrl = Encode.urlEncode(lastUrl);

				try {
					resp.setStatus(401);
					resp.setCharacterEncoding(StandardCharsets.UTF_8.toString());
					resp.setContentType("text/html");
					resp.getWriter().append(String.format(NO_ACCESS, req.getContextPath(), lastUrl));
				} catch (IOException e) {
					e.printStackTrace();
				}

				return false;
			}
		}

		return true;
	};
}
