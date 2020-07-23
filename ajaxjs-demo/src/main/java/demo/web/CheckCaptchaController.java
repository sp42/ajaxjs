package demo.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.framework.filter.CaptchaFilter;
import com.ajaxjs.web.mvc.controller.MvcOutput;
import com.ajaxjs.web.mvc.controller.MvcRequest;

@WebServlet("/CheckCaptcha")
public class CheckCaptchaController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MvcOutput resp = new MvcOutput(response);
		
		try {
			if (new CaptchaFilter().before(null, new MvcRequest(request), resp, null, null)) {
				resp.output("验证码通过！");
				// 你的业务逻辑……
			}
		} catch (Throwable e) {
			resp.output(e.toString());
		}
	}
}
