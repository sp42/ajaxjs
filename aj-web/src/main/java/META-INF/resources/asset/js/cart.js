CAR_COMMON = {
		data: {
			items: [],
			oldItems: null
	// totalPrice:0
	},
	methods: {
		load() {
			aj.xhr.get(this.ajResources.ctx + "/shop/cart/mycartlist/", j => {
				if(!j.result)
					j.result = [];
				
				this.items = j.result;
			});
		},
		// 添加购物车
		addCart(goodsFormatId, goodsNumber) {
			aj.xhr.get(this.ajResources.ctx + '/user/isLogin/', json => {
				if(json.result) {
					aj.xhr.post(this.ajResources.ctx + '/shop/cart/', json => {
						if(json && json.isOk) {
							aj.showOk('成功加入购物车，<a href="' + this.ajResources.ctx + '/shop/cart/">【点击跳转购物车】</a>');
							this.load(); // 刷新
						} else{
							aj.showOk('加入购物车失败！');
						}
					}, {
						goodsNumber : goodsNumber,
						goodsFormatId: goodsFormatId
					});
				} else {
					aj.showOk('尚未登录，<a href="' + this.ajResources.ctx + '/user/login/">【点击跳转登录】</a>')
				}
			});
		},
		deleteCart(id) {
			aj.xhr.dele(this.ajResources.ctx + '/shop/cart/' + id + '/', aj.xhr.defaultCallBack.delegate(null, null, () => {
				this.load();
			}));
		}
	},
	computed: {
		totalPrice() {
			this.oldItems = JSON.parse(JSON.stringify(this.items)); // watcher
																	// oldValue和newValue始终一样
			
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
	watch: {
		items: {
			handler(newValue) {
				// 用户修改了数量
				for(var i = 0; i < newValue.length; i++) {
				if(!this.oldItems[i])
					return;
				
				var item = newValue[i];
				if(Number(item.goodsNumber) !== Number(this.oldItems[i].goodsNumber)){
					 console.log({
						 id: item.id,
						 goodsNumber: item.goodsNumber
					 });
					 
					 aj.xhr.post(this.ajResources.ctx + '/shop/cart/updateGoodsNumber/' + item.id + '/', j => {
							 console.log(j);
						 }, {
							 goodsNumber: item.goodsNumber
						 });
					 }
				}
			},
		    deep: true
		}
	}
};
	
new Vue({
	el : '.shop-cart',
	mixins: [CAR_COMMON]
}).load();