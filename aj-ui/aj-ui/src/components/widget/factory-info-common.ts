import { xhr_get, xhr_put, xhr_post } from '../../util/xhr';

export default {
    props: {
        apiRoot: { type: String, required: false },  // 接口前缀 API 选择器需要这个属性,
        api: { type: String, required: false },     // 完整的 API 地址。与 接口前缀 二选一
        initId: Number, // 有 id 表示编辑
    },
    data() {
        return {
            id: this.initId || 0,
            name: '',
            datasourceId: 0,    // 关联的数据源 id。不是数据绑定，没什么约束力，只是参考用
            datasourceName: '', // 关联的数据源名称。不是数据绑定，没什么约束力，只是参考用
            tableName: '',      // 关联的表名。不是数据绑定，没什么约束力，只是参考用
            editIndex: -1,
        };
    },
    mounted(): void {
        let id: string = this.$route.query.id;
        if (id) {
            this.id = Number(id);
            this.getData();
        }
    },
    methods: {
        /**
         * 获取单个数据
         */
        getDataBase(cb: Function): void {
            xhr_get(`${this.API}/${this.id}`, (j: RepsonseResult) => {
                let r: any = j.data;

                if (r) {
                    this.name = r.name;
                    this.datasourceId = r.datasourceId || 0;
                    this.datasourceName = r.datasourceName || 0;
                    this.tableName = r.tableName || 0;

                    cb(r);
                } else
                    this.$Message.warning('获取配置失败');
            });
        },
        saveOrUpdate(valueObj: any): void {
            if (this.id) {
                valueObj.id = this.id;

                xhr_put(this.API, (j: RepsonseResult) => {
                    if (j.status)
                        this.$Message.success('修改成功');
                    else
                        this.$Message.warning(j.message);
                }, valueObj);
            } else
                xhr_post(this.API, (j: RepsonseResult) => {
                    if (j.status) {
                        this.$Message.success('创建成功');
                        setTimeout(() => this.id = j.newlyId, 800);
                    } else
                        this.$Message.warning(j.message);
                }, valueObj);
        }
    },
    watch: {
        $route() {
            let id: string = this.$route.query.id;

            if (id) {
                this.id = Number(id);
                this.getData();
            } else {
                // 新建
                this.name = this.cfg.dataBinding.tableName = this.cfg.apiUrl = '';
                this.cfg.fields = [];
            }
        },
    },
}