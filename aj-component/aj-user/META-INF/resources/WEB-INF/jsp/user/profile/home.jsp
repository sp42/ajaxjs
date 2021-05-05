<%@ page pageEncoding="UTF-8" import="com.ajaxjs.user.role.RoleService"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tags"%>
<%@taglib uri="/ajaxjs" prefix="c"%>

<tags:user-center>
	<style>
		table.main{
			border: 1px lightgray solid;
			border-collapse: collapse;
			border-spacing: 0;
			width:95%;
		}
		table.main th{
			font-weight:bold;
			text-align:left;
		}
		table.main td, table.main th {
			border: 1px lightgray solid;
			line-height: 160%;
			height: 120%;
			padding: 2% 2%;
		}
		h4{
		    padding: 0 0 3%3%;
		    font-weight: bold;
		    letter-spacing: 3px;
		    font-size: 1.1rem;
		}
		.btns{
			padding:5%;
			text-align:right;
		}
	</style>
	<script src="${ajaxjs_ui_output}/lib/China_AREA_full.js"></script>
	
	<h3 class="aj-center-title">概览</h3>
	
	<h4>欢迎</h4>
	<hr style="width:95%;" class="aj-hr" />
	<div style="padding:0 3% 5% 3%;">
		<div style="float:right;"><a href="${ctx}/user/profile/">修改个人资料</a>
	<c:if test="${RoleService.check(RoleService.ADMIN_SYSTEM_ALLOW_ENTNER)}">
		 | <a href="${ctx}/admin/">进入后台</a> 
	</c:if>
		<br />
		上次登录  查看<a href="${ctx}/user/account/log-history/">登录历史</a></div>
		您好，${empty user.username ? userName : user.username}，很高兴为您服务：），现在是 <span class="clock"></span>。
		
		<script>
			// 定义一个函数用以显示当前时间
			window.addEventListener('load', function () {
				var now = new Date(), year = now.getFullYear(), month = now.getMonth() + 1, day = now.getDate();       
				var newDay = year + "年" + month + "月" + day + "日 ";
				
				document.body.$('.clock').innerHTML = newDay + now.toLocaleTimeString(); 
				setTimeout(arguments.callee, 1000); //在1秒后再次执行
			}, null);
		</script>
	</div>
	
	<h4>我的订单</h4>
	<hr style="width:95%;" class="aj-hr" />
	<div class="box" style="margin: 0 auto;width: 500px;">
		<img src="${ctx}/asset/common/icon/empty-cart.png" width="160" style="vertical-align: middle;" /> 还没消费过，马上<a href="${ctx}/shop/goods/">去看看？</a>~
	</div>
</tags:user-center>