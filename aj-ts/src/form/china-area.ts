
namespace aj.form {
	interface _ChinaArea {
		province: string;
		city: string;
		addressData: any;
	}

	/**
	 * 全国省市区 
	 * 写死属性
	 */
	export class ChinaArea extends VueComponent {
		name = "aj-china-area";

		template = html`
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
		`;

		provinceCode = String;
		cityCode = String;
		districtCode = String;
		provinceName = String;
		cityName = String;
		districtName = String;

		computed = {
			citys(this: _ChinaArea): JsonParam {
				if (!this.province)
					return {};

				return this.addressData[this.province];
			},
			districts(this: _ChinaArea): JsonParam {
				if (!this.city)
					return {};

				return this.addressData[this.city];
			}
		};

		data() {
			return {
				province: this.provinceCode || '',
				city: this.cityCode || '',
				district: this.districtCode || '',
				//@ts-ignore
				addressData: window.China_AREA
			};
		}

		// watch = { // 令下一级修改
		// 	province(val: any, oldval: any) {
		// 		if (val !== oldval)
		// 			this.city = '';
		// 	},
		// 	city(val: any, oldval: any) {
		// 		if (val !== oldval)
		// 			this.district = '';
		// 	}
		// };

		mounted(): void {
			//@ts-ignore
			if (!window.China_AREA)
				throw '中国行政区域数据 脚本没导入';
		}
	}

	new ChinaArea().register();
}
