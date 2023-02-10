
<%@ tag description="HTML" pageEncoding="UTF-8"
	import="com.ajaxjs.util.JspHelper"%>
<%@ taglib uri="/ajaxjs" prefix="c"%>
<%@ attribute fragment="false" required="true" name="namespace" description="命名"%>
<%@ attribute fragment="false" required="true" name="namespace_chs" description="命名（中文）"%>
<%@ attribute fragment="false" required="false" name="show_create" type="Boolean" description=""%>
<%@ attribute fragment="false" required="false" name="date_style" type="Integer" description="1=创建；2=修改; 3=创建 && 修改"%>
<%@ attribute fragment="false" required="false" name="field_style" type="Integer" description="1=id；2=name; 4=content; 8 =state; 15 = all"%>
<%@ attribute fragment="false" required="false" name="two_cols" type="Boolean" description="表格是否双列"%>
<%
	boolean isCreate = request.getParameter("id") == null;
	request.setAttribute("isCreate", isCreate);
	JspHelper.getJspHelper(request);
 //JspHelper.getInfo(request);
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
	text-align: center;
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
						<c:if test="${!isCreate}">
							<input type="hidden" name="id" value="${info.id}" />
						</c:if>

						<table class="aj-table odd">
							<c:if test="${JSP_HELPER.bit(2, field_style)}">
								<tr>
									<td>${namespace_chs}名称</td>
									<td ${two_cols? 'colspan="3"': ''}><input type="text"
										name="name" value="${info.name}" /></td>
								</tr>
							</c:if>
							<c:if test="${JSP_HELPER.bit(4, field_style)}">
								<tr>
									<td style="vertical-align: middle;">${namespace_chs}简介</td>
									<td ${two_cols? 'colspan="3"': ''}><textarea rows="5"
											name="content">${info.content}</textarea></td>
								</tr>
							</c:if>
							<jsp:doBody />
							<c:if test="${JSP_HELPER.bit(8, field_style)}">
								<tr>
									<td>${namespace_chs}状态</td>
									<td ${two_cols? 'colspan="3"': ''}>
										<label><input type="radio" name="stat" value="0" ${info.stat == 0 ? 'checked' : ''} /> 启用</label> 
										<label><input type="radio" name="stat" value="1" ${info.stat == 1 ? 'checked' : ''} /> 禁用</label> 
										<label><input type="radio" name="stat" value="2" ${info.stat == 2 ? 'checked' : ''} /> 已删除</label> 
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
						<button onclick="save();return false;">
							<i class="fa-solid fa-floppy-disk" style="color: green"></i> 保存
						</button>
						<button onclick="setTimeout('parent.window.history.go(-1);', 20)">返回</button>
					</form>
				</div>
			</td>
		</tr>
	</table>

	<script>
		function save() {
			var json = jsp.xhr.formData('.form');
			
			if(${isCreate}) {
				jsp.xhr.postJson('${ctx}/data_service/${namespace}', json, j => {
					if(j && j.status === 1 && j.data) {
						alert('创建${namespace_chs}成功');
						location.assign('?id=' + j.data);
					} else {
						alert('操作失败: ' + j.message);
					}
				});
				/* jsp.xhr.postJson('/${ctx}/common_write?tableName=${TABLE_NAME}', json, j => {
					if(j && j.status === 1 && j.data.newlyId) {
						alert('创建${namespace_chs}成功');
						location.assign('?id=' + j.data.newlyId);
					} else {
						alert('操作失败: ' + j.message);
					}
				}); */
			} else {
				jsp.xhr.putJson('${ctx}/data_service/${namespace}', json, j => {
					if(j && j.status === 1 && j.data === true) 
						alert('修改${namespace_chs}成功');
					else {
						alert('操作失败: ' + j.message);
					}
				});
			}
		}
	</script>
</body>
</html>