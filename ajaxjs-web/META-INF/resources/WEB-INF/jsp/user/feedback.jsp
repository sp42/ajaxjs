<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/head.jsp">
		<jsp:param name="lessFile" value="/asset/less/admin.less" />
	</jsp:include>
	<script src="${ajaxjsui}/js/libs/md5.min.js"></script>
	
	<style>
		.aj-carousel > div{
			    border-top: 2px solid #E7E7E7;
		}
		.aj-carousel.aj-carousel-tab > div > div {
		    background-color: white;
		}
		.aj-carousel.aj-carousel-tab header li.active {
			border-color:white;
		}
	</style>
</head>
<body>
	<div class="center userCenter">
		<%@include file="user-center-menu.jsp"%>
		<div class="right">
			<h3 class="jb">留言反馈</h3>
			<div class="user-login-info aj-carousel aj-carousel-tab">
				<header>
					<ul>
						<li class="active" @click="autoChangeTab($event)">留言</li>
						<li @click="autoChangeTab($event);">留言历史</li>
					</ul>
				</header>
				<div class="content">
					<div class="active">
						<form class="user-form center resetPassword" action="${ctx}/user/center/resetPassword" method="post">
							<dl>
								<label>
									<dt>留言标题</dt>
									<dd>
										<input type="text" name="name"  placeholder="" required  />
<!-- 										<div class="note">虽已登录了，但为安全起见，请先输入原密码方可修改之pattern=""</div> -->
									</dd>
								</label>
								<label>
									<dt>联系电话</dt>
									<dd>
										<input type="text" name="phone" placeholder="" required  value="${userPhone}" />
<!-- 										<div class="note">请输入您的由6~10位由数字和26个英文字母的登录密码</div> -->
									</dd>
								</label>
					
								<label>
									<dt>联系邮箱</dt>
									<dd>
										<input type="text" name="email" placeholder="" required  value="${user.email}" />
<!-- 										<div class="note">请您重复输入新的密码</div> -->
									</dd>
								</label>
								<label>
									<dt>留言内容</dt>
									<dd>
										<textarea name="content" rows="10"></textarea>
									</dd>
								</label>
							
								<dt></dt>
								<dd>
									<input type="submit" value="提交">
								</dd>
							</dl>
						</form>
					</div>
					
					<div class="aj-tableList">
						<header>
							<div style="width:30%">标题</div><div style="width:50%">留言内容</div><div style="width:20%">留言日期</div>
						</header>
						<ul class="list">
							<c:foreach items="${feedbacks}" var="item">
								<li>
									<span style="float:right;"><c:dateFormatter value="${item.createDate}" format="YYYY-MM-dd" /></span>
									<div style="width:30%">${item.name}</div><div style="width:50%">${item.content}<br /><span style="color:red;">回复：</span>${empty item.feedback ? '无' : item.feedback}</div>
								</li>
							</c:foreach>
						</ul>
					</div>
					
				</div>
			</div>
			
								
			<br />
			<br />
			<script>
				new Vue({
					el : '.aj-carousel',
					mixins : [aj._carousel]
				});
			</script>
		</div>
	</div>
</body>
</html>