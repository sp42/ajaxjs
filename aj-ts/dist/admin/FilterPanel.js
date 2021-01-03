"use strict";
/**
 * 搜索、分类下拉
 */
Vue.component('aj-admin-filter-panel', {
    template: "\n        <div class=\"aj-admin-filter-panel\">\n            <form action=\"?\" method=\"GET\">\n                <input type=\"hidden\" name=\"searchField\" :value=\"searchFieldValue\" />\n                <input type=\"text\" name=\"keyword\" placeholder=\"\u8BF7\u8F93\u5165\u641C\u7D22\u4E4B\u5173\u952E\u5B57\" style=\"float: inherit;\" class=\"aj-input\" />\n                <button style=\"margin-top: 0;\" class=\"aj-btn\">\u641C\u7D22</button> &nbsp;\n            </form><slot></slot>\n            <span v-if=\"!noCatalog\">{{label || '\u5206\u7C7B'}}\uFF1A\n                <aj-tree-catelog-select :is-auto-jump=\"true\" :catalog-id=\"catalogId\" :selected-catalog-id=\"selectedCatalogId\"></aj-tree-catelog-select></span>\n         </div>\n    ",
    props: {
        label: { type: String, required: false },
        catalogId: { type: Number, required: false },
        selectedCatalogId: { type: Number, required: false },
        noCatalog: { type: Boolean, default: false },
        searchFieldValue: { required: false, default: 'name' } // 搜索哪个字段？默认为 name
    }
});
