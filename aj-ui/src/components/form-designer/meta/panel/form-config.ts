import ApiSelector from '../../../api-selector/index.vue';

export default {
    components: { ApiSelector },
    props: {
        form_cfg: { type: Object, required: false },
        form_databinding: {
            type: Object, required: false, default() {
                return {};
            }
        },
    },
    data() {
        return {
            read: false,
            isShowApiSelector: false,
            apiList: [] as ApiListSelectedReuslt[],
            crudApi: { // 保存数组的 index
                create: -1, read: -1, update: -1, delete: -1
            },
            infoUrl: '未选择 Read API'
        };
    },
    methods: {
        getApi(): string {
            return process.env.NODE_ENV === 'development' ? '/dp_service' : '/dp';
        },

        /**
         * 用户选择好可用的 api，在 crud 中显示
         */
        rendererApiList(list: ApiListSelectedReuslt[]): void {
            this.apiList = list;
        },

        getInfoApi(): void {
            if (this.crudApi.read !== -1)
                this.infoUrl = this.apiList[this.crudApi.read].url;
        },

        doRead(): void {
            alert(9);
        },

        doCreate(): void {
            alert(9);
        },
        doUpdate(): void {
            alert(9);
        },

        del(): void {
            this.$Modal.confirm({
                title: '操作提示：',
                content: '请求这接口真可能会删除数据库记录，您确定要执行吗？',
                okText: '我确定',
                cancelText: '再想想',
                onOk: () => {
                    // TODO
                }
            });
        }
    }
};