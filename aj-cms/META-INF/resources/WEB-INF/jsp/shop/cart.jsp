<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tags"%>

<tags:user-center>
	<h2 class="aj-center-title">${PAGE_Node.name}</h2>
	<div class="shop-cart">
		<table v-if="items && items.length">
			<thead>
				<tr>
					<th><tags:i18n zh="产品名称" eng="Product name" /></th>
					<th><tags:i18n zh="单价" eng="Price" /></th>
					<th><tags:i18n zh="数量" eng="Quantity" /></th>
					<th><tags:i18n zh="单项总价" eng="Total" /></th>
					<th><tags:i18n zh="操作" eng="Action" /></th>
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
					<td class="price">￥{{item.price}}</td>
					<td>
						<input type="number" data-zeros="true" v-model="item.goodsNumber" min="1" max="1000" />
					</td>
					<td class="price">￥{{item.price * item.goodsNumber}}</td>
					<td>
						<a href="###" @click="deleteCart(item.id)"><tags:i18n zh="移除" eng="Remove" /></a>
					</td>
				</tr>
			</tbody>
		</table>

		<div v-if="!items || !items.length" class="empty">
			<img src="${ctx}/asset/common/icon/empty-cart.png" />
			购物车为空，马上<a href="${ctx}/shop/">去看看？</a>~
		</div>
		
		<div class="checkout">
			<div class="txt">
				<tags:i18n zh="总价" eng="Total" />
			</div>
			<div class="price">￥{{totalPrice}}</div>
			<div v-if="items && items.length">
				<button class="aj-btn" onclick="location.assign('${ctx}/shop/order/checkout/');">
					<tags:i18n zh="下单" eng="Checkout" />
				</button>
			</div>
		</div>
	</div>

	<script src="${ctx}/asset/js/cart.js"></script>
</tags:user-center>

