"use strict";
var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};
/**
* 将上传控件嵌入到一个浮出层中
*/
Vue.component('aj-form-popup-upload', {
    template: html(__makeTemplateObject(["\n        <aj-layer>\n            <h3>\u56FE\u7247\u4E0A\u4F20</h3>\n            <p>\u4E0A\u4F20\u6210\u529F\u540E\u81EA\u52A8\u63D2\u5165\u5230\u6B63\u6587</p>\n            <aj-xhr-upload ref=\"uploadControl\" :action=\"uploadUrl\" :is-img-upload=\"true\" :hidden-field=\"imgName\"\n                :img-place=\"'/images/imgBg.png'\">\n            </aj-xhr-upload>\n            <div>\u4E0A\u4F20\u9650\u5236\uFF1A{{text.maxSize}}kb \u6216\u4EE5\u4E0B\uFF0C\u5206\u8FA8\u7387\uFF1A{{text.maxHeight}}x{{text.maxWidth}}</div>\n        </aj-layer>\n    "], ["\n        <aj-layer>\n            <h3>\u56FE\u7247\u4E0A\u4F20</h3>\n            <p>\u4E0A\u4F20\u6210\u529F\u540E\u81EA\u52A8\u63D2\u5165\u5230\u6B63\u6587</p>\n            <aj-xhr-upload ref=\"uploadControl\" :action=\"uploadUrl\" :is-img-upload=\"true\" :hidden-field=\"imgName\"\n                :img-place=\"'/images/imgBg.png'\">\n            </aj-xhr-upload>\n            <div>\u4E0A\u4F20\u9650\u5236\uFF1A{{text.maxSize}}kb \u6216\u4EE5\u4E0B\uFF0C\u5206\u8FA8\u7387\uFF1A{{text.maxHeight}}x{{text.maxWidth}}</div>\n        </aj-layer>\n    "])),
    data: function () {
        return {
            text: {}
        };
    },
    props: {
        uploadUrl: { type: String, required: true },
        imgName: { type: String, required: false },
        imgId: { type: Number, required: false },
        imgPlace: String // 图片占位符，用户没有选定图片时候使用的图片
    },
    mounted: function () {
        var obj = this.$refs.uploadControl;
        this.text = { maxSize: obj.limitSize || 600, maxHeight: obj.imgMaxHeight, maxWidth: obj.imgMaxWidth };
    },
    methods: {
        /**
         * 显示上传控件
         *
         * @param {Function} callback 上传成功之后的回调函数
         */
        show: function (callback) {
            if (callback)
                this.$refs.uploadControl.uploadOk_callback = callback;
            this.$children[0].show();
        }
    }
});
