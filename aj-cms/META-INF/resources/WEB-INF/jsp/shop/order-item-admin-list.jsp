<%@page pageEncoding="UTF-8" import="com.ajaxjs.user.role.RightConstant, com.ajaxjs.user.role.RoleService"%>
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
				<template slot="title">${uiName}</template>
				<template slot="btns"><a :href="ajResources.ctx + '/admin/${shortName}/'">新建</a> | </template>
			</ajaxjs-admin-header>
	
			<!-- 搜索、分类下拉 -->
			<aj-admin-filter-panel :no-catalog="true">
				<!-- 选择商家 -->
				交易状态 <select class="aj-select" style="width:130px;"
				 onchange="location.assign('?filterField=tradeStatus&filterValue=' + this.selectedOptions[0].value);">
						<option value="null">不指定交易状态</option>
					<c:foreach items="${TradeStatusDict}" var="item">
						<option value="${item.key}" 
							${(param.filterValue != 'null') && (param.filterField == 'tradeStatus') && (param.filterValue == item.key)  ? 'selected' : ''}>
							${item.value}
						</option>
					</c:foreach>
				</select>	

				支付状态 <select class="aj-select" style="width:130px;"
				 onchange="location.assign('?filterField=payStatus&filterValue=' + this.selectedOptions[0].value);">
						<option value="null">不指定支付状态</option>
					<c:foreach items="${PayStatusDict}" var="item">
						<option value="${item.key}" 
							${(param.filterValue != 'null') && (param.filterField == 'payStatus') && (param.filterValue == item.key)  ? 'selected' : ''}>
							${item.value}
						</option>
					</c:foreach>
				</select>	
		<%
			long privilegeTotal = (long)request.getSession().getAttribute("privilegeTotal");
		%>	
		<%if(RoleService.check(privilegeTotal, RightConstant.SHOP)){ %>		
				<!-- 选择商家 -->
				商家过滤器 <select class="aj-select" style="width:100px;" onchange="location.assign('?filterField=i.sellerId&filterValue=' + this.selectedOptions[0].value);">
						<option value="null">不指定商家</option>
					<c:foreach items="${sellers}" var="item">
						<option value="${item.key}" ${(param.filterValue != 'null') && (param.filterField == 'i.sellerId') && (param.filterValue == item.key)  ? 'selected' : ''}>
							${item.value.name}
						</option>
					</c:foreach>
				</select>	
		<%} %>
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
				<col style="text-align: center;" align="center" />
			</colgroup>
			<thead>
				<tr>
					<th>#</th>
					<th>订单详情</th>
					<th>商品名称</th>
					<th>单价x数量=总价</th>
					<th>订单总价</th>
					<th>是否团购</th>
					<th>下单日期/支付日期</th>
					<th>商家</th>
					<th>用户id</th>
					<th class="control">控 制</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="10">
						<aj-admin-xsl params="<%=com.ajaxjs.web.ServletHelper.getAllQueryParameters(request) %>"></aj-admin-xsl>					
						<aj-form-betweenDate></aj-form-betweenDate>
					</td>
				</tr>
			</tfoot>
			<tbody>
				<c:foreach var="current" items="${PageResult}">
					<tr>
						<td>${current.id}</td>
						<td class="order" data-order-id="${current.orderId}">
							订单id：${current.orderId} 订单号：${current.extractData.orderNo}
							<br/> 
							交易状态：${TradeStatusDict[current.extractData.tradeStatus]} | 支付状态：${PayStatusDict[current.extractData.payStatus]}
						</td>
						<td>
							<c:if test="${not empty current.cover}">
							<img src="${ctx}${current.cover}" style="max-width:50px;max-height:60px;vertical-align: middle;" 
						 		onmouseenter="aj.widget.imageEnlarger.singleInstance.imgUrl = '${ctx}${current.cover}';" onmouseleave="aj.widget.imageEnlarger.singleInstance.imgUrl = null;" />
							</c:if>
							${current.extractData.goodsName}
						</td>
						<td>￥${current.goodsPrice} x ${current.goodsNumber} = ￥${current.goodsPrice * current.goodsNumber}</td>
						<td class="totalPay"  data-order-id="${current.orderId}">订单金额：￥${current.extractData.orderPrice}<br />实付金额：￥${current.extractData.totalPrice}</td>
						<td>${empty current.groupId ? '否' : '是' }</td>
						<td>
							<c:dateFormatter value="${current.createDate}" />
							${empty current.payDate ? '' : '<br />'}
							<c:dateFormatter value="${current.payDate}" />
						</td>
						<td><a href="?filterField=i.sellerId&filterValue=${current.sellerId}">${sellers[current.sellerId].name}</a></td>
						<td><a href="?filterField=i.buyerId&filterValue=${current.buyerId}">#${current.buyerId}</a></td>
						<td>
							<a href="../${current.id}/">详情</a> 
						</td>
					</tr>
				</c:foreach>
			</tbody>
		</table>
		<div class="listTable pager">
			<%@include file="/WEB-INF/jsp/pager.jsp" %>
		</div>
		<script>
			new Vue({el: '.listTable'});
			
			/**
			 * 删除元素自己
			 */
			Element.prototype.die = function() {
				this.parentNode.removeChild(this);
			}
			
			// 合并单元格
			function megeCell(columnClass) {
				// 收集所有的列
				var arr = document.querySelectorAll(columnClass);
				
				var map = {};
				for(var i = 0, j = arr.length; i < j; i++) {
					var td = arr[i];
					var orderId = td.dataset.orderId;
					
					if(undefined === map[orderId]) map[orderId] = 1; // 统计跨行的有多少，用一个 map 装着
					else map[orderId] = ++map[orderId];
				} 
				
				var stack = [];
				for(var i = 0, j = arr.length; i < j; i++) {
					var td = arr[i];
					var orderId = td.dataset.orderId;
					var tds = map[orderId];
					
					if( tds > 1 && stack.length === 0) {// 标记
						for(var q = i; q < i + tds ; q++) {// 连续 tds 个都是要合并单元格的
							if(q == i) {
								arr[q].classList.add('firtstOne');
								arr[q].dataset.rowSpan = tds;							
							} else {
								arr[q].classList.add('die');
							}
							
							stack.push(arr[q]); // 入栈
						}
					}
				
				 	if(stack.length && td === stack[0]) {
						stack.shift();// 退栈  
						if(td.className.indexOf('firtstOne') != -1) {
						} else { 
							td.classList.add('die'); // 要删除的元素
						}
					} 
				}
				
				[].forEach.call(document.querySelectorAll('.firtstOne'), i => i.setAttribute('rowspan', i.dataset.rowSpan));
				[].forEach.call(document.querySelectorAll('.die'), i => i.die());
			}
			
			megeCell('.order');
			megeCell('.totalPay');
			
			aj.widget.imageEnlarger();// 鼠标移动大图
		</script>
	</body>
</html>