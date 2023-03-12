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
            aj.xhr.get(DS_CONFIG.API_ROOT + '/admin/project', j => {
                // console.log(j)
                if (j.status == 1) {
                    // 添加 iView 树节点的字段
                    j.data.forEach(item => {
                        item.title = item.name;
                        item.loading = false;
                        item.contextmenu = true;
                        item.children = [];
                    });

                    this.project.treeData = j.data;
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