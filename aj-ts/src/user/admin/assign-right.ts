namespace aj.user.admin {
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
    // ASSIGN_RIGHT.$refs.assignRight.show();
    //BAR.$refs.createUI.show();
    //BAR.$refs.form.load(1);
}