"use strict";
var aj;
(function (aj) {
    var user;
    (function (user) {
        var admin;
        (function (admin) {
            admin.MAIN_LIST = new Vue({
                el: '.user-list',
                //     data: {
                //         list: [],
                //         catalogId: 0,
                //         付款暂停: 0
                //     },
                mounted: function () {
                    var _this = this;
                    // 分类筛选
                    var select = this.$el.$('select[name=roleId]');
                    // @ts-ignore
                    aj.tree.rendererOption(window.UserGroups, select, "", { makeAllOption: true, allOptionName: "用户组" });
                    select.onchange = function () {
                        if (select.selectedIndex === 0)
                            _this.$refs.store.extraParam = {};
                        else {
                            var value = select.options[select.selectedIndex].value;
                            _this.$refs.store.extraParam = {
                                roleId: value
                            };
                        }
                    };
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
                var role = window.UserGroups_IdAsKey[data['roleId']];
                if (!role)
                    return "";
                return role.name;
            }
            aj.widget.img.initImageEnlarger(); // 鼠标移动大图
        })(admin = user.admin || (user.admin = {}));
    })(user = aj.user || (aj.user = {}));
})(aj || (aj = {}));
