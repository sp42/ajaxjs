<%@ page contentType="text/plain; charset=UTF-8"  pageEncoding="UTF-8" import="com.ajaxjs.developertools.monitor.JmxHelper"%>
<%
    JmxHelper.printMBean(request, out);
%>