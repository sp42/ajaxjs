namespace aj.user.admin {
    interface USER_GROUP {
        list: tree.TreeNode[];
        createOrUpdate: any;
        $refs: any;
        load(): void;
    }

    export let USER_GROUP = new Vue({
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
                    let tree: tree.TreeNode = <tree.TreeNode>aj.tree.toTreeMap(<aj.tree.FlatTreeNodeList>j.result);

                    aj.tree.output(tree, (node: tree.TreeNode, nodeId) => {
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
}