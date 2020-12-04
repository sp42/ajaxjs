<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tags"%>
<tags:page>
	<tags:header zh="下单完成" eng="Checkout Done">
		<jsp:attribute name="img">
			<img src="${ctx}/asset/images/breadrcumbs-img-572x360.jpg" />
		</jsp:attribute>
		<jsp:attribute name="breadcrumbs">
			<li><a href="${ctx}/shop/goods/"><tags:i18n zh="商城" eng="Product" /></a></li>
		</jsp:attribute>
	</tags:header>
	
	<style>
		.orderDetail {
			margin: 3% auto;
			width: 80%;
		}
		.shop-cart{
			width:100%;
		}
		.shop-cart td, .shop-cart th {
			padding: 2%;
		}
		.shop-cart td{
			border-bottom: 1px lightgray solid;
		}
		.shop-cart thead {
			border-bottom: 2px gray solid;
		}
		
		h4{
			text-align:left;
			margin: 3% 0;
		}
		.total{
			font-weight:bold;
			margin:2%;		
		}
		.numbers{
			margin:2%;		
			text-align:left;
			float:left;
		}
		.address table{
			width:900px;
		}
	</style>
	<script src="${ajaxjs_ui_output}/lib/China_AREA_full.js"></script>
	
	<div class="orderDetail">
		<h4> <tags:i18n zh="确认电子邮件地址" eng="Confirm email address" /></h4>
		<div><tags:i18n zh="本系统采用电子邮件购买机制，故邮件地址是否正确非常重要，请核对邮件：" eng="This system uses an email purchase mechanism, so it is important that the email address is correct. Please check the email:" /><b>${email}</b><tags:i18n zh="。如需修改，请到" eng="。If you need to modify, please go to" /><a href="${ctx}/user/user-center/account/"><tags:i18n zh="帐号管理" eng="Account management" /></a><tags:i18n zh="进行修改。" eng="To Modify。" /></div>
		<h4><tags:i18n zh="送货地址" eng="Delivery Address" /></h4>
		<div style="clear:both;"></div>
		
		<section class="address">
			<div v-if="list && list.length" style="float:left;text-align:left;">
				<div><tags:i18n zh="收货人：" eng="Receiver：" />{{shr}} </div>
				<div><tags:i18n zh="收货地址：" eng="Shipping address：" /> {{shdz}}</div>
			</div>
			<div v-if="!list || !list.length" style="float:left;text-align:left;">
				<tags:i18n zh="请至少提供一个送货地址，点击进行" eng="Please provide at least one shipping address and click to proceed" />
				<a href="${ctx}/user/address/"><tags:i18n zh="新建地址" eng="New address" /></a>。
			</div>
			<div style="float:right">
				<a href="###" @click="showList()"><tags:i18n zh="选择不同收货地址" eng="Choose a different shipping address" /></a> | <a href="${ctx}/user/address/"><tags:i18n zh="新建地址" eng="New address" /></a>
			</div>
			<aj-layer ref="layer">
				<table class="shop-cart">
					<tr v-for="(item, index) in list">
						<td>{{item.name}}</td><td>{{item.phone}}</td>
						<td>{{toText(item)}}</td><td>{{item.isDefault ? '默认' : ''}}</td>
						<td>
							<a href="###" @click="select(index)"><tags:i18n zh="选择此地址" eng="Select this address" /></a>
						</td>
					</tr>
				</table>
			</aj-layer>
		</section>
		<script>
			ADDRESS = new Vue({
				el : '.orderDetail .address',
				data : {
					shr : '',
					shdz: '',
					list : ${empty addressList ? 'null': addressList},
					addressId:0
				},
				mounted(){
					this.list && this.list.forEach(i => {
						if(i.isDefault) {
							this.add(i);
						}
					});
				},
				methods:{
					add(i) {
						this.shr = i.name + ' ' + i.phone;
						this.shdz = this.toText(i);
						this.addressId = i.id;
					},
					toText(i) {
						return China_AREA[86][i.locationProvince] + China_AREA[i.locationProvince][i.locationCity] +
						China_AREA[i.locationCity][i.locationDistrict] + i.address;
					},
					showList() {
						this.$refs.layer.show();
					},
					select(index) {
						var i = this.list[index];
						this.add(i);
						this.$refs.layer.close();
					}
				}
			});
		</script>
	
		<div style="clear:both;"></div>
		
		<section class="cart">
			<h4><tags:i18n zh="订单明细" eng="Order Details" /></h4>
			<table class="shop-cart">
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
						<td class="flex-cell"><a target="_blank"
							:href="'${ctx}/shop/goods/' + item.goodsId + '/'"> <img
								:src="'${ctx}/' + item.cover" style="" width="100" />
						</a>
							<div style="margin-left: 2%;">
								<a target="_blank"
									:href="'${ctx}/shop/goods/' + item.goodsId + '/'"
									style="font-size: 1.1rem; font-weight: bold;">{{item.goodsName}}</a>
								<div style="font-size: 1rem;">{{item.goodsFormat}}</div>
							</div></td>
						<td><tags:i18n zh="￥" eng="$" />{{item.price}}</td>
						<td>{{item.goodsNumber}}</td>
						<td><tags:i18n zh="￥" eng="$" />{{item.price * item.goodsNumber}}</td>
					</tr>
				</tbody>
			</table>
			<div class="numbers"><tags:i18n zh="一共" eng="Total" />{{items.length}} <tags:i18n zh="种产品，共" eng="Products,Total" />   {{totalNumbers}}<tags:i18n zh="件" eng="Pieces" /></div>
			<table class="total" align="right">
				<tr>
					<td><tags:i18n zh="总价：" eng="Total price：" /></td><td><tags:i18n zh="￥" eng="$" />{{totalPrice}}</td>
				</tr><tr>
					<td><tags:i18n zh="运费：" eng="Freight：" /></td><td><tags:i18n zh="￥" eng="$" /> 0.00</td>
				</tr><tr>
					<td><tags:i18n zh="支付金额：" eng="Payment amount：" /></td><td><tags:i18n zh="￥" eng="$" />{{totalPrice}}</td>
				</tr>
			</table>
			<div style="clear:both;">
				<a class="button button-lg button-primary button-zakaria" href="###" @click="checkout();" style="float: right;margin:2%;">
		            <tags:i18n zh="确定下单" eng="Proceed to checkout" />
		         </a>
			</div>
		</section>
	</div>
	<script>
		new Vue({
			el : '.orderDetail .cart',
			mixins : [ CAR_COMMON ],
			methods: {
				checkout(){
					if(!ADDRESS.addressId) {
						aj.alert.show("请选择送货地址");
						return 
					}
				}
			}
		});
	</script>
</tags:page>

