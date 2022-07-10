import { xhr_get, xhr_post, xhr_put } from '../../../util/xhr';
import Info from '../commander/info.vue';
import List from '../commander/list.vue';
import Create from '../commander/create.vue';
import Update from '../commander/update.vue';
import Delete from '../commander/delete.vue';
import Custom from '../commander/custom.vue';
import CfgPane from './cfg-pane.vue';
import DefaultDML from '../all-dml';

export default {
    props: {
        apiRoot: { type: String, required: true }   // 接口前缀
    },
    components: { CfgPane, Info, List, Create, Update, Delete, Custom },
    data() {
        return {
            API: `${this.apiRoot}/admin/data_service`,
            tableId: 0,                // 配置 id
            allDml: DefaultDML,
            table: {
                apiRoot: this.apiRoot,
                urlRoot: '',
                urlDir: '',          // 目录
                dataSourceUrlDir: '',
                fieldsMapping: {},
                fieldsComments: {}
            } as DataService_TableConfig,
            fields: {},             // 所有的字段，value 是选择状态。该字段每次刷新，不需要持久化到数据库
            isShowCfgDlg: false,
        };
    },

    mounted(): void {
        this.tableId = this.$route.query.id;
        this.table.tableName = this.$route.query.tableName;
        this.table.dataSourceUrlDir = this.$route.query.dataSourceUrlDir;

        // 获取详情
        xhr_get(`${this.API}/${this.tableId}`, (j: JsonResponse) => {
            let _table: DataService_TableConfig = (<any>j.data) as DataService_TableConfig;
            this.table.name = _table.name;
            this.table.urlRoot = _table.urlRoot;
            this.table.urlDir = _table.urlDir;
            this.table.keyGen = _table.keyGen;

            let json = JSON.parse(j.data.json);
            this.allDml = json;

            if (this.allDml.fieldsMapping)
                this.table.fieldsMapping = this.allDml.fieldsMapping;
            else
                this.table.fieldsMapping = DefaultDML.fieldsMapping; // 默认值

            for (let i in _table.fields) {
                this.fields[i] = false;
                this.table.fieldsComments[i] = _table.fields[i];
            }

            this.table.fields = JSON.stringify(this.fields); // 保存所有字段

            // // 为避免其他 watch 属性影响，再写入一次
            // setTimeout(() => {
            //     for (let i in json)
            //         this.allDml[i].sql = json[i].sql;
            // }, 500)

            // this.allDml.others = [
            //     {
            //         type: 'insert',
            //         enable: true,
            //         dir: 'check_or_add',
            //         comment: '先判断记录是否存在，不存在则将记录插入表',
            //         sql: 'INSERT INTO registe_statistics (date) SELECT #{value} WHERE NOT EXISTS (SELECT date FROM registe_statistics WHERE date = #{value})'
            //     }
            // ];
        });
    },
    methods: {
        /**
         * 更新
         */
        saveOrUpdate(): void {
            xhr_put(`${this.API}/${this.tableId}`, (j: RepsonseResult) => {
                if (j.isOk) {
                    this.$Message.success('修改成功');
                } else
                    this.$Message.info('修改失败。原因：' + j.msg);
            }, {
                tableName: this.table.tableName,
                name: this.table.name,
                urlDir: this.table.urlDir,
                urlRoot: this.table.urlRoot,
                keyGen: this.table.keyGen,
                json: JSON.stringify(this.allDml, null, 2)
            });
        },

        /**
         * 新增其他类型
         */
        addOther(): void {
            let cfg: DataService_DML_Base = {
                type: '',
                enable: true,
                sql: '',
                dir: ''
            };

            this.allDml.others.push(cfg);
        }
    }
};