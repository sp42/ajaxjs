import tree from "./tree.js";
import tips from "../widget/tips.vue";
import tableSelector from "../widget/table-selector.vue";
import info from "./info.vue";
import Datasource from "../widget/data-source/data-source.vue";
import { isDev } from '../../util/utils';
import { xhr_get, xhr_post, xhr_put, xhr_del } from '../../util/xhr';

export default {
    mixins: [tree],
    components: { info, tips, tableSelector, Datasource },
    data() {
        return {
            isShowSelectTable: false,
            split1: 0.2,
            dataSource: {
                isShowDataSource: false,
                isMulti: true, // 是否多个数据源
                id: 1,
                name: null,
                crossDb: false
            },
            activeTab: "index",
            activeTabData: null,
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
        refreshConfig() {
            let prefix = this.getSelectedProject();

            if (prefix)
                xhr_get(`${prefix}/common_api/reload_config`, j => {
                    if (j.status)
                        this.$Message.success('刷新成功');
                });
        },
        openTab(a, data) {
            if (data.projectData)
                return;

            let name = data.title + ' ' + data.id;
            let hasTab = null;

            this.mainTabs.forEach(tab => {
                if (tab.name === name)
                    hasTab = tab;
            });

            if (!hasTab)
                this.mainTabs.push({
                    label: name,
                    name: name,
                    closable: true,
                    data: data
                });

            setTimeout(() => {
                this.activeTab = name;
                this.activeTabData = data;
            }, 100);
        },
        onTabClick(tabName) {
            this.activeTab = tabName;

            for (let i = 0; i < this.mainTabs.length; i++) {
                if (this.mainTabs[i].name === tabName) {

                    this.activeTabData = this.mainTabs[i].data;
                    break;
                }
            }
        },
        onTabClose(tabName) {
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
            let dml = Object.assign({}, this.currentData);
            dml.json = JSON.stringify(dml.data);

            delete dml.createDate;
            delete dml.data;
            delete dml.updateDate;
            delete dml.datasourceName;
            delete dml.extractData;

            xhr_put(DATA_SERVICE_API, dml, j => {
                if (j.status === 1) {
                    this.$Message.success('修改命令成功');
                }
            });
        },

        /**
         * 获取当前工程的 API 前缀
         */
        getCurrentApiPrefix() {
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

            let prefix = isDev() ? project.apiPrefixDev : project.apiPrefixProd;

            return prefix;
        },
        del(e) {
            let li = e.currentTarget;

            if (li.classList.contains('disabled'))
                return;

            let prefix = this.getCurrentApiPrefix();

            if (prefix) {
                this.$Modal.confirm({
                    title: '删除服务',
                    content: '<p>确定删除 ' + current.data.name + ' 这个服务吗？</p>',
                    onOk: () => {
                        xhr_del(`${prefix}/common_api/common_api/${current.data.id}/`, j => {
                            if (j.status == 1) {
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