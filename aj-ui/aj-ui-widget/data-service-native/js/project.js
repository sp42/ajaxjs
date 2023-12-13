project = {
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
        openTab(a, data) {
            this.currentData = data.data;
            this.editorData.type = 'info';
        },
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
                    aj.xhr.del(`${DS_CONFIG.API_ROOT}/admin/project/${this.project.form.data.id}`, j => {
                        if (j.status) {
                            this.$Message.info('删除成功');
                            this.loadTreeProejct();
                        }
                    });
                }
            });
        },

        loadTreeProejct() {
            aj.xhr.get(DS_CONFIG.API_ROOT + '/common_api/common_api/list?allow=1', j => {
                if (j.status == 1) {
                    let data = [];
                    // 添加 iView 树节点的字段
                    j.data.forEach(item => {
                        let node = {
                            title: item.name || item.namespace,
                            contextmenu: true,
                            data: item
                        };

                        if (item.children) {
                            node.expand = true;
                            node.children = [];

                            item.children.forEach(item2 => {
                                node.children.push({
                                    title: item2.name || item2.namespace,
                                    data: item2
                                });
                            });
                        }

                        data.push(node);
                    });

                    this.project.treeData = data;
                }
            });
        },

        // 异步加载树数据
        loadTreeData(item, callback) {
            aj.xhr.get(DS_CONFIG.API_ROOT + '/admin?projectId=' + item.id, j => {
                if (j.status == 1) {
                    getTreeData2.call(this, j, callback);
                }
            });
        },

        saveProject() {
            debugger;

            if (this.project.form.data.id) { // 编辑

            } else {

            }
        }
    }

};