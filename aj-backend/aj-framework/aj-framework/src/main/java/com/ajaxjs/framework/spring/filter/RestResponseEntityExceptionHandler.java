package com.ajaxjs.framework.spring.filter;

import com.ajaxjs.framework.spring.response.ResponseResult;
import com.ajaxjs.util.map.JsonHelper;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {
    static final String TPL = "输入字段 [%s] 未通过校验，原因 [%s]，输入值为 [%s]，请检查后再提交。";

    @ExceptionHandler(value = BindException.class)
    public void exceptionHandler(HttpServletRequest req, HttpServletResponse resp, BindException e) {
        String msg = "";
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        for (FieldError err : fieldErrors) {
            msg += String.format(TPL, err.getField(), err.getDefaultMessage(), err.getRejectedValue());
//			msg += "\\\\n";
        }

        ResponseResult result = new ResponseResult();
        result.setErrorCode("400");
        result.setMessage(msg);

        outputJson(resp, result.toString());
    }

    /**
     * 输出 JSON 字符串
     */
    public static void outputJson(HttpServletResponse resp, Object toJson) {
        String json;

        if (toJson instanceof String)
            json = (String) toJson;
        else
            json = JsonHelper.toJson(toJson);

        resp.setCharacterEncoding("UTF-8"); // 避免乱码
        resp.setContentType("application/json"); // 设置 ContentType

        try (PrintWriter writer = resp.getWriter()) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
