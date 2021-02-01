
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