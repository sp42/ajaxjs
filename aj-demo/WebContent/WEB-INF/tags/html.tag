<%@tag description="HTML 标准模版" pageEncoding="UTF-8"%>
<%@attribute name="title"%>
<html>
    <head>
        <title>${title}</title>
        <meta charset="UTF-8" />
    </head>
    <body>
	    <jsp:doBody/>
    </body>
  
</html>