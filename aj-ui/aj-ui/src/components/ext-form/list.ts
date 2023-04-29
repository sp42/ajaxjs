
import List from '../widget/list';
import CommonFactory from '../widget/factory-list-mixins';
import SearchPanel from '../widget/search-panel.vue';
import TagListPanel from '../widget/tag-list-panel.vue';
import Renderer from '../factory-form/form-factory-renderer.vue';
import Info from '../factory-form/form-factory.vue';
import CommonDatasource from '../data-service/datasource/datasource-common';
import DataModelSelector from '../data-model-selector/index.vue';
import { xhr_get } from '../../util/xhr';

/**
 * 扩展表单列表
 */
export default {
    components: { Renderer, Info, DataModelSelector, SearchPanel, TagListPanel },
    mixins: [CommonFactory, CommonDatasource],
    data(): {} {
        return {
            API: `${this.apiRoot}/api/bdp/ext_form`,
            list: {
                columns: [
                    List.id,
                    { title: '扩展表单名称', key: 'name', width: 150, ellipsis: true, tooltip: true, align: 'center' },
                    { title: '数据模型（表名）', key: 'tableName', minWidth: 180, align: 'center' },
                    { title: '数据字段', key: 'fieldName', minWidth: 120, align: 'center' },
                    { title: '接口地址', minWidth: 190, key: 'apiUrl', ellipsis: true, tooltip: true },
                    List.tags,
                    List.createDate,
                    List.status,
                    { title: '操作', slot: 'action', align: 'center', width: 260 }
                ],
                data: [],
                total: 0,
                start: 0,
                limit: 9,
                loading: false,
                search: {
                    name: '',
                    tableName: ''
                },
            } as TableListConfig,

            isShowFieldsSelect: false,
        };
    },
    mounted(): void {
        this.getDatasource();
    },
    methods: {
        getData(): void {
            this.list.loading = true;
            let params: any = { start: this.list.start, limit: this.list.limit, where: `datasourceId=${this.datasource.id}` };

            if (this.list.search.name)
                params.where = `name LIKE '%${this.list.search.name}%'`;

            if (this.list.search.tableName) {
                let tableName = `tableName LIKE '%${this.list.search.tableName}%'`;

                if (params.where)
                    params.where += ` AND ` + tableName;
                else
                    params.where = tableName;
            }

            xhr_get(`${this.API}/list`, (j: RepsonseResult) => {
                this.list.loading = false;
                let r: any = j.result;

                if (j.isOk) {
                    this.list.data = r;
                    // @ts-ignore
                    this.list.total = j.total;
                } else
                    this.$Message.warning(j.msg);
            }, params);
        },

        /**
         * 获取持久化的信息
         */
        getPersistenceInfo(): void {
            let selected: SelectedTable = this.$refs.DataModelSelector.getSelected();

            if (!selected)
                return;
            else if (selected.field) {
                this.$refs.info.extraDataOnSave.datasourceId = selected.datasourceId;
                this.$refs.info.extraDataOnSave.fieldName = selected.field.name;
                this.$refs.info.cfg.tableName = selected.tableName;
                this.isShowEdit = true;
            } else
                this.$Message.warning('未选择任何字段');
        }
    }
};