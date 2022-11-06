import FromRenderer from "./form-factory-renderer.vue";
import { xhr_get } from "../../util/xhr";
import { prepareRequest } from '../widget/data-binding';

export default {
    components: { FromRenderer },
    // props: {
    //     cfg: { type: Object, required: true },      // 所需的配置，元数据
    //     fields: { type: Array, required: true }
    // },
    data() {
        return {
            id: "",
            loadFormId: 0,
            isShow: false,
            cfg: {
                fields: [],
            },
        };
    },
    methods: {
        loadInfo(formId?: number): void {
            if (typeof formId != "number")
                formId = null;

            let id: number = formId || this.loadFormId;
            let isJsonBased: boolean = this.cfg.jsonBased.isJsonBased;

            if (!isJsonBased && !id) {
                this.$Message.warning("请填写 id");
                return;
            }

            this.$refs.FromRenderer.data = {};

            // let dataBinding: DataBinding = this.cfg.dataBinding;
            let dataBinding = this.cfg.dataBinding;
            let params: any = { id: id };
            let r: ManagedRequest = prepareRequest(dataBinding, params, this);

            xhr_get(r.url, (j: RepsonseResult) => {
                if (isJsonBased) {
                    this.$refs.FromRenderer.data = j;
                    this.$refs.FromRenderer.$forceUpdate();
                } else {
                    let r: any = j.result;

                    if (r) {
                        this.$refs.FromRenderer.data = r;
                        this.$refs.FromRenderer.$forceUpdate();
                    } else this.$Message.warning("获取单笔内容失败");
                }
            },
                r.params
            );
        },
    },
};