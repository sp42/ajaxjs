import { xhr_get } from '../../util/xhr';

export default {
    props: {
        API: { type: String, required: true },  // 接口地址
    },
    data() {
        return {
            list: {
                columns: [],
                data: [],
                total: 0,
                start: 0,
                limit: 9,
                pageNo: 1,
                pageSize: 9,
                loading: false,
                search: {
                    name: ''
                },
            } as TableListConfig
        };
    },
    methods: {
        getData(): void {
            this.list.loading = true;
            let params: any = { pageNo: this.list.pageNo, pageSize: this.list.pageSize };

            // if (this.list.search.name)
            //     params.where = `name LIKE '%${this.list.search.name}%'`;

            xhr_get(this.API, (j: RepsonseResult) => {
                this.list.loading = false;
       

                if (j.status) {
                    this.list.data = j.data.rows;
                    this.list.total = j.data.total;
                } else
                    this.$Message.warning(j.message || '获取列表失败');
            }, params);
        },

    }
}