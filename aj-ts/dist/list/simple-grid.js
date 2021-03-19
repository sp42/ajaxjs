"use strict";
var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};
var aj;
(function (aj) {
    var grid;
    (function (grid) {
        Vue.component('aj-simple-grid', {
            template: html(__makeTemplateObject(["\n            <div>\n                <form action=\"?\" method=\"GET\" style=\"float:right;\">\n                    <input type=\"hidden\" name=\"searchField\" value=\"content\" />\n                    <input type=\"text\" name=\"searchclassValue\" placeholder=\"\u8BF7\u8F93\u5165\u641C\u7D22\u4E4B\u5173\u952E\u5B57\" class=\"aj-input\" />\n                    <button style=\"margin-top: 0;\" class=\"aj-btn\">\u641C\u7D22</button>\n                </form>\n                <table class=\"aj-grid aj-table\" style=\"clear: both;width:100%\">\n                    <thead>\n                        <tr>\n                            <th v-for=\"key in columns\" @click=\"sortBy(key)\" :class=\"{ active: sortKey == key }\">\n                                {{ key | capitalize }}\n                                <span class=\"arrow\" :class=\"sortOrders[key] > 0 ? 'asc' : 'dsc'\"></span>\n                            </th>\n                        </tr>\n                    </thead>\n                    <tbody>\n                        <tr v-for=\"entry in filteredData\">\n                            <td v-for=\"key in columns\" v-html=\"entry[key]\"></td>\n                        </tr>\n                    </tbody>\n                </table>\n            </div>\n        "], ["\n            <div>\n                <form action=\"?\" method=\"GET\" style=\"float:right;\">\n                    <input type=\"hidden\" name=\"searchField\" value=\"content\" />\n                    <input type=\"text\" name=\"searchclassValue\" placeholder=\"\u8BF7\u8F93\u5165\u641C\u7D22\u4E4B\u5173\u952E\u5B57\" class=\"aj-input\" />\n                    <button style=\"margin-top: 0;\" class=\"aj-btn\">\u641C\u7D22</button>\n                </form>\n                <table class=\"aj-grid aj-table\" style=\"clear: both;width:100%\">\n                    <thead>\n                        <tr>\n                            <th v-for=\"key in columns\" @click=\"sortBy(key)\" :class=\"{ active: sortKey == key }\">\n                                {{ key | capitalize }}\n                                <span class=\"arrow\" :class=\"sortOrders[key] > 0 ? 'asc' : 'dsc'\"></span>\n                            </th>\n                        </tr>\n                    </thead>\n                    <tbody>\n                        <tr v-for=\"entry in filteredData\">\n                            <td v-for=\"key in columns\" v-html=\"entry[key]\"></td>\n                        </tr>\n                    </tbody>\n                </table>\n            </div>\n        "])),
            props: {
                data: Array,
                columns: Array,
                filterKey: String
            },
            data: function () {
                var sortOrders = {};
                this.columns.forEach(function (key) { return sortOrders[key] = 1; });
                return {
                    sortKey: '',
                    sortOrders: sortOrders
                };
            },
            computed: {
                filteredData: function () {
                    var sortKey = this.sortKey, filterKey = this.filterKey && this.filterKey.toLowerCase(), order = this.sortOrders[sortKey] || 1, data = this.data;
                    if (filterKey) {
                        data = data.filter(function (row) {
                            return Object.keys(row).some(function (key) { return String(row[key]).toLowerCase().indexOf(filterKey) > -1; });
                        });
                    }
                    if (sortKey) {
                        data = data.slice().sort(function (a, b) {
                            a = a[sortKey];
                            b = b[sortKey];
                            return (a === b ? 0 : a > b ? 1 : -1) * order;
                        });
                    }
                    return data;
                }
            },
            filters: {
                capitalize: function (str) {
                    return str.charAt(0).toUpperCase() + str.slice(1);
                }
            },
            methods: {
                sortBy: function (key) {
                    this.sortKey = key;
                    this.sortOrders[key] = this.sortOrders[key] * -1;
                }
            }
        });
    })(grid = aj.grid || (aj.grid = {}));
})(aj || (aj = {}));
