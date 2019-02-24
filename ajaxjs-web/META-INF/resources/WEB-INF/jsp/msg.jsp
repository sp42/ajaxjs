<%@page pageEncoding="UTF-8" isErrorPage="true" trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=320,user-scalable=0,initial-scale=1.0,maximum-scale=1.0, minimum-scale=1.0" />
	<title>Msg</title>
	<style>
        table {
            border-collapse: collapse;
            width: 100%;
        }
        
        td {
            padding: 6px;
            color: white;
            font-size: 1.3rem;
        }
		
		p {
			padding-left: 30px;
		}
		
		hr {
			width: 98%;
		}
		
		textarea {
			width: 90%;
			min-height: 600px;
			outline: none;
			border: 1px solid gray;
			padding: 1%;
			margin: 1%;
		}
		
		table{
			border-collapse:collapse
		}
	</style>
</head>
<body leftMargin="0" topMargin="0">
	<table>
		<tr>
			<td bgColor="#ff6600" rowSpan="2">
				&nbsp;&nbsp;信息Message
			</td>
			<td width="32" bgColor="#ff6600" height="32"></td>
			<td width="32" bgColor="#ff9933" height="32"></td>
			<td width="32" bgColor="#ffcc66" height="32"></td>
			<td width="32" bgColor="#ffee99" height="32"></td>
		</tr>
		<tr>
			<td width="32" bgColor="#ff9933" height="32"></td>
			<td width="32" bgColor="#ffcc66" height="32"></td>
			<td width="32" bgColor="#ffee99" height="32"></td>
			<td width="32" height="32"></td>
		</tr>
	</table>
	<p>
		<%=(request.getParameter("msg"))%>
	</p>
	<hr />
	<div align="center">
		<a href="${pageContext.request.contextPath}">回首页</a> | <a href="javascript:history.go(-1);">上一页</a>
	</div>
</body>
</html>