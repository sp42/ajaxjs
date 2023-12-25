import { xhr_get, xhr_del } from '../../util/xhr';
import { isDev } from '../../util/utils';

export default {
    data() {
        return {
            treeData: [],
            project: {
                isShowEditProjectWin: false,
                treeData: [

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

    created() {
        this.refreshTree();
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

        /**
         * 获取当前树选中的工程
         */
        getSelectedProject() {
            let selectedNodes = this.$refs.treeCmp.getSelectedNodes();

            if (selectedNodes.length === 0) {
                this.$Message.warning('请先选择一个树节点');
                return null;
            }

            let selectedNode = selectedNodes[0];
            let project = selectedNode.parentNode;

            if (project) {
                if (project.parentNode)  // sub node
                    project = project.parentNode;
            } else  // project node
                project = selectedNode.projectData;

            return isDev() ? project.apiPrefixDev : project.apiPrefixProd;
        },

        refreshTree() {
            this.loadTreeData(null, data => {
                this.treeData = data; // 更新根节点的数据
            });
        },

        // 异步加载树数据
        loadTreeData(item, callback) {
            xhr_get(`${window.config.dsApiRoot}/common_api/project/list`, j => {
                if (j.status == 1) {
                    let data = [];

                    j.data.forEach(project => {
                        let projectTreeNode = {
                            title: project.name,
                            loading: false,
                            expand: true,
                            children: [],
                            contextmenu: true,
                            projectData: project,
                            render: renderProjectTreeNode
                        };

                        this.loadTreeProejct2(isDev() ? project.apiPrefixDev : project.apiPrefixProd, projectTreeNode)

                        data.push(projectTreeNode);
                    });

                    callback(data);
                }
            });
        },

        loadTreeProejct2(apiPrefix, projectTreeNode) {
            xhr_get(`${apiPrefix}/common_api/common_api/list`, j => {
                if (j.status == 1) {
                    let data = [];
                    // 添加 iView 树节点的字段
                    j.data.forEach(item => {
                        let title = item.name || item.namespace;
                        let id = projectTreeNode.title + ':' + item.namespace;
                        let node = {
                            title: title,
                            contextmenu: true,
                            data: item,
                            id: id,
                            parentNode: projectTreeNode.projectData,
                            render: renderCrudTreeNode
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
                                    id: id + '/' + item2.namespace,
                                    parentNode: node,
                                    render: renderCrudTreeNode
                                });
                            });
                        }

                        data.push(node);
                    });

                    projectTreeNode.children = data;
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

const renderProjectTreeNode = (h, { root, node, data }) => {
    return [
        h("span", { class: "http-method get" }, "P"),
        h("span", { style: 'font-weight:bold' }, data.title),
    ];
};

const renderCrudTreeNode = (h, { root, node, data }) => {
    if (data.data.type && 'SINGLE' === data.data.type)
        return [
            h("span", { class: "http-method put" }, 'S'),
            h("span", data.title),
        ];
    else
        return [
            h("span", { class: "http-method post" }, 'C'),
            h("span", data.title),
        ];
};