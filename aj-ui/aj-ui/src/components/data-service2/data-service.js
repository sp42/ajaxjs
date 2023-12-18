import tree from "./tree.js";
import tips from "../widget/tips.vue";
import tableSelector from "../widget/table-selector.vue";
import info from "./info.vue";
import Datasource from "../widget/data-source/data-source.vue";
import { xhr_get, xhr_post } from '../../util/xhr';

export default {
    mixins: [tree],
    components: { info, tips, tableSelector, Datasource },
    data() {
        return {
            treeData: [],
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
        this.loadTreeProejct();
    },
    methods: {
        showAbout() {
            this.$Modal.confirm({
                title: '关于 DataService',
                content: '<p>DataService：用数据库管理 SQL 语句，快捷生成 API 接口</p><p>Powered by MyBatis + AJAXJS Framework.</p><p>ver 2023.10.31</p>'
            });
        },
        refreshConfig() {
            xhr_get(`${window.config.dsApiRoot}/common_api/reload_config`, j => {
                if (j.status)
                    this.$Message.success('刷新成功');
            });
        },
        openTab(a, data) {
            let name = data.title + ' ' + data.id;
            let hasTab = false;

            this.mainTabs.forEach(tab => {
                if (tab.name === name)
                    hasTab = true;
            });

            if (!hasTab)
                this.mainTabs.push({
                    label: name,
                    name: name,
                    closable: true,
                    data: data
                });

            setTimeout(() => this.activeTab = name, 100);
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

            aj.xhr.putJson(DATA_SERVICE_API, dml, j => {
                if (j.status === 1) {
                    this.$Message.success('修改命令成功');
                }
            });
        },
        del(e) {
            let li = e.currentTarget;

            if(li.classList.contains('disabled'))
                return;

            var current = this.$refs.tab.value;
            alert(current)
        }   
    },
};