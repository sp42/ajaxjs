import { xhr_get, xhr_post, getPageList } from '../../util/xhr';
import SearchPanel from '../widget/search-panel.vue';
import TagListPanel from '../widget/tag-list-panel.vue';
import List from '../widget/list';
import ListFilter from './list-filter.vue';
import CONST from './const';
import Config from './all-dml';
import CommonDatasource from './datasource/datasource-common';

/**
 * 数据服务的命令列表
 */
export default {
    components: { ListFilter, TagListPanel, SearchPanel },
    mixins: [CommonDatasource],
    props: {
        isEmbed: { type: Boolean, default: false }, // 该前端组件的配置
        isShowTag: { type: Boolean, default: true }, // 该前端组件的配置
        apiRoot: { type: String, required: true }   // 接口前缀
    },
    data(): {} {
        return {
            API: `${this.apiRoot}/admin/data_service`,
            createSelect: true, // 是否显示创建选择的类型
            selectTable: {
                isShowDlg: false, // 是否弹出选择表
                api: '',
                column: [
                    { title: '表名', key: 'tableName', width: 220 },
                    { title: '表说明', key: 'comment' }
                ]
            },
            list: {
                columns: [
                    List.id,
                    { title: '表名', minWidth: 160, key: 'tableName' },
                    { title: 'URL 目录', minWidth: 240, slot: 'urlDir' },
                    { title: '描述', key: 'name', minWidth: 220, ellipsis: true, tooltip: true },
                    List.tags,
                    List.createDate,
                    { title: '操作', slot: 'action', align: 'center', minWidth: 140 }
                ],
                data: [],
                start: 0, limit: 6,
                total: 0,
                current: 1
            } as TableListConfig,
            dataSourceUrlDir: '',
            DBType: CONST.DBType,
            current: 0,
            isCrossDb: false
        };
    },

    mounted(): void {
        // CONST.apiRoot = this.API; // 保存 apiRoot，其他组件也可以用

        if (this.isEmbed)
            this.getData();
        else
            this.getDatasource();
    },
    methods: {
        /**
         * 进入编辑
         * 
         * @param row 
         */
        openInfo({ id, tableName }): void {
            location.hash = `#/data-service/info?id=${id}&tableName=${tableName}&_tab=true&dataSourceUrlDir=${this.isEmbed ? '' : this.dataSourceUrlDir}`;
        },

        handleChangePageSize(p: number): void {
            this.list.limit = p;
            this.getData();
        },
        getData(): void {
            const url: string = this.isEmbed ? this.API : `${this.API}?datasourceId=${this.datasource.id}`;

            xhr_get(url, getPageList(this, this.list), {
                start: this.list.start,
                limit: this.list.limit
            });
        },

        /**
         * 创建 数据服务 配置
         * 
         * @param row 
         */
        create(row: any, databaseName: string): void {
            console.log('创建数据服务配置');

            let cfg = JSON.parse(JSON.stringify(Config));
            let tableName = databaseName ? databaseName + '.' + row.tableName : row.tableName;

            for (let i in cfg) {
                let item: DataService_DML_Base = cfg[i]; // 替换成为真实表名

                if (item.sql)
                    item.sql = item.sql.replace('${tableName}', tableName);
            }

            let param = { tableName: tableName, name: row.comment, json: JSON.stringify(cfg, null, 2) };

            if (!this.isEmbed)
                Object.assign(param, { datasourceId: this.datasource.id });

            xhr_post(this.API, (j: RepsonseResult) => {
                if (j.status) {
                    this.$Message.success('创建成功');
                    this.getData();
                    this.$refs.selectTable.close();
                } else
                    this.$Message.error('创建失败，原因：' + j.message);
            }, param);
        },
        handleDelete(index: number): void {
            List.delInfo.call(this, index);
        },

        /**
         * 弹窗“选择表”
         */
        showSelectTable(): void {
            this.selectTable.api = this.isEmbed ? `${this.apiRoot}/datasource/getAllTables` : `${this.apiRoot}/datasource/${this.datasource.id}/getAllTables`;

            setTimeout(() => { // maybe nextTick
                this.$refs.selectTableList.getData();
                this.selectTable.isShowDlg = true;
            }, 300);
        },

        /**
         * 刷新配置
         */
        refreshServerSide(): void {
            xhr_get(`${this.API}/reload`, (j: RepsonseResult) => {
                if (j.status)
                    this.$Message.success(j.message);
                else
                    this.$Message.error('刷新失败，原因：' + j.message);
            });
        }
    },
    watch: {
        /**
         * 分页
         * 
         * @param v 
         */
        'list.current'(v: number): void {
            this.list.start = (v - 1) * this.list.limit;
            this.getData();
        },

        'datasource.id'(dataSourceId: number): void {
            this.getData();

            // 获取数据源的 url
            this.datasource.list.forEach((item: any) => {
                if (item.id === dataSourceId) {
                    this.isCrossDb = !!item.crossDB;
                    this.dataSourceUrlDir = item.urlDir;

                    return false;
                }
            });
        }
    }
};