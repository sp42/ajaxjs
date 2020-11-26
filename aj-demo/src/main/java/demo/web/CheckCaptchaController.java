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
import com.ajaxjs.web.mvc.filter.FilterContext;

@WebServlet("/CheckCaptcha")
public class CheckCaptchaController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MvcOutput resp = new MvcOutput(response);
		FilterContext cxt = new FilterContext();
		cxt.request = new MvcRequest(request);
		cxt.response = resp;

		try {
			if (new CaptchaFilter().before(cxt)) {
				resp.output("验证码通过！");
				// 你的业务逻辑……
			}
		} catch (Throwable e) {
			resp.output(e.toString());
		}
	}
}
