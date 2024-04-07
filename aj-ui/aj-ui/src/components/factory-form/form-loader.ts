import FromRenderer from "./form-factory-renderer.vue";
import { xhr_get, xhr_post, xhr_put } from "../../util/xhr";
import { prepareRequest } from "../widget/data-binding";
import { findNode } from "./form-factory";
import { dateFormat } from '../../util/utils';

export default {
    components: { FromRenderer },
    props: [
        'formid', 'entityid'
    ],
    data() {
        return {
            isShowBtns: true,                   // 是否显示按钮，还是自定义按钮？
            formId: Number(this.formid) || 0,   // 表单配置 id
            entityId: this.entityid || 0,       // 实体 id
            cfg: {
                fields: [],
            },
            status: 1,                          // 0=查看/1=新增/2=修改
            oldJson: null                       // JSON Based 下的旧 JSON 完整数据。因为 data 只有部分
        };
    },
    mounted(): void {
        this.load();
    },
    methods: {
        /**
         * 重置表单，但没作用
         */
        resetFields(): void {
            this.$refs.FromRenderer.$refs.formDynamic.resetFields();
        },

        /**
         * 创建
         */
        create(): void {
            let cfg: FormFactory_Config = this.cfg, api: DataBinding;

            let callback = (j: RepsonseResult) => {
                if (j.status) {
                    this.$Message.success(j.message);
                    setTimeout(() => {
                        location.hash = location.hash + '&entityId=' + j.newlyId;
                    }, 2000);
                } else
                    this.$Message.error(j.message || '创建失败，原因未知！');
            };

            if (cfg.isRESTful_writeApi) {
                api = cfg.updateApi;
                let r: ManagedRequest = this._initParams(api);

                xhr_post(r.url, callback, r.params);
            } else {
                api = cfg.createApi;
                let r: ManagedRequest = this._initParams(api);

                (api.httpMethod == 'post' ? xhr_post : xhr_put)(r.url, callback, r.params);
            }
        },

        _initParams(api: DataBinding): ManagedRequest {
            let r: ManagedRequest = prepareRequest(api, this.$refs.FromRenderer.data, this);

            deleteFieldIfNull(r.params);
            date2str(r.params);

            return r;
        },

        /**
         * 更新
         */
        update(): void {
            let cfg: FormFactory_Config = this.cfg, api: DataBinding = cfg.updateApi;
            let params: any = api.baseParams || {};

            if (cfg.jsonBased.isJsonBased) {// Raw body post
                let jsonTarget: any = findNode(this.oldJson, this.entityId.split('.'));
                Object.assign(jsonTarget, this.$refs.FromRenderer.data);

                let r: ManagedRequest = prepareRequest.call(this, api, this.oldJson);
                let json: string = JSON.stringify(r.params);

                console.log(json);

                xhr_post(r.url, (j: RepsonseResult) => {
                    console.log(j)
                }, json, { contentType: 'application/json' });
            } else {
                let r: ManagedRequest = this._initParams(api, this.$refs.FromRenderer.data, this);

                xhr_put(r.url, (j: RepsonseResult) => {
                    if (j.status)
                        this.$Message.success(j.message);
                    else
                        this.$Message.error(j.message || '更新失败，原因未知！');
                }, r.params);
            }
        },

        /**
         *
         */
        load() {
            let formId: string = this.$route.query.formId;
            if (formId)
                this.formId = formId;

            if (!this.formId) {
                this.$Message.error('缺少表单配置 id');
                return;
            }

            let entityId: string = this.$route.query.entityId;
            if (entityId)
                this.entityId = entityId;

            if (this.entityId)          // 有 id 表示修改状态
                this.status = 2;
            else
                this.status = 1;

            xhr_get(`${window["config"].dsApiRoot}/common_api/widget_config/${this.formId}`,
                (j: RepsonseResult) => {
                    if (j && j.status) {
                        this.cfg = j.data.config;

                        let cfg: FormFactory_Config = this.cfg
                        let isJsonBased = cfg.jsonBased.isJsonBased;
                        let dataBinding: DataBinding = cfg.dataBinding;

                        if (isJsonBased) {
                            this.status = 2; // JSON 配置模式下没有新建
                            let r: ManagedRequest = prepareRequest.call(this, dataBinding);

                            xhr_get(`${r.url}`, (j: RepsonseResult) => {
                                this.oldJson = j; // 完整的

                                let jsonTarget: any = findNode(this.oldJson, this.entityId.split('.'));// 部分的，目标的

                                this.$refs.FromRenderer.data = {};
                                Object.assign(this.$refs.FromRenderer.data, jsonTarget);
                            }, r.params);
                        } else {
                            if (this.entityId) {// 加载单笔内容

                                this.$refs.FromRenderer.data = {};
                                let r: ManagedRequest = prepareRequest.call(this, dataBinding/* , { id: this.entityId } */);

                                xhr_get(`${r.url}/${this.entityId}`, (j: RepsonseResult) => {
                                    if (isJsonBased) {
                                        this.$refs.FromRenderer.data = j;
                                        this.$refs.FromRenderer.$forceUpdate();
                                    } else {
                                        let r = j.data;

                                        if (r) {
                                            this.$refs.FromRenderer.data = r;
                                            this.$refs.FromRenderer.$forceUpdate();
                                        } else this.$Message.warning("获取单笔内容失败");
                                    }
                                }/* , r.params */);
                            }
                        }
                    } else this.$Message.error("获取表单配置失败");
                }
            );
        },
    },
    watch: {
        $route() {
            this.load();
        },
    },
};

/**
 * 删除值为 null 的字段
 * 
 * @param params 
 */
function deleteFieldIfNull(params: any): void {
    for (let i in params) {
        if (params[i] === null)
            delete params[i];
    }
}

function date2str(params: any): void {
    for (let i in params) {
        let v = params[i];

        if (v instanceof Date)
            params[i] = dateFormat.call(v, 'yyyy-MM-dd hh:mm:ss');
    }
}