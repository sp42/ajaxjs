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
            isShowSelectTable: true,
            split1: 0.2,
            dataSource: {
                isShowDataSource: false
            },
            treeData2: [],
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
        this.getTreeData();
    },
    methods: {
        getTreeData() {
            let arr = [{
                title: '文章',
                children: [
                    {
                        method: 'GET',
                        path: "/foo/bar"
                    }
                    // {
                    //     title: "leaf 1-1-2",
                    //     render: (h, { root, node, data }) => {
                    //         return [
                    //             h("span", { class: "http-method get" }, "GET"),
                    //             h("span", data.title),
                    //         ];
                    //     },
                    // },
                ]
            }];

            this.treeData = [];

            arr.forEach(node => {
                let title = node.title, _children = node.children;

                let children = this.getTreeNode(_children);

                this.treeData.push({
                    title: title,
                    children: children
                });

                console.log(children)
            });
        },
        getTreeNode(children) {
            let arr = [];
            children.forEach(({ method, path }) => {

                arr.push({
                    title: path,
                    render: (h, { root, node, data }) => {
                        let nodeCfg = { // 节点事件的配置
                            on: {
                                // click: () => this.appendApiDetail(data) 
                            }// 点击加载接口详情
                        };

                        // 右键点击事件
                        nodeCfg.on['contextmenu'] = (e) => {
                            e.preventDefault();
                            // this.contextSelectdNode.root = root;
                            // this.contextSelectdNode.node = node;

                            // // this.contextSelectdNode.data = data;
                            // this.$refs.contentFileMenu.$refs.reference = e.target;
                            // this.$refs.contentFileMenu.currentVisible = !this.$refs.contentFileMenu.currentVisible;
                        };

                        let arr = [
                            h('span', Object.assign({ class: 'http-method ' + method.toLowerCase() }, nodeCfg), method),
                            h('span', nodeCfg, path)
                        ];

                        return arr;
                    }
                });
            });

            return arr;
        },

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

        togglePanel() {
            let config = this.$el.querySelector('.config');
            if (config.style.height == '500px') {
                config.style.height = '0';
            } else
                config.style.height = '500px';

        }
    }
});