ADDRESS = new Vue({
	el: '.address',
	data: {
		shr : '',
		shdz: '',
		list : null,
		addressId: 0
	},
	methods: {
		load(list) {
			this.list= list;
			this.list.forEach(i => {
				if(i.isDefault) 
					this.add(i);
			});
		},
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

new Vue({
	el : '.cart',
	data: {
		items: [],
		isDirectOrder: false // 是否直接下单
	},
	mounted() {
		var map = this.get().search, goodsId = map['goodsId'], goodsFormatId = map['goodsFormatId'],
		goodsNumber = map['goodsNumber'];
		
		if(goodsId && goodsFormatId) { // 直接下单
			this.isDirectOrder = true;
			
			aj.parallel(
			  [
			    resolve => {
			    	 aj.xhr.get('../../goods/getJson/' + goodsId, j => {
			    		 resolve(j.result);
			    	 });
			     
			    },
			    resolve => {
			    	 aj.xhr.get('../../gooodsFormat/' + goodsFormatId, j => {
			    		 resolve(j.result);
			    	 });
			    }
			  ],
			  datas => {
			    console.log("finily", datas);
			    var goods = datas[0], format = datas[1];
			    this.items.push({
			    	goodsId: goods.id,
			    	goodsName: goods.name,
			    	goodsFormat: format.name,
			    	price: format.price,
			    	cover: goods.cover,
			    	goodsNumber: goodsNumber
			    })
			  }
			);
		} else {			
			aj.xhr.get(this.ajResources.ctx + "/shop/cart/mycartlist/", j => {
				if(!j.result)
					j.result = [];
				
				this.items = j.result;
			});
		}
	},
	computed: {
		totalPrice() {
			var k = 0;
			for(var i =0; i < this.items.length; i++) {
				k += this.items[i].price * this.items[i].goodsNumber;
			}
			
			return k;
		},
		totalNumbers() {
			var k = 0;
			for(var i =0; i < this.items.length; i++) {
				k += this.items[i].goodsNumber;
			}
			
			return k;
		}
	},
	methods: {
		/**
		 * 获取浏览器 url 参数读取 search 和 hash 的参数 location.params().hash['appinstall'];
		 * location.params().search['isappinstalled'];
		 */
		get(search, hash) {
			search = search || window.location.search;
			hash = hash || window.location.hash;

			var fn = function(str, reg) {
				if (str) {
					var data = {};
					str.replace(reg, function($0, $1, $2, $3) {
						data[$1] = $3;
					});

					return data;
				}
			};

			return {
				search : fn(search, new RegExp("([^?=&]+)(=([^&]*))?", "g")) || {},
				hash : fn(hash, new RegExp("([^#=&]+)(=([^&]*))?", "g")) || {}
			};
		},
		checkout() {
			if(!ADDRESS.addressId) {
				aj.alert.show("请选择送货地址");
				return;
			}
			
			var payment;
			document.querySelectorAll('input[name=payment]').forEach(i => {
				if(i.checked)
					payment = i.value;
			});
			
			if(!payment) {
				aj.alert.show('请选择支付方式');
				return;
			}

			var cartIds = [];
			this.items.forEach(i => {
				cartIds.push(i.id);
			});
			
			var checkoutForm;
			
			if(!this.isDirectOrder) {
				checkoutForm = aj('.checkoutForm');
				checkoutForm.$('input[name=cartIds]').value = cartIds.join(',');
			} else {
				checkoutForm = aj('.directCheckoutForm');
				
				var map = this.get().search, goodsId = map['goodsId'], goodsFormatId = map['goodsFormatId'],
				goodsNumber = map['goodsNumber'];
				
				checkoutForm.$('input[name=goodsId]').value = goodsId;
				checkoutForm.$('input[name=formatId]').value = goodsFormatId;
				checkoutForm.$('input[name=goodsNumber]').value = goodsNumber;
			}

			checkoutForm.$('input[name=payType]').value = payment;
			checkoutForm.$('input[name=addressId]').value = ADDRESS.addressId;
			checkoutForm.submit();
			
// aj.xhr.post('${ctx}/shop/order/', j => {
// if(j.isOk)
// aj.alert.show("提交订单成功！", {
// afterClose() {
// location.assign('${ctx}/shop/order/');
// }
// });
// }, {
// cartIds: cartIds,
// addressId:ADDRESS.addressId
// });
		}
	}
});