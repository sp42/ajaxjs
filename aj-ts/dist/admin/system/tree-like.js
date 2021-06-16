"use strict";
var aj;
(function (aj) {
    var admin;
    (function (admin) {
        var treeLike;
        (function (treeLike) {
            new Vue({
                el: '.tree-like',
                data: {
                    selectedId: 0,
                    selectedName: ''
                },
                mounted: function () {
                    var _this = this;
                    // 新增顶级分类
                    aj.xhr.form(".createTopNode", function (j) {
                        document.querySelector(".createTopNode input[name=name]").value = '';
                        _this.refresh(j);
                    }, { noFormValid: true });
                    // 添加子分类
                    aj.xhr.form(".createUnderNode", function (j) {
                        document.querySelector(".createUnderNode input[name=name]").value = '';
                        _this.refresh(j);
                    }, { noFormValid: true });
                    // 修改名称
                    aj.xhr.form(this.$el.$('.rename'), function (j) {
                        _this.$refs.layer.close();
                        _this.refresh(j);
                    });
                    this.render();
                },
                methods: {
                    onChange: function (ev) {
                        var selectEl = ev.target, option = selectEl.selectedOptions[0], id = option.value, 
                        // pid: string = <string>option.dataset['pid'],
                        name = option.innerHTML.replace(/&nbsp;|└─/g, '');
                        this.selectedId = Number(id);
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
                        aj.showConfirm("\u786E\u5B9A\u5220\u9664\u8BE5\u5206\u7C7B[" + this.selectedName + "]\uFF1F<br />[" + this.selectedName + "]\u4E0B\u6240\u6709\u7684\u5B50\u8282\u70B9\u4E5F\u4F1A\u968F\u7740\u4E00\u5E76\u5168\u90E8\u5220\u9664\u3002", function () { return aj.xhr.dele(_this.selectedId + "/", _this.refresh); });
                    },
                    refresh: function (j) {
                        if (j.isOk) {
                            aj.alert(j.msg);
                            this.render();
                        }
                        else
                            aj.alert(j.msg);
                    },
                    render: function () {
                        var select = this.$el.$('select');
                        select.innerHTML = '';
                        aj.xhr.get('json', function (j) { return aj.tree.rendererOption(j.result, select); });
                    }
                }
            });
        })(treeLike = admin.treeLike || (admin.treeLike = {}));
    })(admin = aj.admin || (aj.admin = {}));
})(aj || (aj = {}));
