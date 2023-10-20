package com.ajaxjs.framework.spring.filter;

import com.ajaxjs.framework.BusinessException;
import com.ajaxjs.framework.spring.ICustomException;
import com.ajaxjs.framework.spring.response.ResponseResult;
import com.ajaxjs.util.convert.ConvertToJson;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 全局异常拦截器
 *
 * @author Frank Cheung
 */
public class GlobalExceptionHandler implements HandlerExceptionResolver {
    private static final LogHelper LOGGER = LogHelper.getLog(GlobalExceptionHandler.class);

    /**
     * 判断是否期望 JSON 的结果
     *
     * @return true 表示为希望是 JSON
     */
    public static boolean isJson(HttpServletRequest request) {
        return "application/json".equals(request.getHeader("Accept"));
    }

    public static final String EXCEPTION_CXT_KEY = "EXCEPTION_CXT_KEY";

    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception ex) {
        LOGGER.warning(ex);

        Throwable _ex = ex.getCause() != null ? ex.getCause() : ex;
        String msg = _ex.getMessage();

        if (msg == null)
            msg = _ex.toString();

        req.setAttribute(EXCEPTION_CXT_KEY, _ex);

        resp.setCharacterEncoding("UTF-8"); // 避免乱码
        resp.setHeader("Cache-Control", "no-cache, must-revalidate");

        if (_ex instanceof SecurityException || _ex instanceof IllegalAccessError || _ex instanceof IllegalAccessException)// 设置状态码
            resp.setStatus(HttpStatus.UNAUTHORIZED.value());
        else
            resp.setStatus(HttpStatus.OK.value());

        if (req.getAttribute("SHOW_HTML_ERR") != null && ((boolean) req.getAttribute("SHOW_HTML_ERR"))) {
            try {
                resp.getWriter().write(msg);
            } catch (IOException e) {
                LOGGER.warning(e);
            }
        } else {
            msg = javaValue2jsonValue(ConvertToJson.jsonStringConvert(msg));
            resp.setContentType(MediaType.APPLICATION_JSON_VALUE); // 设置 ContentType

            ResponseResult resultWrapper = new ResponseResult();

            if (_ex instanceof BusinessException) {
                resultWrapper.setErrorCode("200");
            } else if (_ex instanceof ICustomException) {
                int errCode = ((ICustomException) _ex).getErrCode();
                resultWrapper.setErrorCode(String.valueOf(errCode));
                resp.setStatus(errCode);
            } else {
                resultWrapper.setErrorCode("500");
                resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }

            resultWrapper.setMessage(msg);

            try {
                resp.getWriter().write(resultWrapper.toString());
            } catch (IOException e) {
                LOGGER.warning(e);
            }
        }

        return new ModelAndView();
//        return null;// 默认视图，跳转 jsp
    }

    /**
     * 转义注释和缩进
     *
     * @param str JSON 字符串
     * @return 转换后的字符串
     */
    private static String javaValue2jsonValue(String str) {
        return str.replaceAll("\"", "\\\\\"").replaceAll("\t", "\\\\\t");
    }
}
