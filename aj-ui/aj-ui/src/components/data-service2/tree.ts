import { xhr_get, xhr_del } from '../../util/xhr';
import { isDev } from '../../util/utils';

export default {
    data() {
        return {
            treeData: [],
            project: {
                name: '',
                parentServiceName: '', // 如果选择了父服务节点，显示其名字
                parentId: -1,
                parentNode: null,
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
                    //@ts-ignore
                    xhr_del(`${window.config.dsApiRoot}/admin/project/${this.project.form.data.id}`, (j: RepsonseResult) => {
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
        getSelectedProject(): string {
            let selectedNodes = this.$refs.treeCmp.getSelectedNodes();

            if (selectedNodes.length === 0) {
                this.$Message.warning('请先选择一个树节点，<br >选择一个项目或者一个服务。');
                return null;
            }

            let selectedNode = selectedNodes[0];
            let project = selectedNode.parentNode;
            this.project.parentServiceName = '';
            this.project.parentId = -1;

            if (project) {
                if (project.parentNode) { // sub node
                    this.project.parentServiceName = project.data.name;
                    this.project.parentId = project.data.id;
                    this.project.parentNode = project.data;
                    project = project.parentNode;
                } else { // level 1 node
                    this.project.parentServiceName = selectedNode.data.name;
                    this.project.parentId = selectedNode.data.id;
                    this.project.parentNode = selectedNode;
                }
            } else { // project node
                project = selectedNode.projectData;
                this.project.parentNode = project;
            }

            this.project.name = project.name;

            return isDev() ? project.apiPrefixDev : project.apiPrefixProd;
        },

        refreshTree() {
            this.loadTreeData(null, data => {
                this.treeData = data; // 更新根节点的数据
            });
        },

        // 异步加载树数据
        loadTreeData(item: null, callback: Function): void {
            // @ts-ignore
            xhr_get(`${window.config.dsApiRoot}/common_api/project/list`, (j: RepsonseResult) => {
                if (j.status) {
                    let data: DS_TreeNode_Project[] = [];

                    j.data.forEach(project => {
                        let projectTreeNode: DS_TreeNode_Project = {
                            title: project.name,
                            loading: false,
                            expand: true,
                            children: [],
                            contextmenu: true,
                            projectData: project,
                            render: renderProjectTreeNode
                        };

                        this.loadTreeProejct(isDev() ? project.apiPrefixDev : project.apiPrefixProd, projectTreeNode)

                        data.push(projectTreeNode);
                    });

                    callback(data);
                }
            });
        },

        /**
         * 加载服务列表
         * 
         * @param apiPrefix 
         * @param projectTreeNode 
         */
        loadTreeProejct(apiPrefix: string, projectTreeNode: DS_TreeNode_Project): void {
            xhr_get(`${apiPrefix}/common_api/common_api/list`, (j: RepsonseResult) => {
                if (j.status) {
                    let data: DS_TreeNode_Service[] = [];

                    // 添加 iView 树节点的字段
                    j.data.forEach((item) => {
                        let title: string = item.name || item.namespace;
                        let id: string = projectTreeNode.title + ':' + item.namespace;
                        let node: DS_TreeNode_Service = {
                            title: title,
                            contextmenu: true,
                            data: item,
                            id: id,
                            isCreate: false,
                            parentNode: projectTreeNode.projectData,
                            render: renderCrudTreeNode
                        };

                        if (item.children) {
                            node.expand = true;
                            node.children = [];

                            item.children.forEach(item2 => {
                                let title: string = item2.name || item2.namespace;

                                node.children.push({
                                    title: title,
                                    contextmenu: true,
                                    data: item2,
                                    id: id + '/' + item2.namespace,
                                    isCreate: false,
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
        saveProject() { }
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