package com.ajaxjs.web.view;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * 选择标签之 else 标签
 *
 * @author sp42 frank@ajaxjs.com
 */
public class OtherWise extends SimpleTagSupport {
    @Override
    public void doTag() throws JspException, IOException {
        Choose parent = (Choose) getParent(); // 获得父类标签对象

        if (!parent.isExecute()) { // 判断父类为false才输出
            getJspBody().invoke(null);
            parent.setExecute(false); // 把父类设置为false
        }
    }
}
