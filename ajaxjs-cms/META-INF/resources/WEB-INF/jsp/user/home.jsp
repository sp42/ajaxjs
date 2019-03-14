<%@ page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/jsp/common/head.jsp">
	<jsp:param name="lessFile" value="/asset/less/admin.less" />
</jsp:include>
</head>
<body>
	<div class="center userCenter">
		<%@include file="user-center-menu.jsp"%>
		
		<div class="right home">
			<section>
				<a href="${ctx}/news" class="btn right">更多动态</a>
				<h3 class="jb">最新动态</h3>
				
				<ul class="list">
					<c:foreach items="${topNews}" var="item">
						<li>
							<span style="float:right;"><c:dateFormatter value="${item.createDate}" format="YYYY-MM-dd" /></span>
							<a href="${ctx}/news/${item.id}/" target="_blank">${item.name}</a>
						</li>
					</c:foreach>
				</ul>
			</section>
			<section>
				<a href="${ctx}/user/center/order/" class="btn right">更多订单</a>
				<h3 class="jb">我的订单</h3>
				<div style="text-align:center;">
				<!-- 无记录时显示这个图片 提示无记录 -->
					<%-- <img src="${commonAsset}/images/empty.png" width="330" />
					<div>暂无记录</div> --%>
					<!-- 有记录时循环遍历 -->
					<div class="aj-tableList">
						<header>
							<div style="width:20%">订单号</div>
							<div style="width:30%">商品描述</div>
							<div style="width:10%">商品价格</div>
							<div style="width:20%">交易状态</div>
							<div style="width:10%">实付款</div>
							<!-- <div style="width:10%">交易时间</div> --><!-- 因为位置不够 -->
						</header>
						<ul class="list">
							<c:foreach items="${orderList}" var="orderItem">
								<li>
									<%-- <span style="float:right;"><c:dateFormatter value="${orderItem.createDate}" format="YYYY-MM-dd" /></span> --%>
									<div style="width:20%">${orderItem.orderNo}</div>
									<div style="width:30%">${orderItem.userName}</div>
									<div style="width:10%">${orderItem.currentNnitPrice}</div>
									<c:choose>
										<c:when test="${orderItem.status==10}"><div style="width:20%">未付款</div></c:when>
										<c:when test="${orderItem.status==20}"><div style="width:20%">已付款</div></c:when>
										<c:when test="${orderItem.status==40}"><div style="width:20%">已付款</div></c:when>
										<c:when test="${orderItem.status==50}"><div style="width:20%">交易关闭</div></c:when>
										<c:when test="${orderItem.status==1}"><div style="width:20%">作废</div></c:when>
										<c:when test="${orderItem.status==0}"><div style="width:20%">已取消</div></c:when>
									</c:choose>
									<div style="width:10%">${orderItem.payment}</div>
									<%-- <div style="width:10%">${orderItem.endTime}</div> --%>
								</li>
							</c:foreach> 
						</ul>
					</div>
				</div>
			</section>
			<section>
				<a href="loginInfo" class="btn right">查看更多</a>
				<h3 class="jb">最近登录</h3>

				<ul class="list">
					<c:foreach items="${loginLog}" var="item">
						<li>
							<span style="float:right;">${item.createDate}</span>
							登录 ip：${item.ip}  登录类型：${item.ip}
						</li>
					</c:foreach>
				</ul>
			</section>

			
		</div>
	</div>
</body>
</html>