
import list from '../widget/list';
import { xhr_get, xhr_put, xhr_post, xhr_del } from '../../util/xhr';
import Renderer from './ext-form-renderer.vue';
import Info from './ext-form-info.vue';

// const API = `${global.apiPrefix}/ext_form`;
// const API = `/dp_service/admin/ext_form`;

export default {
    name: 'form-advanced-form',
    components: { Renderer, Info },
    props: {
        apiRoot: { type: String, required: true }   // 接口前缀
    },
    data(): {} {
        return {
            API: `${this.apiRoot}/admin/ext_form`,
            columns: [
                list.id,
                { title: '数据模型', slot: 'tableName', width: 260 },
                { title: '数据字段', slot: 'fieldName', minWidth: 190 },
                list.createDate,
                {
                    title: '状态', render(h: Function, params: any) {
                        if (params.row.stat === undefined)
                            return '';
                        else
                            return h('span', params.row.stat === 0 ? '停用' : '正常');
                    }, width: 220
                },
                { title: '操作', slot: 'action', align: 'center', width: 260 }
            ],
            tableData: [
                { id: 1, name: '易发艳', content: 'dsdsa', createDate: 12 },
                { id: 2, name: '酸小鱼', content: 'dsdsa', createDate: 10 }
            ],
            showDemo: false,        // 演示
            showInfo: false,        // 演示
            demoTitle: '',
            demoData: [],
            total: 0,               // 记录总数
            loading: false,
            addNew: false,          // 是否新增
            editIndex: -1,          // 编辑记录之 id
            editTableName: '',
            editFieldName: '',
        };
    },
    mounted(): void {
        this.getData();

        setTimeout(() => {
            // this.openDemo(this.tableData[1]);
        }, 2000);
    },
    methods: {
        /**
         * 进入编辑
         * 
         * @param row 
         */
        openInfo(row: any, id: number): void {
            // this.$routerThen.push({ path: './ext-form/info', query: { id: id } }).onChange((value: string, vm: any): void => {
            //     this.getData();
            // });
            this.demoTitle = row.tableName;
            this.showInfo = true;
            this.$refs.info.getData(id);
        },

        /**
         * 获取列表数据
         */
        getData(): void {
            // this.$http.get(this.API).then((resp: any) => {
            //     let result: RepsonseResult = resp.data;

            //     if (result.result == null)
            //         this.tableData = [];
            //     else {
            //         // @ts-ignore
            //         this.total = result.total;
            //         this.tableData = result.result;
            //     }
            // });
            xhr_get(this.API, (j: RepsonseResult) => {
                if (j.result == null)
                    this.tableData = [];
                else {
                    // @ts-ignore
                    this.total = j.total;
                    this.tableData = j.result;
                }
            });
        },

        /**
         * 进入新增状态
         */
        handleAdd(): void {
            this.addNew = true;
            let row: {} = { tableName: '', fieldName: '' };
            this.tableData.push(row);
            this.handleEdit(row, this.tableData.length - 1);
        },

        /**
         * 进入编辑状态
         * 
         * @param row 
         * @param index 
         */
        handleEdit(row: any, index: number): void {
            this.editIndex = index;

            // 改为实际字段
            this.editTableName = row.tableName;
            this.editFieldName = row.fieldName;
        },
        handleSave(index: number): void {
            if (!this.editTableName || !this.editFieldName) {
                this.$Message.error('请填写完整内容');
                return;
            }

            let data: {} = { tableName: this.editTableName, fieldName: this.editFieldName };

            if (this.addNew) { // create
                xhr_post(this.API, (j: RepsonseResult) => {
                    if (j.isOk) {
                        this.$Message.info('创建成功');
                        this.handleCancel();
                        this.getData();
                    } else
                        this.$Message.error(j.msg);
                }, data);
            } else
                xhr_put(`${this.API}/${this.tableData[index].id}/`, (j: RepsonseResult) => {
                    if (j.isOk) {
                        this.$Message.info('修改成功');
                        this.handleCancel();
                        this.getData();
                    } else
                        this.$Message.error(j.msg);
                }, data);
        },

        /**
         * 取消编辑
         */
        handleCancel(): void {
            this.editIndex = -1;
            this.addNew = false;
            this.tableData.pop();
        },

        /**
         * 删除
         * 
         * @param index 
         */
        handleDelete(index: number): void {
            xhr_del(`${this.API}/${this.tableData[index].id}/`, list.afterDelete(() => {
                this.tableData.splice(index, 1);
                this.addNew = false;
            }).bind(this));
        },

        openDemo(row: any, index: number): void {
            xhr_get(`${this.API}_cfg?extFormId=${row.id}`, (j: RepsonseResult) => {
                if (j.result == null) {
                    this.$Message.error('未有任何配置');
                    this.demoData = [];
                } else {
                    // @ts-ignore
                    this.demoData = j.result;
                }
            });

            this.demoTitle = row.tableName;
            this.showDemo = true;
        }
    }
};