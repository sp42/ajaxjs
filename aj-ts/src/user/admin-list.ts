namespace aj.user.admin {
    let BAR = new Vue({
        el: '.user-list',
        //     data: {
        //         list: [],
        //         catalogId: 0,
        //         付款暂停: 0
        //     },
        mounted(): void {
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

            // onCatalogChange(v) {
            //     alert(v)
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

    function avatar(data: JsonParam): string {
        let prefix = '',//'${aj_allConfig.uploadFile.imgPerfix}';
            avatar = <string>data.avatar;

        if (!avatar)
            return "";

        if (avatar.indexOf('http') === -1)
            avatar = prefix + avatar;

        return '<aj-avatar avatar="' + avatar + '"></aj-avatar>';
    }

    let date = (data: any) => new Date(data.createDate).format("yyyy-MM-dd hh:mm");

    function group(data: JsonParam) {
        if (!data['roleId'])
            return "";

        // @ts-ignore
        let role = window.UserGroupsJson[data['roleId']];
        if (!role)
            return "";

        return role.name;
    }

    // aj.widget.imageEnlarger();// 鼠标移动大图
    interface USER_GROUP {
        list: [];
        createOrUpdate: any;
        $refs: any;
        load(): void;
    }

    export let USER_GROUP = new Vue({
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
        mounted(this: USER_GROUP): void {
            this.load();
            //         xhr.form(this.$refs.createOrUpdate.$el.$('form'), j => {
            //             j && aj.msg.show(j.msg);
            //             this.load();
            //         });
        },
        methods: {
            load(this: USER_GROUP): void {
                this.list = [];
                xhr.get('../user_group/list/', (j: RepsonseResult) => {
                    if (j.errorMsg) {
                        aj.modal(null, { text: j.errorMsg });
                        return;
                    }

                    let tree = this.toTree(j.result);

                    this.output(tree, (node, nodeId) => {
                        let level = node.level - 1;
                        node.indentName = new Array(level * 8).join('&nbsp;') + (level == 0 ? '' : '└─') + node.name;

                        this.list.push(node);
                    });
                });
            },
            mofidly(this: USER_GROUP, id: string, name: string, content: string): void {
                this.createOrUpdate.isCreate = false;
                this.createOrUpdate.id = id;
                this.createOrUpdate.name = name;
                this.createOrUpdate.content = content;
                this.createOrUpdate.action = '../user_group/' + id + '/';
                this.$refs.createOrUpdate.show();
            },
            onCreateBtnClk(this: USER_GROUP, pid: number): void {
                this.createOrUpdate.isCreate = true;
                this.createOrUpdate.id = pid;
                this.createOrUpdate.name = this.createOrUpdate.content = null;
                this.createOrUpdate.action = '../user_group/';
                this.$refs.createOrUpdate.show();
            },
            dele(this: USER_GROUP, id: string, title: string): void {
                showConfirm(`请确定删除用户组【${title}】？`, () =>
                    xhr.dele(`../user_group/${id}/`, (j: RepsonseResult) => {
                        if (j.isOk) {
                            msg.show('删除成功！');
                            this.load();
                            //setTimeout(() => location.reload(), 1500);
                        } else
                            aj.alert('删除失败！');
                    })
                );
            }
        }
    });

    interface RoleCheckRight {
        resId: number;
        rightValue: number;
        toggleRight(val: boolean, right: number): void;
    }

    // 功能权限控件
    Vue.component("aj-admin-role-check-right", {
        template: '#aj-admin-role-check-right',
        props: {
            resId: Number, 			// 资源权限值
            setRightValue: Number,	// 操作权限值， 8421码
        },
        data() {
            return {
                enabled: true,
                rightValue: this.setRightValue,
                allowRead: (this.setRightValue & 1) === 1,
                allowCreate: (this.setRightValue & 2) === 2,
                allowUpdate: (this.setRightValue & 4) === 4,
                allowDelete: (this.setRightValue & 8) === 8
            };
        },
        mounted(): void {
            /* 可以通过 props 单向绑定 resRightValue，但每个组件要设置一样属性。这里避免多处重复设置属性  */
            this.$watch('$parent.$parent.resRightValue', (v) => this.enabled = this.check(v, this.resId));
        },
        methods: {
            /**
             * 检查是否有权限
             
             * @return {Boolean} true =  有权限，反之无
             */
            check(num: number, pos: number): boolean {
                // console.log(num)
                num = num >>> pos;
                return (num & 1) === 1;
            },
            toggleRight(this: RoleCheckRight, val: boolean, right: number): void {
                if (val === false && ((this.rightValue & right) === right)) // 有权限
                    this.rightValue -= right;

                if (val === true && ((this.rightValue & right) !== right))
                    this.rightValue += right;
            },
            userEnableClick(this: RoleCheckRight, ev: Event): void { // 用户点击事件，不是来自数据的变化，修改立刻被保存到服务端
                let isEnable = (<HTMLInputElement>ev.target).checked,
                    userGroupId = ASSIGN_RIGHT.userGroupId; // 全局变量

                if (userGroupId && this.resId) {
                    xhr.post('../user_group/updateResourceRightValue', j => msg.show(j.msg), {
                        userGroupId: userGroupId,
                        isEnable: isEnable,
                        resId: this.resId
                    });
                }
            },
            crudClick() { }
        },
        watch: {
            enabled(): void {
                // @ts-ignore
                this.$el.$('.crud input', input => input.disabled = !this.enabled);
            },
            allowRead(this: RoleCheckRight, val: boolean): void {
                this.toggleRight(val, 1);
            },
            allowCreate(this: RoleCheckRight, val: boolean): void {
                this.toggleRight(val, 2);
            },
            allowUpdate(this: RoleCheckRight, val: boolean): void {
                this.toggleRight(val, 4);
            },
            allowDelete(this: RoleCheckRight, val: boolean): void {
                this.toggleRight(val, 8);
            }
        }
    });

    export let ASSIGN_RIGHT = new Vue({
        el: '.assign-right',
        data: {
            userGroupId: null,		// 用户组 id
            resRightValue: 0,		// 用户组权限总值
            currentUserGroup: ''	// UI 提示用
        },
        mounted(): void {
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
    ASSIGN_RIGHT.$refs.assignRight.show();
    //BAR.$refs.createUI.show();
    //BAR.$refs.form.load(1);
}