import { xhr_get } from '../../util/xhr';
import test from './test-dataservice';

export default {
    data(): {} {
        return {
            DataServiceTreeData: test,
            DataServiceFilter: {
                keyword: "", data: ['foo', 'bar']
            },
        };
    },
    methods: {
        getDataService(): void {
            xhr_get(`${this.API}/datasource`, (j: RepsonseResult) => {
                // console.log(j.result);
                let DataServiceTreeData: any[] = [];
                // let i: number = 0;

                if (j.result) {
                    j.result.forEach((item: any) => {
                        DataServiceTreeData.push({
                            title: '数据源：' + item.name,
                            loading: false,
                            datasourceId: item.id,
                            urlDir: item.urlDir,
                            children: []
                        })
                    });
                }

                this.DataServiceTreeData = DataServiceTreeData;
            }, { start: 0, limit: 999, appId: window.sessionStorage.getItem('APP_ID') || 1432285183943376896 });
        },

        /**
         * 加载表列表和配置。点击 表名 时候执行
         */
        loadTablesCfg(item: any, callback: Function): void {
            let datasourceDir: string = item.urlDir;
            let datasourceId: number = item.datasourceId;

            xhr_get(`${this.API}/data_service?datasourceId=${datasourceId}`, (j: RepsonseResult) => {
                if (j.result) {
                    this.list = j.result;
                    let data: any[] = [];
                    let i: number = 0;

                    j.result.forEach((item: any) => {
                        let children: any[] = [];
                        let urlDir: string = item.urlDir; // 配置目录

                        if (item.json) {
                            let cfgs: any = JSON.parse(item.json);
                            // console.log(":::::::::", item);

                            for (let key in cfgs) {
                                let cfg = cfgs[key];
                                // console.log(":::::::::》》》》", cfg);
                                cfg.type = key;
                                cfg.datasourceId = datasourceId;
                                cfg.tableName = item.tableName;

                                if (!cfg.enable)
                                    continue;

                                let dir: string = "/" + datasourceDir + "/" + urlDir;
                                if (cfg.dir)
                                    dir += "/" + cfg.dir;

                                switch (key) {
                                    case 'info':
                                    case 'list':
                                        children.push(this.renderDataServiceNode(dir, "GET", cfg));
                                        break;
                                    case 'create':
                                        children.push(this.renderDataServiceNode(dir, "POST", cfg));
                                        break;
                                    case 'update':
                                        children.push(this.renderDataServiceNode(dir + "/?id=${id}", "PUT", cfg));
                                        break;
                                    case 'delete':
                                        children.push(this.renderDataServiceNode(dir + "/?id=${id}", "DELETE", cfg));
                                        break;
                                }
                            }
                        }

                        data.push({
                            title: item.tableName,
                            children: children
                        });
                    });

                    callback(data);
                } else {
                    this.$Message.error('该数据源下尚未有任何表的配置');
                    callback([]);
                }
            });
        },

        renderDataServiceNode(dir: string, method: string, cfg: any, isAddTypeNode?: boolean): {} {
            return {
                title: dir,
                render: (h: Function, { root, node, data }) => {
                    this.$set(data, 'method', method);
                    this.$set(data, 'cfg', cfg);

                    let nodeCfg = { // 节点事件的配置
                        on: {
                            click: () => {
                                this.appendDataServiceDetail(data);// 点击加载接口详情
                            }
                        }
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
                        arr.unshift(h('span', { class: 'api-type dataservice' }, '数')); // 头部加入

                    return arr;
                }
            }
        },

        /**
         * 点击加载接口详情
         * 
         * @param data 
         */
        appendDataServiceDetail(data: any): void {
            if (!data.isLoaded) {
                // 预先加载
                let tableFields: any[] = [];
                xhr_get(`${this.API}/data_service/getAllFieldsByDataSourceAndTablename?datasourceId=${data.cfg.datasourceId}&tableName=${data.cfg.tableName}`,
                    (j: RepsonseResult) => {
                        console.log(j)
                        //@ts-ignore
                        j.result.forEach(item => {
                            tableFields.push({
                                name: item.name, type: item.type, required: item.null == 'YES', description: item.comment
                            });
                        });
                    });

                // 请求参数
                let params: any[] = [];
                switch (data.cfg.type) {
                    case 'info':
                    case 'delete':
                        params.push({ name: 'id', type: 'int', required: true, description: '实体 id' });
                        break;
                    case 'list':
                        params.push({ name: 'start', type: 'int', required: true, description: '分页 start' });
                        params.push({ name: 'limit', type: 'int', required: true, description: '分页 limit' });
                        break;
                    case 'create':
                    case 'update':
                        params = tableFields;
                        break;
                }

                let paramsChildren: any[] = [];

                params.forEach((item: any) => {
                    let desc: string = item.description;
                    paramsChildren.push({ title: `${item.name}: ${item.type}，必填：${item.required ? '是' : '否'} ${desc ? '，说明：' + desc : ''}`, disableCheckbox: true });
                });

                // 响应结果
                let resp: any[] = [];
                switch (data.cfg.type) {
                    case 'info':
                    case 'list':
                        resp = tableFields;
                        break;
                    case 'create':
                        resp.push({ name: 'isOk', type: 'boolean', description: '是否创建成功' });
                        resp.push({ name: 'newlyId', type: 'long', description: '新增记录之 id' });
                        break;
                    case 'update':
                        resp.push({ name: 'isOk', type: 'boolean', description: '是否更新成功' });
                        break;
                    case 'delete':
                        resp.push({ name: 'isOk', type: 'boolean', description: '是否删除成功' });
                        break;
                }

                let respChildren: any[] = [];
                resp.forEach((item: any) => {
                    let desc: string = item.description;
                    respChildren.push({ title: `${item.name}: ${item.type} ${desc ? '，说明：' + desc : ''}`, disableCheckbox: true });
                });

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
                    children: respChildren ? respChildren : [{ title: '无', disableCheckbox: true }]
                });

                this.$set(data, 'children', children);
                this.$set(data, 'expand', true);
                this.$set(data, 'isLoaded', true);
            }
        },
    }
};