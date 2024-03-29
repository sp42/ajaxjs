/* 生成下面结构 */
// let arr = [{
//     title: '文章',
//     children: [
//         {
//             method: 'GET',
//             path: "/foo/bar"
//         }
//         // {
//         //     title: "leaf 1-1-2",
//         //     render: (h, { root, node, data }) => {
//         //         return [
//         //             h("span", { class: "http-method get" }, "GET"),
//         //             h("span", data.title),
//         //         ];
//         //     },
//         // },
//     ]
// }];

ds_tree = {
    methods: {
        getTreeNode(children) {
            let arr = [];
            children.forEach(({ method, path, id }) => {

                arr.push({
                    title: path,
                    render: (h, { root, node, data }) => {
                        let nodeCfg = { // 节点事件的配置
                            on: {
                                click: () => {
                                    debugger
                                    this.allData.every(dml => {

                                        if (dml.id == id) {
                                            this.currentData = dml;
                                            console.log(dml);

                                            return false;
                                        }

                                        return true;
                                    });
                                }
                            }// 点击加载接口详情
                        };

                        // 右键点击事件
                        nodeCfg.on['contextmenu'] = (e) => {
                            e.preventDefault();
                            // this.contextSelectdNode.root = root;
                            // this.contextSelectdNode.node = node;

                            // // this.contextSelectdNode.data = data;
                            // this.$refs.contentFileMenu.$refs.reference = e.target;
                            // this.$refs.contentFileMenu.currentVisible = !this.$refs.contentFileMenu.currentVisible;
                        };

                        let arr = [
                            h('span', Object.assign({ class: 'http-method ' + method.toLowerCase() }, nodeCfg), method),
                            h('span', nodeCfg, path)
                        ];

                        return arr;
                    }
                });
            });

            return arr;
        }
    }
};

function getTreeData2(j, callback) {
    this.allData = this.allData.concat(j.data);

    // 转化为符合 tree 的结构
    let treeList = [];

    j.data.forEach(item => {
        let dmls = [];

        for (let i in item.data) {
            let method = 'UNKNOW', path = '/' + item.urlDir, id = item.id;

            if (i == 'create')
                method = "POST";
            else if (i == 'update')
                method = 'PUT';
            else if (i == 'list' || i == 'info')
                method = 'GET';
            else if (i == 'delete') {
                path += '/{id}'
                method = 'DELETE';
            } else if (i == 'others') {
                let others = item.data[i];

                if (others.length) {
                    others.forEach(other => {
                        let method = 'UNKNOW';

                        if (other.type == 'getOne')
                            method = "GET";

                        dmls.push({
                            method: method,
                            path: path + '/' + other.dir,
                            id: id
                        });
                    });
                }
            }

            dmls.push({
                method, path, id
            });
        }

        let treeNode = {
            title: item.name,
            children: dmls
        };

        treeList.push(treeNode);
    });

    let treeData = [];

    treeList.forEach(node => {
        let title = node.title, _children = node.children;
        let children = this.getTreeNode(_children);

        treeData.push({
            title: title,
            children: children
        });
    });

    // debugger
    callback(treeData);
}