namespace aj.user.admin {
    export let MAIN_LIST = new Vue({
        el: '.user-list',
        //     data: {
        //         list: [],
        //         catalogId: 0,
        //         付款暂停: 0
        //     },
        mounted(): void {
            // 分类筛选
            let select: HTMLSelectElement = this.$el.$('select[name=roleId]');

            // @ts-ignore
            tree.rendererOption(<tree.TreeNode[]>window.UserGroups, select, "", { makeAllOption: true, allOptionName: "用户组" });

            select.onchange = () => {
                if (select.selectedIndex === 0)
                    this.$refs.store.extraParam = {};
                else {
                    let value: string = select.options[select.selectedIndex].value;

                    this.$refs.store.extraParam = {
                        roleId: value
                    };
                }
            }

            // this.$refs.pager.$on("pager-result", result => {
            //     this.list = result;
            //     this.maxRows = result.length;
            // });

            // this.$refs.pager.get();
        },
        methods: {
            gridCols(): any[] {
                return ['id', avatar, 'name', 'username', sex, telOrMail, date, group, 'stat'];
            },

            // 编辑按钮事件
            // onEditClk(id) {
            //     location.assign('../' + id + '/');
            // },

            // onCreateClk() { }
        }
    });

    function sex(data: JsonParam): string {
        switch (data['sex']) {
            case 1:
                return '男';
            case 2:
                return '女';
            default:
                return '未知';
        }
    }

    function telOrMail(data: JsonParam) {
        let email = data['email'],
            phone = data['phone'];

        if (email && phone)
            return email + '<br /> ' + phone;

        return email || phone;
    }

    let avatar: CellRendererConfig = {
        cmpName: "aj-avatar",
        editMode: false,
        cmpProps(data: JsonParam): JsonParam {

            let avatar = <string>data.avatar,
                prefix = '';//'${aj_allConfig.uploadFile.imgPerfix}'

            avatar = "https://static001.geekbang.org/account/avatar/00/10/10/51/9fedfd70.jpg?x-oss-process=image/resize,w_200,h_200";

            if (!avatar)
                return { avatar: "" };

            if (avatar.indexOf('http') === -1)
                avatar = prefix + avatar;

            return { avatar };
        }
    };

    let date = (data: any) => new Date(data.createDate).format("yyyy-MM-dd hh:mm");

    function group(data: JsonParam) {
        if (!data['roleId'])
            return "";

        // @ts-ignore
        let role = window.UserGroups_IdAsKey[data['roleId']];
        if (!role)
            return "";

        return role.name;
    }

    aj.widget.img.initImageEnlarger();// 鼠标移动大图
}