"use strict";
// Tree-like option control
aj.treeLike = {
    methods: {
        // 遍历各个元素，输出
        output: (function () {
            var stack = [];
            return function (map, cb) {
                stack.push(map);
                for (var i in map) {
                    map[i].level = stack.length; // 层数，也表示缩进多少个字符
                    cb(map[i], i);
                    var c = map[i].children;
                    if (c) {
                        for (var q = 0, p = c.length; q < p; q++)
                            this.output(c[q], cb);
                    }
                }
                stack.pop();
            };
        })(),
        // 递归查找父亲节点，根据传入 id
        findParent: function (map, id) {
            for (var i in map) {
                if (i == id)
                    return map[i];
                var c = map[i].children;
                if (c) {
                    for (var q = 0, p = c.length; q < p; q++) {
                        var result = this.findParent(c[q], id);
                        if (result != null)
                            return result;
                    }
                }
            }
            return null;
        },
        // 生成树，将扁平化的结构 还原为树状的结构
        // 父id 必须在子id之前，不然下面 findParent() 找不到后面的父节点，故这个数组必须先排序
        toTree: function (jsonArr) {
            if (!jsonArr)
                return;
            var m = {};
            for (var i = 0, j = jsonArr.length; i < j; i++) {
                var n = jsonArr[i];
                var parentNode = this.findParent(m, n.pid);
                if (parentNode == null) { // 没有父节点，那就表示这是根节点，保存之
                    m[n.id] = n; // id 是key，value 新建一对象
                }
                else { // 有父亲节点，作为孩子节点保存
                    var obj = {};
                    obj[n.id] = n;
                    if (!parentNode.children)
                        parentNode.children = [];
                    parentNode.children.push(obj);
                }
            }
            return m;
        },
        /**
         * 渲染 Option 标签的 DOM
         */
        rendererOption: function (json, select, selectedId, cfg) {
            if (cfg && cfg.makeAllOption) {
                var option = document.createElement('option');
                option.value = option.innerHTML = "全部分类";
                select.appendChild(option);
            }
            // 生成 option
            var temp = document.createDocumentFragment();
            this.output(this.toTree(json), function (node, nodeId) {
                var option = document.createElement('option'); // 节点
                option.value = nodeId;
                if (selectedId && selectedId == nodeId) // 选中的
                    option.selected = true;
                option.dataset['pid'] = node.pid;
                //option.style= "padding-left:" + (node.level - 1) +"rem;";
                option.innerHTML = new Array(node.level * 5).join('&nbsp;') + (node.level == 1 ? '' : '└─') + node.name;
                temp.appendChild(option);
            });
            select.appendChild(temp);
        }
    }
};
// 下拉分类选择器，异步请求远端获取分类数据
Vue.component('aj-tree-catelog-select', {
    mixins: [aj.treeLike],
    template: '<select :name="fieldName" @change="onSelected" class="aj-tree-catelog-select aj-select" style="width: 200px;"></select>',
    props: {
        catalogId: {
            type: Number, required: true
        },
        selectedCatalogId: {
            type: Number, required: false
        },
        fieldName: {
            type: String, default: 'catalogId'
        },
        isAutoJump: Boolean // 是否自动跳转 catalogId
    },
    mounted: function () {
        var _this = this;
        var fn = function (j) {
            var arr = [{ id: 0, name: "请选择分类" }];
            _this.rendererOption(arr.concat(j.result), _this.$el, _this.selectedCatalogId, { makeAllOption: false });
        };
        aj.xhr.get(this.ajResources.ctx + "/admin/tree-like/getListAndSubByParentId/" + this.catalogId + "/", fn);
    },
    methods: {
        onSelected: function ($event) {
            if (this.isAutoJump) {
                var el = $event.target, catalogId = el.selectedOptions[0].value;
                location.assign('?' + this.fieldName + '=' + catalogId);
            }
            else
                this.BUS.$emit('aj-tree-catelog-select-change', $event, this);
        }
    }
});
// 指定 id 的那个 option 选中
aj.selectOption = function (id) {
    this.$el.$('option', function (i) {
        if (i.value == id)
            i.selected = true;
    });
};
Vue.component('aj-tree-like-select', {
    mixins: [aj.treeLike],
    template: '<select :name="name" class="aj-select" @change="onSelected"></select>',
    props: {
        catalogId: {
            type: Number, required: true
        },
        selectedId: {
            type: Number, required: false
        },
        name: {
            type: String, required: false, default: 'catalogId'
        },
        api: {
            type: String, default: '/admin/tree-like/'
        }
    },
    mounted: function () {
        var _this = this;
        var url = this.ajResources.ctx + this.api + this.catalogId + "/";
        var fn = function (j) { return _this.rendererOption(j.result, _this.$el, _this.selectedCatalogId, { makeAllOption: false }); };
        aj.xhr.get(url, fn);
    },
    methods: {
        onSelected: function ($event) {
            var el = $event.target, catalogId = el.selectedOptions[0].value;
            this.$emit('selected', Number(catalogId));
        },
        select: aj.selectOption
    }
});
