import { xhr_get, xhr_post, xhr_put, xhr_del } from '../../util/xhr';
import List from '../widget/list';

export default {
    props: {
        isPickup: {
            type: Boolean,
            default: false,
        },
        onPickup: {
            type: Function
        },
        simpleApi: {
            type: String,
            required: true
        }
    },
    data(): {} {
        return {
            isCreate: true,
            isShowEditWin: false,
            permissionData: {},
            columnsDef: [
                List.id,
                {
                    title: "权限名称",
                    key: "name",
                },
                {
                    title: "权限编码",
                    key: "code",
                    ellipsis: true
                },
                List.status,
                List.createDate,
                {
                    title: "操作",
                    slot: "action",
                    width: 120,
                },
            ],
            listData: [],
            list: {
                total: 0,
                limit: 5,
                current: 1,
                data: []
            },
            ruleValidate: {
                name: [
                    { required: true, message: '该字段非空约束', trigger: 'blur' }
                ],
                code: [
                    { required: true, message: '该字段非空约束', trigger: 'blur' }
                ],
            }
        };
    },
    mounted(): void {
        this.getData();
    },
    methods: {
        getData(): void {
            let api: string = this.simpleApi + '/permission/page';
            if (this.isPickup)
                api += '?q_stat=0';

            xhr_get(api, List.getPageList(this, this.list), {
                limit: this.list.limit,
                pageNo: this.list.current,
            });
        },
        pickup(index): void { },
        doSearch(): void {
            alert(9);
        },
        showCreate(): void {
            this.permissionData = {};
            this.isShowEditWin = true;
            this.isCreate = true;
        },
        doDelete(id: number): void {
            xhr_del(`${this.simpleApi}/permission/${id}`, (j: RepsonseResult) => {
                if (j.status) {
                    this.$Message.success('删除成功');
                    this.getData();
                }
            });
        },
        edit(id: number): void {
            this.isShowEditWin = true;
            this.isCreate = false;

            xhr_get(`${this.simpleApi}/permission/${id}`, (j: RepsonseResult) => {
                if (j.status)
                    this.permissionData = j.data;
            });
        },
        handleChangePageSize(p: number): void {
            this.list.limit = p;
            this.getData();
        },
        save(): void {
            let data: any = List.copyBeanClean(this.permissionData);

            if (this.isCreate) {
                xhr_post(`${this.simpleApi}/permission`, (j: RepsonseResult) => {
                    if (j.status) {
                        this.$Message.success('创建成功');
                        this.getData();
                    }
                }, data);
            } else {
                xhr_put(`${this.simpleApi}/permission/${this.permissionData.id}`, (j: RepsonseResult) => {
                    if (j.status) {
                        this.$Message.success('修改成功');
                        this.getData();
                    }
                }, data);
            }
        }
    },
    watch: {
        /**
         * 分页
         * 
         * @param v 
         */
        'list.current'(v: number): void {
            this.getData();
        },
        isPickup(v: boolean): void {
            this.getData();

            if (v) {
                for (let i: number = 0; i < this.columnsDef.length; i++)
                    if (this.columnsDef[i].title === '状态') {
                        this.columnsDef.splice(i, 1);
                        break;
                    }
            } else
                this.columnsDef.splice(3, 0, List.status);
        }
    }
};

