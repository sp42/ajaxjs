<%@ page pageEncoding="UTF-8"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<tags:account-center>
	<style>
		.ajaxjs-borderTable td, .ajaxjs-borderTable th{
			border-right: 0;
		    border-left: 0; 
		    text-align:center;
		    font-size: .8rem;
	    }
	</style>
	<h3>登录历史</h3>
	<table class="ajaxjs-borderTable" width="85%" align="center">
		<tr>
			<th>日 期</th>
			<th>时 间</th>		
			<th>IP</th>		
			<th>城市</th>		
			<th>客户端标识 UserAgent</th>		
		</tr>
		<c:foreach var="current" items="${list}">
			<tr>
				<td width="100"><c:dateFormatter value="${current.createDate}" format="YYYY-MM-dd" /></td>
				<td><c:dateFormatter value="${current.createDate}" format="hh:mm:ss" /></td>
				<td>${current.ip}</td>
				<td>${current.ip}</td>
				<td style="font-size:.7rem;">${current.userAgent}</td>
			</tr>
		</c:foreach>
	</table>
	<p class="note">由于您的宽带运营商会不定期调整网络，我们获取的IP所在地可能不准确，请您通过登录时间与产品判断是否为您本人操作；如确定非您本人登录，建议您 修改密码</p>
</tags:account-center>