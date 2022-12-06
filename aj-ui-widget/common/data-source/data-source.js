const DBType = { 'MY_SQL': 'MySQL', 'ORACLE': 'Oracle', 'SQL_SERVER': 'Sql Server', 'SPARK': 'Spark', 'SQLITE': 'SQLite', DB2: 'DB2' };

Vue.component('Datasource', {
    template: '#data-source-tpl',
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
                data: {}
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
            aj.xhr.get('http://localhost:8080/adp/data_service/datasource', j => {
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
            aj.xhr.postJson('http://localhost:8080/adp/data_service/datasource', this.form.data, j => {
                if (j.status === 1) {
                    let newlyId = j.data;
                    this.getList(() => this.activedItem = newlyId);
                    this.$Message.success('创建数据源成功');
                    this.form.data.id = newlyId;
                }
            });
        },
        update() {
            let entity = Object.assign({}, this.form.data);
            aj.xhr.putJson('http://localhost:8080/adp/data_service/datasource', entity, j => {
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
                    aj.xhr.del('http://localhost:8080/adp/data_service/datasource/' + id, j => {
                        this.$Message.success('删除数据源成功');
                        this.getList(() => this.add());
                    });
                }
            });
        },
        test() {
            aj.xhr.get('http://localhost:8080/adp/data_service/datasource/test/' + this.activedItem, j => {
                if (j.status === 1) {
                    this.$Modal.success({
                        title: '连接数据源成功',
                    });
                }
            });
        }
    }
});

