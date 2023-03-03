Vue.component('table-selector', {
    template: '#table-selector-tpl',
    data() {
        return {
            // isCrossDb: false,
            list_column: [
                {
                    title: "#",
                    // key: 'id',
                    width: 60,
                    type: "index",
                    // type: 'selection'
                },
                {
                    title: "表名",
                    key: 'tableName',
                },
                {
                    title: "说明",
                    key: 'comment',
                },
                {
                    title: "操作",
                    slot: 'select'
                }
            ],
            listData: [], // 显示数据
            data: [], // 全部数据
            total: 0,
            current: 1,
            pageSize: 5,
            isFilter: false,
            start: 0,
            searchKeyword: "",
            loading: false,
            databaseList: [],
            databaseName: "",
        }
    },
    props: {
        apiUrl: { type: String, required: true },
        isCrossDb: { type: Boolean, required: true },
        dataSourceId: { type: Number, required: true },
    },
    mounted() {
        // this.isCrossDb = true;
        // this.getData();
    },
    methods: {
        getData() {
            this.loading = true;
            let url = `${DS_CONFIG.API_ROOT}/admin/${this.dataSourceId}/getAllTables?start=${this.start}&limit=${this.pageSize}`;

            if (this.searchKeyword)
                url += `&tablename=${this.searchKeyword}`;
            if (this.databaseName)
                url += `&dbName=${this.databaseName}`;

            aj.xhr.get(url, j => {
                if (j.status) {
                    this.data = j.data;
                    this.total = j.total;
                } else
                    this.$Message.error(j.message);

                this.loading = false;
            });
        },
        handleChangePageSize() {
            let start = (this.current - 1) * this.pageSize,
                end = start + this.pageSize;

            this.listData = this.data.slice(start, end);
        },
        onSelect(arr) {
            arr.forEach((item) => {
                this.data.forEach((_item) => { });
            });
        },
        resetData() {
            this.searchKeyword = "";

            this.start = 0;
            this.getData();
            this.$refs.inputEl.$el.querySelector("input").value = "";
        },
        search(ev) {
            let input = ev.target,
                v = input.value;

            if (v) {
                this.searchKeyword = v;
                this.start = 0;
                this.current = 1;
                this.getData();
            }
        },
        onkeypress(ev) {
            let input = ev.target,
                v = input.value;

            if (v) {
                this.isFilter = true;
                let arr = this.data.filter((item) => item.tableName.indexOf(v) != -1);
                this.listData = arr;
            } else {
                this.resetData();
            }
        },
    },
    watch: {
        /**
         * 分页
         *
         * @param v
         */
        current(v) {
            this.start = (v - 1) * this.pageSize;
            this.getData();
        },
        dataSourceId(id) {
            if (id) {
                if (this.isCrossDb)
                    aj.xhr.get(`${DS_CONFIG.API_ROOT}/admin/${this.dataSourceId}/get_databases`, j => {
                        if (j.status) {
                            // 过滤 mysql 自带的库
                            let not = ['information_schema', 'performance_schema', 'sys', 'mysql'];
                            let arr = j.data.filter(db => !not.includes(db));

                            this.databaseList = arr;

                            // 默认选中第一个
                            this.start = 0;
                            this.current = 1;
                            this.databaseName = arr[0];
                        } else
                            this.$Message.error(j.message);
                    });
                else {
                    this.databaseName = null;
                    this.getData();
                }
            }
        },
        databaseName(v) {
            this.getData(); // 会重复请求
        },
    },
});