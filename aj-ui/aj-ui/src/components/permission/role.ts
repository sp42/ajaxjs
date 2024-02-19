import { xhr_get, xhr_post, xhr_put, xhr_del } from '../../util/xhr';

import List from '../widget/list';

export default {
    data(): {} {
        return {
            contextData: null,
            roleTreeData: [],
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

            this.roleForm.isTop = this.contextData.parentId == -1;

            xhr_get(`${this.simpleApi}/role/${this.contextData.id}`, (j: JsonResponse) => {
                if (j.status) {
                    this.currentRole = j.data;
                } else
                    this.$Message.warning(j.message || '获取数据失败');
            });
        },
        createTopRoleNode(): void {
            this.roleForm.isTop = true;
            this.roleForm.isCreate = true;
            this.currentRole = {};
            this.contextData = { id: -1 };
            this.isShisShowRoleEditForm = true;
        },
        delRole(): void {
            let treeNodeName: string = this.contextData.title;

            this.$Modal.confirm({
                title: '删除角色',
                content: `<p>确定删除 ${treeNodeName} 这个节点吗？<br />注意：该节点下<b>所有的子节点</b>也会一并被删除！</p>`,
                onOk: () => {
                    xhr_del(`${this.permissionApi}/role/${this.contextData.id}`, (j: RepsonseResult) => {
                        if (j.status) {
                            this.$Message.success('删除成功');
                            this.refreshRoleList();
                        }
                    });
                }
            });
        },
        addSubNode(): void {
            this.roleForm.isTop = false;
            this.roleForm.isCreate = true;
            this.currentRole = {};
            this.isShisShowRoleEditForm = true;
        },
        refreshRoleList(): void {
            xhr_get(`${this.permissionApi}/role_tree`, (j: JsonResponse) => {
                if (j.status) {
                    this.roleTreeData = j.data;
                } else
                    this.$Message.warning(j.message || '获取数据失败');
            });
        },

        saveRole(): void {
            let data: any = List.copyBeanClean(this.currentRole);
            data.parentId = this.contextData.id;

            if (this.roleForm.isCreate) {
                xhr_post(`${this.simpleApi}/role`, (j: RepsonseResult) => {
                    if (j.status) {
                        this.$Message.success('创建成功');
                        this.refreshRoleList();
                    }
                }, data);
            } else {
                xhr_put(`${this.simpleApi}/role/${data.id}`, (j: RepsonseResult) => {
                    if (j.status) {
                        this.$Message.success('修改成功');
                        this.refreshRoleList();
                    }
                }, data);
            }
        },

        onTreeNodeClk(nodeArr: any[], node: any): void {
            // debugger
            this.currentRole = { name: node.title, id: node.id };
        }
    }
}