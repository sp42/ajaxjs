import { xhr_get, xhr_del } from '../../util/xhr';

export default {
    data() {
        return {
            project: {
                isShowEditProjectWin: false,
                treeData: [
                    {
                        title: 'parent',
                        expand: true,
                        loading: false,
                        children: []
                    }
                ],
                form: {
                    data: {
                        name: 'sds'
                    },
                    rules: {
                        name: [
                            { required: true, message: '项目名称不能为空', trigger: 'blur' }
                        ],
                    }
                }
            }
        };
    },
    methods: {
        handleContextMenuEdit() {
            this.project.isShowEditProjectWin = true;
        },

        handleContextMenuCreate() {
            this.handleContextMenuEdit();
            this.project.form.data = { name: '', content: '', id: null };
        },
        handleContextMenu(data) {
            this.project.form.data = data;
        },

        handleContextMenuDelete() {
            this.$Modal.confirm({
                title: '确定删除吗？',
                content: `<p>删除<b>工程 ${this.project.form.data.name}</b> 以及其所有的<b>命令</b>？</p>`,
                onOk: () => {
                    xhr_del(`${window.config.dsApiRoot}/admin/project/${this.project.form.data.id}`, j => {
                        if (j.status) {
                            this.$Message.info('删除成功');
                            this.loadTreeProejct();
                        }
                    });
                }
            });
        },

        loadTreeProejct() {
            xhr_get(`${window.config.IAM_ApiRoot}/common_api/common_api/list`, j => {
                if (j.status == 1) {
                    let data = [];
                    // 添加 iView 树节点的字段
                    j.data.forEach(item => {
                        let title = item.name || item.namespace;
                        let node = {
                            title: title,
                            contextmenu: true,
                            data: item,
                            id: item.namespace
                        };

                        if (item.children) {
                            node.expand = true;
                            node.children = [];

                            item.children.forEach(item2 => {
                                let title = item2.name || item2.namespace;
                                node.children.push({
                                    title: title,
                                    contextmenu: true,
                                    data: item2,
                                    id: item.namespace + '/' + item2.namespace
                                });
                            });
                        }

                        data.push(node);
                    });

                    this.treeData = data;
                }
            });
        },

        // 异步加载树数据
        loadTreeData(item, callback) {
            xhr_get(`${window.config.dsApiRoot}/admin?projectId=` + item.id, j => {
                if (j.status == 1) {
                    getTreeData2.call(this, j, callback);
                }
            });
        },

        saveProject() {
            if (this.project.form.data.id) { // 编辑

            } else {

            }
        }
    }
};