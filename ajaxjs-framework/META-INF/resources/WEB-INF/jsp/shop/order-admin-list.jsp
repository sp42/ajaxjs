<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
	</head>
	<body>
		<div class="vue">
			<!-- 后台头部导航 -->
			<ajaxjs-admin-header>
				<template slot="title">${uiName}一览</template>
				<template slot="btns"><a :href="ajResources.ctx + '/admin/${shortName}/'">新建</a> | </template>
			</ajaxjs-admin-header>
	
			<!-- 搜索、分类下拉 -->
			<aj-admin-filter-panel :no-catalog="true">
				交易状态 <select class="ajaxjs-select" style="width:130px;"
				 onchange="location.assign('?filterField=tradeStatus&filterValue=' + this.selectedOptions[0].value);">
						<option value="null">不指定交易状态</option>
					<c:foreach items="${TradeStatusDict}" var="item">
						<option value="${item.key}" 
							${(param.filterValue != 'null') && (param.filterField == 'tradeStatus') && (param.filterValue == item.key)  ? 'selected' : ''}>
							${item.value}
						</option>
					</c:foreach>
				</select>	

				支付状态 <select class="ajaxjs-select" style="width:130px;"
				 onchange="location.assign('?filterField=payStatus&filterValue=' + this.selectedOptions[0].value);">
						<option value="null">不指定支付状态</option>
					<c:foreach items="${PayStatusDict}" var="item">
						<option value="${item.key}" 
							${(param.filterValue != 'null') && (param.filterField == 'payStatus') && (param.filterValue == item.key)  ? 'selected' : ''}>
							${item.value}
						</option>
					</c:foreach>
				</select>	
			</aj-admin-filter-panel>
		</div>
		
		<script>
			new Vue({el:'body > div'});
		</script>

		<!-- 列表渲染，采用传统后端 MVC 渲染 -->
		<table class="ajaxjs-niceTable listTable">
			<colgroup>
				<col />
				<col />
				<col />
				<col />
				<col />
				<col />
				<col />
				<col />
				<col style="text-align: center;" align="center" />
			</colgroup>
			<thead>
				<tr>
					<th>#</th>
					<th>订单号/交易号</th>
					<th>支付类型</th>
					<th>订单金额</th>
					<th>最终金额</th>
					<th>交易状态</th>
					<th>支付状态</th>
					<th>下单日期/支付日期</th>
					<th class="control">控 制</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="10">
						一张订单可以包含多个商品，每个商品对应一张子订单，于是一张订单包含多张子订单。
						
						<form action="." method="GET" class="dateRange" @submit="valid($event)">
							起始时间：
							<aj-form-calendar-input field-name="startDate" :date-only="true" :position-fixed="true"></aj-form-calendar-input>
							截至时间：
							<aj-form-calendar-input field-name="endDate" :date-only="true" :position-fixed="true"></aj-form-calendar-input>
							<button class="aj-btn">查询</button>
						</form>
						<script>
							aj.form.betweenDate('.dateRange');
						</script>
					</td>
				</tr>
			</tfoot>
			<tbody>
				<c:foreach var="current" items="${PageResult}">
					<tr>
						<td>${current.id}</td>
						<td>${current.orderNo}<br/><span style="color:gray">${current.outerTradeNo}</span></td>
						<td>${PayTypeDict[current.payType]}</td>
						<td>￥${current.orderPrice}</td>
						<td>￥${current.totalPrice}</td>
						<td>${TradeStatusDict[current.tradeStatus]}</td>
						<td>${PayStatusDict[current.payStatus]}</td>
						<td>
							<c:dateFormatter value="${current.createDate}" />
							${empty current.payDate ? '' : '<br />'}
							<c:dateFormatter value="${current.payDate}" />
						</td>
						<td>
							<a href="${ctx}/admin/user/${current.buyerId}/">
								<img src="${commonAssetIcon}/user.png" style="width:16px;vertical-align: sub;" />用户详情
							</a> | 
							<a href="?userId=${current.buyerId}">
								该用户订单
							</a> | 
							<a href="../${current.id}/">订单详情</a>
						</td>
					</tr>
				</c:foreach>
			</tbody>
		</table>
		<div class="listTable pager">
			<%@include file="/WEB-INF/jsp/pager.jsp" %>
		</div>
		<script>
			aj.imageEnlarger();// 鼠标移动大图
		</script>
	</body>
</html>