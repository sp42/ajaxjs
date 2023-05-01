<%@tag description="Common List" pageEncoding="UTF-8"%>
<%@ taglib uri="/ajaxjs" prefix="c"%>
<%@attribute fragment="false" required="true" name="item" type="java.util.Map" description="实体 Map"%>
<%@attribute fragment="false" required="true" name="style" type="Integer" description="1=状态列；2=创建日期；4=修改日期；8=删除；16=修改；32=查看"%>
<%@attribute fragment="false" required="true" name="namespace" description="命名"%>
<%@attribute fragment="false" required="true" name="namespace_chs" description="命名（中文）"%>
<%@attribute fragment="false" required="false" name="show_create" type="Boolean" description=""%>

<c:if test="${JSP_HELPER.bit(1, style)}">
	<td>${JSP_HELPER.getState(item.stat)}</td>
</c:if>
<c:if test="${JSP_HELPER.bit(2, style)}">
	<td>${JSP_HELPER.formatDate(item.createDate)}</td>
</c:if>
<c:if test="${JSP_HELPER.bit(4, style)}">
	<td>${JSP_HELPER.formatDate(item.updateDate)}</td>
</c:if>

<td> 
	<c:if test="${JSP_HELPER.bit(8, style)}">
		<a href="javascript:del(${item.id}, '${namespace_chs}: ${empty item.name ? item.id: item.name}')">
			<i class="fa-solid fa-times" style="color:red;"></i> 删除
		</a> 	| 
	</c:if>

	<c:if test="${JSP_HELPER.bit(16, style)}">
	<a href="../info/${namespace}.jsp?id=${item.id}">
		<i class="fa-solid fa-pen-to-square" style="color:#12c8ff;"></i> 编辑
	</a>
	</c:if>
	<c:if test="${JSP_HELPER.bit(32, style)}">
	<a href="../info/${namespace}.jsp?id=${item.id}">
		<i class="fa-solid fa-eye" style="color:#12c8ff;"></i> 查看
	</a>
	</c:if>
</td>