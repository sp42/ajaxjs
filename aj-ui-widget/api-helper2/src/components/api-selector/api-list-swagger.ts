import testData from './test-swagger';

export default {
    data(): {} {
        return {
            SwaggerTreeData: [],
            SwaggerFilter: {
                keyword: "", data: []
            },
            SwaggerTreeRootNode: null
        };
    },
    methods: {
        getSwaggerData(): void {
            let tagMap = {};

            for (let path in testData.paths) {
                let _path: any = testData.paths[path];

                for (let method in _path) {
                    let tag: string = _path[method].tags[0];
                    // console.log('http method::', method)
                    // console.log('tag::', tag);

                    if (!tagMap[tag])
                        tagMap[tag] = [];

                    tagMap[tag].push({ method, path });
                }
            }

            let i: number = 0;

            // 转换为符合 tree 的数据格式
            for (let tag in tagMap) {
                let children: any[] = [];
                let arr: any[] = tagMap[tag];

                arr.forEach(({ method, path }) => children.push(this.renderSwaggerNode(path, method.toUpperCase())));
                this.SwaggerTreeData.push({
                    title: tag,
                    expand: ((i++) === 0),
                    children: children
                });
            }
        },
        renderSwaggerNode(dir: string, method: string, isAddTypeNode?: boolean): {} {
            this.SwaggerFilter.data.push(method + " " + dir); // 加入搜索

            return {
                title: dir,
                render: (h: Function, { root, node, data }) => {
                    this.$set(data, 'method', method);

                    if (!this.SwaggerTreeRootNode)
                        this.SwaggerTreeRootNode = root;

                    let nodeCfg = { // 节点事件的配置
                        on: { click: () => this.appendApiDetail(data) }// 点击加载接口详情
                    };

                    if (isAddTypeNode)// 右键点击事件
                        nodeCfg.on['contextmenu'] = (e: Event) => {
                            e.preventDefault();
                            this.contextSelectdNode.root = root;
                            this.contextSelectdNode.node = node;

                            // this.contextSelectdNode.data = data;
                            this.$refs.contentFileMenu.$refs.reference = e.target;
                            this.$refs.contentFileMenu.currentVisible = !this.$refs.contentFileMenu.currentVisible;
                        };

                    let arr: any[] = [
                        h('span', Object.assign({ class: 'http-method ' + method.toLowerCase() }, nodeCfg), method),
                        h('span', nodeCfg, data.title)
                    ];

                    if (isAddTypeNode)
                        arr.unshift(h('span', { class: 'api-type swagger' }, 'S')); // 头部加入

                    return arr;
                }
            }
        },

        /**
         * 点击加载接口详情
         * 
         * @param data 
         */
        appendApiDetail(data: any): void {
            if (!data.isLoaded) {
                console.log(testData.paths[data.title][data.method.toLowerCase()]);
                let meta: any = testData.paths[data.title][data.method.toLowerCase()];

                // 请求参数
                let paramsChildren: any[] = [];
                let params: any[] = meta.parameters;

                params.forEach((item: any) => {
                    if (!item.type && item.schema && item.schema.$ref) {
                        let fields = this.getFields(item);

                        for (let field in fields) {
                            let type: string = fields[field].type;
                            let desc: string = fields[field].description;

                            paramsChildren.push({
                                title: `${field}: ${type || field} ${desc ? '，说明：' + desc : ''}`, disableCheckbox: true,
                            });
                        }
                    } else {
                        let desc: string = item.description;
                        paramsChildren.push({
                            title: `${item.name}: ${item.type}，必填：${item.required ? '是' : '否'} ${desc ? '，说明：' + desc : ''}`, disableCheckbox: true,
                        });
                    }
                });

                // 响应结果
                let respChildren: any[] = [];
                let resp = meta.responses['200'];

                if (resp && resp.schema && resp.schema.$ref) {
                    // debugger
                    let fields = this.getFields(resp);

                    for (let field in fields) {
                        let type: string = fields[field].type;
                        let desc: string = fields[field].description;

                        respChildren.push({
                            title: `${field}: ${type || field} ${desc ? '，说明：' + desc : ''}`, disableCheckbox: true,
                        });
                    }
                }

                // 整理为树节点
                const children = data.children || [];

                children.push({
                    title: '请求参数',
                    disableCheckbox: true,
                    expand: true,
                    children: paramsChildren.length ? paramsChildren : [{ title: '无', disableCheckbox: true }]
                }, {
                    title: '响应结果',
                    disableCheckbox: true,
                    expand: true,
                    children: respChildren
                });

                this.$set(data, 'children', children);
                this.$set(data, 'expand', true);
                this.$set(data, 'isLoaded', true);
            }
        },

        /**
         * 获取实体字段
         * 
         * @param item 
         * @returns 
         */
        getFields(item: any): any | null {
            let arr: string[] = item.schema.$ref.split('/');
            let schema: string = arr.pop();
            let _schema = testData.definitions[schema];

            if (_schema)
                return _schema.properties;
            else console.warn('找不到元数据 ' + schema);

            return null;
        },

        treeSwaggerHandler(nodeArr, node): void {
            // console.log(arguments)
        },

        /**
         * 在过滤器中选择结果
         * 
         * @param value 
         */
        filterHandler(value: string): void {
            let arr: string[] = value.split(' ');
            let method: string = arr[0], url: string = arr[1];
            let target = this.SwaggerTreeRootNode.find((el: any) => el.node.method == method && el.node.title == url);

            if (!target)
                console.log('找不到目标节点', method, url);
            else {
                console.log(target)
                target.checked = true;
                target.expand = true;
                target.node.checked = true;
                target.node.expand = true;
                // this.$set( target.node, 'expand', true);
                this.$set(target, 'checked', true);
                this.$set(target.node, 'checked', true);
            }
        }

    }
};