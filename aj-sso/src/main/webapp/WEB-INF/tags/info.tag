
<%@ tag description="HTML" pageEncoding="UTF-8" import="com.ajaxjs.util.JspHelper"%>
<%@ taglib uri="/ajaxjs" prefix="c"%>
<%@ attribute fragment="false" required="true" name="namespace" description="命名"%>
<%@ attribute fragment="false" required="true" name="namespace_chs" description="命名（中文）"%>
<%@ attribute fragment="false" required="false" name="show_create" type="Boolean" description=""%>
<%@ attribute fragment="false" required="false" name="date_style" type="Integer" description="1=创建；2=修改; 3=创建 && 修改"%>
<%@ attribute fragment="false" required="false" name="field_style" type="Integer" description="1=id；2=name; 4=content; 8 =state; 15 = all"%>
<%@ attribute fragment="false" required="false" name="two_cols" type="Boolean" description="表格是否双列"%>
<%
	JspHelper.getInfo(request);
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

.form {
	width: 660px;
	min-height: 600px;
	margin: 0 auto;
	text-align:center;
}

.form>table {
	width: 100%;
	margin-bottom: 20px;
}

.form input[type=text], .form input[type=password], .form textarea {
	width: 90%;
}

h1 {
	font-weight: bold;
	font-size: 18pt;
	text-align: center;
	letter-spacing: 3px;
	margin-bottom: 20px;
}
</style>
</head>
<body class="middleHeight">
	<table>
		<tr>
			<td>
				<div class="list_panel">
					<h1>${isCreate ? '创建' : '编辑'}${namespace_chs}
						<c:if test="${!isCreate and JSP_HELPER.bit(1, field_style)}">
							#${info.id}
						</c:if>
					</h1>

					<form class="form">
						<table class="aj-table odd">
							<c:if test="${JSP_HELPER.bit(2, field_style)}">
								<tr>
									<td>${namespace_chs}名称</td>
									<td ${two_cols? 'colspan="3"': ''}><input type="text" value="${info.name}" /></td>
								</tr>
							</c:if>
							<c:if test="${JSP_HELPER.bit(4, field_style)}">
								<tr>
									<td style="vertical-align: middle;">${namespace_chs}简介</td>
									<td ${two_cols? 'colspan="3"': ''}><textarea rows="5">${info.content}</textarea></td>
								</tr>
							</c:if>
							<jsp:doBody />
							<c:if test="${JSP_HELPER.bit(8, field_style)}">
								<tr>
									<td>${namespace_chs}状态</td>
									<td ${two_cols? 'colspan="3"': ''}>
										<label><input type="radio" name="stat" /> 启用</label> 
										<label><input type="radio" name="stat" /> 禁用</label> 
										<label><input type="radio" name="stat" /> 已删除</label> 
											
										<span class="note">不选择默认表示启用状态</span>
									</td>
								</tr>
							</c:if>
						</table>

						<c:if test="${!isCreate and (not empty date_style) }">
							<div style="color: gray; font-size: 9pt; margin: 20px;">
								<c:if test="${JSP_HELPER.bit(1, date_style)}">
									创建日期：${JSP_HELPER.formatDate(info.createDate)}
								</c:if>
								<c:if test="${JSP_HELPER.bit(2, date_style)}">
									修改日期：${JSP_HELPER.formatDate(info.updateDate)}
								</c:if>
							</div>
						</c:if>
						<button>
							<i class="fa-solid fa-floppy-disk" style="color: green"></i> 保存
						</button>
						<button onclick="setTimeout('parent.window.history.go(-1);', 20)">返回</button>
					</form>
				</div>
			</td>
		</tr>
	</table>
	<script type="text/javascript">
		function del(id, name) {
			if (confirm('删除记录 [' + (name || id) + "]？")) {
				alert('deleted!')
			}
		}
	</script>
</body>
</html>