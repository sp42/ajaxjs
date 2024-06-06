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
                    title: "权限说明",
                    key: "content",
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
            listData: [
                {
                    name: "John Brown",
                    age: 18,
                    address: "New York No. 1 Lake Park",
                },
                {
                    name: "Jim Green",
                    age: 24,
                    address: "London No. 1 Lake Park",
                },
                {
                    name: "Joe Black",
                    age: 30,
                    address: "Sydney No. 1 Lake Park",
                },
                {
                    name: "Jon Snow",
                    age: 26,
                    address: "Ottawa No. 2 Lake Park",
                },
            ],
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
            }
        };
    },
    mounted(): void {
        this.getData();
    },
    methods: {
        getData(): void {
            xhr_get('http://localhost:8888/iam/simple_api/permission/page?allow=1', List.getPageList(this, this.list), {
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
            xhr_del(`http://localhost:8888/iam/simple_api/permission/${id}?allow=1`, (j: RepsonseResult) => {
                if (j.status) {
                    this.$Message.success('删除成功');
                    this.getData();
                }
            });
        },
        edit(id: number): void {
            this.isShowEditWin = true;
            this.isCreate = false;

            xhr_get(`http://localhost:8888/iam/simple_api/permission/${id}?allow=1`, (j: RepsonseResult) => {
                if (j.status) {
                    this.permissionData = j.data;
                }
            });
        },
        handleChangePageSize(p: number): void {
            this.list.limit = p;
            this.getData();
        },
        save(): void {
            let data: any = List.copyBeanClean(this.permissionData);

            if (this.isCreate) {
                xhr_post(`http://localhost:8888/iam/simple_api/permission?allow=1`, (j: RepsonseResult) => {
                    if (j.status) {
                        this.$Message.success('创建成功');
                        this.getData();
                    }
                }, data);
            } else {
                xhr_put(`http://localhost:8888/iam/simple_api/permission/${this.permissionData.id}?allow=1`, (j: RepsonseResult) => {
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
    }
};

