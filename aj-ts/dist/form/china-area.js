"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};
var aj;
(function (aj) {
    var form;
    (function (form) {
        /**
         * 全国省市区
         * 写死属性
         */
        var ChinaArea = /** @class */ (function (_super) {
            __extends(ChinaArea, _super);
            function ChinaArea() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = "aj-china-area";
                _this.template = html(__makeTemplateObject(["\n\t\t\t<div class=\"aj-china-area\">\n\t\t\t\t<span>\u7701</span>\n\t\t\t\t<select v-model=\"province\" class=\"aj-select\" style=\"width:120px;\" :name=\"provinceName || 'locationProvince'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in addressData[86]\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t\t<span>\u5E02 </span>\n\t\t\t\t<select v-model=\"city\" class=\"aj-select\" style=\"width:120px;\" :name=\"cityName || 'locationCity'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in citys\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t\t<span>\u533A</span>\n\t\t\t\t<select v-model=\"district\" class=\"aj-select\" style=\"width:120px;\" :name=\"districtName || 'locationDistrict'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in districts\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t</div>\n\t\t"], ["\n\t\t\t<div class=\"aj-china-area\">\n\t\t\t\t<span>\u7701</span>\n\t\t\t\t<select v-model=\"province\" class=\"aj-select\" style=\"width:120px;\" :name=\"provinceName || 'locationProvince'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in addressData[86]\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t\t<span>\u5E02 </span>\n\t\t\t\t<select v-model=\"city\" class=\"aj-select\" style=\"width:120px;\" :name=\"cityName || 'locationCity'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in citys\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t\t<span>\u533A</span>\n\t\t\t\t<select v-model=\"district\" class=\"aj-select\" style=\"width:120px;\" :name=\"districtName || 'locationDistrict'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in districts\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t</div>\n\t\t"]));
                _this.provinceCode = String;
                _this.cityCode = String;
                _this.districtCode = String;
                _this.provinceName = String;
                _this.cityName = String;
                _this.districtName = String;
                _this.computed = {
                    citys: function () {
                        if (!this.province)
                            return {};
                        return this.addressData[this.province];
                    },
                    districts: function () {
                        if (!this.city)
                            return {};
                        return this.addressData[this.city];
                    }
                };
                return _this;
            }
            ChinaArea.prototype.data = function () {
                return {
                    province: this.provinceCode || '',
                    city: this.cityCode || '',
                    district: this.districtCode || '',
                    //@ts-ignore
                    addressData: window.China_AREA
                };
            };
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
            ChinaArea.prototype.mounted = function () {
                //@ts-ignore
                if (!window.China_AREA)
                    throw '中国行政区域数据 脚本没导入';
            };
            return ChinaArea;
        }(aj.VueComponent));
        form.ChinaArea = ChinaArea;
        new ChinaArea().register();
    })(form = aj.form || (aj.form = {}));
})(aj || (aj = {}));
