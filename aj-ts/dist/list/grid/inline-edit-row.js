"use strict";
/**
    总体就是一个普通的 HTML Table，程序是通过读取 JSON 数据（实际 JS 数组）里面的字段，
    然后通过动态添加表格的方法逐行添加 tr，td，在添加行的过程中根据需要设置样式，添加方法等。
    每一行数据（即 tr 的 DOM 对象）都有 id 属性，其值就等于数据中的idColumn对应的列的值
 */
Vue.component('aj-grid-inline-edit-row', {
    template: "\n        <tr class=\"aj-grid-inline-edit-row\" :class=\"{editing: isEditMode}\">\n            <td v-if=\"showCheckboxCol\" class=\"selectCheckbox\">\n                <input type=\"checkbox\" @change=\"selectCheckboxChange\" :data-id=\"id\" />\n            </td>\n            <td v-if=\"showIdCol\">{{id}}</td>\n            <td v-for=\"key in columns\" :style=\"styleModifly\" class=\"cell\" @dblclick=\"dbEdit\">\n                <aj-cell-renderer v-if=\"!isEditMode\" :html=\"renderCell(data, key)\" :form=\"data\"></aj-cell-renderer>\n                <aj-cell-renderer v-if=\"isEditMode && key && key.editMode\" :html=\"rendererEditMode(data, key)\" :form=\"data\"></aj-cell-renderer>\n                <input type=\"text\" v-if=\"canEdit(key)\" size=\"0\" v-model=\"data[key]\" /> \n            </td>\n            <td v-if=\"showControl\" class=\"control\">\n                <aj-cell-renderer v-if=\"controlUi\" :html=\"controlUi\" :form=\"data\"></aj-cell-renderer>\n                <span @click=\"onEditClk\"><img :src=\"ajResources.commonAsset + '/icon/update.gif'\" /> \n                {{!isEditMode ? \"\u7F16\u8F91\" : \"\u786E\u5B9A\"}}</span>\n                <span @click=\"dele(id)\"><img :src=\"ajResources.commonAsset + '/icon/delete.gif'\" /> \u5220\u9664</span> \n            </td>\n        </tr>    \n    ",
    props: {
        rowData: { type: Object, required: true },
        showIdCol: { type: Boolean, default: true },
        columns: Array,
        showCheckboxCol: { type: Boolean, default: true },
        showControl: { type: Boolean, default: true },
        filterField: Array,
        enableInlineEdit: { type: Boolean, default: false },
        deleApi: String,
        controlUi: String // 自定义“操作”按钮，这里填组件的名字
    },
    data: function () {
        return {
            id: this.rowData.id,
            data: this.rowData,
            isEditMode: false
        };
    },
    mounted: function () {
        for (var i in this.data) // 监视每个字段
            this.$watch('data.' + i, this.makeWatch(i));
    },
    computed: {
        filterData: function () {
            var data = JSON.parse(JSON.stringify(this.data)); // 剔除不要的字段
            delete data.id;
            delete data.dirty;
            if (this.filterField && this.filterField.length)
                this.filterField.forEach(function (i) { return delete data[i]; });
            return data;
        },
        styleModifly: function () {
            return {
                padding: this.isEditMode ? 0 : '',
            };
        }
    },
    methods: {
        // 是否固定的字段，不能被编辑
        isFixedField: function (field) {
            if (this.filterField && this.filterField.length) {
                for (var i = 0, j = this.filterField.length; i < j; i++) {
                    if (this.filterField[i] == field)
                        return true;
                }
            }
            return false;
        },
        // 没有指定编辑器的情况下，使用 input 作为编辑器
        canEdit: function (key) {
            return this.isEditMode && !this.isFixedField(key) && !key.editMode;
        },
        renderCell: function (data, key) {
            var v;
            if (typeof key == 'function') {
                v = key(data);
                return v;
            }
            if (key === '')
                return '';
            if (typeof key == 'object' && key.showMode) {
                if (typeof key.showMode === 'function')
                    return key.showMode(data);
                if (typeof key.showMode === 'string')
                    return data[key.showMode];
            }
            v = data[key];
            return (v === null ? '' : v) + '';
        },
        // 编辑按钮事件
        onEditClk: function () {
            if (this.enableInlineEdit)
                this.isEditMode = !this.isEditMode;
            else if (this.$parent.onEditClk)
                this.$parent.onEditClk(this.id);
        },
        rendererEditMode: function (data, cfg) {
            if (typeof cfg === 'string')
                return cfg;
            if (cfg.editMode && typeof cfg.editMode === 'function')
                return cfg.editMode(data);
            return "NULL";
        },
        makeWatch: function (field) {
            return function (_new) {
                var arr = this.$parent.list;
                var data;
                for (var i = 0, j = arr.length; i < j; i++) {
                    if (this.id && (arr[i].id === this.id)) {
                        data = arr[i];
                        break;
                    }
                }
                if (!data)
                    throw '找不到匹配的实体！目标 id: ' + this.id;
                if (!data.dirty)
                    data.dirty = {};
                data.dirty[field] = _new; // 保存新的值，key 是字段名
            };
        },
        /**
         *
         * @param ev
         */
        dbEdit: function (ev) {
            if (!this.enableInlineEdit)
                return;
            this.isEditMode = !this.isEditMode;
            if (this.isEditMode) {
                var el_1 = ev.target;
                setTimeout(function () {
                    if (el_1.tagName !== 'INPUT')
                        el_1 = el_1.$('input');
                    el_1 && el_1.focus();
                }, 200);
            }
        },
        /**
         *
         * @param this
         * @param ev
         */
        selectCheckboxChange: function (ev) {
            var checkbox = ev.target, parent = this.$parent;
            if (checkbox.checked)
                parent.$set(parent.selected, this.id, true);
            //this.$parent.selected[this.id] = true;
            else
                parent.$set(parent.selected, this.id, false);
        },
        /**
         * 删除记录
         *
         * @param this
         * @param id
         */
        dele: function (id) {
            var _this = this;
            aj.showConfirm("\u786E\u5B9A\u5220\u9664\u8BB0\u5F55id:[" + id + "]", function () {
                return aj.xhr.dele(_this.deleApi + '/' + id + '/', function (j) {
                    if (j.isOk) {
                        aj.msg.show('删除成功');
                        //@ts-ignore
                        _this.$parent.reload();
                    }
                });
            });
        }
    }
});
