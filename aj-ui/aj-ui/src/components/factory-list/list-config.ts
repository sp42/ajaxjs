import ListSelector from "./list-selector.vue";
import FormPerviewLoader from "../factory-form/loader.vue";
import FormFactoryMethod from "../factory-form/form-factory.vue";
import ApiBinding from "../widget/api-binding.vue";
import EventGroup from "../widget/event-group.vue";

export default {
    components: { ListSelector, FormPerviewLoader, ApiBinding, EventGroup },
    props: {
        listCfg: Object,
        parent: Object,
        apiRoot: { type: String, required: true }, // API 选择器需要这个属性
    },

    data() {
        let self: any = this;

        return {
            formSelectorCols: [
                { key: "id", title: "#", width: 60 },
                { key: "name", title: "名称", minWidth: 80 },
                { key: "tableName", title: "表名", minWidth: 70 },
                {
                    key: "apiUrl",
                    title: "接口地址",
                    minWidth: 260,
                    ellipsis: true,
                    tooltip: true,
                },
                {
                    title: "预览",
                    width: 70,
                    render(h: Function, params: any) {
                        return h(
                            "a",
                            {
                                on: {
                                    click: (event: Event) => {
                                        let FormPerviewLoader = self.$refs.FormPerviewLoader;
                                        debugger;
                                        // @ts-ignore
                                        FormPerviewLoader.cfg = FormFactoryMethod.methods.copyValue(
                                            {},
                                            params.row
                                        ); // 数据库记录转换到 配置对象;
                                        FormPerviewLoader.isShow = true;
                                    },
                                },
                            },
                            "预览"
                        );
                    },
                },
            ],
        };
    },
    methods: {
        /**
         * 显示表单配置
         *
         * @returns
         */
        getFormConfig(): string {
            let cfg: ListFactory_ListConfig = this.listCfg;
            
            if (cfg.bindingForm && cfg.bindingForm.id)
                return (cfg.bindingForm.name || "") + "#" + cfg.bindingForm.id;
            else return "未绑定";
        },

        /**
         * 选中表单配置之后
         *
         * @param formCfg
         */
        onFormSelected({ id, name }): void {
            this.$refs.SelectForm.isShowListModal = false;

            let cfg: ListFactory_ListConfig = this.listCfg;
            cfg.bindingForm.id = id;
            cfg.bindingForm.name = name;

            this.$forceUpdate();
        },
    },
};