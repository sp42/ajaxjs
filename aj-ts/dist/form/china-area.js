"use strict";
/**
 * 全国省市区
 * 写死属性
 */
Vue.component('aj-china-area', {
    template: "\n\t\t<div class=\"aj-china-area\">\n\t\t\t<span>\u7701</span> \n\t\t\t<select v-model=\"province\" class=\"aj-select\" style=\"width:120px;\" :name=\"provinceName || 'locationProvince'\">\n\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t<option v-for=\"(v, k) in addressData[86]\" :value=\"k\">{{v}}</option>\n\t\t\t</select>\n\t\t\t<span>\u5E02 </span>\n\t\t\t<select v-model=\"city\" class=\"aj-select\" style=\"width:120px;\" :name=\"cityName || 'locationCity'\">\n\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t<option v-for=\"(v, k) in citys\" :value=\"k\">{{v}}</option>\n\t\t\t</select>\n\t\t\t<span>\u533A</span>  \n\t\t\t<select v-model=\"district\" class=\"aj-select\" style=\"width:120px;\" :name=\"districtName || 'locationDistrict'\">\n\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t<option v-for=\"(v, k) in districts\" :value=\"k\">{{v}}</option>\n\t\t\t</select>\n\t\t</div>\n\t",
    props: {
        provinceCode: String,
        cityCode: String,
        districtCode: String,
        provinceName: String,
        cityName: String,
        districtName: String
    },
    data: function () {
        //@ts-ignore
        if (!window.China_AREA)
            throw '中国行政区域数据 脚本没导入';
        return {
            province: this.provinceCode || '',
            city: this.cityCode || '',
            district: this.districtCode || '',
            //@ts-ignore
            addressData: window.China_AREA
        };
    },
    watch: {
        province: function (val, oldval) {
            //            if(val !== oldval) 
            //                this.city = '';
        },
    },
    computed: {
        citys: function () {
            if (!this.province)
                return;
            return this.addressData[this.province];
        },
        districts: function () {
            if (!this.city)
                return;
            return this.addressData[this.city];
        }
    }
});
