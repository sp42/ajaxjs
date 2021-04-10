"use strict";
var aj;
(function (aj) {
    var user;
    (function (user) {
        var admin;
        (function (admin) {
            var BAR = new Vue({
                el: '.user-list',
                //     data: {
                //         list: [],
                //         catalogId: 0,
                //         付款暂停: 0
                //     },
                mounted: function () {
                    // this.$refs.pager.$on("pager-result", result => {
                    //     this.list = result;
                    //     this.maxRows = result.length;
                    // });
                    // this.$refs.pager.get();
                },
                methods: {
                    gridCols: function () {
                        return ['id', avatar, 'name', 'username', sex, telOrMail, date, group, 'stat'];
                    },
                }
            });
            function sex(data) {
                switch (data['sex']) {
                    case 1:
                        return '男';
                    case 2:
                        return '女';
                    default:
                        return '未知';
                }
            }
            function telOrMail(data) {
                var email = data['email'], phone = data['phone'];
                if (email && phone)
                    return email + '<br /> ' + phone;
                return email || phone;
            }
            var avatar = {
                cmpName: "aj-avatar",
                editMode: false,
                cmpProps: function (data) {
                    var avatar = data.avatar, prefix = ''; //'${aj_allConfig.uploadFile.imgPerfix}'
                    avatar = "https://static001.geekbang.org/account/avatar/00/10/10/51/9fedfd70.jpg?x-oss-process=image/resize,w_200,h_200";
                    if (!avatar)
                        return { avatar: "" };
                    if (avatar.indexOf('http') === -1)
                        avatar = prefix + avatar;
                    return { avatar: avatar };
                }
            };
            var date = function (data) { return new Date(data.createDate).format("yyyy-MM-dd hh:mm"); };
            function group(data) {
                if (!data['roleId'])
                    return "";
                // @ts-ignore
                var role = window.UserGroupsJson[data['roleId']];
                if (!role)
                    return "";
                return role.name;
            }
            aj.widget.img.initImageEnlarger(); // 鼠标移动大图
            admin.USER_GROUP = new Vue({
                el: '.user-group',
                // mixins: [aj.treeLike],
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
                    this.load();
                    //         xhr.form(this.$refs.createOrUpdate.$el.$('form'), j => {
                    //             j && aj.msg.show(j.msg);
                    //             this.load();
                    //         });
                },
                methods: {
                    load: function () {
                        this.list = [];
                        // xhr.get('../user_group/list/', (j: RepsonseResult) => {
                        //     if (j.errorMsg) {
                        //         aj.modal(null, { text: j.errorMsg });
                        //         return;
                        //     }
                        //     let tree = this.toTree(j.result);
                        //     this.output(tree, (node, nodeId) => {
                        //         let level = node.level - 1;
                        //         node.indentName = new Array(level * 8).join('&nbsp;') + (level == 0 ? '' : '└─') + node.name;
                        //         this.list.push(node);
                        //     });
                        // });
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
            // 功能权限控件
            Vue.component("aj-admin-role-check-right", {
                template: '#aj-admin-role-check-right',
                props: {
                    resId: Number,
                    setRightValue: Number,
                },
                data: function () {
                    return {
                        enabled: true,
                        rightValue: this.setRightValue,
                        allowRead: (this.setRightValue & 1) === 1,
                        allowCreate: (this.setRightValue & 2) === 2,
                        allowUpdate: (this.setRightValue & 4) === 4,
                        allowDelete: (this.setRightValue & 8) === 8
                    };
                },
                mounted: function () {
                    var _this = this;
                    /* 可以通过 props 单向绑定 resRightValue，但每个组件要设置一样属性。这里避免多处重复设置属性  */
                    this.$watch('$parent.$parent.resRightValue', function (v) { return _this.enabled = _this.check(v, _this.resId); });
                },
                methods: {
                    /**
                     * 检查是否有权限
                     
                     * @return {Boolean} true =  有权限，反之无
                     */
                    check: function (num, pos) {
                        // console.log(num)
                        num = num >>> pos;
                        return (num & 1) === 1;
                    },
                    toggleRight: function (val, right) {
                        if (val === false && ((this.rightValue & right) === right)) // 有权限
                            this.rightValue -= right;
                        if (val === true && ((this.rightValue & right) !== right))
                            this.rightValue += right;
                    },
                    userEnableClick: function (ev) {
                        var isEnable = ev.target.checked, userGroupId = admin.ASSIGN_RIGHT.userGroupId; // 全局变量
                        if (userGroupId && this.resId) {
                            aj.xhr.post('../user_group/updateResourceRightValue', function (j) { return aj.msg.show(j.msg); }, {
                                userGroupId: userGroupId,
                                isEnable: isEnable,
                                resId: this.resId
                            });
                        }
                    },
                    crudClick: function () { }
                },
                watch: {
                    enabled: function () {
                        var _this = this;
                        // @ts-ignore
                        this.$el.$('.crud input', function (input) { return input.disabled = !_this.enabled; });
                    },
                    allowRead: function (val) {
                        this.toggleRight(val, 1);
                    },
                    allowCreate: function (val) {
                        this.toggleRight(val, 2);
                    },
                    allowUpdate: function (val) {
                        this.toggleRight(val, 4);
                    },
                    allowDelete: function (val) {
                        this.toggleRight(val, 8);
                    }
                }
            });
            admin.ASSIGN_RIGHT = new Vue({
                el: '.assign-right',
                data: {
                    userGroupId: null,
                    resRightValue: 0,
                    currentUserGroup: '' // UI 提示用
                },
                mounted: function () {
                    //         // 点击树节点时候，加载用户组的详情信息
                    //         this.BUS.$on('tree-node-click', (data) => {
                    //             if (data.id) {
                    //                 this.userGroupId = data.id;
                    //                 this.currentUserGroup = data.name;
                    //                 xhr.get('../user_group/' + data.id + '/', j => this.resRightValue = j.result.accessKey || 0);
                    //             }
                    //         });
                }
            });
            // USER_GROUP.$refs.layer.show();
            // ASSIGN_RIGHT.$refs.assignRight.show();
            //BAR.$refs.createUI.show();
            //BAR.$refs.form.load(1);
        })(admin = user.admin || (user.admin = {}));
    })(user = aj.user || (aj.user = {}));
})(aj || (aj = {}));
