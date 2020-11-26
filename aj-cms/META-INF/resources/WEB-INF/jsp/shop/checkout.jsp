<%@page pageEncoding="UTF-8" import="com.ajaxjs.shop.ShopHelper"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tags"%>
<tags:content bodyClass="shop-checkout" bannerImg="${ctx}/images/memberBanner.jpg">
	<h2 class="aj-center-title" style="padding:0;">${PAGE_Node.name}</h2>
	
	<section class="payment">
		<h4><tags:i18n zh="选择支付方式" eng="Payment" /></h4>
		
		<table>
		<tr>
			<c:if test="${ShopHelper.hasState(aj_allConfig.shop.AllowPayType, ShopHelper.WX_PAY)}">
				<td>
					<label for="wx"><img src="http://static.ajaxjs.com/wxpay.jpg" /></label>
				</td>
			</c:if>

			<c:if test="${ShopHelper.hasState(aj_allConfig.shop.AllowPayType, ShopHelper.ALI_PAY)}">
				<td>
					<label for="ali"><img src="http://static.ajaxjs.com/alipay.jpg" /></label>
				</td>
			</c:if>
		
			<c:if test="${ShopHelper.hasState(aj_allConfig.shop.AllowPayType, ShopHelper.OFFLINE_PAY)}">
				<td><label for="ship">货到付款</label></td>
			</c:if>
		
			<c:if test="${ShopHelper.hasState(aj_allConfig.shop.AllowPayType, ShopHelper.POINTS_PAY)}">
				<td><label for="point">积分付款</label></td>
			</c:if>
		</tr>
			
		<tr>
			<c:if test="${ShopHelper.hasState(aj_allConfig.shop.AllowPayType, ShopHelper.WX_PAY)}">
				<td><input type="radio" name="payment" id="wx"   value="1" /></td>
			</c:if>
			<c:if test="${ShopHelper.hasState(aj_allConfig.shop.AllowPayType, ShopHelper.ALI_PAY)}">
				<td><input type="radio" name="payment" id="ali"  value="2" /></td>
			</c:if>
			<c:if test="${ShopHelper.hasState(aj_allConfig.shop.AllowPayType, ShopHelper.OFFLINE_PAY)}">
				<td><input type="radio" name="payment" id="ship" value="4" /></td>
			</c:if>
			<c:if test="${ShopHelper.hasState(aj_allConfig.shop.AllowPayType, ShopHelper.POINTS_PAY)}">
				<td><input type="radio" name="payment" id="point" value="8" /></td>
			</c:if>
		</tr>
		</table>
	</section>
	
	<section class="address">
		<h4><tags:i18n zh="送货地址" eng="Delivery Address" /></h4>
		
		<div v-if="list && list.length" style="float:left;text-align:left;">
			<div><tags:i18n zh="收货人：" eng="Receiver：" />{{shr}} </div>
			<div><tags:i18n zh="收货地址：" eng="Shipping address：" /> {{shdz}}</div>
		</div>
		
		<div v-if="!list || !list.length" style="float:left;text-align:left;">
			<tags:i18n zh="请至少提供一个送货地址，点击进行" eng="Please provide at least one shipping address and click to proceed" /><a href="${ctx}/user/address/"><tags:i18n zh="新建地址" eng="New address" /></a>。
		</div>
		
		<div style="float:right">
			<a href="###" @click="showList()"><tags:i18n zh="选择不同收货地址" eng="Choose a different shipping address" /></a> | 
			<a href="${ctx}/user/address/" target="_blank"><tags:i18n zh="新建地址" eng="New address" /></a>
		</div>
		
		<aj-layer ref="layer">
			<h5>地址簿</h5>
			<table class="shop-cart">
				<tr v-for="(item, index) in list">
					<td>{{item.name}}</td><td>{{item.phone}}</td>
					<td>{{toText(item)}}</td><td>{{item.isDefault ? '默认' : ''}}</td>
					<td>
						<a href="###" @click="select(index)">
							<tags:i18n zh="选择此地址" eng="Select this address" />
						</a>
					</td>
				</tr>
			</table>
		</aj-layer>
	</section>
	
	<section class="cart">
		<h4><tags:i18n zh="订单明细" eng="Order Details" /></h4>
		
		<table class="list">
			<thead>
				<tr>
					<th><tags:i18n zh="产品名称" eng="Product name" /></th>
					<th><tags:i18n zh="单价" eng="Price" /></th>
					<th><tags:i18n zh="数量" eng="Quantity" /></th>
					<th><tags:i18n zh="单项总价" eng="Total" /></th>
				</tr>
			</thead>
			<tbody>
				<tr v-for="item in items">
					<td class="info">
						<a :href="'${ctx}/shop/goods/' + item.goodsId + '/'"> 
							<img :src="ajResources.imgPerfix + item.cover"  width="146" />
							<div>
								<h3>{{item.goodsName}}</h3>
								<span>{{item.goodsFormat}}</span>
							</div>
						</a>
					</td>
					<td><tags:i18n zh="￥" eng="$" />{{item.price}}</td>
					<td>{{item.goodsNumber}}</td>
					<td><tags:i18n zh="￥" eng="$" />{{item.price * item.goodsNumber}}</td>
				</tr>
			</tbody>
		</table>
		
		<div class="numbers">
			<tags:i18n zh="一共" eng="Total" /> {{items.length}} <tags:i18n zh="种产品，共" eng="Products,Total" /> 
			{{totalNumbers}}
			<tags:i18n zh="件" eng="Pieces" />
		</div>
		
		<table class="total" align="right">
			<tr>
				<td><tags:i18n zh="总价：" eng="Total price：" /></td><td><tags:i18n zh="￥" eng="$" />{{totalPrice}}</td>
			</tr><tr>
				<td><tags:i18n zh="运费：" eng="Freight：" /></td><td><tags:i18n zh="￥" eng="$" />0.00</td>
			</tr><tr>
				<td><tags:i18n zh="支付金额：" eng="Payment amount：" /></td><td><tags:i18n zh="￥" eng="$" />{{totalPrice}}</td>
			</tr>
		</table>
		
		<!-- 下单创建订单的表单 -->
		<form action="${ctx}/shop/order/" method="POST" class="checkoutForm hide">
			<input type="text" name="payType" value="" />
			<input type="text" name="cartIds" value="" />
			<input type="text" name="addressId" value="" />
		</form>
		
		<!-- 下单创建订单的表单（直接下单） -->
		<form action="${ctx}/shop/order/directOrder/" method="POST" class="directCheckoutForm hide">
			<input type="text" name="payType" value="" />
			<input type="text" name="goodsId" value="" />
			<input type="text" name="goodsNumber" value="" />
			<input type="text" name="formatId" value="" />
			<input type="text" name="addressId" value="" />
		</form>
		
		<div class="btn">
			<button @click="checkout()">
	            <tags:i18n zh="确定下单" eng="Proceed to checkout" />
	         </button>
			<button onclick="history.back();">返回修改</button>&nbsp;&nbsp;&nbsp;
		</div>
	</section>

	<div style="clear:both;"></div>

	<script src="${ajaxjs_ui_output}/lib/China_AREA_full.js"></script>
	<script src="${ctx}/asset/js/checkout.js"></script>
	<script>
		ADDRESS.load(${empty addressList ? 'null': addressList});
	</script>
</tags:content>

