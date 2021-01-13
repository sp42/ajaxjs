"use strict";
var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};
Vue.component('aj-tree-item', {
    template: html(__makeTemplateObject(["\n        <li>\n            <div :class=\"{bold: isFolder, node: true}\" @click=\"toggle\">\n                <span>\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7</span>{{model.name}}\n                <span v-if=\"isFolder\">[{{open ? '-' : '+'}}]</span>\n            </div>\n            <ul v-show=\"open\" v-if=\"isFolder\" :class=\"{show: open}\">\n                <aj-tree-item class=\"item\" v-for=\"(model, index) in model.children\" :key=\"index\" :model=\"model\">\n                </aj-tree-item>\n                <li v-if=\"allowAddNode\" class=\"add\" @click=\"addChild\">+</li>\n            </ul>\n        </li>\n    "], ["\n        <li>\n            <div :class=\"{bold: isFolder, node: true}\" @click=\"toggle\">\n                <span>\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7</span>{{model.name}}\n                <span v-if=\"isFolder\">[{{open ? '-' : '+'}}]</span>\n            </div>\n            <ul v-show=\"open\" v-if=\"isFolder\" :class=\"{show: open}\">\n                <aj-tree-item class=\"item\" v-for=\"(model, index) in model.children\" :key=\"index\" :model=\"model\">\n                </aj-tree-item>\n                <li v-if=\"allowAddNode\" class=\"add\" @click=\"addChild\">+</li>\n            </ul>\n        </li>\n    "])),
    props: {
        model: Object,
        allowAddNode: { type: Boolean, default: false } // 是否允许添加新节点
    },
    data: function () {
        return { open: false };
    },
    computed: {
        isFolder: function () {
            return !!(this.model.children && this.model.children.length);
        }
    },
    methods: {
        /**
         * 点击节点时的方法
         *
         * @param this
         */
        toggle: function () {
            if (this.isFolder)
                this.open = !this.open;
            this.BUS && this.BUS.$emit('tree-node-click', this.model);
        },
        /**
         * 变为文件夹
         *
         * @param this
         */
        changeType: function () {
            if (!this.isFolder) {
                Vue.set(this.model, 'children', []);
                this.addChild();
                this.open = true;
            }
        },
        addChild: function () {
            this.model.children.push({
                name: 'new stuff'
            });
        }
    }
});
