import tree from "./tree";
import tips from "../widget/tips.vue";
import tableSelector from "../widget/table-selector.vue";
import info from "./info.vue";
import Datasource from "../widget/data-source/data-source.vue";
import { isDev } from '../../util/utils';
import { xhr_get, xhr_post, xhr_put, xhr_del } from '../../util/xhr';

// 新建 tab 的 index
let NEW_TAB: number = 1;

export default {
    mixins: [tree],
    components: { info, tips, tableSelector, Datasource },
    data() {
        return {
            isShowSelectTable: false,
            createSelect: false, // 是否显示创建选择的类型
            split1: 0.2,
            dataSource: {
                isShowDataSource: false,
                isMulti: true, // 是否多个数据源
                id: 1,
                name: null,
                crossDb: false
            },
            activeTab: "index",
            activeTabData: null as DS_TreeNode_Service,
            mainTabs: [
                // {
                //     label: "数据服务",
                //     name: "tab1",
                //     closable: true,
                //     index: 0
                // },
            ],
            table: {
                createRule: {},
                fieldsMapping: {
                }
            },
            showFields: false,
            fields: [],
            dmlSelected: null, // 命令选中高亮显示
        }
    },
    mounted() {
    },
    methods: {
        showAbout() {
            this.$Modal.confirm({
                title: '关于 DataService',
                content: '<p>DataService：用数据库管理 SQL 语句，快捷生成 API 接口</p><p>Powered by MyBatis + AJAXJS Framework.</p><p>ver 2023.10.31</p>'
            });
        },
        createService(): void {
            let prefix = this.getSelectedProject();

            if (prefix) {
                this.createSelect = true;
            }
        },
        refreshConfig() {
            let prefix = this.getSelectedProject();

            if (prefix)
                xhr_get(`${prefix}/common_api/reload_config`, (j: RepsonseResult) => {
                    if (j.status)
                        this.$Message.success('刷新成功');
                });
        },
        // 打开 tab
        openTab(a, _data: any): void {
            if (_data.projectData)
                return;

            let data: DS_TreeNode_Service = <DS_TreeNode_Service>_data;

            let name = data.title + ' ' + data.id;
            let hasTab = null;

            this.mainTabs.forEach(tab => {
                if (tab.name === name)
                    hasTab = tab;
            });

            let label: string = name; // tab label 太长

            // if (label.length > 10)
            //     label = label.substring(0, 25) + '...';

            if (!hasTab)
                this.mainTabs.push({
                    label: label,
                    name: name,
                    closable: true,
                    data: data
                });

            setTimeout(() => {
                this.activeTab = name;
                this.activeTabData = data;
            }, 100);
        },
        // 新建
        addNew(isCustomSQL: boolean) {
            let parentNode = this.project.parentNode.data;
            let hasParent: boolean = !!parentNode; // 是否有父节点

            let data: DS_TreeNode_Service = {
                title: '新建服务',
                contextmenu: true,
                data: {
                    type: isCustomSQL ? 'SINGLE' : "CRUD",
                    namespace: '',
                    pid: hasParent ? parentNode.id : -1
                },
                id: hasParent ? parentNode.namespace + "/" : "",
                isCreate: true,
                parentNode: this.project.parentNode,
                // render: renderCrudTreeNode
            };

            let name: string = '新建服务-' + NEW_TAB++;
            this.mainTabs.push({
                label: name,
                name: name,
                closable: true,
                // @ts-ignore
                data: data
            });

            this.activeTab = name;
            this.activeTabData = data;
            this.createSelect = false;
        },

        addNewByTable(): void {
            this.isShowSelectTable = true;
            this.createSelect = false;
        },
        onTabClick(tabName: string): void {
            this.activeTab = tabName;

            for (let i = 0; i < this.mainTabs.length; i++) {
                if (this.mainTabs[i].name === tabName) {

                    this.activeTabData = this.mainTabs[i].data;
                    break;
                }
            }
        },
        onTabClose(tabName: string) {
            let index = -1;

            for (let i = 0; i < this.mainTabs.length; i++) {
                if (this.mainTabs[i].name === tabName) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                this.$delete(this.mainTabs, index);

                if (this.mainTabs[0])
                    this.activeTab = this.mainTabs[0].name;
                else
                    this.activeTab = 'index';
            }
        },
        changeDatasource(ds) {
            this.dataSource.id = ds.id;
            this.dataSource.name = ds.name;
            this.dataSource.crossDb = ds.crossDb;
            this.dataSource.isShowDataSource = false;
        },

        // 保存命令
        saveDML() {
            let current: DS_TreeNode_Service = this.activeTabData;
            let dml = Object.assign({}, current.data);

            delete dml.createDate;
            delete dml.children;
            delete dml.updateDate;

            for (let i in dml) {
                let v = dml[i];
                if (v === null)
                    delete dml[i];

                if (v === true)
                    dml[i] = 1;
                else if (v === false)
                    dml[i] = 0;
            }

            let prefix: string = this.getCurrentApiPrefix();

            if (current.isCreate) {
                xhr_post(`${prefix}/common_api/common_api`, (j: RepsonseResult) => {
                    if (j.status) {
                        this.$Message.success('创建命令成功');
                        this.refreshTree();

                        let newlyId: number = j.data;
                        current.data.id = newlyId;
                        current.isCreate = false;

                        let parentNode = this.project.parentNode.data;
                        let hasParent: boolean = !!parentNode; // 是否有父节点
                        let projectName: string = this.project.name;

                        if (hasParent) {
                            current.id = projectName + ":" + parentNode.namespace + "/" + dml.namespace;
                        } else
                            current.id = projectName + ":" + dml.namespace;


                        // 获取 tab
                        for (let i = 0; i < this.mainTabs.length; i++) {
                            let tab: any = this.mainTabs[i];

                            if (tab.name === this.activeTab) {
                                // debugger
                                tab.name = current.id;
                                // this.$set(tab, 'label', name);
                                this.activeTab = current.id;
                                break;
                            }
                        }


                        // iview tab 改名会导致 tab 内容消失，于是改为关闭了再打开
                        // this.onTabClose(this.activeTab);

                    } else
                        this.$Message.error(j.message);
                }, dml);
            } else {
                xhr_put(`${prefix}/common_api/common_api`, (j: RepsonseResult) => {
                    if (j.status)
                        this.$Message.success('修改命令成功');
                    else
                        this.$Message.error(j.message);
                }, dml);
            }
        },

        /**
         * 获取当前工程的 API 前缀
         */
        getCurrentApiPrefix(): string {
            let current = this.activeTabData;

            if (!current) {
                this.$Message.warning('请先选择一个 tab');
                return null;
            }

            let project;

            if (current.id.indexOf('/') != -1)
                project = current.parentNode.parentNode;
            else
                project = current.parentNode;

            let prefix: string = isDev() ? project.apiPrefixDev : project.apiPrefixProd;

            return prefix;
        },
        del(e: MouseEvent): void {
            let li: HTMLElement = e.currentTarget as HTMLElement;

            if (li.classList.contains('disabled'))
                return;

            let current = this.activeTabData;
            let prefix: string | null = this.getCurrentApiPrefix();

            if (prefix) {
                this.$Modal.confirm({
                    title: '删除服务',
                    content: '<p>确定删除 ' + current.data.name + ' 这个服务吗？</p>',
                    onOk: () => {
                        xhr_del(`${prefix}/common_api/common_api/${current.data.id}/`, (j: RepsonseResult) => {
                            if (j.status) {
                                this.$Message.success('删除成功');
                                this.onTabClose(this.activeTab);
                                this.activeTab = 'index';
                                this.activeTabData = null;
                                this.refreshTree();
                            }
                        });
                    }
                });
            }
        }
    },
};