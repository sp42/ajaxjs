<%@tag import="com.ajaxjs.framework.PageResult"%>
<%@ tag pageEncoding="UTF-8" import="com.ajaxjs.util.JspHelper"%>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="/ajaxjs"%>
<%@attribute fragment="false" required="true" name="namespace"
	description="命名"%>
<%@attribute fragment="false" required="true" name="namespace_chs"
	description="命名（中文）"%>
<%@attribute fragment="false" required="false" name="show_create"
	type="Boolean" description="是否显示 新建 按钮"%>
<%@attribute fragment="false" required="false" name="page"
	type="Boolean" description="是否分页"%>
<%
	JspHelper.init(request);
	Object obj = jspContext.getAttribute("page");
	
	if (obj != null && (boolean) obj)
		JspHelper.page(request);
	else
		JspHelper.getList(request);
	
	JspHelper.closeConn(request);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<jsp:include page="/common/head.jsp" />
<style>
html, body, body>table {
	height: 100%;
}

h1 {
	font-weight: bold;
	font-size: 18pt;
	text-align: left;
	letter-spacing: 2px;
}

.list_panel {
	min-height: 600px;
}

.search-panel {
	text-align: left;
}

.search-panel form {
	float: right;
}

.list_panel .list {
	clear: both;
	margin-top: 20px;
}

.list_panel .list table {
	margin: 10px 0;
	width: 100%;
}

.list-page {
	font-size: 9pt;
	text-align: center;
	margin: 10px auto;
	color: gray;
}

.list-page a {
	display: block;
	border: 1px solid lightgray;
	margin: 0;
	padding: 7px 10px;
	float: left;
	border-right: 0;
}

.list-page a.selected {
	background-color: lightgray;
	color: white;
}

.list-page a:last-child {
	border-right: 1px solid lightgray;
}

.list-page .bar {
	margin: 15px auto;
	overflow: hidden;
	width: fit-content;
}

.list-page .info {
	clear: both;
}

.list-page .info input {
	color: gray;
	text-align: center;
	width: 30px;
}
</style>
</head>

<body class="middleHeight">
	<table>
		<tr>
			<td>
				<div class="list_panel">
					<h1>${namespace_chs}管理</h1>
					<div class="search-panel">
						<form class="form" action="" method=get>
							<input type="text" name="keyword" value="${param.keyword}" />
							<button>
								<i class="fa-solid fa-magnifying-glass" style="color: green;"></i>搜索
							</button>
						</form>
						<c:if test="${empty show_create or show_create}">
							<button style="margin-top: 20px" onclick="location.assign('../info/${namespace}.jsp');">
								<i class="fa-solid fa-plus" style="color: green;"></i>新增${namespace_chs}
							</button>
						</c:if>
					</div>
					<div class="list">
						<jsp:doBody />
						<c:choose>
							<c:when test="${page}">
								<jsp:useBean id="JspPageHelper" class="com.ajaxjs.util.JspPageHelper" />
								<jsp:setProperty name="JspPageHelper" property="request" value="${pageContext.request}" />
								<jsp:setProperty name="JspPageHelper" property="pageResult" value="${PAGE_RESULT}" />

								<div class="list-page">
									<div class="bar">
										${JspPageHelper.perLink}
										<%--分页数过多影响 HTML 加载，这里判断下 --%>
										<c:if test="${PAGE_RESULT.getTotalPage() < 1000}">
											<c:foreach items="${JspPageHelper.getLink()}" var="item">
												<a href="${item.url}" class="${item.selected ? 'selected' : ''}">${item.pageNo} </a>
											</c:foreach>
										</c:if>
										${JspPageHelper.nextLink}
									</div>
									
									<div class="info">${JspPageHelper.info}
										每页记录数 
										<input onkeydown="refreshLimit2(event)" onblur="refreshLimit(this)" size="3" title="输入一个数字确定每页记录数"
											name="limit" value="${empty param.limit ? PAGE_RESULT.getPageSize() : param.limit}" />
									</div>
								</div>
							</c:when>
							<c:otherwise>
								<div style="color: gray; font-size: 9pt; margin: 20px; text-align: center;">共
									${JSP_HELPER.getListSize(list)} 笔记录</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</td>
		</tr>
	</table>
	<script>
		// 更新分页
		function refreshLimit(input) {
			var url = location.search;
			var limit = input.value;

			if (url.indexOf('limit=') != -1)
				url = url.replace(/limit=\d+/, 'limit=' + limit);
			else
				url += url.indexOf('?') != -1 ? ('&limit=' + limit)
						: ('?limit=' + limit);

			location.assign(url);
		}

		function refreshLimit2(e) {
			if (e.keyCode == 13)
				refreshLimit(e.target);
		}

		function del(id, name) {
			if (confirm('警告：\n是否删除记录 [' + (name || id) + "]？")) {
				alert('deleted!')
			}
		}
	</script>
</body>
</html>