import List from '../widget/list';
import { xhr_get, xhr_del } from '../../util/xhr';

/**
 * 组件生成器公用
 */
export default {
    props: {
        apiRoot: { type: String, required: false },   // 接口前缀。与 完整的 API 地址 二选一
        api: { type: String, required: false }       // 完整的 API 地址。与 接口前缀 二选一
    },
    data() {
        return {
            isShowEdit: false,  // 编辑
            perview: {          // 预览
                isShow: false,
                title: '',
                data: {}        // 配置对象
            }
        };
    },
    watch: {
        /**
         * 分页
         * 
         * @param v 
         */
        current(v: number): void {
            this.start = (v - 1) * this.list.limit;
            this.getData();
        },
        'list.pageNo'(v: number): void {
            this.list.start = (v - 1) * this.list.limit;
            this.getData();
        }
    },
    methods: {
        /**
         * 创建新记录
         */
        createNew(): void {
            let cfg: any = this.$refs.WidgetFactory.cfg;
            cfg.name = cfg.dataBinding.tableName = cfg.dataBinding.url = '';
            cfg.fields = [];

            this.isShowEdit = true;
        },

        openInfo(row: any): void {
            this.perview.title = row.name;
            this.$refs.WidgetFactory.cfg.id = row.id;
            this.$refs.WidgetFactory.getData();
            setTimeout(() => this.isShowEdit = true, 300);
        },

        deleteInfo(id: number, index: number): void {
            this.list.loading = true;
            xhr_del(`${this.API}/${id}`, List.afterDelete(() => {
                this.list.data.splice(index, 1);
                this.list.total--;
                this.list.loading = false;
            }).bind(this));
        },

        getData(): void {
            this.list.loading = true;
            let params: any = { start: this.list.start, limit: this.list.limit };

            if (this.list.search.name)
                params.where = `name LIKE '%${this.list.search.name}%'`;

            // 应用隔离
            let dp_appId: string = window.sessionStorage.getItem('APP_ID');
            if (dp_appId) {
                let where: string = "appId='" + dp_appId + "'";

                if (params.where) {
                    params.where += " AND " + where;
                } else {
                    params.where = where;
                }
            }

            let api: string = `${this.API}/page`;

            if (this.listParams)
                api += '?' + this.listParams;

            xhr_get(api, (j: RepsonseResult) => {
                this.list.loading = false;

                if (j.status) {
                    this.list.data = j.data.rows;
                    this.list.total = j.data.total;
                } else
                    this.$Message.warning(j.message || '未知异常');
            }, params);
        },

        // 要覆盖这个方法
        rawVoToConfig(vo: any): any {
            // alert(vo);
        },

        /**
         * 预览
         * 
         * @param row 
         */
        openDemo(row: any): void {
            if (this.$refs.FormLoader && this.$refs.FormLoader.$refs.FromRenderer)
                this.$refs.FormLoader.$refs.FromRenderer.data = {}; // 清除之前的数据

            xhr_get(`${this.API}/${row.id}`, (j: RepsonseResult) => {
                let r: any = j.data;

                if (r) {
                    this.perview.title = row.name;
                    this.perview.isShow = true;
                    this.perview.data = this.rawVoToConfig(r); // 数据库记录转换到 配置对象;
                } else {
                    this.$Message.error('未有任何配置');
                    this.perview.data = {};
                }
            });
        },

        /**
         * 分页记录数
         */
        handleChangePageSize(pageSize: number): void {
            this.list.limit = pageSize;
            this.getData();
        },

        /**
         * 进入详情页，采用相对路径
         */
        goInfo(id: Number, path: string): void {
            this.$router.push({ path: path, query: { id: id } });
        }
    }
};