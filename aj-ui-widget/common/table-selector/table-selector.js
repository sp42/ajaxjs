Vue.component('table-selector', {
    template: '#table-selector-tpl',
    data() {
        return {
            isCrossDb: false,
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
        dsid: {},
    },
    mounted() {
        this.isCrossDb = true;
        // this.getData();
    },
    methods: {
        getData() {
            this.loading = true;
            let url = `${DS_CONFIG.API_ROOT}/admin/1/getAllTables?start=${this.start}&limit=${this.pageSize}`;

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
        isCrossDb(v) {
            if (v) {
                aj.xhr.get(`${DS_CONFIG.API_ROOT}/admin/1/get_databases`, j => {
                    if (j.status) {
                        this.databaseList = j.data;
                    } else
                        this.$Message.error(j.message);
                });
            }
        },
        databaseName(v) {
            this.getData();
        },
    },
});