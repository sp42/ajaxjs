import dataservice from './api-list-dataservice';
import swagger from './api-list-swagger';

export default {
    props: {
        apiRoot: { type: String, required: true }  // 接口前缀
    },
    mixins: [dataservice, swagger],
    data() {
        return {
            API: this.apiRoot,
            showSelector: false,
            selectedTab: 'dataservice',
            treeAllApi: [],
            selectedApi: { // 已加入的 api。treeAllApi 包含 dom 的信息，不能用来持久化
                DataService: {},
                Swagger: {}
            },
            contextSelectdNode: {// 右击时选中的节点，可删除、测试等 
                root: {}, node: {}, data: {}
            }
        }
    },
    mounted(): void {
        this.getDataService();
        this.getSwaggerData();
    },
    methods: {
        /**
         * 自动填充的过滤
         * 
         * @param value 
         * @param option 
         * @returns 
         */
        auotCompleteFilter(value: string, option: string): boolean {
            return option.toUpperCase().indexOf(value.toUpperCase()) !== -1;
        },

        /**
         * 选好了，加入到列表中
         */
        selectHandler(): void {
            let arr: any[];
            // if ('dataservice' == this.selectedTab) {
            arr = this.$refs.treeDataservice.getCheckedNodes();

            if (arr && arr.length) {
                arr.forEach((treeNode: any) => {
                    if (treeNode.method && treeNode.title) {
                        let key: string = treeNode.method + '_' + treeNode.title;

                        if (!this.selectedApi.DataService[key]) { // 防止重复加入相同的 
                            this.treeAllApi.push(this.renderDataServiceNode(treeNode.title, treeNode.method, treeNode.cfg, true));
                            this.selectedApi.DataService[key] = true;
                        } else {
                            // this.$Message.error(`已加入相同的 ${treeNode.title} API`);
                        }
                    }
                });
            }
            // } else if ('swagger' == this.selectedTab) {
            arr = this.$refs.treeSwagger.getCheckedNodes();

            if (arr && arr.length) {
                arr.forEach((treeNode: any) => {
                    if (treeNode.method && treeNode.title) {
                        let key: string = treeNode.method + '_' + treeNode.title;

                        if (!this.selectedApi.Swagger[key]) { // 防止重复加入相同的 
                            this.treeAllApi.push(this.renderSwaggerNode(treeNode.title, treeNode.method, true));
                            this.selectedApi.Swagger[key] = true;
                        } else {
                            /*                   if (treeNode.title == '请求参数')
                                                  debugger;
                                              this.$Message.error(`已加入相同的 ${treeNode.title} API`); */
                        }
                    }
                });
            }
            // }

            this.sendSelected();
        },

        /**
         * watch 无效，于是写到了这里
         * 
         * @private
         */
        sendSelected(): void {
            let arr: ApiListSelectedReuslt[] = [];

            for (let key in this.selectedApi.DataService) {
                let _arr: string[] = key.split('_');
                arr.push({ method: _arr[0], url: key.replace(_arr[0] + '_', '') });
            }

            this.$emit('apiselected', arr);
        },

        /**
         * 删除树节点
         */
        handleContextMenuDelete(): void {
            let { root, node } = this.contextSelectdNode;
            console.log(node)
            const index = root.indexOf(node);
            this.treeAllApi.splice(index, 1);

            let key: string = node.node.method + '_' + node.node.title;
            if (this.selectedApi.Swagger[key])
                delete this.selectedApi.Swagger[key];

            if (this.selectedApi.DataService[key])
                delete this.selectedApi.DataService[key];
        },

        /**
         * 测试 API
         */
        handleContextMenuTest(): void {
            alert('TODO')
        }
    },
    watch: {
        selectedApi: {
            deep: false,
            handler(v: any): void {
                alert(this.$emit)
                this.$emit('apiselected', 888);
            }
        }
    }
};