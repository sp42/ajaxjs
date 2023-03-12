const DBType = { 'MY_SQL': 'MySQL', 'ORACLE': 'Oracle', 'SQL_SERVER': 'Sql Server', 'SPARK': 'Spark', 'SQLITE': 'SQLite', DB2: 'DB2' };

const DATASOURCE_API = window.API_ROOT ? API_ROOT + '/data_service/datasource' : 'http://localhost:8080/adp/data_service/datasource';
const DATA_SERVICE_API = window.API_ROOT ? API_ROOT + '/data_service/admin' : 'http://localhost:8080/adp/data_service/admin';

export default {
    data() {
        return {
            isCreate: true,
            datasources: [
                {
                    id: 1,
                    name: '加载中……'
                }
            ],
            activedItem: null,
            editing: {},
            form: {
                data: {},
                rules: {
                    name: [
                        { required: true, message: '数据源名称不能为空', trigger: 'blur' }
                    ],
                }
            },
            DBType: DBType
        };
    },
    mounted() {
        this.getList();
    },
    methods: {
        active(item) {
            this.activedItem = item.id;
            this.form.data = item;
        },
        getList(cb) {
            aj.xhr.get(DATASOURCE_API, j => {
                this.datasources = j.data;
                cb && cb();
            });
        },
        add() {
            this.activedItem = null;
            this.form.data = {
                name: ''
            };
        },
        create() {
            this.$refs.editForm.validate((valid) => {
                if (valid) {
                    aj.xhr.postJson(DATASOURCE_API, this.form.data, j => {
                        if (j.status === 1) {
                            let newlyId = j.data;
                            this.getList(() => this.activedItem = newlyId);
                            this.$Message.success('创建数据源成功');
                            this.form.data.id = newlyId;
                        }
                    });
                } else
                    this.$Message.error('表单验证不通过');
            });
        },
        update() {
            let entity = Object.assign({}, this.form.data);
            aj.xhr.putJson(DATASOURCE_API, entity, j => {
                if (j.status === 1) {
                    this.$Message.success('修改数据源成功');
                }
            });
        },
        del(id, name) {
            this.$Modal.confirm({
                title: '删除数据源',
                content: `是否删除数据源 #${name}？`,
                onOk: () => {
                    aj.xhr.del(DATASOURCE_API + id, j => {
                        this.$Message.success('删除数据源成功');
                        this.getList(() => this.add());
                    });
                }
            });
        },
        test() {
            aj.xhr.get(DATASOURCE_API + '/test/' + this.activedItem, j => {
                if (j.status === 1) {
                    this.$Modal.success({
                        title: '连接数据源成功',
                    });
                }
            });
        }
    }
};

