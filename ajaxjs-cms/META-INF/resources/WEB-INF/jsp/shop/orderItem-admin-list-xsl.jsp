<%@ page pageEncoding="utf-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta charset="utf-8" />
	<title></title>
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />
</head>
<body>
	<!-- 列表渲染，采用传统后端 MVC 渲染 -->
		<table class="ajaxjs-niceTable listTable" border="1">
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
				</tr>
			</thead>
	
			<tbody>
				<c:foreach var="current" items="${PageResult}">
					<tr>
						<td>${current.id}</td>
						<td class="order" data-order-id="${current.orderId}">
							订单id：${current.orderId} 订单号：${current.extractData.orderNo}
							<br/> 
							交易状态：${TradeStatusDict[current.extractData.tradeStatus]} | 支付状态：${PayStatusDict[current.extractData.payStatus]}
						</td>
						<td>${current.extractData.goodsName}</td>
						<td>￥${current.goodsPrice} x ${current.goodsNumber} = ￥${current.goodsPrice * current.goodsNumber}</td>
						<td>订单金额：￥${current.extractData.orderPrice}<br />实付金额：￥${current.extractData.totalPrice}</td>
						<td>${empty current.groupId ? '否' : '是' }</td>
						<td><c:dateFormatter value="${current.createDate}" /></td>
						<td><a href="?filterField=i.sellerId&filterValue=${current.sellerId}">${sellers[current.sellerId].name}</a></td>
						<td><a href="?filterField=i.buyerId&filterValue=${current.buyerId}">#${current.buyerId}</a></td>
						 
					</tr>
				</c:foreach>
			</tbody>
		</table>
</body>
</html>
