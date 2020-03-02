<%@page pageEncoding="UTF-8" import="java.util.Date"%>
<%@page trimDirectiveWhitespaces="true"%>1
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title><%="欢迎！"%></title>
    <style> body {margin: 5%;}</style>
</head>
<body>
    当前日期是 <%= new Date().toString()%> 
    <%@include file="../../WEB-INF/jsp/footer.jsp"%>
    
<%   
	// 内部类代码将会在doService()方法中
	class PageUtil {
	    ……
	}
%>
<%! // 注意此处有感叹号
	private void foo() {
		……
	}  
	// 也可以是属性
	private String aVariableOfClassScope = "foo";
	……
%>
<%foo(); %>
<%=((Student)request.getAttribute("student")).getName()%>
${student.name}
${ 条件表达式 ? 表达式1：表达式2 }
// 判断 a 参数是否为 1。如果是返回 current 字符串，表示 1 当前选中，否则输出空字符串 
${"1" == param.a ? "current" : ""}
${empty param.a}     // 判断 a 是否空
${not empty param.a} // 判断 a 是否非空
// 假设 URL 连接是 test.jsp?a=1&b=2&c=3
${param.a} // 返回 1 字符串，等同于 <%=request.getParameter("a")%>
// 获取表单值，例如提交的表单中含有 <input type="text" name="name" />
${pageContext.request.contextPath}

${ctx}
${param.name}
<%request.setAttribute("viewUtils", viewUtils) %>

${viewUtils.formatDateShorter(current.createDate)}
${'请求完成耗时：'.concat(requestTimeRecorder).concat('秒') }
EL不支持字符串+操作符，故使用String的concat()代替

读取Java常量
${Date.foo}
</body>
</html>