<%@page pageEncoding="UTF-8" import="java.util.*" %>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>Insert title here</title>
	</head>
	<body>
	
	
Hello,<c:if test="true">World</c:if>
Hello,
<c:if test="${bar > 10}">

World
</c:if>

<c:choose>
    <c:when test="${user==null}">
        用户不存在
    </c:when>
    <c:otherwise>
        用户名是 ：${user.name}
    </c:otherwise>
</c:choose>

<% 
    List<String> list = new ArrayList<>(); // 遍历列表 
    list.add("aaa");
    list.add("bbb");
    list.add("ccc");
    list.add("ddd");
    request.setAttribute("list", list);
%>
<c:foreach var="str" items="${list}">

	${str}，前 ${currentcnt}/一共 ${cnt}

</c:foreach>

<% 
    Map<String, String> map = new HashMap<>(); // 遍历 Map 
    map.put("aa","111");
    map.put("bb","222");
    map.put("cc","333");
    map.put("dd","444");
    request.setAttribute("map", map);
%>
<c:foreach var="entry" items="${map}">
    键名：${entry.key } = 值：${entry.value }
</c:foreach>


<select class="aj-select" name="sellerId">
  <c:foreach items="${sellers}" var="item">
<option value="${item.key}" ${item.key==info.sellerId?'selected':''}>${item.value.name}</option>
  </c:foreach>
</select>
	</body>
</html>