export default {
    data(): {} {
        return {
            contextData: null,
            roleForm: {
                isTop: false,
                isCreate: false
            }
        };
    },
    methods: {
        handleContextMenu(data: any): void {
            this.contextData = data;
        },
        editRole(): void {
            this.roleForm.isCreate = false;
            this.isShisShowRoleEditForm = true;
        },
        createTopRoleNode(): void {
            this.roleForm.isTop = true;
            this.roleForm.isCreate = true;
            this.isShisShowRoleEditForm = true;
        },
        delRole(): void {
            let treeNodeName: string = this.contextData.title;
            this.$Modal.confirm({
                title: '删除角色',
                content: `<p>确定删除 ${treeNodeName} 这个节点吗？<br />注意：该节点下<b>所有的子节点</b>也会一并被删除！</p>`,
                onOk: () => {
                    // xhr_del(`${prefix}/common_api/common_api/${current.data.id}/`, (j: RepsonseResult) => {
                    //     if (j.status) {
                    //         this.$Message.success('删除成功');
                    //         this.onTabClose(this.activeTab);
                    //         this.activeTab = 'index';
                    //         this.activeTabData = null;
                    //         this.refreshTree();
                    //     }
                    // });
                }
            });
        },
        addSubNode(): void {
            this.roleForm.isTop = false;
            this.roleForm.isCreate = true;
            this.isShisShowRoleEditForm = true;
        },
        refreshRoleList(): void {

        },

        saveRole(): void { },

        onTreeNodeClk(nodeArr: any[], node: any): void {
            // debugger
            this.currentRole = { name: node.title };
        }
    }
}