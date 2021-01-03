/**
 * 全国省市区 
 * 写死属性
 */
Vue.component('aj-china-area', {
	template: `
		<div class="aj-china-area">
			<span>省</span> 
			<select v-model="province" class="aj-select" style="width:120px;" :name="provinceName || 'locationProvince'">
				<option value="">请选择</option>
				<option v-for="(v, k) in addressData[86]" :value="k">{{v}}</option>
			</select>
			<span>市 </span>
			<select v-model="city" class="aj-select" style="width:120px;" :name="cityName || 'locationCity'">
				<option value="">请选择</option>
				<option v-for="(v, k) in citys" :value="k">{{v}}</option>
			</select>
			<span>区</span>  
			<select v-model="district" class="aj-select" style="width:120px;" :name="districtName || 'locationDistrict'">
				<option value="">请选择</option>
				<option v-for="(v, k) in districts" :value="k">{{v}}</option>
			</select>
		</div>
	`,
	props: {
		provinceCode: String,
		cityCode: String,
		districtCode: String,
		provinceName: String,
		cityName: String,
		districtName: String
	},
	data() {
		if (!China_AREA)
			throw '中国行政区域数据 脚本没导入';
		return {
			province: this.provinceCode || '',
			city: this.cityCode || '',
			district: this.districtCode || '',
			addressData: China_AREA
		};
	},
	watch: { // 令下一级修改
		province(val, oldval) {
			//            if(val !== oldval) 
			//                this.city = '';

		},
		//        city(val, oldval) {
		//            if(val !== oldval)
		//                this.district = '';
		//        }
	},

	computed: {
		citys() {
			if (!this.province)
				return;

			return this.addressData[this.province];
		},
		districts() {
			if (!this.city)
				return;

			return this.addressData[this.city];
		}
	}
});