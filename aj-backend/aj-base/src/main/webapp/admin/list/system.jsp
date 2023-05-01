<%@page import="com.ajaxjs.util.map.MapTool"%>
<%@page import="java.util.*, com.ajaxjs.spring.DiContextUtil, com.ajaxjs.auth.service.system.*, com.ajaxjs.auth.model.SystemModel.Systematic"%>
<%@ page pageEncoding="UTF-8" import="com.ajaxjs.util.JspHelper"%>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="/ajaxjs"%>
<%
	ISystematicService service = DiContextUtil.getBean(ISystematicService.class);
	//System.out.println(service.list()); 

	request.setAttribute("list", MapTool.simpleBean2Map(service.list()));
	request.setAttribute("namespace","system");
	request.setAttribute("namespace_chs","系统");
%>

<myTag:list namespace="${namespace}" namespace_chs="${namespace_chs}" listData="${list}">
	<script>
		tenantFilter();
	</script>
	
	<table class="aj-table even">
		<thead>
			<tr>
				<th>#</th>
				<th style="min-width: 200px;">系统名称</th>
				<th>简介</th>
				<th>开发厂商</th>
				<th>联系人及联系方式</th>
				<th>状态</th>
				<th>创建日期</th>
				<th>修改日期</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:foreach items="${list}" var="item">
				<tr>
					<td>${item.id}</td>
					<td>${item.name}</td>
					<td style="max-width: 300px;">${item.content}</td>
					<td>${item.devFirm}</td>
					<td>${item.contact}</td>
					<myTag:list-common-rol style="31" item="${item}" namespace="${namespace}" namespace_chs="${namespace_chs}" />
				</tr>
			</c:foreach>
		</tbody>
	</table>
</myTag:list>