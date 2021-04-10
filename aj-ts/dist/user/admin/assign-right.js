"use strict";
var aj;
(function (aj) {
    var user;
    (function (user) {
        var admin;
        (function (admin) {
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
