"use strict";
new Vue({
    el: '.tree-like',
    data: {
        selectedId: 0,
        selectedName: ''
    },
    mounted: function () {
        var _this = this;
        // 新增顶级${uiName}
        aj.xhr.form(".createTopNode", function (json) {
            document.querySelector(".createTopNode input[name=name]").value = '';
            _this.refresh(json);
        });
        // 在${uiName}下添加子${uiName}
        aj.xhr.form(".createUnderNode", function (json) {
            document.querySelector(".createUnderNode input[name=name]").value = '';
            _this.refresh(json);
        });
        // 修改名称
        aj.xhr.form(this.$el.$('.rename'), function (json) {
            _this.$refs.layer.close();
            _this.refresh(json);
        });
        this.render();
    },
    methods: {
        onChange: function (ev) {
            var selectEl = ev.target, option = selectEl.selectedOptions[0], id = option.value, pid = option.dataset['pid'], name = option.innerHTML.replace(/&nbsp;|└─/g, '');
            this.selectedId = id;
            this.selectedName = name;
        },
        rename: function () {
            if (!this.selectedId) {
                aj.alert('未选择任何分类');
                return;
            }
            this.$refs.layer.show();
        },
        // 删除
        dele: function () {
            var _this = this;
            if (!this.selectedId) {
                aj.alert('未选择任何分类');
                return;
            }
            aj.showConfirm('确定删除该${uiName}[{0}]？<br />[{0}]下所有的子节点也会随着一并全部删除。'.replace(/\{0\}/g, this.selectedName), function () { return aj.xhr.dele("" + _this.selectedId + "/", _this.refresh); });
        },
        refresh: function (json) {
            if (json.isOk) {
                aj.alert(json.msg);
                this.render();
            }
            else
                aj.alert(json.msg);
        },
        render: function () {
            var select = this.$el.$('select');
            select.innerHTML = '';
            aj.xhr.get('.', function (j) { return aj.list.tree.rendererOption(j.result, select); });
        }
    }
});
