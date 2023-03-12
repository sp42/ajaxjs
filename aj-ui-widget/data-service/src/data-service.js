import './components/project';
import Tree from './components/tree';
import './common/utils';

import Toolbar from './components/toolbar.vue';
import Info from './components/info.vue';
import Edit from './components/edit.vue';
import Datasource from './data-source/data-source.vue';
import Project from './components/project.vue';
import TableSelector from './table-selector/table-selector.vue';

export default {
    mixins: [project, Tree],
    components: { Toolbar, Info, Datasource, TableSelector, Edit, Project },
    data() {
        return {
            isShowSelectTable: false,
            split1: 0.2,
            dataSource: {
                isShowDataSource: false,
                isMulti: true, // 是否多个数据源
                id: 0,
                name: null,
                crossDb: false
            },
            allData: [], // 所有的数据
            currentData: {}, // 当前编辑的数据（总数据）
            currentDML: {}, // 当前编辑的数据（DML）
            currentType: '',
            current: {
                data: {},   // 当前编辑的数据（总数据）
                dml: {},    // 当前编辑的数据（DML）
                type: ''
            },
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

        }
    },
    mounted() {
        document.title = 'Data Service';
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
            aj.xhr.get(DS_CONFIG.API_ROOT + '/admin/reload', j => {
                if (j.status)
                    this.$Message.success('刷新成功');
            });
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

        // 选择 DML
        selectItem(item, index, type) {
            this.code = item.sql;
            this.dmlSelected = index;
            this.currentDML = item;
            this.currentType = type;
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

            console.log(dml)


            aj.xhr.putJson(DATA_SERVICE_API, dml, j => {
                if (j.status === 1) {
                    this.$Message.success('修改命令成功');
                }
            });
        }
    }
};