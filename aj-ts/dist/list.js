"use strict";
/**
 * 相册列表
 */
Vue.component('aj-attachment-picture-list', {
    template: "\n        <table>\n            <tr>\n            <td>\n                <div class=\"label\">\u76F8\u518C\u56FE\uFF1A</div>\n                <ul>\n                    <li v-for=\"pic in pics\" style=\"float:left;margin-right:1%;text-align:center;\">\n                        <a :href=\"picCtx + pic.path\" target=\"_blank\"><img :src=\"picCtx + pic.path\" style=\"max-width: 100px;max-height: 100px;\" /></a><br />\n                        <a href=\"###\" @click=\"delPic(pic.id);\">\u5220 \u9664</a>\n                    </li>\n                </ul>\n            </td>\n            <td>\n                <aj-xhr-upload ref=\"attachmentPictureUpload\" :action=\"uploadUrl\" :is-img-upload=\"true\" :img-place=\"blankBg\"></aj-xhr-upload>\n            </td></tr>\n        </table>\n    ",
    props: {
        picCtx: String,
        uploadUrl: String,
        blankBg: String,
        delImgUrl: String,
        apiUrl: String
    },
    data: function () {
        return {
            pics: []
        };
    },
    mounted: function () {
        this.getData();
        this.$refs.attachmentPictureUpload.uploadOk_callback = this.getData;
    },
    methods: {
        getData: function () {
            var _this = this;
            aj.xhr.get(this.apiUrl, function (j) { return _this.pics = j.result; });
        },
        delPic: function (picId) {
            var _this = this;
            aj.showConfirm("确定删除相册图片？", function () {
                aj.xhr.dele(_this.delImgUrl + picId + "/", function (j) {
                    if (j.isOk)
                        _this.getData(); // 刷新
                });
            });
        }
    }
});

"use strict";
Vue.component('aj-simple-grid', {
    template: "\n        <div>\n            <form action=\"?\" method=\"GET\" style=\"float:right;\">\n                <input type=\"hidden\" name=\"searchField\" value=\"content\" />\n                <input type=\"text\" name=\"searchValue\" placeholder=\"\u8BF7\u8F93\u5165\u641C\u7D22\u4E4B\u5173\u952E\u5B57\" class=\"aj-input\" />\n                <button style=\"margin-top: 0;\" class=\"aj-btn\">\u641C\u7D22</button>\n            </form>\n            <table class=\"aj-grid ajaxjs-borderTable\">\n                <thead>\n                    <tr>\n                        <th v-for=\"key in columns\" @click=\"sortBy(key)\" :class=\"{ active: sortKey == key }\">\n                            {{ key | capitalize }}\n                            <span class=\"arrow\" :class=\"sortOrders[key] > 0 ? 'asc' : 'dsc'\"></span>\n                        </th>\n                    </tr>\n                </thead>\n                <tbody>\n                    <tr v-for=\"entry in filteredData\">\n                        <td v-for=\"key in columns\" v-html=\"entry[key]\"></td>\n                    </tr>\n                </tbody>\n            </table>\n        </div>\n    ",
    props: {
        data: Array,
        columns: Array,
        filterKey: String
    },
    data: function () {
        var sortOrders = {};
        this.columns.forEach(function (key) { return sortOrders[key] = 1; });
        return {
            sortKey: '',
            sortOrders: sortOrders
        };
    },
    computed: {
        filteredData: function () {
            var sortKey = this.sortKey;
            var filterKey = this.filterKey && this.filterKey.toLowerCase();
            var order = this.sortOrders[sortKey] || 1;
            var data = this.data;
            if (filterKey) {
                data = data.filter(function (row) {
                    return Object.keys(row).some(function (key) { return String(row[key]).toLowerCase().indexOf(filterKey) > -1; });
                });
            }
            if (sortKey) {
                data = data.slice().sort(function (a, b) {
                    a = a[sortKey];
                    b = b[sortKey];
                    return (a === b ? 0 : a > b ? 1 : -1) * order;
                });
            }
            return data;
        }
    },
    filters: {
        capitalize: function (str) {
            return str.charAt(0).toUpperCase() + str.slice(1);
        }
    },
    methods: {
        sortBy: function (key) {
            this.sortKey = key;
            this.sortOrders[key] = this.sortOrders[key] * -1;
        }
    }
});

"use strict";
var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};
var aj;
(function (aj) {
    var list;
    (function (list) {
        list.datastore = {
            props: {
                apiUrl: { type: String, required: true },
                hrefStr: { type: String, required: false },
                isPage: { type: Boolean, default: true },
                initPageSize: { type: Number, required: false, default: 9 },
                autoLoad: { type: Boolean, default: true },
                initBaseParam: { type: Object, default: function () { return {}; } },
                pageParamNames: { type: Array, default: function () { return ['start', 'limit']; } },
                onLoad: Function
            },
            data: function () {
                return {
                    baseParam: this.initBaseParam,
                    result: [],
                    extraParam: {},
                    realApiUrl: this.apiUrl,
                    pageSize: this.initPageSize,
                    total: 0,
                    totalPage: 0,
                    pageStart: 0,
                    currentPage: 0
                };
            },
            methods: {
                /**
                 * 分页，跳到第几页，下拉控件传入指定的页码
                 *
                 * @param this
                 * @param ev
                 */
                jumpPageBySelect: function (ev) {
                    var selectEl = ev.target;
                    var currentPage = selectEl.options[selectEl.selectedIndex].value;
                    this.pageStart = (Number(currentPage) - 1) * this.pageSize;
                    this.getData();
                },
                /**
                 * PageSize 改变时候重新分页
                 *
                 * @param this
                 * @param ev
                 */
                onPageSizeChange: function (ev) {
                    this.pageSize = Number(ev.target.value);
                    this.count();
                    this.getData();
                },
                count: function () {
                    var totalPage = this.total / this.pageSize, yushu = this.total % this.pageSize;
                    this.totalPage = parseInt(String(yushu == 0 ? totalPage : totalPage + 1));
                    //@ts-ignore
                    this.currentPage = parseInt((this.pageStart / this.pageSize) + 1);
                },
                /**
                 * 前一页
                 *
                 * @param this
                 */
                previousPage: function () {
                    this.pageStart -= this.pageSize;
                    //@ts-ignore
                    this.currentPage = parseInt((this.pageStart / this.pageSize) + 1);
                    this.getData();
                },
                /**
                 * 下一页
                 *
                 * @param this
                 */
                nextPage: function () {
                    this.pageStart += this.pageSize;
                    this.currentPage = (this.pageStart / this.pageSize) + 1;
                    this.getData();
                },
            }
        };
        var pageParams = {
            start: 'start',
            limit: 'limit'
        };
        /**
         * 一般情况下不会单独使用这个组件
         */
        Vue.component('aj-list', {
            mixins: [list.datastore],
            template: html(__makeTemplateObject(["\n\t\t\t<div class=\"aj-list\">\n\t\t\t\t<ul v-if=\"showDefaultUi\">\n\t\t\t\t\t<li v-for=\"(item, index) in result\">\n\t\t\t\t\t\t<slot v-bind=\"item\">\n\t\t\t\t\t\t\t<a href=\"#\" @click=\"show(item.id, index, $event)\" :id=\"item.id\">{{item.name}}</a>\n\t\t\t\t\t\t</slot>\n\t\t\t\t\t</li>\n\t\t\t\t</ul>\n\t\t\t\t<footer v-if=\"isPage\" class=\"pager\">\n\t\t\t\t\t<a v-if=\"pageStart > 0\" href=\"#\" @click=\"previousPage\">\u4E0A\u4E00\u9875</a>\n\t\t\t\t\t<a v-if=\"(pageStart > 0 ) && (pageStart + pageSize < total)\"\n\t\t\t\t\t\tstyle=\"text-decoration: none;\">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</a>\n\t\t\t\t\t<a v-if=\"pageStart + pageSize < total\" href=\"#\" @click=\"nextPage\">\u4E0B\u4E00\u9875</a>\n\t\t\t\t\t<a href=\"javascript:;\" @click=\"getData\"><i class=\"fa fa-refresh\" aria-hidden=\"true\"></i> \u5237\u65B0</a>\n\t\t\t\t\t<input type=\"hidden\" name=\"start\" :value=\"pageStart\" />\n\t\t\t\t\t\u9875\u6570\uFF1A{{currentPage}}/{{totalPage}} \u8BB0\u5F55\u6570\uFF1A{{pageStart}}/{{total}}\n\t\t\t\t\t\u6BCF\u9875\u8BB0\u5F55\u6570\uFF1A <input size=\"2\" title=\"\u8F93\u5165\u4E00\u4E2A\u6570\u5B57\u786E\u5B9A\u6BCF\u9875\u8BB0\u5F55\u6570\" type=\"text\" :value=\"pageSize\" @change=\"onPageSizeChange\" />\n\t\t\t\t\t\u8DF3\u8F6C\uFF1A\n\t\t\t\t\t<select @change=\"jumpPageBySelect\">\n\t\t\t\t\t\t<option :value=\"n\" v-for=\"n in totalPage\">{{n}}</option>\n\t\t\t\t\t</select>\n\t\t\t\t</footer>\n\t\t\t\t<div v-show=\"!!autoLoadWhenReachedBottom\" class=\"buttom\"></div>\n\t\t\t</div>\n\t\t"], ["\n\t\t\t<div class=\"aj-list\">\n\t\t\t\t<ul v-if=\"showDefaultUi\">\n\t\t\t\t\t<li v-for=\"(item, index) in result\">\n\t\t\t\t\t\t<slot v-bind=\"item\">\n\t\t\t\t\t\t\t<a href=\"#\" @click=\"show(item.id, index, $event)\" :id=\"item.id\">{{item.name}}</a>\n\t\t\t\t\t\t</slot>\n\t\t\t\t\t</li>\n\t\t\t\t</ul>\n\t\t\t\t<footer v-if=\"isPage\" class=\"pager\">\n\t\t\t\t\t<a v-if=\"pageStart > 0\" href=\"#\" @click=\"previousPage\">\u4E0A\u4E00\u9875</a>\n\t\t\t\t\t<a v-if=\"(pageStart > 0 ) && (pageStart + pageSize < total)\"\n\t\t\t\t\t\tstyle=\"text-decoration: none;\">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</a>\n\t\t\t\t\t<a v-if=\"pageStart + pageSize < total\" href=\"#\" @click=\"nextPage\">\u4E0B\u4E00\u9875</a>\n\t\t\t\t\t<a href=\"javascript:;\" @click=\"getData\"><i class=\"fa fa-refresh\" aria-hidden=\"true\"></i> \u5237\u65B0</a>\n\t\t\t\t\t<input type=\"hidden\" name=\"start\" :value=\"pageStart\" />\n\t\t\t\t\t\u9875\u6570\uFF1A{{currentPage}}/{{totalPage}} \u8BB0\u5F55\u6570\uFF1A{{pageStart}}/{{total}}\n\t\t\t\t\t\u6BCF\u9875\u8BB0\u5F55\u6570\uFF1A <input size=\"2\" title=\"\u8F93\u5165\u4E00\u4E2A\u6570\u5B57\u786E\u5B9A\u6BCF\u9875\u8BB0\u5F55\u6570\" type=\"text\" :value=\"pageSize\" @change=\"onPageSizeChange\" />\n\t\t\t\t\t\u8DF3\u8F6C\uFF1A\n\t\t\t\t\t<select @change=\"jumpPageBySelect\">\n\t\t\t\t\t\t<option :value=\"n\" v-for=\"n in totalPage\">{{n}}</option>\n\t\t\t\t\t</select>\n\t\t\t\t</footer>\n\t\t\t\t<div v-show=\"!!autoLoadWhenReachedBottom\" class=\"buttom\"></div>\n\t\t\t</div>\n\t\t"])),
            props: {
                showDefaultUi: { type: Boolean, default: true },
                isShowFooter: { type: Boolean, default: true },
                autoLoadWhenReachedBottom: { type: String, default: '' },
                isDataAppend: { type: Boolean, default: false }
            },
            mounted: function () {
                this.autoLoad && this.getData();
                // this.BUS.$on('base-param-change', this.onBaseParamChange.bind(this));
                if (!!this.autoLoadWhenReachedBottom) {
                    // var scrollSpy = new aj.scrollSpy({ scrollInElement: aj(this.autoLoadWhenReachedBottom), spyOn: thish.$el.$('.buttom') });
                    // scrollSpy.onScrollSpyBackInSight = e => this.nextPage();
                }
            },
            methods: {
                foo: function () {
                    window.alert(9);
                },
                getData: function () {
                    var _this = this;
                    this.lastRequestParam = {};
                    aj.apply(this.lastRequestParam, this.baseParam);
                    aj.apply(this.lastRequestParam, this.extraParam);
                    initPageParams.call(this);
                    aj.xhr.get(this.apiUrl, this.onLoad || (function (j) {
                        if (j.result) {
                            if (j.total === undefined)
                                aj.alert('JSON 缺少 total 字段');
                            if (j.total == 0 || j.result.length == 0)
                                aj.alert('没有找到任何记录');
                            _this.result = j.result;
                            if (_this.isPage) {
                                _this.total = j.total;
                                _this.count();
                            }
                        }
                        _this.$emit('pager-result', _this.result);
                    }), this.lastRequestParam);
                },
                /**
                 * 复位
                 *
                 * @param this
                 */
                reset: function () {
                    this.total = this.totalPage = this.pageStart = this.currentPage = 0;
                    this.pageSize = this.initPageSize;
                },
                doAjaxGet: function (j) {
                    if (this.isPage) {
                        this.total = j.total;
                        //@ts-ignore
                        this.result = this.isDataAppend ? this.result.concat(j.result) : j.result;
                        this.count();
                    }
                    else
                        this.result = j.result;
                },
                onBaseParamChange: function (params) {
                    aj.apply(this.baseParam, params);
                    this.pageStart = 0; // 每次 baseParam 被改变，都是从第一笔开始
                    this.getData();
                }
            },
            watch: {
                baseParam: function () {
                    this.getData();
                }
            }
        });
        /**
         * 生成分页参数的名字
         *
         * @param this
         */
        function initPageParams() {
            var params = {};
            params[this.pageParamNames[0]] = this.pageStart;
            params[this.pageParamNames[1]] = this.pageSize;
            this.isPage && aj.apply(this.lastRequestParam, params);
        }
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));

"use strict";
/**
 * 动态组件，可否换为  component？
 */
Vue.component('aj-cell-renderer', {
    props: {
        html: { type: String, default: '' },
        form: Object
    },
    render: function (h) {
        if (this.html.indexOf('<aj-') != -1) {
            var com = Vue.extend({
                template: this.html,
                props: {
                    form: Object
                }
            });
            return h(com, {
                props: {
                    form: this.form
                }
            });
        }
        else {
            return this._v(this.html); // html
        }
    }
});
Vue.component('aj-grid-select-row', {
    template: '<a href="#" @click="fireSelect">选择</a>',
    props: { type: { type: String, required: true } },
    methods: {
        fireSelect: function () {
            //@ts-ignore
            this.BUS.$emit('on-' + this.type + '-select', this.$parent.form);
        }
    }
});
Vue.component('aj-grid-open-link', {
    template: '<a href="#" @click="fireSelect"><i class="fa fa-external-link"></i> 详情</a>',
    methods: {
        fireSelect: function () {
            //@ts-ignore
            this.BUS.$emit('on-open-link-clk', this.$parent.form);
        }
    }
});

"use strict";

"use strict";
var aj;
(function (aj) {
    var list;
    (function (list) {
        var grid;
        (function (grid) {
            grid.SectionModel = {
                data: function () {
                    return {
                        isSelectAll: false,
                        selected: {},
                        selectedTotal: 0,
                        maxRows: 0 // 最多的行数，用于判断是否全选
                    };
                },
                mounted: function () {
                    this.BUS && this.BUS.$on('on-delete-btn-clk', this.batchDelete);
                },
                methods: {
                    /**
                     * 批量删除
                     */
                    batchDelete: function () {
                        var _this = this;
                        if (this.selectedTotal > 0) {
                            aj.showConfirm('确定批量删除记录？', function () {
                                for (var id in _this.selected) {
                                    aj.xhr.dele(_this.apiUrl + "/" + id + "/", function (j) {
                                        console.log(j);
                                    });
                                }
                            });
                        }
                        else
                            aj.alert('未选择记录');
                    },
                    /**
                     * 全选
                     */
                    selectAll: function () {
                        var _this = this;
                        var checkAll = function (item) {
                            item.checked = true;
                            var id = item.dataset.id;
                            if (!id)
                                throw '需要提供 id 在 DOM 属性中';
                            _this.$set(_this.selected, Number(id), true);
                        }, diskCheckAll = function (item) {
                            item.checked = false;
                            var id = item.dataset.id;
                            if (!id)
                                throw '需要提供 id 在 DOM 属性中';
                            _this.$set(_this.selected, Number(id), false);
                        };
                        this.$el.$('table .selectCheckbox input[type=checkbox]', this.selectedTotal === this.maxRows ? diskCheckAll : checkAll);
                    }
                },
                watch: {
                    selected: {
                        handler: function (_new) {
                            var j = 0;
                            // clear falses
                            for (var i in this.selected) {
                                if (this.selected[i] === false)
                                    delete this.selected[i];
                                else
                                    j++;
                            }
                            this.selectedTotal = j;
                            if (j === this.maxRows)
                                this.$el.$('.top-checkbox').checked = true;
                            else
                                this.$el.$('.top-checkbox').checked = false;
                        },
                        deep: true
                    }
                }
            };
            Vue.component('aj-grid', {
                mixins: [grid.SectionModel],
                template: '<div class="aj-grid"><slot v-bind="this"></slot></div>',
                props: {
                    apiUrl: { type: String, required: true }
                },
                data: function () {
                    return {
                        list: [],
                        updateApi: null,
                        showAddNew: false
                    };
                },
                mounted: function () {
                    var _this = this;
                    this.$children.forEach(function (child) {
                        switch (child.$options._componentTag) {
                            case 'aj-entity-toolbar':
                                _this.$toolbar = child;
                                break;
                            case 'aj-grid-inline-edit-row':
                                _this.$row = child;
                                break;
                            case 'aj-list':
                                _this.$store = child;
                                break;
                        }
                    });
                    this.$store.$on("pager-result", function (result) {
                        console.log(result);
                        _this.list = result;
                        _this.maxRows = result.length;
                    });
                    // this.$store.autoLoad && this.$store.getDataData();
                },
                methods: {
                    /**
                     * 按下【新建】按钮时候触发的事件，你可以覆盖这个方法提供新的事件
                     *
                     * @param this
                     */
                    onCreateClk: function () {
                        this.showAddNew = true;
                    },
                    /**
                     * 重新加载数据
                     *
                     * @param this
                     */
                    reload: function () {
                        this.$store.getData();
                    },
                    /**
                     *
                     * @param this
                     */
                    onDirtySaveClk: function () {
                        var _this = this;
                        var dirties = getDirty.call(this);
                        if (!dirties.length) {
                            aj.msg.show('没有修改过的记录');
                            return;
                        }
                        dirties.forEach(function (item) {
                            aj.xhr.put(_this.apiUrl + "/" + item.id + "/", function (j) {
                                if (j.isOk) {
                                    _this.list.forEach(function (item) {
                                        if (item.dirty)
                                            delete item.dirty;
                                    });
                                    aj.msg.show('修改记录成功');
                                }
                            }, item.dirty);
                        });
                    }
                }
            });
            /**
             * 获取修改过的数据
             *
             * @param this
             */
            function getDirty() {
                var dirties = [];
                this.list.forEach(function (item) {
                    if (item.dirty) { // 有这个 dirty 就表示修改过的
                        // item.dirty.id = item.id;
                        dirties.push(item);
                    }
                });
                return dirties;
            }
        })(grid = list.grid || (list.grid = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));

"use strict";
var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};
Vue.component('aj-grid-inline-edit-row-create', {
    template: html(__makeTemplateObject(["\n        <tr class=\"aj-grid-inline-edit-row isEditMode\">\n            <td><input type=\"checkbox\" /></td>\n            <td></td>\n            <td v-for=\"key in columns\" style=\"padding:0\" class=\"cell\" @dblclick=\"dbEdit\">\n                <aj-select v-if=\"key != null && key.type == 'select'\" :name=\"key.name\" :options=\"key.data\"\n                    style=\"width: 200px;\"></aj-select>\n                <input v-if=\"key != null && !key.type\" type=\"text\" size=\"0\" :name=\"key\" />\n            </td>\n            <td class=\"control\">\n                <span @click=\"addNew\"><img :src=\"ajResources.commonAsset + '/icon/update.gif'\" />\u65B0\u589E</span>\n                <span @click=\"$parent.showAddNew = false\"><img :src=\"ajResources.commonAsset + '/icon/delete.gif'\" /> \u64A4\u9500</span>\n            </td>\n        </tr>\n    "], ["\n        <tr class=\"aj-grid-inline-edit-row isEditMode\">\n            <td><input type=\"checkbox\" /></td>\n            <td></td>\n            <td v-for=\"key in columns\" style=\"padding:0\" class=\"cell\" @dblclick=\"dbEdit\">\n                <aj-select v-if=\"key != null && key.type == 'select'\" :name=\"key.name\" :options=\"key.data\"\n                    style=\"width: 200px;\"></aj-select>\n                <input v-if=\"key != null && !key.type\" type=\"text\" size=\"0\" :name=\"key\" />\n            </td>\n            <td class=\"control\">\n                <span @click=\"addNew\"><img :src=\"ajResources.commonAsset + '/icon/update.gif'\" />\u65B0\u589E</span>\n                <span @click=\"$parent.showAddNew = false\"><img :src=\"ajResources.commonAsset + '/icon/delete.gif'\" /> \u64A4\u9500</span>\n            </td>\n        </tr>\n    "])),
    props: {
        columns: { type: Array, required: true },
        createApi: { type: String, required: false, default: '.' }
    },
    methods: {
        /**
         * 编辑按钮事件
         *
         * @param this
         */
        addNew: function () {
            var _this = this;
            var map = {};
            this.$el.$('*[name]', function (i) { return map[i.name] = i.value; });
            this.BUS.$emit('before-add-new', map);
            aj.xhr.post(this.createApi, function (j) {
                if (j && j.isOk) {
                    aj.msg.show('新建实体成功');
                    _this.$el.$('input[name]', function (i) {
                        i.value = '';
                    });
                    // @ts-ignore
                    _this.$parent.reload();
                    // @ts-ignore
                    _this.$parent.showAddNew = false;
                }
                else if (j && j.msg) {
                    aj.msg.show(j.msg);
                }
            }, map);
        },
        /**
         *
         * @param this
         * @param $event
         */
        dbEdit: function ($event) {
            this.isEditMode = !this.isEditMode;
            var el = $event.target;
            if (el.tagName !== 'INPUT')
                el = el.$('input');
            setTimeout(function () { return el && el.focus(); }, 200);
        }
    }
});

"use strict";
var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};
var aj;
(function (aj) {
    var list;
    (function (list) {
        var grid;
        (function (grid) {
            /**
                总体就是一个普通的 HTML Table，程序是通过读取 JSON 数据（实际 JS 数组）里面的字段，
                然后通过动态添加表格的方法逐行添加 tr，td，在添加行的过程中根据需要设置样式，添加方法等。
                每一行数据（即 tr 的 DOM 对象）都有 id 属性，其值就等于数据中的idColumn对应的列的值
             */
            Vue.component('aj-grid-inline-edit-row', {
                template: html(__makeTemplateObject(["\n            <tr class=\"aj-grid-inline-edit-row\" :class=\"{editing: isEditMode}\">\n                <td v-if=\"showCheckboxCol\" class=\"selectCheckbox\">\n                    <input type=\"checkbox\" @change=\"selectCheckboxChange\" :data-id=\"id\" />\n                </td>\n                <td v-if=\"showIdCol\">{{id}}</td>\n                <td v-for=\"cellRenderer in columns\" :style=\"styleModifly\" class=\"cell\" @dblclick=\"dbEdit\">\n                    <aj-cell-renderer v-if=\"!isEditMode\" :html=\"renderCell(data, cellRenderer)\" :form=\"data\"></aj-cell-renderer>\n                    <aj-cell-renderer v-if=\"isEditMode && cellRenderer && cellRenderer.editMode\"\n                        :html=\"rendererEditMode(data, cellRenderer)\" :form=\"data\">\n                    </aj-cell-renderer>\n                    <input type=\"text\" v-if=\"canEdit(cellRenderer)\" size=\"0\" v-model=\"data[cellRenderer]\" />\n                </td>\n                <td v-if=\"showControl\" class=\"control\">\n                    <aj-cell-renderer v-if=\"controlUi\" :html=\"controlUi\" :form=\"data\"></aj-cell-renderer>\n                    <span @click=\"onEditClk\" class=\"edit\"><i class=\"fa fa-pencil\" aria-hidden=\"true\"></i>\n                        {{!isEditMode ? \"\u7F16\u8F91\" : \"\u786E\u5B9A\"}}</span>\n                    <span @click=\"dele(id)\" class=\"delete\"><i class=\"fa fa-times\" aria-hidden=\"true\"></i> \u5220\u9664</span>\n                </td>\n            </tr>\n        "], ["\n            <tr class=\"aj-grid-inline-edit-row\" :class=\"{editing: isEditMode}\">\n                <td v-if=\"showCheckboxCol\" class=\"selectCheckbox\">\n                    <input type=\"checkbox\" @change=\"selectCheckboxChange\" :data-id=\"id\" />\n                </td>\n                <td v-if=\"showIdCol\">{{id}}</td>\n                <td v-for=\"cellRenderer in columns\" :style=\"styleModifly\" class=\"cell\" @dblclick=\"dbEdit\">\n                    <aj-cell-renderer v-if=\"!isEditMode\" :html=\"renderCell(data, cellRenderer)\" :form=\"data\"></aj-cell-renderer>\n                    <aj-cell-renderer v-if=\"isEditMode && cellRenderer && cellRenderer.editMode\"\n                        :html=\"rendererEditMode(data, cellRenderer)\" :form=\"data\">\n                    </aj-cell-renderer>\n                    <input type=\"text\" v-if=\"canEdit(cellRenderer)\" size=\"0\" v-model=\"data[cellRenderer]\" />\n                </td>\n                <td v-if=\"showControl\" class=\"control\">\n                    <aj-cell-renderer v-if=\"controlUi\" :html=\"controlUi\" :form=\"data\"></aj-cell-renderer>\n                    <span @click=\"onEditClk\" class=\"edit\"><i class=\"fa fa-pencil\" aria-hidden=\"true\"></i>\n                        {{!isEditMode ? \"\u7F16\u8F91\" : \"\u786E\u5B9A\"}}</span>\n                    <span @click=\"dele(id)\" class=\"delete\"><i class=\"fa fa-times\" aria-hidden=\"true\"></i> \u5220\u9664</span>\n                </td>\n            </tr>\n        "])),
                props: {
                    rowData: { type: Object, required: true },
                    showIdCol: { type: Boolean, default: true },
                    columns: Array,
                    showCheckboxCol: { type: Boolean, default: true },
                    showControl: { type: Boolean, default: true },
                    filterField: Array,
                    enableInlineEdit: { type: Boolean, default: false },
                    deleApi: String,
                    controlUi: String // 自定义“操作”按钮，这里填组件的名字
                },
                data: function () {
                    return {
                        id: this.rowData.id,
                        data: this.rowData,
                        isEditMode: false
                    };
                },
                mounted: function () {
                    for (var i in this.data) // 监视每个字段
                        this.$watch('data.' + i, makeWatch.call(this, i));
                },
                computed: {
                    filterData: function () {
                        var data = JSON.parse(JSON.stringify(this.data)); // 剔除不要的字段
                        delete data.id;
                        delete data.dirty;
                        if (this.filterField && this.filterField.length)
                            this.filterField.forEach(function (i) { return delete data[i]; });
                        return data;
                    },
                    /**
                     *
                     *
                     * @param this
                     */
                    styleModifly: function () {
                        return {
                            padding: this.isEditMode ? 0 : '',
                        };
                    }
                },
                methods: {
                    /**
                     * 没有指定编辑器的情况下，使用 input 作为编辑器
                     *
                     * @param this
                     * @param cellRenderer
                     */
                    canEdit: function (cellRenderer) {
                        return this.isEditMode && !isFixedField.call(this, cellRenderer) && !(cellRenderer.editMode);
                    },
                    /**
                     * 渲染单元格
                     *
                     * @param this
                     * @param data
                     * @param cellRenderer
                     */
                    renderCell: function (data, cellRenderer) {
                        var v;
                        if (cellRenderer === '')
                            return '';
                        if (typeof cellRenderer == 'string') {
                            v = data[cellRenderer];
                            return v + "";
                        }
                        if (typeof cellRenderer == 'function') {
                            v = cellRenderer(data);
                            return v;
                        }
                        if (typeof cellRenderer == 'object') {
                            var cfg = cellRenderer;
                            if (!!cfg.renderer)
                                v = cfg.renderer(data);
                            // if (typeof key.showMode === 'function')
                            //     return key.showMode(data);
                            // if (typeof key.showMode === 'string')
                            //     return data[key.showMode];
                        }
                        return (v === null ? '' : v) + '';
                    },
                    /**
                     * 编辑按钮事件
                     *
                     * @param this
                     */
                    onEditClk: function () {
                        if (this.enableInlineEdit)
                            this.isEditMode = !this.isEditMode;
                        //@ts-ignore    
                        else if (this.$parent.onEditClk) // 打开另外的编辑界面
                            //@ts-ignore    
                            this.$parent.onEditClk(this.id);
                    },
                    /**
                     * 渲染编辑模式下的行
                     *
                     * @param this
                     * @param data
                     * @param cfg
                     */
                    rendererEditMode: function (data, cellRenderer) {
                        if (typeof cellRenderer === 'string')
                            return cellRenderer.toString();
                        if (cellRenderer.editMode && typeof cellRenderer.editRenderer === 'function')
                            return cellRenderer.editRenderer(data);
                        return "NULL";
                    },
                    /**
                     * 双击单元格进入编辑
                     *
                     * @param ev
                     */
                    dbEdit: function (ev) {
                        if (!this.enableInlineEdit)
                            return;
                        this.isEditMode = !this.isEditMode;
                        if (this.isEditMode) {
                            var el_1 = ev.target;
                            setTimeout(function () {
                                if (el_1.tagName !== 'INPUT')
                                    el_1 = el_1.$('input');
                                el_1 && el_1.focus();
                            }, 200);
                        }
                    },
                    /**
                     *
                     * @param this
                     * @param ev
                     */
                    selectCheckboxChange: function (ev) {
                        var checkbox = ev.target, parent = this.$parent;
                        if (checkbox.checked)
                            parent.$set(parent.selected, this.id, true);
                        //this.$parent.selected[this.id] = true;
                        else
                            parent.$set(parent.selected, this.id, false);
                    },
                    /**
                     * 删除记录
                     *
                     * @param this
                     * @param id
                     */
                    dele: function (id) {
                        var _this = this;
                        aj.showConfirm("\u786E\u5B9A\u5220\u9664\u8BB0\u5F55 id:[" + id + "] \u5417\uFF1F", function () {
                            return aj.xhr.dele(_this.$parent.apiUrl + "/" + id + "/", function (j) {
                                if (j.isOk) {
                                    aj.msg.show('删除成功');
                                    _this.$parent.reload();
                                }
                            });
                        });
                    }
                }
            });
            /**
             * 生成该字段的 watch 函数
             *
             * @param this
             * @param field
             */
            function makeWatch(field) {
                return function (_new) {
                    var arr = this.$parent.list, data;
                    for (var i = 0, j = arr.length; i < j; i++) { // 已知 id 找到原始数据
                        if (this.id && (String(arr[i].id) == this.id)) {
                            data = arr[i];
                            break;
                        }
                    }
                    if (!data)
                        throw '找不到匹配的实体！目标 id: ' + this.id;
                    if (!data.dirty)
                        data.dirty = {
                            id: this.id
                        };
                    data.dirty[field] = _new; // 保存新的值，key 是字段名
                };
            }
            /**
             * 是否固定的字段，固定的字段不能被编辑
             *
             * @param this
             * @param cellRenderer
             */
            function isFixedField(cellRenderer) {
                if (this.filterField && this.filterField.length) {
                    for (var i = 0, j = this.filterField.length; i < j; i++) {
                        if (this.filterField[i] == cellRenderer)
                            return true;
                    }
                }
                return false;
            }
        })(grid = list.grid || (list.grid = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));

"use strict";
var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};
/**
 * 工具条
 */
Vue.component('aj-entity-toolbar', {
    template: html(__makeTemplateObject(["\n        <div class=\"toolbar\"> \n            <form v-if=\"search\" class=\"right\">\n                <input type=\"text\" name=\"keyword\" placeholder=\"\u8BF7\u8F93\u5165\u5173\u952E\u5B57\" size=\"12\" />\n                <button @click=\"doSearch\"><i class=\"fa fa-search\" style=\"color:#417BB5;\"></i>\u641C\u7D22</button>\n            </form>\n            <aj-form-between-date v-if=\"betweenDate\" class=\"right\"></aj-form-between-date>\n            <ul>\n                <li v-if=\"create\" @click=\"$emit('on-create-btn-clk')\"><i class=\"fa fa-plus\" style=\"color:#0a90f0;\"></i> \u65B0\u5EFA</li>\n                <li v-if=\"save\" @click=\"$emit('on-save-btn-clk')\"><i class=\"fa fa-floppy-o\" style=\"color:rgb(205, 162, 4);\"></i> \u4FDD\u5B58</li>\n                <li v-if=\"deleBtn\" @click=\"$emit('on-delete-btn-clk')\"><i class=\"fa fa-trash-o\" style=\"color:red;\"></i> \u5220\u9664</li>\n                <li v-if=\"excel\"><i class=\"fa fa-file-excel-o\" style=\"color:green;\"></i> \u5BFC\u51FA</li>\n                <slot></slot>\n                \n            </ul>\n        </div>\n    "], ["\n        <div class=\"toolbar\"> \n            <form v-if=\"search\" class=\"right\">\n                <input type=\"text\" name=\"keyword\" placeholder=\"\u8BF7\u8F93\u5165\u5173\u952E\u5B57\" size=\"12\" />\n                <button @click=\"doSearch\"><i class=\"fa fa-search\" style=\"color:#417BB5;\"></i>\u641C\u7D22</button>\n            </form>\n            <aj-form-between-date v-if=\"betweenDate\" class=\"right\"></aj-form-between-date>\n            <ul>\n                <li v-if=\"create\" @click=\"$emit('on-create-btn-clk')\"><i class=\"fa fa-plus\" style=\"color:#0a90f0;\"></i> \u65B0\u5EFA</li>\n                <li v-if=\"save\" @click=\"$emit('on-save-btn-clk')\"><i class=\"fa fa-floppy-o\" style=\"color:rgb(205, 162, 4);\"></i> \u4FDD\u5B58</li>\n                <li v-if=\"deleBtn\" @click=\"$emit('on-delete-btn-clk')\"><i class=\"fa fa-trash-o\" style=\"color:red;\"></i> \u5220\u9664</li>\n                <li v-if=\"excel\"><i class=\"fa fa-file-excel-o\" style=\"color:green;\"></i> \u5BFC\u51FA</li>\n                <slot></slot>\n                \n            </ul>\n        </div>\n    "])),
    props: {
        betweenDate: { type: Boolean, default: true },
        create: { type: Boolean, default: true },
        save: { type: Boolean, default: true },
        excel: { type: Boolean, default: false },
        deleBtn: { type: Boolean, default: true },
        search: { type: Boolean, default: true }
    },
    methods: {
        /**
         * 获取关键字进行搜索
         *
         * @param this
         * @param e
         */
        doSearch: function (e) {
            e.preventDefault();
            aj.apply(this.$parent.$store.extraParam, { keyword: aj.form.utils.getFormFieldValue(this.$el, 'input[name=keyword]') });
            this.$parent.$store.reload();
        }
    }
});

"use strict";
Vue.component('aj-tree-like-select', {
    template: '<select :name="fieldName" class="aj-select" @change="onSelected" style="min-width:180px;"></select>',
    props: {
        fieldName: { type: String, required: false, default: 'catalogId' },
        apiUrl: { type: String, default: function () {
                return aj.ctx + '/admin/tree-like/';
            } },
        isAutoLoad: { type: Boolean, default: true },
        isAutoJump: Boolean,
        initFieldValue: String
    },
    data: function () {
        return {
            fieldValue: this.initFieldValue
        };
    },
    mounted: function () {
        this.isAutoLoad && this.getData();
    },
    methods: {
        onSelected: function (ev) {
            var el = ev.target;
            this.fieldValue = el.selectedOptions[0].value;
            if (this.isAutoJump)
                location.assign('?' + this.fieldName + '=' + this.fieldValue);
            else
                this.BUS && this.BUS.$emit('aj-tree-catelog-select-change', ev, this);
        },
        getData: function () {
            var _this = this;
            var fn = function (j) {
                var arr = [{ id: 0, name: "请选择分类" }];
                aj.list.tree.rendererOption(arr.concat(j.result), _this.$el, _this.fieldValue, { makeAllOption: false });
                if (_this.fieldValue) // 有指定的选中值
                    aj.form.utils.selectOption.call(_this, _this.fieldValue);
            };
            // aj.xhr.get(this.ajResources.ctx + this.apiUrl + "/admin/tree-like/getListAndSubByParentId/", fn);
            aj.xhr.get(this.apiUrl, fn);
        }
    }
});

"use strict";
var aj;
(function (aj) {
    var list;
    (function (list) {
        var tree;
        (function (tree) {
            /**
             * 排序
             * 父id 必须在子 id 之前，不然下面 findParent() 找不到后面的父节点，故先排序
             *
             * @param jsonArray
             */
            function makeTree(jsonArray) {
                var arr = [];
                for (var i = 0, j = jsonArray.length; i < j; i++) {
                    var n = jsonArray[i];
                    if (n.pid === -1)
                        arr.push(n);
                    else {
                        var parentNode = findParentInArray(arr, n.pid);
                        if (parentNode) {
                            if (!parentNode.children)
                                parentNode.children = [];
                            parentNode.children.push(n);
                        }
                        else
                            console.log('parent not found!');
                    }
                }
                return arr;
            }
            tree.makeTree = makeTree;
            /**
             * 根据传入 id 在一个数组中查找父亲节点
             *
             * @param jsonArray
             * @param id
             */
            function findParentInArray(jsonArray, id) {
                for (var i = 0, j = jsonArray.length; i < j; i++) {
                    var map = jsonArray[i];
                    if (map.id == id)
                        return map;
                    if (map.children) {
                        var result = findParentInArray(map.children, id);
                        if (result != null)
                            return result;
                    }
                }
                return null;
            }
            tree.findParentInArray = findParentInArray;
            /**
             * 根据传入 id 查找父亲节点
             *
             * @param map
             * @param id
             */
            function findParentInMap(map, id) {
                for (var i in map) {
                    if (i == id)
                        return map[i];
                    var c = map[i].children;
                    if (c) {
                        for (var q = 0, p = c.length; q < p; q++) {
                            var result = findParentInMap(c[q], id);
                            if (result != null)
                                return result;
                        }
                    }
                }
                return null;
            }
            tree.findParentInMap = findParentInMap;
            var stack = [];
            /**
             * 遍历各个元素，输出
             *
             * @param map
             * @param cb
             */
            function output(map, cb) {
                stack.push(map);
                for (var i in map) {
                    map[i].level = stack.length; // 层数，也表示缩进多少个字符
                    cb(map[i], i);
                    var c = map[i].children;
                    if (c) {
                        for (var q = 0, p = c.length; q < p; q++)
                            output(c[q], cb);
                    }
                }
                stack.pop();
            }
            tree.output = output;
            /**
             * 生成树，将扁平化的数组结构 还原为树状的结构
             * 父id 必须在子id之前，不然下面 findParent() 找不到后面的父节点，故前提条件是，这个数组必须先排序
             *
             * @param jsonArray
             */
            function toTree(jsonArray) {
                if (!jsonArray)
                    return {};
                var m = {};
                for (var i = 0, j = jsonArray.length; i < j; i++) {
                    var n = jsonArray[i], parentNode = findParentInMap(m, n.pid + "");
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
            }
            tree.toTree = toTree;
            /**
             * 渲染 Option 标签的 DOM
             *
             * @param jsonArray
             * @param select
             * @param selectedId
             * @param cfg
             */
            function rendererOption(jsonArray, select, selectedId, cfg) {
                if (cfg && cfg.makeAllOption) {
                    var option = document.createElement('option');
                    option.value = option.innerHTML = "全部分类";
                    select.appendChild(option);
                }
                // 生成 option
                var temp = document.createDocumentFragment();
                output(toTree(jsonArray), function (node, nodeId) {
                    var option = document.createElement('option'); // 节点
                    option.value = nodeId;
                    if (selectedId && selectedId == nodeId) // 选中的
                        option.selected = true;
                    option.dataset['pid'] = node.pid + "";
                    //option.style= "padding-left:" + (node.level - 1) +"rem;";
                    option.innerHTML = new Array(node.level * 5).join('&nbsp;') + (node.level == 1 ? '' : '└─') + node.name;
                    temp.appendChild(option);
                });
                select.appendChild(temp);
            }
            tree.rendererOption = rendererOption;
        })(tree = list.tree || (list.tree = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));

"use strict";
var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};
Vue.component('aj-tree-item', {
    template: html(__makeTemplateObject(["\n        <li>\n            <div :class=\"{bold: isFolder, node: true}\" @click=\"toggle\">\n                <span>\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7</span>{{model.name}}\n                <span v-if=\"isFolder\">[{{open ? '-' : '+'}}]</span>\n            </div>\n            <ul v-show=\"open\" v-if=\"isFolder\" :class=\"{show: open}\">\n                <aj-tree-item class=\"item\" v-for=\"(model, index) in model.children\" :key=\"index\" :model=\"model\">\n                </aj-tree-item>\n                <li v-if=\"allowAddNode\" class=\"add\" @click=\"addChild\">+</li>\n            </ul>\n        </li>\n    "], ["\n        <li>\n            <div :class=\"{bold: isFolder, node: true}\" @click=\"toggle\">\n                <span>\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7</span>{{model.name}}\n                <span v-if=\"isFolder\">[{{open ? '-' : '+'}}]</span>\n            </div>\n            <ul v-show=\"open\" v-if=\"isFolder\" :class=\"{show: open}\">\n                <aj-tree-item class=\"item\" v-for=\"(model, index) in model.children\" :key=\"index\" :model=\"model\">\n                </aj-tree-item>\n                <li v-if=\"allowAddNode\" class=\"add\" @click=\"addChild\">+</li>\n            </ul>\n        </li>\n    "])),
    props: {
        model: Object,
        allowAddNode: { type: Boolean, default: false } // 是否允许添加新节点
    },
    data: function () {
        return { open: false };
    },
    computed: {
        isFolder: function () {
            return !!(this.model.children && this.model.children.length);
        }
    },
    methods: {
        /**
         * 点击节点时的方法
         *
         * @param this
         */
        toggle: function () {
            if (this.isFolder)
                this.open = !this.open;
            this.BUS && this.BUS.$emit('tree-node-click', this.model);
        },
        /**
         * 变为文件夹
         *
         * @param this
         */
        changeType: function () {
            if (!this.isFolder) {
                Vue.set(this.model, 'children', []);
                this.addChild();
                this.open = true;
            }
        },
        addChild: function () {
            this.model.children.push({
                name: 'new stuff'
            });
        }
    }
});

"use strict";
Vue.component('aj-tree', {
    template: '<ul class="aj-tree"><aj-tree-item :model="treeData"></aj-tree-item></ul>',
    props: {
        apiUrl: String,
        topNodeName: String // 根节点显示名称
    },
    data: function () {
        return {
            treeData: { name: this.topNodeName || 'TOP', children: null }
        };
    },
    mounted: function () {
        var _this = this;
        // aj.xhr.get(this.ajResources.ctx + this.url, (j: RepsonseResult) => this.treeData.children = makeTree(j.result));
        aj.xhr.get(this.apiUrl, function (j) { return _this.treeData.children = aj.list.tree.makeTree(j.result); });
        // 递归组件怎么事件上报呢？通过事件 bus
        this.BUS && this.BUS.$on('treenodeclick', function (data) { return _this.$emit('treenodeclick', data); });
    }
});
