"use strict";
Vue.component('aj-tree-like-select', {
    template: '<select :name="fieldName" class="aj-select" @change="onSelected" style="min-width:180px;"></select>',
    props: {
        fieldName: { type: String, required: false, default: 'catalogId' },
        apiUrl: { type: String, default: '/admin/tree-like/' },
        isAutoLoad: { type: Boolean, default: true },
        isAutoJump: Boolean,
        initFieldValue: String
    },
    data: function () {
        return {
            fieldValue: this.initFieldValue
        };
    },
    mounted: function () {
        this.isAutoLoad && this.getData();
    },
    methods: {
        onSelected: function (ev) {
            var el = ev.target;
            this.fieldValue = el.selectedOptions[0].value;
            if (this.isAutoJump)
                location.assign('?' + this.fieldName + '=' + this.fieldValue);
            else
                this.BUS && this.BUS.$emit('aj-tree-catelog-select-change', ev, this);
        },
        getData: function () {
            var _this = this;
            var fn = function (j) {
                var arr = [{ id: 0, name: "请选择分类" }];
                aj.list.tree.rendererOption(arr.concat(j.result), _this.$el, _this.fieldValue, { makeAllOption: false });
                if (_this.fieldValue) // 有指定的选中值
                    aj.form.utils.selectOption.call(_this, _this.fieldValue);
            };
            // aj.xhr.get(this.ajResources.ctx + this.apiUrl + "/admin/tree-like/getListAndSubByParentId/", fn);
            aj.xhr.get(this.apiUrl, fn);
        }
    }
});
