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
var CCA;
(function (CCA) {
    /**
     * SQL 编辑器
     */
    var SqlEditor = /** @class */ (function (_super) {
        __extends(SqlEditor, _super);
        function SqlEditor() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.name = "sql-editor";
            _this.template = html(__makeTemplateObject(["<div>\n    <textarea :value=\"parentValue\" @input=\"$emit('update:parentValue', $event.target.value)\" :disabled=\"disabled\" cols=\"60\" rows=\"5\"></textarea>\n    <br />\n    <label><input type=\"checkbox\" /> \u81EA\u5B9A\u4E49 SQL</label> <a href=\"javascript:void\">\u5F39\u51FA\u7F16\u8F91</a>\n</div>"], ["<div>\n    <textarea :value=\"parentValue\" @input=\"$emit('update:parentValue', $event.target.value)\" :disabled=\"disabled\" cols=\"60\" rows=\"5\"></textarea>\n    <br />\n    <label><input type=\"checkbox\" /> \u81EA\u5B9A\u4E49 SQL</label> <a href=\"javascript:void\">\u5F39\u51FA\u7F16\u8F91</a>\n</div>"]));
            _this.props = {
                disabled: { type: Boolean },
                parentValue: String
            };
            return _this;
        }
        return SqlEditor;
    }(aj.VueComponent));
    new SqlEditor().register();
    /**
     * 接口地址
     */
    var ApiUrl = /** @class */ (function (_super) {
        __extends(ApiUrl, _super);
        function ApiUrl() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.name = "api-url";
            _this.template = html(__makeTemplateObject(["<span class=\"api-url\">\n    <span v-html=\"getMethod()\"></span>\n    <a :href=\"getUrl\">{{getUrl()}}</a>\n</span>"], ["<span class=\"api-url\">\n    <span v-html=\"getMethod()\"></span>\n    <a :href=\"getUrl\">{{getUrl()}}</a>\n</span>"]));
            _this.props = {
                method: { type: String, default: 'get' },
                root: { type: String, required: true },
                dir: { type: String, required: true },
                subDir: { type: String, required: false },
            };
            /**
             * HTTP 方法
             */
            _this.method = "";
            return _this;
        }
        ApiUrl.prototype.getMethod = function () {
            switch (this.method) {
                case 'delete':
                    return '<span style="color: red;">DELETE</span>';
                case 'put':
                    return '<span style="color: blue;">PUT</span>';
                case 'post':
                    return '<span style="color: rgb(224, 60, 254);">POST</span>';
                case 'get':
                default:
                    return '<span style="color: rgb(20, 215, 20);">GET</span>';
            }
        };
        ApiUrl.prototype.getUrl = function () {
            // @ts-ignore
            var url = this.root + '/' + this.dir + '/';
            // @ts-ignore
            if (this.subDir)
                url += this.subDir;
            return url;
        };
        return ApiUrl;
    }(aj.VueComponent));
    new ApiUrl().register();
    /**
     * 自定义操作
     */
    var CustomAction = /** @class */ (function (_super) {
        __extends(CustomAction, _super);
        function CustomAction() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.name = "custom-action";
            _this.template = html(__makeTemplateObject(["<tr>\n    <td class=\"first\" style=\"padding-top: 30px;padding-bottom: 30px;\">\n        <input type=\"checkbox\" />\n        <input style=\"width:180px;\" type=\"text\" v-model=\"name\" placeholder=\"\u64CD\u4F5C\u540D\u79F0\uFF0C\u4E5F\u4F5C\u4E3AURL\" />\n        <br />\n        <select style=\"margin:10px 0 0 15px; width:180px;\" v-model=\"method\">\n            <option value=\"get\">\u67E5\u8BE2\u64CD\u4F5C</option>\n            <option value=\"post\">\u65B0\u589E\u64CD\u4F5C</option>\n            <option value=\"put\">\u4FEE\u6539\u64CD\u4F5C</option>\n            <option value=\"delete\">\u5220\u9664\u64CD\u4F5C</option>\n            <option value=\"get\">\u5B58\u50A8\u8FC7\u7A0B</option>\n            <option value=\"get\">\u6279\u5904\u7406</option>\n        </select>\n    </td>\n    <td>\n        <textarea placeholder=\"\u64CD\u4F5C\u8BF4\u660E\uFF0C\u63CF\u8FF0\u8FD9\u4E2A\u63A5\u53E3\u7684\u4F5C\u7528\u3002\u53EF\u9009\u7684\" cols=\"30\" rows=\"4\" style=\"min-height: 20px;\"></textarea>\n    </td>\n    <td>\n        <sql-editor :disabled=\"!true\"></sql-editor>\n    </td>\n    <td>\n        <api-url :root=\"root\" :dir=\"dir\" :sub-dir=\"name\" :method=\"method\"></api-url>\n        <br />\n        <button title=\"\u6DFB\u52A0\u540E\u4E0D\u662F\u771F\u6B63\u7684\u4FDD\u5B58\uFF0C\u53EA\u662F\u6DFB\u52A0\u5230\u7F13\u51B2\u533A\u3002\n\u6309\u4E0A\u65B9\u7684\u3010\u4FDD\u5B58\u3011\u771F\u6B63\u4FDD\u5B58\u3002\n\u591A\u6B21\u6DFB\u52A0\u540E\u53EF\u4EE5\u4E00\u6B21\u3010\u4FDD\u5B58\u3011\u3002\">\n            \u6DFB\u52A0\n        </button>\n    </td>\n</tr>"], ["<tr>\n    <td class=\"first\" style=\"padding-top: 30px;padding-bottom: 30px;\">\n        <input type=\"checkbox\" />\n        <input style=\"width:180px;\" type=\"text\" v-model=\"name\" placeholder=\"\u64CD\u4F5C\u540D\u79F0\uFF0C\u4E5F\u4F5C\u4E3AURL\" />\n        <br />\n        <select style=\"margin:10px 0 0 15px; width:180px;\" v-model=\"method\">\n            <option value=\"get\">\u67E5\u8BE2\u64CD\u4F5C</option>\n            <option value=\"post\">\u65B0\u589E\u64CD\u4F5C</option>\n            <option value=\"put\">\u4FEE\u6539\u64CD\u4F5C</option>\n            <option value=\"delete\">\u5220\u9664\u64CD\u4F5C</option>\n            <option value=\"get\">\u5B58\u50A8\u8FC7\u7A0B</option>\n            <option value=\"get\">\u6279\u5904\u7406</option>\n        </select>\n    </td>\n    <td>\n        <textarea placeholder=\"\u64CD\u4F5C\u8BF4\u660E\uFF0C\u63CF\u8FF0\u8FD9\u4E2A\u63A5\u53E3\u7684\u4F5C\u7528\u3002\u53EF\u9009\u7684\" cols=\"30\" rows=\"4\" style=\"min-height: 20px;\"></textarea>\n    </td>\n    <td>\n        <sql-editor :disabled=\"!true\"></sql-editor>\n    </td>\n    <td>\n        <api-url :root=\"root\" :dir=\"dir\" :sub-dir=\"name\" :method=\"method\"></api-url>\n        <br />\n        <button title=\"\u6DFB\u52A0\u540E\u4E0D\u662F\u771F\u6B63\u7684\u4FDD\u5B58\uFF0C\u53EA\u662F\u6DFB\u52A0\u5230\u7F13\u51B2\u533A\u3002\\n\u6309\u4E0A\u65B9\u7684\u3010\u4FDD\u5B58\u3011\u771F\u6B63\u4FDD\u5B58\u3002\\n\u591A\u6B21\u6DFB\u52A0\u540E\u53EF\u4EE5\u4E00\u6B21\u3010\u4FDD\u5B58\u3011\u3002\">\n            \u6DFB\u52A0\n        </button>\n    </td>\n</tr>"]));
            _this.props = {
                root: { type: String, required: true },
                dir: { type: String, required: true },
            };
            return _this;
        }
        CustomAction.prototype.data = function () {
            return {
                name: '',
                method: 'get'
            };
        };
        return CustomAction;
    }(aj.VueComponent));
    new CustomAction().register();
})(CCA || (CCA = {}));
