interface ChinaArea {
	province: any;
	city: any;
	addressData: any;
}

aj.loadScript('China_AREA.js', "", () => {
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
			if (!window.China_AREA)
				throw '中国行政区域数据 脚本没导入';

			return {
				province: this.provinceCode || '',
				city: this.cityCode || '',
				district: this.districtCode || '',
				addressData: window.China_AREA
			};
		},
		watch: { // 令下一级修改
			province(val: any, oldval: any) {
				//            if(val !== oldval) 
				//                this.city = '';

			},
			//        city(val, oldval) {
			//            if(val !== oldval)
			//                this.district = '';
			//        }
		},

		computed: {
			citys(this: ChinaArea) {
				if (!this.province)
					return;

				return this.addressData[this.province];
			},
			districts(this: ChinaArea) {
				if (!this.city)
					return;

				return this.addressData[this.city];
			}
		}
	});

});