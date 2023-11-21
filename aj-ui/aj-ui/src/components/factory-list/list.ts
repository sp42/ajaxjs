import List from '../widget/list';
import TagListPanel from '../widget/tag-list-panel.vue';
import CommonFactory from '../widget/factory-list-mixins';
import SearchPanel from '../widget/search-panel.vue';
import ListFactory from './list-factory.vue';

export default {
    components: { ListFactory, SearchPanel, TagListPanel },
    mixins: [CommonFactory],
    data() {
        return {
            API: this.api || `${this.apiRoot}/common_api/widget_config`,
            list: {
                columns: [
                    List.id,
                    { title: '列表名称', key: 'name', minWidth: 130, ellipsis: true, tooltip: true },
                    {
                        title: '关联数据库', render(h, params) {
                            if (params.row.datasourceName)
                                return h('span', params.row.datasourceName + "/" + params.row.tableName);
                            else
                                return h('span', params.row.tableName);
                        }, width: 280, ellipsis: true
                    },
                    {
                        title: '接口地址', minWidth: 230, render: (h, params) => {
                            return h('span', params.row.config.dataBinding.url);
                        }, ellipsis: true, tooltip: true
                    },
                    List.createDate,
                    // List.status,
                    { title: '操作', slot: 'action', align: 'center', width: 260 }
                ],
                data: [],
                total: 0,
                start: 0,
                limit: 9,
                loading: false,
                search: {
                    name: ''
                },
            } as TableListConfig
        };
    },

    mounted() {
        // this.openInfo({ name: 'test', id: 105 });
        // this.openDemo(104)
        this.getData();
    },

    methods: {
        /**
         * 预览
         * 
         * @param id 
         */
        openDemo(id: number): void { // 直接调用，不另外新建一套预览。内部逻辑复杂
            this.$refs.WidgetFactory.id = id;
            this.$refs.WidgetFactory.getData(() => this.$refs.WidgetFactory.doRenderer());
        },
    },
};