import { xhr_get, xhr_post, xhr_put } from '../../../util/xhr';
import List from '../../widget/list';

const DBType: {} = { 1: 'MySQL', 2: 'Oracle', 3: 'SqlServer', 4: 'Spark', 5: 'SQLite' };
const DBTypeName = { MySql: 1, Oracle: 2, SqlServer: 3, Spark: 4, SQLite: 5 };

/**
 * 数据源管理
 */
export default {
    props: {
        apiRoot: { type: String, required: true }  // 接口前缀
    },
    data(): {} {
        return {
            API: `${this.apiRoot}/admin/datasource`,
            form: {
                data: {},
                isLoading: false,
                isShow: false,
                isCreate: true,
                createRules: {
                    urlDir: [{ required: true, message: '数据源编码全局唯一不重复', trigger: 'blur' }],
                    name: [{ required: true, message: '请输入数据源名称或者简单的描述', trigger: 'blur' }],
                    type: [{ required: true, message: '请选择数据库类型', trigger: 'change', type: 'number' }],
                    url: [{ required: true, message: '请输入数据库连接 URL', trigger: 'blur' }]
                },
            },
            list: {
                columns: [
                    List.id,
                    { title: '数据源名称', key: 'name', minWidth: 100, ellipsis: true, tooltip: true },
                    {
                        title: '数据源编码', minWidth: 100, render(h: Function, params: any) {
                            return h('span', '' + params.row.urlDir);
                        }
                    },
                    {
                        title: '数据库类型', width: 150, align: 'center',
                        render(h: Function, params: any) {
                            return h('div', DBType[params.row.type]);
                        }
                    },
                    { title: '链接地址', minWidth: 190, key: 'url', ellipsis: true, tooltip: true },
                    { title: '登录用户', width: 100, key: 'username', align: 'center' },
                    { title: '是否跨库', width: 100, key: 'crossDB', align: 'center' },
                    List.createDate,
                    { title: '操作', slot: 'action', align: 'center', width: 260 }
                ],
                data: [],
                total: 0,
                loading: false
            } as TableListConfig,
            DBType: DBTypeName // 数据库类型
        };
    },
    mounted(): void {
        this.getData();
    },
    methods: {
        getData(): void {
            let params: any = { start: 0, limit: 99 };

            let dp_appId: string = window.sessionStorage.getItem('APP_ID');
            if (dp_appId)
                params.appId = dp_appId;

            xhr_get(this.API, (j: RepsonseResult) => {
                if (j.result) {
                    // @ts-ignore
                    this.list.total = j.total;
                    console.log(j.result)
                    this.list.data = j.result;
                } else
                    this.$Message.warning('获取数据失败');
            }, params);
        },

        /**
         * 编辑
         * 
         * @param row 
         * @param index 
         */
        edit(row: any, index: number): void {
            this.form.isShow = true;
            this.form.isCreate = false;

            console.log(row)
            this.form.data = JSON.parse(JSON.stringify(row));
            console.log(this.form.data)
        },

        /**
         * 新建
         */
        create(): void {
            this.form.isShow = true;
            this.form.isLoading = true;
            this.form.isCreate = true;
            this.form.data = {};
        },

        /**
         * 
         */
        createOrUpdate(): void {
            this.$refs.editForm.validate((valid: boolean) => {
                if (valid) {
                    if (this.form.isCreate) { // 新建
                        this.form.data.appId = window.sessionStorage.getItem('APP_ID') || 1432285183943376896;
                        this.form.data.stat = 1; // 创建状态总是可用的

                        xhr_post(this.API, (j: RepsonseResult) => {
                            if (j.isOk) {
                                this.$Message.success('创建成功');
                                this.getData();
                            } else this.$Message.info('创建失败。原因：' + j.msg);

                            this.form.isShow = this.form.isLoading = false;
                        }, this.form.data);
                    } else {  // 修改
                        xhr_put(`${this.API}/${this.form.data.id}`, (j: RepsonseResult) => {
                            if (j.isOk) {
                                this.$Message.success('修改成功');

                                let updateIndex: number;
                                this.list.data.forEach((item: any, index: number) => {
                                    if (item.id == this.form.data.id) {
                                        updateIndex = index;
                                        return false;
                                    }
                                });

                                Object.assign(this.list.data[updateIndex], this.form.data);
                            } else this.$Message.info('修改失败。原因：' + j.msg);

                            this.form.isShow = this.form.isLoading = false;
                        },
                            this.form.data
                        );
                    }

                    this.showCreate = false;
                } else {
                    this.form.isLoading = false; // 表单验证不成功会关闭弹窗
                    this.$nextTick(() => this.form.isLoading = true);
                }
            });
        },

        /**
         * 删除
         * 
         * @param index 
         */
        del(index: number): void {
            List.delInfo.call(this, index);
        }
    }
};