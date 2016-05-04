<%@tag pageEncoding="UTF-8" body-content="scriptless" import="com.ajaxjs.javascript.Cross" description="使用定制标记执行 JavaScript 代码片段"%>
<%@attribute name="runat" type="String" required="false" rtexprvalue="true"%>
<%
Cross.init((PageContext) jspContext);
%>
<%
	if("client".equals(runat) || "both".equals(runat)){ 
%>
    <script type="text/javascript">
        <jsp:doBody/>
    </script>
<%
	}
%>
<%
	if("server".equals(runat) || "both".equals(runat)){ 
%>
    <jsp:doBody var="source"/>
    <%
    Cross.runtime.eval( (String) jspContext.getAttribute("source"));
              //  (PageContext) jspContext);
    %>
<%
	}
%>