myHTMLInclude();

Vue.use(VueCodemirror);
const addTabBtn = { label: "+", name: "addTab", closable: false };

Vue.component('tips', {
    template: `<i style="cursor: pointer;" class="ivu-icon ivu-icon-ios-help-circle-outline" :title="text"></i>`,
    props: {
        text: { type: String, required: true }
    }
});

new Vue({
    el: document.querySelector('.data-service'),
    data() {
        return {
            isShowSelectTable: false,
            split1: 0.2,
            dataSource: {
                isShowDataSource: false
            },
            allData: [], // 所有的数据
            currentData: {}, // 当前编辑的数据（总数据）
            currentDML: {}, // 当前编辑的数据（DML）
            currentType: '',
            contextData: null,
            treeData2: [],

            projectTreeData: [
                {
                    title: 'parent',
                    expand: true,
                    loading: false,
                    children: []
                }
            ],
            treeData: [
                {
                    title: 'parent 1',
                    expand: true,
                    children: [
                        {
                            title: 'parent 1-1',
                            expand: true,
                            children: [
                                {
                                    title: 'leaf 1-1-1'
                                },
                                {
                                    title: 'leaf 1-1-2'
                                }
                            ]
                        },
                        {
                            title: 'parent 1-2',
                            expand: true,
                            children: [
                                {
                                    title: 'leaf 1-2-1'
                                },
                                {
                                    title: 'leaf 1-2-1'
                                }
                            ]
                        }
                    ]
                }
            ],
            activeTab: "tab1",
            mainTabs: [
                {
                    label: "数据服务",
                    name: "tab1",
                    closable: true,
                }
            ],
            table: {
                createRule: {},
                fieldsMapping: {

                }
            },
            allDml: {
                create: {},
                update: {},
                create: {},
                delete: {}
            },
            showFields: false,
            fields: [],

            dmlSelected: null, // 命令选中高亮显示
            code: `SELECT * FROM user\n`,
            cmOption: {
                tabSize: 4,
                styleActiveLine: true,
                lineNumbers: true,
                mode: 'text/x-mysql',
                // theme: "monokai"
            },
        }
    },
    mounted() {
        // getTreeData.call(this);
        this.loadTreeProejct();
    },
    methods: {
        showAbout() {
            this.$Modal.confirm({
                title: '关于 DataService',
                content: '<p>DataService：用数据库管理 SQL 语句，快捷生成 API 接口</p><p>Powered by MyBatis + AJAXJS Framework.</p><p>ver 2022.10.31</p>',

            });
        },
        refreshConfig() {
            this.$Message.success('刷新成功');
        },
        ifAdd(name) {
            if (name === "addTab") {
                this.mainTabs.pop(); // 先删除，再增加

                let tabName = "tab" + (this.mainTabs.length + 1);
                this.mainTabs.push({
                    label: "新 API",
                    name: tabName,
                    closable: true,
                });

                setTimeout(() => {
                    this.mainTabs.push(addTabBtn);
                    this.activeTab = tabName;
                }, 100);
            }
        },

        loadTreeProejct() {
            aj.xhr.get('http://localhost:8080/adp/admin/data_service/project', j => {
                // console.log(j)
                if (j.status == 1) {
                    // 添加 iView 树节点的字段
                    j.data.forEach(item => {
                        item.title = item.name;
                        item.loading = false;
                        item.contextmenu = true;
                        item.children = [];
                    });

                    this.projectTreeData = j.data;
                }
            });
        },

        handleContextMenu(data) {
            this.contextData = data;
        },
        handleContextMenuEdit() {
            this.$Message.info('Click edit of' + this.contextData.title);
        },
        handleContextMenuDelete() {
            this.$Message.info('Click delete of' + this.contextData.title);
        },

        // 异步加载树数据
        loadTreeData(item, callback) {
            console.log('-------', item)
            aj.xhr.get('http://localhost:8080/adp/admin/data_service?projectId=' + item.id, j => {
                if (j.status == 1) {
                    getTreeData2.call(this, j, callback);
                }
            });
        },

        // 选择 DML
        selectItem(item, index, type) {
            this.code = item.sql;
            this.dmlSelected = index;
            this.currentDML = item;
            this.currentType = type;
        },

        togglePanel() {
            let config = this.$el.querySelector('.config');
            if (config.style.height == '500px') {
                config.style.height = '0';
            } else
                config.style.height = '500px';

        },
        getDml(item, key) {
            let method = 'UNKNOW', path = '/' + this.currentData.urlDir, id = this.currentData.id;

            if (key == 'create')
                method = '<span style="color:green;">POST</span>';
            else if (key == 'update')
                method = 'PUT';
            else if (key == 'list' || key == 'info')
                method = 'GET';
            else if (key == 'delete') {
                path += '/{id}'
                method = 'DELETE';
            } else if (key == 'others') {
                return null;
            }

            return method + ' ' + path;
        }
    }
});