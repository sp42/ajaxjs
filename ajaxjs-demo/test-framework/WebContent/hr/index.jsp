<%@page pageEncoding="UTF-8" import="com.ajaxjs.mvc.filter.DataBaseFilter, com.ajaxjs.orm.JdbcConnection, com.ajaxjs.cms.domain.*"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<%@taglib uri="/ajaxjs_config" prefix="config"%>
<%
	DataBaseFilter.initDb();
	DomainEntityService service = new DomainEntityService("entity_hr", "data.hrCatalog_Id", "招聘", "hr");
	request.setAttribute("list", service.findList());
	JdbcConnection.closeDb(); // 关闭数据库连接
%>
<tags:content banner="../asset/images/6_03.png" bodyClass="">
	<jsp:attribute name="left">

	</jsp:attribute>
	<jsp:attribute name="body">
		<%-- 本页通过 jsp 获取后台数据 --%>
		<table cellpaddig="0" cellspace="0" border="0" align="left" class="list">
			<tr>
				<th>职位名称</th>
				<th>职位要求</th>
				<th>工作经验</th>
				<th>发布日期</th>
			</tr>
		<c:foreach items="${list}" var="item">
			<tr>
				<td width="150">${item.name}</td>
					<td width="350">${item.content}</td>
					<td>${item.expr}</td>
					<td><c:dateFormatter value="${item.createDate}" /></td>
			</tr>
		</c:foreach>
		</table>
		<style>
		 	table.list {
			    border-spacing: 0;
			    width: 100%;
			    margin:0 auto;
			    margin-top:5%; 
			}
			table.list th {
				background-color: #d3d3d3;
				border-bottom: 2px solid gray;
				padding: 10px;
				color: gray;
				text-align: center;
			}
			table.list td{
				text-align:center;
				padding: 2% 0;
				font-size:.9rem;
				border-bottom: 1px solid lightgray;
			}
		</style>
	</jsp:attribute>
</tags:content>
