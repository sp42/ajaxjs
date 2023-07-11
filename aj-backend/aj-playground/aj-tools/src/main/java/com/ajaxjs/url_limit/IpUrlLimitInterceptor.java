//package com.ajaxs.url_limit;
//
//
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//
///**
// * ip+url重复请求现在拦截器
// * <p>
// * 通过intercept和redis针对url+ip在一定时间内访问的次数来将ip禁用
// * <a href="https://blog.csdn.net/qq_41995919/article/details/125269393">...</a>
// */
//public class IpUrlLimitInterceptor implements HandlerInterceptor {
//    private RedisUtil getRedisUtil() {
//        return SpringContextUtil.getBean(RedisUtil.class);
//    }
//
//    private static final String LOCK_IP_URL_KEY = "lock_ip_";
//
//    private static final String IP_URL_REQ_TIME = "ip_url_times_";
//
//    private static final long LIMIT_TIMES = 5;
//
//    private static final int IP_LOCK_TIME = 60;
//
//    @Override
//    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
//        log.info("request请求地址uri={},ip={}", httpServletRequest.getRequestURI(), IpAdrressUtil.getIpAdrress(httpServletRequest));
//
//        if (ipIsLock(IpAdrressUtil.getIpAdrress(httpServletRequest))) {
//            log.info("ip访问被禁止={}", IpAdrressUtil.getIpAdrress(httpServletRequest));
//            ApiResult result = new ApiResult(ResultEnum.LOCK_IP);
//            returnJson(httpServletResponse, JSON.toJSONString(result));
//            return false;
//        }
//
//        if (!addRequestTime(IpAdrressUtil.getIpAdrress(httpServletRequest), httpServletRequest.getRequestURI())) {
//            ApiResult result = new ApiResult(ResultEnum.LOCK_IP);
//            returnJson(httpServletResponse, JSON.toJSONString(result));
//            return false;
//        }
//
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
//    }
//
//
//    /**
//     * 判断ip是否被禁用
//     *
//     * @param ip
//     * @return
//     */
//    private Boolean ipIsLock(String ip) {
//        RedisUtil redisUtil = getRedisUtil();
//        return redisUtil.hasKey(LOCK_IP_URL_KEY + ip);
//    }
//
//    /**
//     * @param ip
//     * @param uri
//     * @return java.lang.Boolean
//     * @Description: 记录请求次数
//     * @author: shuyu.wang
//     * @date: 2019-10-12 17:18
//     */
//    private Boolean addRequestTime(String ip, String uri) {
//        String key = IP_URL_REQ_TIME + ip + uri;
//        RedisUtil redisUtil = getRedisUtil();
//
//        if (redisUtil.hasKey(key)) {
//            long time = redisUtil.incr(key, (long) 1);
//
//            if (time >= LIMIT_TIMES) {
//                redisUtil.getLock(LOCK_IP_URL_KEY + ip, ip, IP_LOCK_TIME);
//                return false;
//            }
//        } else
//            redisUtil.getLock(key, (long) 1, 1);
//
//        return true;
//    }
//
//    private void returnJson(HttpServletResponse response, String json) {
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/json; charset=utf-8");
//
//        try (PrintWriter writer = response.getWriter()) {
//            writer.print(json);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
