"use strict";
/**
 * 后台头部导航
 */
Vue.component('ajaxjs-admin-header', {
    template: "\t\n        <header class=\"ajaxjs-admin-header\">\n            <div>\n                <slot name=\"btns\"></slot>\n                <a href=\"#\" target=\"_blank\">\n                    <img width=\"12\" src=\"data:image/gif;base64,R0lGODlhEAAQAIABAAAAAP///yH5BAEAAAEALAAAAAAQABAAAAImjG+gq+je3gOBWURrlvVEuWlcKE4T2Xkql6zshkLuOIO1mVj6VgAAOw==\" /> \n                    \u65B0\u7A97\u53E3\u6253\u5F00\n                </a>\n            </div>\n            <fieldset>\n                <legend>\n                    <slot name=\"title\">\n                        {{isCreate ? \"\u65B0\u5EFA\":\"\u7F16\u8F91\"}}{{uiName}} \uFF1A<span v-if=\"infoId\">No.{{infoId}}</span>\n                    </slot>\n                </legend>\n            </fieldset>\n        </header>\n    ",
    props: {
        isCreate: Boolean,
        uiName: String,
        infoId: Number // 实体 id
    }
});
