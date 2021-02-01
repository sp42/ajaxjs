"use strict";
// namespace aj.user.admin {
//     BAR = new Vue({
//         el: '.user-list',
//         mixins: [aj.SectionModel],
//         data: {
//             list: [],
//             catalogId: 0,
//             付款暂停: 0
//         },
//         mounted() {
//             this.$refs.pager.$on("pager-result", result => {
//                 this.list = result;
//                 this.maxRows = result.length;
//             });
//             this.$refs.pager.get();
//         },
//         methods: {
//             gridCols: (() => {
//                 var sex = data => {
//                     switch (data['sex']) {
//                         case 1:
//                             return '男';
//                         case 2:
//                             return '女';
//                         default:
//                             return '未知';
//                     }
//                 },
//                     date = data => new Date(data.createDate).format("yyyy-MM-dd hh:mm"),
//                     telOrMail = data => {
//                         var email = data['email'], phone = data['phone'];
//                         if (email && phone)
//                             return email + '<br /> ' + phone;
//                         return email || phone;
//                     };
//                 var UserGroupsJson = '${ UserGroupsJson }', group = data => {
//                     if (!data['roleId'])
//                         return "";
//                     var role = UserGroupsJson[data['roleId']];
//                     if (!role)
//                         return "";
//                     return role.name;
//                 }
//                 var avatar = data => {
//                     var prefix = ''//'${aj_allConfig.uploadFile.imgPerfix}';
//                     var avatar = data.avatar;
//                     if (!avatar)
//                         return "";
//                     if (avatar.indexOf('http') === -1)
//                         avatar = prefix + avatar;
//                     return '<aj-avatar avatar="' + avatar + '"></aj-avatar>';
//                 }
//                 return () => {
//                     return ['id', avatar, 'name', 'username', sex, telOrMail, date, group, 'stat'];
//                 };
//             })(),
//             // 编辑按钮事件
//             onEditClk(id) {
//                 location.assign('../' + id + '/');
//             },
//             onCatalogChange(v) {
//                 alert(v)
//             },
//             onCreateClk() { }
//         }
//     });
//     // USER_GROUP.$refs.layer.show();
//     //BAR.$refs.createUI.show();
//     //BAR.$refs.form.load(1);
//     aj.widget.imageEnlarger();// 鼠标移动大图
// }

"use strict";
;
(function () {
    var WeiboOauthLoginInstance;
    Vue.component('aj-oauth-login', {
        template: '<a href="###" @click="loginWeibo"><img src="https://www.sinaimg.cn/blog/developer/wiki/240.png" /></a>',
        props: {
            clientId: { required: true, type: String },
            redirectUri: { required: true, type: String } // 回调地址
        },
        data: function () {
            return {
                weiboAuthWin: null,
                result: ''
            };
        },
        mounted: function () {
            WeiboOauthLoginInstance = this;
        },
        methods: {
            /**
             *
             * @param this
             */
            loginWeibo: function () {
                var url = 'https://api.weibo.com/oauth2/authorize?client_id=' + this.clientId;
                url += '&response_type=code&state=register&redirect_uri=';
                url += this.redirectUri;
                this.weiboAuthWin = window.open(url, '微博授权登录', 'width=770,height=600,menubar=0,scrollbars=1,resizable=1,status=1,titlebar=0,toolbar=0,location=1');
            },
            /**
             * 关闭窗口
             * @param this
             */
            closeWin: function () {
                if (this.result) {
                    console.log(this.result);
                    this.weiboAuthWin.close();
                }
                else {
                    console.log("请求返回值为空");
                }
            }
        }
    });
})();

"use strict";
// Vue.component('aj-tree-user-role-select', {
//     mixins: [aj.treeLike],
//     template: '<select name="roleId" class="aj-select"></select>',
//     props: {
//         value: { type: Number, required: false },// 请求远端的分类 id，必填
//         json: Array,
//         noJump: { type: Boolean, defualt: false }// 是否自动跳转
//     },
//     mounted() {
//         this.rendererOption(this.json, this.$el, this.value, { makeAllOption: false });
//         if (!this.noJump)
//             this.$el.onchange = () => location.assign("?roleId=" + this.$el.options[this.$el.selectedIndex].value);
//     }
// });
