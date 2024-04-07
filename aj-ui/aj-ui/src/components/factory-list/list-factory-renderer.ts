import FromRenderer from '../factory-form/form-factory-renderer.vue';
import FormLoaderMethod from '../factory-form/loader';
import { xhr_get, xhr_del } from '../../util/xhr';
import { prepareRequest } from '../widget/data-binding';

/**
 * 列表渲染器
 * 调用器
 */
export default {
    components: { FromRenderer },
    props: {
        col: { type: Array, required: true },
        cfg: { type: Object, required: true },
        initTableData: { type: Array, required: true },
        searchFields: { type: Array, required: false },
        apiRoot: { type: String, required: false } // 可选的
    },
    data() {
        return {
            // cfg: this.initCfg,
            isShowForm: false,          // 是否显示表单窗体
            tableData: this.initTableData,
            list: {
                columns: [], // 未使用
                data: this.initTableData,
                total: 0,
                pageNo: 1,
                pageSize: 10,
                loading: false,
                search: {
                    name: ''
                }
            } as TableListConfig,
            form: {
                title: '',
                cfg: {} as FormFactory_Config,
                fields: [] as FormFactory_ItemConfig[]
            }
        };
    },
    methods: {
        getData(): void {
            this.list.loading = true;
            let params: any = { pageNo: this.list.pageNo, pageSize: this.list.pageSize };

            if (this.list.search.name)
                params.where = `name LIKE '%${this.list.search.name}%'`;

            let listCfg: ListFactory_ListConfig = this.cfg;
            let r: ManagedRequest = prepareRequest(listCfg.dataBinding, params, this);
            this.list.data = []; // 清空数据

            xhr_get(r.url, (j: RepsonseResult) => {
                if (j.status) {
                    this.list.data = j.data.rows;
                    this.list.total = j.data.total;
                } else this.$Message.warning(j.message);

                this.list.loading = false;
            }, r.params);
        },

        viewEntity(row: any, rowId: number): void {
            this._openForm(row, rowId, 0);
        },

        createEntity(): void {
            this.$refs.FromRenderer.data = {};
            this.$refs.FromRenderer.$forceUpdate();
            this._openForm(null, null, 1);
        },

        editEntity(row: any, rowId: number): void {
            this._openForm(row, rowId, 2);
        },

        /**
         * 删除
         * 
         * @param url 
         * @param text 
         */
        delEntity(url: string, text: string): void {
            this.$Modal.confirm({
                title: "确认删除",
                content: "是否删除" + text + "？",
                onOk: () => {
                    xhr_del(url, (j: RepsonseResult) => {
                        if (j && j.status) {
                            this.$Message.success('删除成功');
                            this.getData();
                        } else
                            this.$Message.error(j.message);
                    });
                },
            });
        },

        _openForm(row: any, rowId: number, formMode: number): void {
            // 加载表单配置
            let apiRoot: string = this.apiRoot || this.$parent.$parent.$parent.apiRoot;

            if (!apiRoot)
                alert('无法获取根目录');

            if (formMode == 0)
                this.form.title = `查看 ${row.name}`;
            else if (formMode == 1)
                this.form.title = `新建`;
            else if (formMode == 2)
                this.form.title = `编辑 ${row.name}`;

            let formCfgId: number;//  表单配置

            if (!this.cfg.bindingForm || !this.cfg.bindingForm.id) {
                alert('未绑定表单，无法打开');
                return;
            } else
                formCfgId = this.cfg.bindingForm.id;//  表单配置

            xhr_get(`${apiRoot}/common_api/widget_config/${formCfgId}`, (j: RepsonseResult) => {
                if (j.status) {
                    this.isShowForm = true;
                    this.form.cfg = j.data.config;// 数据库记录转换到 配置对象;
                    let cfg: FormFactory_Config = this.form.cfg;
                    this.form.fields = cfg.fields;
                    this.$refs.FromRenderer.status = formMode;

                    if (formMode == 0 || formMode == 2) {
                        FormLoaderMethod.methods.loadInfo.call({
                            cfg: cfg,
                            $refs: { FromRenderer: this.$refs.FromRenderer }
                        }, rowId);
                    }
                } else
                    this.$Message.error('未有任何配置');
            });
        },
        btnClk(js: string, entity?: object, index?: number): void {
            if (entity) {
                let fn: Function = new Function('row', 'index', js);
                fn.call(this, entity, index);
            } else
                eval(js);
        }
    },
    watch: {
        'list.pageNo'(v: number): void {
            this.getData();
        }
    }
};