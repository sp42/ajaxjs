import { xhr_get } from '../../util/xhr';
import { getCookie } from '../../util/cookies';

export default {
    props: {
        apiRoot: { type: String, required: true },  // 接口前缀
        isSingleSelect: { type: Boolean, default: false } // 是否只允许选择一个字段
    },
    data() {
        return {
            API: `${this.apiRoot}/admin/datasource`,
            datasource: {
                id: 0,
                list: [],
                name: ''
            },
            database: {
                isShow: false,
                name: '',
                list: []
            },
            tables: [],
            selectedTableName: '',
            fields: [] as CheckableDataBaseColumnMeta[]
        };
    },

    mounted(): void {
        this.getDatasource();
    },

    methods: {
        /**
         * 获取数据源
         */
        getDatasource(): void {
            let params: any = { start: 0, limit: 999 };

            let dp_appId: string = window.sessionStorage.getItem('APP_ID');
            if (dp_appId)
                params.appId = dp_appId;

            xhr_get(this.API, (j: RepsonseResult) => {
                if (j.result) {
                    this.datasource.list = j.result;
                    if (this.datasource.list[0])
                        this.datasource.id = this.datasource.list[0].id; // 默认显示第一个数据源的
                } else
                    this.$Message.error('获取数据源失败');
            }, params);
        },

        /**
         * 获取表
         */
        getTables(): void {
            let url: string = `${this.API}/${this.datasource.id}/getAllTables`;

            if (this.database.isShow)
                url += '?dbName=' + this.database.name;

            xhr_get(url, (j: RepsonseResult) => {
                if (j.result) {
                    let arr: any[] = j.result as any[];
                    arr.sort(sortFn);

                    this.tables = arr;
                    if (this.tables[0])
                        this.selectedTableName = this.tables[0].tableName;
                } else
                    this.$Message.error('获取表失败');
            });
        },

        /**
         * 获取字段
         */
        getFields(): void {
            let url: string = `${this.API}/${this.datasource.id}/getFields/${this.selectedTableName}`;

            if (this.database.isShow)
                url += '?dbName=' + this.database.name;

            xhr_get(url, (j: RepsonseResult) => {
                if (j.result) {
                    let arr: any[] = j.result as any[];
                    arr.sort(sortFn);

                    this.fields = arr.map((item: CheckableDataBaseColumnMeta) => {
                        let obj: any = <any>item;
                        item.isKey = obj.isKey === 'true';
                        item.defaultValue = obj.default;
                        (item as CheckableDataBaseColumnMeta).checked = false;

                        return item;
                    });
                } else
                    this.$Message.error('获取字段列表失败');
            });
        },

        /**
         * 返回选择结果
         * 
         * @returns 
         */
        getSelected(): SelectedTable {
            let fields: CheckableDataBaseColumnMeta[] = this.fields.filter((item: CheckableDataBaseColumnMeta) => item.checked);
            let s: SelectedTable = { datasourceId: this.datasource.id, datasourceName: this.datasource.name, tableName: this.selectedTableName };

            if (this.isSingleSelect) {
                if (fields.length > 1) {
                    this.$Message.warning('请只选择一个字段');
                    return null;
                }

                s.field = fields[0];
            } else
                s.fields = fields;

            return s;
        },

        /**
         * 全选字段
         */
        selectAllField(): void {
            this.fields.forEach((item: CheckableDataBaseColumnMeta) => item.checked = true);
        },
        getDatabase(id: number): void {
            xhr_get(this.API + '/../data_service/getDatabases', (j: RepsonseResult) => {
                if (j.result) {
                    this.database.list = j.result;
                    this.database.name = this.database.list[0]; // 默认显示第一个数据库的
                } else
                    this.$Message.warning('获取数据库名失败');
            }, { datasourceId: id });
        }
    },
    watch: {
        'datasource.id'(v: number): void {
            // this.getTables(); // 会重复执行，没有 库名 FIXME

            // 为了获取 getDatasourceName
            this.datasource.list.forEach((item: any) => {
                if (item.id == v) {
                    this.datasource.name = item.name;

                    if (item.crossDB) {// 显示选择库
                        this.database.isShow = true;
                        this.getDatabase(item.id);
                    } else
                        this.database.isShow = false;
                }
            });
        },
        selectedTableName(v: string): void {
            this.getFields();
        },
        'database.name'(dbName: string): void {
            this.getTables();
        }
    }
}

const sortFn = (a: any, b: any) => {
    if (a.tableName > b.tableName)
        return 1;
    if (a.tableName < b.tableName)
        return -1;

    return 0;
};