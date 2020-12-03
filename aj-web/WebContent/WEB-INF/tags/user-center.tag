<%@tag pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tags"%>
<tags:content bannerImg="${ctx}/images/memberBanner.jpg">
	<div class="account-center">
		<div class="center">
			<div class="left">
				<div class="avatar">
				<c:choose>
					<c:when test="${empty userAvatar}">
						<div class="emptyUserAvatar"></div>
					</c:when>
					<c:otherwise>
						<div class="imgHolder">
							<img src="${userAvatar}" />
						</div>
					</c:otherwise>
				</c:choose>
			
					<h3>${userName}</h3>
				</div>
				<menu>
					<ul>
						<li><a href="${ctx}/user/">概 览</a></li>
						<li><a href="${ctx}/shop/order/">订单</a></li>
						<li><a href="${ctx}/shop/cart/">购物车</a></li>
						<li><a href="${ctx}/user/profile/">个人信息</a></li>
						<li><a href="${ctx}/user/account/">帐号管理</a></li>
					<%-- 
						<li><a href="${ctx}/user/account-center/oauth/">账户绑定</a></li>
						<li><a href="${ctx}/user/account-center/log_history/">登录历史</a></li> --%>
						
						<li><a href="javascript:logout();">退出登录</a></li>
					</ul>
				</menu>
				
				<script>
					// find current hightlight
				
					var pathname = document.location.pathname;
					var a = document.querySelectorAll('menu ul li a');
				
					for(var i = 0, j = a.length; i < j; i++) {
						if(pathname === a[i].getAttribute("href")) {
							a[i].parentNode.classList.add('selected');
							break;
						}
					}
					
					function logout() {
						aj.showConfirm('确定退出吗？', () => aj.xhr.get('${ctx}/user/logout/', json => {
								if(json.isOk) {
									aj.msg.show(json.msg);
									setTimeout(()=>location.assign('${ctx}/'), 1000);
								}
						}));
					}
				</script>
			</div>
			<div class="right">
				<jsp:doBody/>
			</div>
		</div>
	</div>
</tags:content>
