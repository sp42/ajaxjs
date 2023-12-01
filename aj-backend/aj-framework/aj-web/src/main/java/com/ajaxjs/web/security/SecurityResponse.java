/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.web.security;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 *
 */
public class SecurityResponse extends HttpServletResponseWrapper {
    private final boolean isXssCheck = true;

    private final boolean isCRLFCheck = true;

    private final boolean isCookiesSizeCheck = true;

    /**
     * 创建一个 SecurityResponse 实例。
     *
     * @param response 响应对象
     */
    public SecurityResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void addCookie(Cookie cookie) {
        if (isCRLFCheck) {
            String name = cookie.getName(), value = cookie.getValue();
            Cookie newCookie = new Cookie(Filter.cleanCRLF(name), Filter.cleanCRLF(value));

            if (cookie.getDomain() != null)
                newCookie.setDomain(cookie.getDomain());

            newCookie.setComment(cookie.getComment());
            newCookie.setHttpOnly(cookie.isHttpOnly());
            newCookie.setMaxAge(cookie.getMaxAge());
            newCookie.setPath(cookie.getPath());
            newCookie.setSecure(cookie.getSecure());
            newCookie.setVersion(cookie.getVersion());

            /*
             * 检查 Cookie 容量大小和是否在白名单中。
             */
            if (isCookiesSizeCheck && (cookie.getValue().length() > MAX_COOKIE_SIZE))
                throw new SecurityException("超出 Cookie 允许容量：" + MAX_COOKIE_SIZE);

//		if (!delegate.isInWhiteList(cookie.getName()))
//			throw new SecurityException("cookie: " + cookie.getName() + " 不在白名单中，添加无效！");

            super.addCookie(newCookie);
        } else
            super.addCookie(cookie);
    }

    /**
     * Cookie 最大容量
     */
    private static final int MAX_COOKIE_SIZE = 4 * 1024;

    @Override
    public void setDateHeader(String name, long date) {
        if (isCRLFCheck)
            name = Filter.cleanCRLF(name);

        super.setDateHeader(name, date);
    }

    @Override
    public void setIntHeader(String name, int value) {
        if (isCRLFCheck)
            name = Filter.cleanCRLF(name);

        super.setIntHeader(name, value);
    }

    @Override
    public void addHeader(String name, String value) {
        if (isXssCheck)
            value = Filter.cleanXSS(value);

        if (isCRLFCheck) {
            name = Filter.cleanCRLF(name);
            value = Filter.cleanCRLF(value);
        }

        super.addHeader(name, value);
    }

    @Override
    public void setHeader(String name, String value) {
        if (isXssCheck)
            value = Filter.cleanXSS(value);

        if (isCRLFCheck) {
            name = Filter.cleanCRLF(name);
            value = Filter.cleanCRLF(value);
        }

        super.setHeader(name, value);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setStatus(int sc, String value) {
        if (isXssCheck)
            value = Filter.cleanCRLF(value);

        super.setStatus(sc, value);
    }

    @Override
    public void sendError(int sc, String value) throws IOException {
        if (isXssCheck)
            value = Filter.cleanCRLF(value);

        super.sendError(sc, value);
    }
}
