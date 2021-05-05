<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
		<style>
			a{
			    text-decoration: underline;
			}
			label{
				min-width:20%;
				display:inline-block;
				overflow: hidden;
			}
			label span{
				color:lightgray;
			}
			div> span{
				color:black;
				font-weight:bold;
			}
		</style>
	</head>
	<body>
		<div class="aj-form-row-holder">
			<!-- 后台头部导航 -->
			<aj-admin-header>
				<template slot="title">${uiName}</template>
				<template slot="btns">
					<a :href="ajResources.ctx + '/admin/${shortName}/list/'">${uiName}列表</a> | 
				</template>
			</aj-admin-header> 

			<form action="." method="PUT" class="entityEdit">
				<input type="hidden" name="id" value="${info.id}" /><!-- 传送 id 参数 -->
			
				<div>	
					<label>订单 id：${info.id}</label>
					<label>订单号：${info.extractData.orderNo}<span>（外部显示唯一的订单号）</span></label>
					<label>交易订单号：${info.extractData.outerTradeNo}<span>（交易支付用）</span></label>
				</div>
		
				<div><label>
			 		货品名称：
			 			<div style="display:inline-block">
			<span>${info.extractData.goodsName}</span> 规格：${info.extractData.formatName} 单价数量总价： ￥${info.goodsPrice} x ${info.goodsNumber} = ￥${info.goodsAmount}元 
			<a href="${ctx}/admin/goods/${info.goodsId}/">商品详情</a> 
			<a href="${ctx}/admin/simple-group/${info.groupId}/" class="${empty info.groupId ? 'hide' : ''}">团购详情</a>
			 			</div>
			 		</label>
				</div>
				<div>
			 		<label>购买用户： <a href="${ctx}/user/center/info/${info.buyerId}">
			 			<img src="${commonAssetIcon}/user.png" style="width:16px;vertical-align: sub;" /> ${userName}</a> 
			 			<a href="../list/?filterField=i.buyerId&filterValue=${info.buyerId}">查看该用户所有订单</a></label>
			 		<label>商家：<a href="../list/?filterField=i.sellerId&filterValue=${info.sellerId}">${sellers[info.sellerId].name}</a>
			 		</label>
				</div>
				<div><label>
			 		订单金额：￥${info.extractData.orderPrice}</label><label>  实际支付金额：￥${info.extractData.totalPrice} <span>（可能是优惠之后的金额）</span></label>
				</div>
		
				
				<div>
					<label>支付类型：${PayTypeDict[info.extractData.payType]}</label>
					<label>交易状态：
						<select class="aj-select" name="tradeStatus" style="width:60%;">
							<c:foreach items="${TradeStatusDict}" var="item">
								<option value="${item.key}" ${item.key == info.extractData.tradeStatus ? 'selected' : ''}>${item.value}</option>
							</c:foreach>
						</select>
					</label>
					<label>支付状态：${PayStatusDict[info.extractData.payStatus]}</label>
				</div>
				<div><label>
			 		下单日期：<c:dateFormatter value="${info.createDate}"  /></label>
			 		<label>  修改日期：
			 		<c:dateFormatter value="${info.updateDate}"  /></label>
			 		<label>支付日期：<c:dateFormatter value="${info.extractData.payDate}"  /></label>
				</div>
				<div>
					<label>收货人：
						<input placeholder="" size="16" name="shippingTarget" value="${info.extractData.shippingTarget}" type="text" />
					</label>
					<label>收货人电话：
						<input placeholder="" size="20" name="shippingPhone" value="${info.extractData.shippingPhone}" type="text" />
					</label>
					<br />
					<br />
					<label>收货人地址：
						<input placeholder="" size="60" name="shippingAddress" value="${info.extractData.shippingAddress}" type="text" />
					</label>
				</div>
				<div>
					<label>快递公司：
						<input placeholder="" size="15" name="shipping" value="${info.extractData.shipping}" type="text" />
					</label>
					<label>快递单号：
						<input placeholder="" size="20" name="shippingCode" value="${info.extractData.shippingCode}" type="text" />
					</label>
					<label>快递运费：
						￥<input placeholder="" size="5" name="shippingFee" value="${info.extractData.shippingFee}" type="text" />元
					</label>
				</div>		
 				<div>
					<label style="width:60%">备注：
						<textarea style="vertical-align: top;width:70%" class="aj-input" name="content">${info.content}</textarea>
					</label>
				</div>
				
				<div>
					<!--按钮 -->
					<aj-admin-info-btns :is-create="${isCreate}"></aj-admin-info-btns>
				</div>
			</form>
 		</div>
 
		<script>
			App = new Vue({el: '.aj-form-row-holder'});
			
			// 表单提交
			aj.xhr.form('form.entityEdit', json => {
			 if(json && json.msg)
				 aj.alert(json.msg);
			});
		</script>
	</body>
</html>