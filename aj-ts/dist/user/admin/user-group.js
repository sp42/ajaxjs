"use strict";
var aj;
(function (aj) {
    var user;
    (function (user) {
        var admin;
        (function (admin) {
            admin.USER_GROUP = new Vue({
                el: '.user-group',
                data: {
                    list: [],
                    createOrUpdate: {
                        isCreate: true,
                        id: null,
                        name: null,
                        content: null,
                        action: null
                    }
                },
                mounted: function () {
                    var _this = this;
                    this.load();
                    aj.xhr.form(this.$refs.createOrUpdate.$el.$('form'), function (j) {
                        j && aj.msg.show(j.msg);
                        _this.load();
                    });
                },
                methods: {
                    load: function () {
                        var _this = this;
                        this.list = [];
                        aj.xhr.get('../user_group/list/', function (j) {
                            var tree = aj.tree.toTreeMap(j.result);
                            aj.tree.output(tree, function (node, nodeId) {
                                var level = node.level - 1;
                                node.indentName = new Array(level * 8).join('&nbsp;') + (level == 0 ? '' : '└─') + node.name;
                                _this.list.push(node);
                            });
                        });
                    },
                    mofidly: function (id, name, content) {
                        this.createOrUpdate.isCreate = false;
                        this.createOrUpdate.id = id;
                        this.createOrUpdate.name = name;
                        this.createOrUpdate.content = content;
                        this.createOrUpdate.action = '../user_group/' + id + '/';
                        this.$refs.createOrUpdate.show();
                    },
                    onCreateBtnClk: function (pid) {
                        this.createOrUpdate.isCreate = true;
                        this.createOrUpdate.id = pid;
                        this.createOrUpdate.name = this.createOrUpdate.content = null;
                        this.createOrUpdate.action = '../user_group/';
                        this.$refs.createOrUpdate.show();
                    },
                    dele: function (id, title) {
                        var _this = this;
                        aj.showConfirm("\u8BF7\u786E\u5B9A\u5220\u9664\u7528\u6237\u7EC4\u3010" + title + "\u3011\uFF1F", function () {
                            return aj.xhr.dele("../user_group/" + id + "/", function (j) {
                                if (j.isOk) {
                                    aj.msg.show('删除成功！');
                                    _this.load();
                                    //setTimeout(() => location.reload(), 1500);
                                }
                                else
                                    aj.alert('删除失败！');
                            });
                        });
                    }
                }
            });
        })(admin = user.admin || (user.admin = {}));
    })(user = aj.user || (aj.user = {}));
})(aj || (aj = {}));
