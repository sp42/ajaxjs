"use strict";

/**
 * 起始时间、截止时间的范围选择
 */
Vue.component('aj-form-between-date', {
    template: html(__makeTemplateObject(["\n        <form action=\".\" method=\"GET\" class=\"dateRange\" @submit=\"valid\">\n            <aj-form-calendar-input :date-only=\"true\" :position-fixed=\"true\" placeholder=\"\u8D77\u59CB\u65F6\u95F4\" field-name=\"startDate\" ></aj-form-calendar-input>\n            - <aj-form-calendar-input :date-only=\"true\" :position-fixed=\"true\" placeholder=\"\u622A\u81F3\u65F6\u95F4\" field-name=\"endDate\"></aj-form-calendar-input>\n            <button class=\"aj-btn\">\u6309\u65F6\u95F4\u7B5B\u9009</button>\n        </form>    \n    "], ["\n        <form action=\".\" method=\"GET\" class=\"dateRange\" @submit=\"valid\">\n            <aj-form-calendar-input :date-only=\"true\" :position-fixed=\"true\" placeholder=\"\u8D77\u59CB\u65F6\u95F4\" field-name=\"startDate\" ></aj-form-calendar-input>\n            - <aj-form-calendar-input :date-only=\"true\" :position-fixed=\"true\" placeholder=\"\u622A\u81F3\u65F6\u95F4\" field-name=\"endDate\"></aj-form-calendar-input>\n            <button class=\"aj-btn\">\u6309\u65F6\u95F4\u7B5B\u9009</button>\n        </form>    \n    "])),
    props: {
        isAjax: { type: Boolean, default: true } // 是否 AJAX 模式
    },
    methods: {
        valid: function (ev) {
            var start = this.$el.$('input[name=startDate]').value, end = this.$el.$('input[name=endDate]').value;
            if (!start || !end) {
                aj.alert("输入数据不能为空");
                ev.preventDefault();
                return;
            }
            if (new Date(start) > new Date(end)) {
                aj.alert("起始日期不能晚于结束日期");
                ev.preventDefault();
                return;
            }
            //@ts-ignore
            if (this.isAjax) {
                ev.preventDefault();
                var grid = this.$parent.$parent;
                aj.apply(grid.$refs.pager.extraParam, {
                    startDate: start, endDate: end
                });
                grid.reload();
            }
        }
    }
});

"use strict";

Vue.component('aj-form-calendar-input', {
    template: html(__makeTemplateObject(["\n        <div class=\"aj-form-calendar-input\" :class=\"{'show-time': showTime}\" @mouseover=\"onMouseOver\">\n            <div class=\"icon fa fa-calendar\"></div>\n            <input :placeholder=\"placeholder\" size=\"12\" :name=\"fieldName\" :value=\"date + (dateOnly ? '' : ' ' + time)\"\n                type=\"text\" autocomplete=\"off\" />\n            <aj-form-calendar ref=\"calendar\" :show-time=\"showTime\" @pick-date=\"recEvent\" @pick-time=\"recTimeEvent\">\n            </aj-form-calendar>\n        </div>\n    "], ["\n        <div class=\"aj-form-calendar-input\" :class=\"{'show-time': showTime}\" @mouseover=\"onMouseOver\">\n            <div class=\"icon fa fa-calendar\"></div>\n            <input :placeholder=\"placeholder\" size=\"12\" :name=\"fieldName\" :value=\"date + (dateOnly ? '' : ' ' + time)\"\n                type=\"text\" autocomplete=\"off\" />\n            <aj-form-calendar ref=\"calendar\" :show-time=\"showTime\" @pick-date=\"recEvent\" @pick-time=\"recTimeEvent\">\n            </aj-form-calendar>\n        </div>\n    "])),
    data: function () {
        return {
            date: this.fieldValue,
            time: ''
        };
    },
    props: {
        fieldName: { type: String, required: true },
        fieldValue: { type: String, required: false, default: '' },
        dateOnly: { type: Boolean, required: false, default: true },
        showTime: false,
        positionFixed: Boolean,
        placeholder: { type: String, default: '请输入日期' } // 提示文字
    },
    watch: {
        fieldValue: function (n) {
            this.date = n;
        }
    },
    mounted: function () {
        if (this.positionFixed) {
            var el = this.$el.$('.aj-form-calendar');
            el.classList.add('positionFixed');
        }
        // 2012-07-08
        // firefox中解析 new Date('2012/12-23') 不兼容，提示invalid date 无效的日期
        // Chrome下可以直接将其格式化成日期格式，且时分秒默认为零
        // var arr = date.split('-'), now = new Date(arr[0], arr[1] - 1, arr[2],
        // " ", "", " ");
        if (this.fieldValue) {
            var arr = this.fieldValue.split(' ')[0], _arr = arr.split('-');
            // @ts-ignore
            this.$refs.calendar.date = new Date(arr[0], arr[1] - 1, arr[2], " ", "", " ");
        }
    },
    methods: {
        recTimeEvent: function (time) {
            this.time = time;
        },
        recEvent: function (date) {
            this.date = date.trim();
        },
        onMouseOver: function (ev) {
            if (this.positionFixed) {
                var el = ev.currentTarget, b = el.getBoundingClientRect(), c = this.$el.$('.aj-form-calendar');
                c.style.top = (b.top + el.clientHeight - 0) + 'px';
                c.style.left = ((b.left - 0) + 0) + 'px';
            }
        }
    }
});

"use strict";


var aj;
(function (aj) {
    var form;
    (function (form) {
        /**
         * 日期选择器
         */
        var Calendar = /** @class */ (function (_super) {
            __extends(Calendar, _super);
            function Calendar() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = "aj-form-calendar";
                _this.template = html(__makeTemplateObject(["\n            <div class=\"aj-form-calendar\">\n                <div class=\"selectYearMonth\">\n                    <a href=\"###\" @click=\"getDate('preYear')\" class=\"preYear\" title=\"\u4E0A\u4E00\u5E74\">&lt;</a>\n                    <select @change=\"setMonth\" v-model=\"month\">\n                        <option value=\"1\">\u4E00\u6708</option>\n                        <option value=\"2\">\u4E8C\u6708</option>\n                        <option value=\"3\">\u4E09\u6708</option>\n                        <option value=\"4\">\u56DB\u6708</option>\n                        <option value=\"5\">\u4E94\u6708</option>\n                        <option value=\"6\">\u516D\u6708</option>\n                        <option value=\"7\">\u4E03\u6708</option>\n                        <option value=\"8\">\u516B\u6708</option>\n                        <option value=\"9\">\u4E5D\u6708</option>\n                        <option value=\"10\">\u5341\u6708</option>\n                        <option value=\"11\">\u5341\u4E00\u6708</option>\n                        <option value=\"12\">\u5341\u4E8C\u6708</option>\n                    </select>\n                    <a href=\"###\" @click=\"getDate('nextYear')\" class=\"nextYear\" title=\"\u4E0B\u4E00\u5E74\">&gt;</a>\n                </div>\n                <div class=\"showCurrentYearMonth\">\n                    <span class=\"showYear\">{{year}}</span>/<span class=\"showMonth\">{{month}}</span>\n                </div>\n                <table>\n                    <thead>\n                        <tr>\n                            <td>\u65E5</td>\n                            <td>\u4E00</td>\n                            <td>\u4E8C</td>\n                            <td>\u4E09</td>\n                            <td>\u56DB</td>\n                            <td>\u4E94</td>\n                            <td>\u516D</td>\n                        </tr>\n                    </thead>\n                    <tbody @click=\"pickDay\"></tbody>\n                </table>\n                <div v-if=\"showTime\" class=\"showTime\">\n                    \u65F6 <select class=\"hour aj-select\">\n                        <option v-for=\"n in 24\">{{n}}</option>\n                    </select>\n                    \u5206 <select class=\"minute aj-select\">\n                        <option v-for=\"n in 61\">{{n - 1}}</option>\n                    </select>\n                    <a href=\"#\" @click=\"pickupTime\">\u9009\u62E9\u65F6\u95F4</a>\n                </div>\n            </div>\n        "], ["\n            <div class=\"aj-form-calendar\">\n                <div class=\"selectYearMonth\">\n                    <a href=\"###\" @click=\"getDate('preYear')\" class=\"preYear\" title=\"\u4E0A\u4E00\u5E74\">&lt;</a>\n                    <select @change=\"setMonth\" v-model=\"month\">\n                        <option value=\"1\">\u4E00\u6708</option>\n                        <option value=\"2\">\u4E8C\u6708</option>\n                        <option value=\"3\">\u4E09\u6708</option>\n                        <option value=\"4\">\u56DB\u6708</option>\n                        <option value=\"5\">\u4E94\u6708</option>\n                        <option value=\"6\">\u516D\u6708</option>\n                        <option value=\"7\">\u4E03\u6708</option>\n                        <option value=\"8\">\u516B\u6708</option>\n                        <option value=\"9\">\u4E5D\u6708</option>\n                        <option value=\"10\">\u5341\u6708</option>\n                        <option value=\"11\">\u5341\u4E00\u6708</option>\n                        <option value=\"12\">\u5341\u4E8C\u6708</option>\n                    </select>\n                    <a href=\"###\" @click=\"getDate('nextYear')\" class=\"nextYear\" title=\"\u4E0B\u4E00\u5E74\">&gt;</a>\n                </div>\n                <div class=\"showCurrentYearMonth\">\n                    <span class=\"showYear\">{{year}}</span>/<span class=\"showMonth\">{{month}}</span>\n                </div>\n                <table>\n                    <thead>\n                        <tr>\n                            <td>\u65E5</td>\n                            <td>\u4E00</td>\n                            <td>\u4E8C</td>\n                            <td>\u4E09</td>\n                            <td>\u56DB</td>\n                            <td>\u4E94</td>\n                            <td>\u516D</td>\n                        </tr>\n                    </thead>\n                    <tbody @click=\"pickDay\"></tbody>\n                </table>\n                <div v-if=\"showTime\" class=\"showTime\">\n                    \u65F6 <select class=\"hour aj-select\">\n                        <option v-for=\"n in 24\">{{n}}</option>\n                    </select>\n                    \u5206 <select class=\"minute aj-select\">\n                        <option v-for=\"n in 61\">{{n - 1}}</option>\n                    </select>\n                    <a href=\"#\" @click=\"pickupTime\">\u9009\u62E9\u65F6\u95F4</a>\n                </div>\n            </div>\n        "]));
                _this.watch = {
                    date: function () {
                        this.year = this.date.getFullYear();
                        this.month = this.date.getMonth() + 1;
                        this.render();
                    }
                };
                _this.showTime = Boolean; // 是否显示时间，即“时分秒”
                _this.date = new Date();
                _this.year = new Date().getFullYear();
                _this.month = new Date().getMonth() + 1;
                _this.day = 1;
                return _this;
            }
            Calendar.prototype.data = function () {
                var date = new Date;
                return {
                    date: date,
                    year: date.getFullYear(),
                    month: date.getMonth() + 1,
                    day: 1
                };
            };
            Calendar.prototype.mounted = function () {
                this.$options.watch.date.call(this);
            };
            /**
             * 画日历
             */
            Calendar.prototype.render = function () {
                var arr = this.getDateArr(), // 用来保存日期列表
                frag = document.createDocumentFragment(); // 插入日期
                while (arr.length) {
                    var row = document.createElement("tr"); // 每个星期插入一个 tr
                    for (var i = 1; i <= 7; i++) { // 每个星期有7天
                        var cell = document.createElement("td");
                        if (arr.length) {
                            var d = arr.shift();
                            if (d) {
                                cell.innerHTML = d + "";
                                var text = this.year + '-' + this.month + '-' + d;
                                cell.className = 'day day_' + text;
                                cell.title = text; // 保存日期在 title 属性
                                var on = new Date(this.year, this.month - 1, d);
                                // 判断是否今日
                                if (isSameDay(on, this.date)) {
                                    cell.classList.add('onToday');
                                    // this.onToday && this.onToday(cell);// 点击 今天 时候触发的事件
                                }
                                // 判断是否选择日期
                                // this.selectDay && this.onSelectDay && this.isSameDay(on, this.selectDay) &&
                                // this.onSelectDay(cell);
                            }
                        }
                        row.appendChild(cell);
                    }
                    frag.appendChild(row);
                }
                // 先清空内容再插入(ie的table不能用innerHTML)
                // while (el.hasChildNodes())
                // el.removeChild(el.firstChild);
                var tbody = this.$el.$("table tbody");
                tbody.innerHTML = '';
                tbody.appendChild(frag);
            };
            /**
             * 获取指定的日期
             *
             * @param dateType
             * @param month
             */
            Calendar.prototype.getDate = function (dateType, month) {
                var nowYear = this.date.getFullYear(), nowMonth = this.date.getMonth() + 1; // 当前日期
                switch (dateType) {
                    case 'preMonth': // 上一月
                        this.date = new Date(nowYear, nowMonth - 2, 1);
                        break;
                    case 'nextMonth': // 下一月
                        this.date = new Date(nowYear, nowMonth, 1);
                        break;
                    case 'setMonth': // 指定月份
                        this.date = new Date(nowYear, month - 1, 1);
                        break;
                    case 'preYear': // 上一年
                        this.date = new Date(nowYear - 1, nowMonth - 1, 1);
                        break;
                    case 'nextYear': // 下一年
                        this.date = new Date(nowYear + 1, nowMonth - 1, 1);
                        break;
                }
            };
            /**
             *
             *
             * @param $event
             */
            Calendar.prototype.setMonth = function (ev) {
                var el = ev.target;
                this.getDate('setMonth', Number(el.selectedOptions[0].value));
            };
            /**
             * 获取空白的非上月天数 + 当月天数
             *
             * @param this
             */
            Calendar.prototype.getDateArr = function () {
                var arr = [];
                // 用 当月第一天 在一周中的日期值 作为 当月 离 第一天的天数
                for (var i = 1, firstDay = new Date(this.year, this.month - 1, 1).getDay(); i <= firstDay; i++)
                    arr.push(0);
                // 用 当月最后一天 在一个月中的 日期值 作为 当月的天数
                for (var i = 1, monthDay = new Date(this.year, this.month, 0).getDate(); i <= monthDay; i++)
                    arr.push(i);
                return arr;
            };
            /**
             * 获取日期
             *
             * @param $event
             */
            Calendar.prototype.pickDay = function (ev) {
                var el = ev.target, date = el.title;
                this.$emit('pick-date', date);
                return date;
            };
            /**
             *
             * @param $event
             */
            Calendar.prototype.pickupTime = function ($event) {
                var hour = this.$el.$('.hour'), minute = this.$el.$('.minute'), time = hour.selectedOptions[0].value + ':' + minute.selectedOptions[0].value;
                this.$emit('pick-time', time);
            };
            return Calendar;
        }(aj.VueComponent));
        form.Calendar = Calendar;
        /**
         * 判断两个日期是否同一日
         *
         * @param d1
         * @param d2
         */
        function isSameDay(d1, d2) {
            return (d1.getFullYear() == d2.getFullYear() && d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate());
        }
        new Calendar().register();
    })(form = aj.form || (aj.form = {}));
})(aj || (aj = {}));

"use strict";


var aj;
(function (aj) {
    var form;
    (function (form) {
        /**
         * 全国省市区
         * 写死属性
         */
        var ChinaArea = /** @class */ (function (_super) {
            __extends(ChinaArea, _super);
            function ChinaArea() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = "aj-china-area";
                _this.template = html(__makeTemplateObject(["\n\t\t\t<div class=\"aj-china-area\">\n\t\t\t\t<span>\u7701</span>\n\t\t\t\t<select v-model=\"province\" class=\"aj-select\" style=\"width:120px;\" :name=\"provinceName || 'locationProvince'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in addressData[86]\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t\t<span>\u5E02 </span>\n\t\t\t\t<select v-model=\"city\" class=\"aj-select\" style=\"width:120px;\" :name=\"cityName || 'locationCity'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in citys\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t\t<span>\u533A</span>\n\t\t\t\t<select v-model=\"district\" class=\"aj-select\" style=\"width:120px;\" :name=\"districtName || 'locationDistrict'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in districts\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t</div>\n\t\t"], ["\n\t\t\t<div class=\"aj-china-area\">\n\t\t\t\t<span>\u7701</span>\n\t\t\t\t<select v-model=\"province\" class=\"aj-select\" style=\"width:120px;\" :name=\"provinceName || 'locationProvince'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in addressData[86]\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t\t<span>\u5E02 </span>\n\t\t\t\t<select v-model=\"city\" class=\"aj-select\" style=\"width:120px;\" :name=\"cityName || 'locationCity'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in citys\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t\t<span>\u533A</span>\n\t\t\t\t<select v-model=\"district\" class=\"aj-select\" style=\"width:120px;\" :name=\"districtName || 'locationDistrict'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in districts\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t</div>\n\t\t"]));
                _this.provinceCode = String;
                _this.cityCode = String;
                _this.districtCode = String;
                _this.provinceName = String;
                _this.cityName = String;
                _this.districtName = String;
                _this.computed = {
                    citys: function () {
                        if (!this.province)
                            return {};
                        return this.addressData[this.province];
                    },
                    districts: function () {
                        if (!this.city)
                            return {};
                        return this.addressData[this.city];
                    }
                };
                return _this;
            }
            ChinaArea.prototype.data = function () {
                return {
                    province: this.provinceCode || '',
                    city: this.cityCode || '',
                    district: this.districtCode || '',
                    //@ts-ignore
                    addressData: window.China_AREA
                };
            };
            // watch = { // 令下一级修改
            // 	province(val: any, oldval: any) {
            // 		if (val !== oldval)
            // 			this.city = '';
            // 	},
            // 	city(val: any, oldval: any) {
            // 		if (val !== oldval)
            // 			this.district = '';
            // 	}
            // };
            ChinaArea.prototype.mounted = function () {
                //@ts-ignore
                if (!window.China_AREA)
                    throw '中国行政区域数据 脚本没导入';
            };
            return ChinaArea;
        }(aj.VueComponent));
        form.ChinaArea = ChinaArea;
        new ChinaArea().register();
    })(form = aj.form || (aj.form = {}));
})(aj || (aj = {}));

"use strict";

var aj;
(function (aj) {
    var form;
    (function (form_1) {
        Vue.component('aj-edit-form', {
            template: html(__makeTemplateObject(["\n            <form class=\"aj-table-form\" :action=\"getInfoApi + (isCreate ? '' : info.id + '/')\" :method=\"isCreate ? 'POST' : 'PUT'\">\n                <h3>{{isCreate ? \"\u65B0\u5EFA\" : \"\u7F16\u8F91\" }}{{uiName}}</h3>\n                <!-- \u4F20\u9001 id \u53C2\u6570 -->\n                <input v-if=\"!isCreate\" type=\"hidden\" name=\"id\" :value=\"info.id\" />\n                <slot v-bind:info=\"info\"></slot>\n                <div class=\"aj-btnsHolder\">\n                    <button><img :src=\"ajResources.commonAsset + '/icon/save.gif'\" /> {{isCreate ? \"\u65B0\u5EFA\":\"\u4FDD\u5B58\"}}</button>\n                    <button onclick=\"this.up('form').reset();return false;\">\u590D \u4F4D</button>\n                    <button v-if=\"!isCreate\" v-on:click.prevent=\"del()\">\n                        <img :src=\"ajResources.commonAsset + '/icon/delete.gif'\" /> \u5220 \u9664\n                    </button>\n                    <button @click.prevent=\"close\">\u5173\u95ED</button>\n                </div>\n            </form>\n        "], ["\n            <form class=\"aj-table-form\" :action=\"getInfoApi + (isCreate ? '' : info.id + '/')\" :method=\"isCreate ? 'POST' : 'PUT'\">\n                <h3>{{isCreate ? \"\u65B0\u5EFA\" : \"\u7F16\u8F91\" }}{{uiName}}</h3>\n                <!-- \u4F20\u9001 id \u53C2\u6570 -->\n                <input v-if=\"!isCreate\" type=\"hidden\" name=\"id\" :value=\"info.id\" />\n                <slot v-bind:info=\"info\"></slot>\n                <div class=\"aj-btnsHolder\">\n                    <button><img :src=\"ajResources.commonAsset + '/icon/save.gif'\" /> {{isCreate ? \"\u65B0\u5EFA\":\"\u4FDD\u5B58\"}}</button>\n                    <button onclick=\"this.up(\\'form\\').reset();return false;\">\u590D \u4F4D</button>\n                    <button v-if=\"!isCreate\" v-on:click.prevent=\"del()\">\n                        <img :src=\"ajResources.commonAsset + '/icon/delete.gif'\" /> \u5220 \u9664\n                    </button>\n                    <button @click.prevent=\"close\">\u5173\u95ED</button>\n                </div>\n            </form>\n        "])),
            props: {
                isCreate: Boolean,
                uiName: String,
                apiUrl: { type: String, required: true } // 获取实体详情的接口地址 
            },
            data: function () {
                return {
                    id: 0,
                    info: {},
                };
            },
            mounted: function () {
                var _this = this;
                aj.xhr.form(this.$el, function (j) {
                    if (j) {
                        if (j.isOk) {
                            var msg_1 = (_this.isCreate ? "新建" : "保存") + _this.uiName + "成功";
                            aj.msg.show(msg_1);
                            _this.$parent.close();
                        }
                        else
                            aj.msg.show(j.msg);
                    }
                }, {
                    beforeSubmit: function (form, json) {
                        //json.content = App.$refs.htmleditor.getValue({cleanWord : eval('${aj_allConfig.article.cleanWordTag}'), encode : true});
                        return true;
                    }
                });
            },
            methods: {
                load: function (id, cb) {
                    var _this = this;
                    this.id = id;
                    aj.xhr.get(this.apiUrl + id + "/", function (j) {
                        _this.info = j.result;
                        cb && cb(j);
                    });
                },
                close: function () {
                    if (this.$parent.$options._componentTag === 'aj-layer')
                        //@ts-ignore
                        this.$parent.close();
                    else
                        history.back();
                },
                /**
                 * 执行删除
                 *
                 * @param this
                 */
                del: function () {
                    var id = form_1.utils.getFormFieldValue(this.$el, 'input[name=id]'), title = form_1.utils.getFormFieldValue(this.$el, 'input[name=name]');
                    aj.showConfirm("\u8BF7\u786E\u5B9A\u5220\u9664\u8BB0\u5F55\uFF1A\n" + title + "\uFF1F", function () {
                        return aj.xhr.dele("../" + id + "/", function (j) {
                            if (j.isOk) {
                                aj.msg.show('删除成功！');
                                //setTimeout(() => location.reload(), 1500);
                            }
                            else
                                aj.alert('删除失败！');
                        });
                    });
                }
            }
        });
    })(form = aj.form || (aj.form = {}));
})(aj || (aj = {}));

"use strict";


var aj;
(function (aj) {
    var form;
    (function (form) {
        /**
         * HTML 在綫編輯器
         *
         * 注意：必须提供一个 <slot> 包含有 <textarea class="hide" name="content">${info.content}</textarea>
         */
        var HtmlEditor = /** @class */ (function (_super) {
            __extends(HtmlEditor, _super);
            function HtmlEditor() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = "aj-form-html-editor";
                _this.template = html(__makeTemplateObject(["\n            <div class=\"aj-form-html-editor\">\n                <ul class=\"toolbar\" @click=\"onToolBarClk\">\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u4F53\" class=\"fa-font\"></i>\n                        <div class=\"fontfamilyChoser\" @click=\"onFontfamilyChoserClk\">\n                            <a style=\"font-family: '\u5B8B\u4F53'\">\u5B8B\u4F53</a>\n                            <a style=\"font-family: '\u9ED1\u4F53'\">\u9ED1\u4F53</a>\n                            <a style=\"font-family: '\u6977\u4F53'\">\u6977\u4F53</a>\n                            <a style=\"font-family: '\u96B6\u4E66'\">\u96B6\u4E66</a>\n                            <a style=\"font-family: '\u5E7C\u5706'\">\u5E7C\u5706</a>\n                            <a style=\"font-family: 'Microsoft YaHei'\">Microsoft YaHei</a>\n                            <a style=\"font-family: Arial\">Arial</a>\n                            <a style=\"font-family: 'Arial Narrow'\">Arial Narrow</a>\n                            <a style=\"font-family: 'Arial Black'\">Arial Black</a>\n                            <a style=\"font-family: 'Comic Sans MS'\">Comic Sans MS</a>\n                            <a style=\"font-family: Courier\">Courier</a>\n                            <a style=\"font-family: System\">System</a>\n                            <a style=\"font-family: 'Times New Roman'\">Times New Roman</a>\n                            <a style=\"font-family: Verdana\">Verdana</a>\n                        </div>\n                    </li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u53F7\" class=\"fa-header\"></i>\n                        <div class=\"fontsizeChoser\" @click=\"onFontsizeChoserClk\">\n                            <a style=\"font-size: xx-small; \">\u6781\u5C0F</a>\n                            <a style=\"font-size: x-small;  \">\u7279\u5C0F</a>\n                            <a style=\"font-size: small;    \">\u5C0F</a>\n                            <a style=\"font-size: medium;   \">\u4E2D</a>\n                            <a style=\"font-size: large;    \">\u5927</a>\n                            <a style=\"font-size: x-large;  \">\u7279\u5927</a>\n                            <a style=\"font-size: xx-large; line-height: 140%\">\u6781\u5927</a>\n                        </div>\n                    </li>\n                    <li><i title=\"\u52A0\u7C97\" class=\"bold fa-bold\"></i></li>\n                    <li><i title=\"\u659C\u4F53\" class=\"italic fa-italic\"></i></li>\n                    <li><i title=\"\u4E0B\u5212\u7EBF\" class=\"underline fa-underline\"></i></li>\n                    <li><i title=\"\u5DE6\u5BF9\u9F50\" class=\"justifyleft fa-align-left\"></i></li>\n                    <li><i title=\"\u4E2D\u95F4\u5BF9\u9F50\" class=\"justifycenter fa-align-center\"></i></li>\n                    <li><i title=\"\u53F3\u5BF9\u9F50\" class=\"justifyright fa-align-right\"></i></li>\n                    <li><i title=\"\u6570\u5B57\u7F16\u53F7\" class=\"insertorderedlist fa-list-ol\"></i></li>\n                    <li><i title=\"\u9879\u76EE\u7F16\u53F7\" class=\"insertunorderedlist fa-list-ul\"></i></li>\n                    <li><i title=\"\u589E\u52A0\u7F29\u8FDB\" class=\"outdent fa-outdent\"></i></li>\n                    <li><i title=\"\u51CF\u5C11\u7F29\u8FDB\" class=\"indent fa-indent\"></i></li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u4F53\u989C\u8272\" class=\"fa-paint-brush\"></i>\n                        <div class=\"fontColor colorPicker\" v-html=\"createColorPickerHTML()\" @click=\"onFontColorPicker\"></div>\n                    </li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u80CC\u666F\u989C\u8272\" class=\"fa-pencil\"></i>\n                        <div class=\"bgColor colorPicker\" v-html=\"createColorPickerHTML()\" @click=\"onFontBgColorPicker\"></div>\n                    </li>\n                    <li><i title=\"\u589E\u52A0\u94FE\u63A5\" class=\"createLink fa-link\"></i></li>\n                    <li><i title=\"\u589E\u52A0\u56FE\u7247\" class=\"insertImage fa-file-image-o\"></i></li>\n                    <li><i title=\"\u4E00\u952E\u5B58\u56FE\" class=\"saveRemoteImage2Local fa-hdd-o\"></i></li>\n                    <li><i title=\"\u6E05\u7406 HTML\" class=\"cleanHTML fa-eraser\"></i></li>\n                    <li><i title=\"\u5207\u6362\u5230\u4EE3\u7801\" class=\"switchMode fa-code\"></i></li>\n                </ul>\n            \n                <div class=\"editorBody\">\n                    <iframe srcdoc=\"<html><body></body></html>\"></iframe>\n                    <slot></slot>\n                </div>\n            </div>\n        "], ["\n            <div class=\"aj-form-html-editor\">\n                <ul class=\"toolbar\" @click=\"onToolBarClk\">\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u4F53\" class=\"fa-font\"></i>\n                        <div class=\"fontfamilyChoser\" @click=\"onFontfamilyChoserClk\">\n                            <a style=\"font-family: '\u5B8B\u4F53'\">\u5B8B\u4F53</a>\n                            <a style=\"font-family: '\u9ED1\u4F53'\">\u9ED1\u4F53</a>\n                            <a style=\"font-family: '\u6977\u4F53'\">\u6977\u4F53</a>\n                            <a style=\"font-family: '\u96B6\u4E66'\">\u96B6\u4E66</a>\n                            <a style=\"font-family: '\u5E7C\u5706'\">\u5E7C\u5706</a>\n                            <a style=\"font-family: 'Microsoft YaHei'\">Microsoft YaHei</a>\n                            <a style=\"font-family: Arial\">Arial</a>\n                            <a style=\"font-family: 'Arial Narrow'\">Arial Narrow</a>\n                            <a style=\"font-family: 'Arial Black'\">Arial Black</a>\n                            <a style=\"font-family: 'Comic Sans MS'\">Comic Sans MS</a>\n                            <a style=\"font-family: Courier\">Courier</a>\n                            <a style=\"font-family: System\">System</a>\n                            <a style=\"font-family: 'Times New Roman'\">Times New Roman</a>\n                            <a style=\"font-family: Verdana\">Verdana</a>\n                        </div>\n                    </li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u53F7\" class=\"fa-header\"></i>\n                        <div class=\"fontsizeChoser\" @click=\"onFontsizeChoserClk\">\n                            <a style=\"font-size: xx-small; \">\u6781\u5C0F</a>\n                            <a style=\"font-size: x-small;  \">\u7279\u5C0F</a>\n                            <a style=\"font-size: small;    \">\u5C0F</a>\n                            <a style=\"font-size: medium;   \">\u4E2D</a>\n                            <a style=\"font-size: large;    \">\u5927</a>\n                            <a style=\"font-size: x-large;  \">\u7279\u5927</a>\n                            <a style=\"font-size: xx-large; line-height: 140%\">\u6781\u5927</a>\n                        </div>\n                    </li>\n                    <li><i title=\"\u52A0\u7C97\" class=\"bold fa-bold\"></i></li>\n                    <li><i title=\"\u659C\u4F53\" class=\"italic fa-italic\"></i></li>\n                    <li><i title=\"\u4E0B\u5212\u7EBF\" class=\"underline fa-underline\"></i></li>\n                    <li><i title=\"\u5DE6\u5BF9\u9F50\" class=\"justifyleft fa-align-left\"></i></li>\n                    <li><i title=\"\u4E2D\u95F4\u5BF9\u9F50\" class=\"justifycenter fa-align-center\"></i></li>\n                    <li><i title=\"\u53F3\u5BF9\u9F50\" class=\"justifyright fa-align-right\"></i></li>\n                    <li><i title=\"\u6570\u5B57\u7F16\u53F7\" class=\"insertorderedlist fa-list-ol\"></i></li>\n                    <li><i title=\"\u9879\u76EE\u7F16\u53F7\" class=\"insertunorderedlist fa-list-ul\"></i></li>\n                    <li><i title=\"\u589E\u52A0\u7F29\u8FDB\" class=\"outdent fa-outdent\"></i></li>\n                    <li><i title=\"\u51CF\u5C11\u7F29\u8FDB\" class=\"indent fa-indent\"></i></li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u4F53\u989C\u8272\" class=\"fa-paint-brush\"></i>\n                        <div class=\"fontColor colorPicker\" v-html=\"createColorPickerHTML()\" @click=\"onFontColorPicker\"></div>\n                    </li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u80CC\u666F\u989C\u8272\" class=\"fa-pencil\"></i>\n                        <div class=\"bgColor colorPicker\" v-html=\"createColorPickerHTML()\" @click=\"onFontBgColorPicker\"></div>\n                    </li>\n                    <li><i title=\"\u589E\u52A0\u94FE\u63A5\" class=\"createLink fa-link\"></i></li>\n                    <li><i title=\"\u589E\u52A0\u56FE\u7247\" class=\"insertImage fa-file-image-o\"></i></li>\n                    <li><i title=\"\u4E00\u952E\u5B58\u56FE\" class=\"saveRemoteImage2Local fa-hdd-o\"></i></li>\n                    <li><i title=\"\u6E05\u7406 HTML\" class=\"cleanHTML fa-eraser\"></i></li>\n                    <li><i title=\"\u5207\u6362\u5230\u4EE3\u7801\" class=\"switchMode fa-code\"></i></li>\n                </ul>\n            \n                <div class=\"editorBody\">\n                    <iframe srcdoc=\"<html><body></body></html>\"></iframe>\n                    <slot></slot>\n                </div>\n            </div>\n        "]));
                // <iframe :src="ajResources.commonAsset + '/resources/htmleditor_iframe.jsp?basePath=' + basePath"></iframe>
                _this.props = {
                    fieldName: { type: String, required: true },
                    content: { type: String, required: false },
                    basePath: { type: String, required: false, default: '' },
                    uploadImageActionUrl: String // 图片上传路径
                };
                _this.fieldName = "";
                _this.fieldValue = "";
                /**
                 * 图片上传路径
                 */
                _this.uploadImageActionUrl = "";
                _this.iframeEl = document.body;
                _this.sourceEditor = document.body;
                _this.iframeWin = window;
                _this.iframeDoc = document;
                _this.mode = "iframe";
                _this.toolbarEl = document.body;
                return _this;
            }
            HtmlEditor.prototype.mounted = function () {
                var _this = this;
                var el = this.$el;
                this.iframeEl = el.$('iframe');
                this.sourceEditor = el.$('textarea');
                this.iframeWin = this.iframeEl.contentWindow;
                this.mode = 'iframe'; // 当前可视化编辑 iframe|textarea
                this.toolbarEl = el.$('.toolbar');
                // 这个方法只能写在 onload 事件里面， 不写 onload 里还不执行
                this.iframeWin.onload = function (ev) {
                    _this.iframeDoc = _this.iframeWin.document;
                    _this.iframeDoc.designMode = 'on';
                    _this.sourceEditor.value && _this.setValue(_this.sourceEditor.value); // 有内容
                    _this.iframeDoc.addEventListener('paste', onImagePaste.bind(_this)); // 直接剪切板粘贴上传图片
                };
            };
            /**
             * 当工具条点击的时候触发
             *
             * @param ev
             */
            HtmlEditor.prototype.onToolBarClk = function (ev) {
                var _this = this;
                var el = ev.target, clsName = el.className.split(' ').shift();
                switch (clsName) {
                    case 'createLink':
                        var result = prompt("请输入 URL 地址");
                        if (result)
                            this.format("createLink", result);
                        break;
                    case 'insertImage':
                        // @ts-ignore
                        if (window.isCreate)
                            aj.alert('请保存记录后再上传图片。');
                        else {
                            // @ts-ignore
                            App.$refs.uploadLayer.show(function (json) {
                                // if (json.result)
                                //     json = json.result;
                                if (json && json.isOk)
                                    _this.format("insertImage", json.fullUrl);
                            });
                        }
                        break;
                    case 'switchMode':
                        this.setMode();
                        break;
                    case 'cleanHTML':
                        // @ts-ignore
                        this.iframeDoc.body.innerHTML = HtmlSanitizer.SanitizeHtml(this.iframeDoc.body.innerHTML); // 清理冗余 HTML
                        break;
                    case 'saveRemoteImage2Local':
                        saveRemoteImage2Local.call(this);
                        break;
                    default:
                        this.format(clsName);
                }
            };
            HtmlEditor.prototype.format = function (type, para) {
                // this.iframeWin.focus();
                if (para)
                    this.iframeDoc.execCommand(type, false, para);
                else
                    this.iframeDoc.execCommand(type, false);
                this.iframeWin.focus();
            };
            HtmlEditor.prototype.insertEl = function (html) {
                this.iframeDoc.body.innerHTML = html;
            };
            /**
             * 設置 HTML
             *
             * @param v
             */
            HtmlEditor.prototype.setValue = function (v) {
                var _this = this;
                setTimeout(function () {
                    _this.iframeWin.document.body.innerHTML = v;
                    // self.iframeBody.innerHTML = v;
                }, 500);
            };
            /**
             * 获取内容的 HTML
             *
             * @param cleanWord
             * @param encode
             */
            HtmlEditor.prototype.getValue = function (cleanWord, encode) {
                var result = this.iframeDoc.body.innerHTML;
                if (cleanWord)
                    result = cleanPaste(result);
                if (encode)
                    result = encodeURIComponent(result);
                return result;
            };
            /**
             * 切換 HTML 編輯 or 可視化編輯
             *
             */
            HtmlEditor.prototype.setMode = function () {
                if (this.mode == 'iframe') {
                    this.iframeEl.classList.add('hide');
                    this.sourceEditor.classList.remove('hide');
                    this.sourceEditor.value = this.iframeDoc.body.innerHTML;
                    this.mode = 'textarea';
                    grayImg.call(this, true);
                }
                else {
                    this.iframeEl.classList.remove('hide');
                    this.sourceEditor.classList.add('hide');
                    this.iframeDoc.body.innerHTML = this.sourceEditor.value;
                    this.mode = 'iframe';
                    grayImg.call(this, false);
                }
            };
            /**
             * 选择字体
             *
             * @param ev
             */
            HtmlEditor.prototype.onFontfamilyChoserClk = function (ev) {
                var el = ev.target;
                this.format('fontname', el.innerHTML);
                /* 如何解决点击之后马上隐藏面板？由于 js（单击事件） 没有控制 CSS 的 :hover 伪类的方法，故所以必须使用以下技巧：*/
                var menuPanel = el.parentNode;
                menuPanel.style.display = 'none';
                setTimeout(function () { return menuPanel.style.display = ''; }, 300);
            };
            /**
             * 选择字号大小
             *
             * @param ev
             */
            HtmlEditor.prototype.onFontsizeChoserClk = function (ev) {
                var el = ev.target, els = ev.currentTarget.children;
                for (var i = 0, j = els.length; i < j; i++)
                    if (el == els[i])
                        break;
                this.format('fontsize', i + "");
            };
            HtmlEditor.prototype.onFontColorPicker = function (ev) {
                this.format('foreColor', ev.target.title);
            };
            HtmlEditor.prototype.onFontBgColorPicker = function (ev) {
                this.format('backColor', ev.target.title);
            };
            /**
             * 创建颜色选择器
             */
            HtmlEditor.prototype.createColorPickerHTML = function () {
                var cl = ['00', '33', '66', '99', 'CC', 'FF'], b, d, e, f, h = ['<div class="colorhead"><span class="colortitle">颜色选择</span></div><div class="colorbody"><table cellspaci="0" cellpadding="0"><tr>'];
                // 创建 body  [6 x 6的色盘]
                for (var i = 0; i < 6; ++i) {
                    h.push('<td><table class="colorpanel" cellspacing="0" cellpadding="0">');
                    for (var j = 0, a = cl[i]; j < 6; ++j) {
                        h.push('<tr>');
                        for (var k = 0, c = cl[j]; k < 6; ++k) {
                            b = cl[k];
                            e = (k == 5 && i != 2 && i != 5) ? ';border-right:none;' : '';
                            f = (j == 5 && i < 3) ? ';border-bottom:none' : '';
                            d = '#' + a + b + c;
                            // T = document.all ? '&nbsp;' : '';
                            h.push('<td unselectable="on" style="background-color: ' + d + e + f + '" title="' + d + '"></td>');
                        }
                        h.push('</tr>');
                    }
                    h.push('</table></td>');
                    if (cl[i] == '66')
                        h.push('</tr><tr>');
                }
                h.push('</tr></table></div>');
                return h.join('');
            };
            return HtmlEditor;
        }(aj.VueComponent));
        form.HtmlEditor = HtmlEditor;
        /**
         * 使图标变灰色
         *
         * @param this
         * @param isGray
         */
        function grayImg(isGray) {
            this.toolbarEl.$('i', function (item) {
                if (item.className.indexOf('switchMode') != -1)
                    return;
                item.style.color = isGray ? 'lightgray' : '';
                // if (item.className.indexOf('switchMode') != -1)
                //     item.style.color = isGray ? 'lightgray' : '';
                // else
                //     item.style.filter = isGray ? 'grayscale(100%)' : '';
            });
        }
        function saveRemoteImage2Local() {
            var str = [], remotePicArr = new Array();
            var arr = this.iframeDoc.querySelectorAll('img');
            for (var i = 0, j = arr.length; i < j; i++) {
                var imgEl = arr[i], url = imgEl.getAttribute('src');
                if (/^http/.test(url)) {
                    str.push(url);
                    remotePicArr.push(imgEl);
                }
            }
            if (str.length)
                aj.xhr.post('../downAllPics/', function (json) {
                    var _arr = json.pics;
                    for (var i = 0, j = _arr.length; i < j; i++)
                        remotePicArr[i].src = "images/" + _arr[i];
                    aj.alert('所有图片下载完成。');
                }, { pics: str.join('|') });
            else
                aj.alert('未发现有远程图片');
        }
        /**
         * Remove additional MS Word content
         * MSWordHtmlCleaners.js https://gist.github.com/ronanguilloux/2915995
         *
         * @param html
         */
        function cleanPaste(html) {
            html = html.replace(/<(\/)*(\\?xml:|meta|link|span|font|del|ins|st1:|[ovwxp]:)((.|\s)*?)>/gi, ''); // Unwanted tags
            html = html.replace(/(class|style|type|start)=("(.*?)"|(\w*))/gi, ''); // Unwanted sttributes
            html = html.replace(/<style(.*?)style>/gi, ''); // Style tags
            html = html.replace(/<script(.*?)script>/gi, ''); // Script tags
            html = html.replace(/<!--(.*?)-->/gi, ''); // HTML comments
            return html;
        }
        /*
        * 富文本编辑器中粘贴图片时，chrome可以得到e.clipBoardData.items并从中获取二进制数据，以便ajax上传到后台，
        * 实现粘贴图片的功能。firefox中items为undefined，可选的方案：1将base64原样上传到后台进行文件存储替换，2将内容清空，待粘贴完毕后取图片src，再恢复现场
        * https://stackoverflow.com/questions/2176861/javascript-get-clipboard-data-on-paste-event-cross-browser
        */
        /**
         *
         * @param this
         * @param ev
         */
        function onImagePaste(ev) {
            var _this = this;
            if (!this.uploadImageActionUrl) {
                aj.alert('未提供图片上传地址');
                return;
            }
            var items = ev.clipboardData && ev.clipboardData.items;
            var file = null; // file就是剪切板中的图片文件
            if (items && items.length) { // 检索剪切板items
                for (var i = 0; i < items.length; i++) {
                    var item = items[i];
                    if (item.type.indexOf('image') !== -1) {
                        // @ts-ignore
                        if (window.isCreate) { // 有图片
                            aj.alert('请保存记录后再上传图片。');
                            return;
                        }
                        file = item.getAsFile();
                        break;
                    }
                }
            }
            if (file) {
                ev.preventDefault();
                aj.img.changeBlobImageQuality(file, function (newBlob) {
                    Vue.options.components["aj-xhr-upload"].extendOptions.methods.doUpload.call({
                        action: _this.uploadImageActionUrl,
                        progress: 0,
                        uploadOk_callback: function (j) {
                            if (j.isOk)
                                this.format("insertImage", this.ajResources.imgPerfix + j.imgUrl);
                        },
                        $blob: newBlob,
                        $fileName: 'foo.jpg'
                    });
                });
            }
        }
        new HtmlEditor().register();
    })(form = aj.form || (aj.form = {}));
})(aj || (aj = {}));

"use strict";

/**
* 将上传控件嵌入到一个浮出层中
*/
Vue.component('aj-form-popup-upload', {
    template: html(__makeTemplateObject(["\n        <aj-layer>\n            <h3>\u56FE\u7247\u4E0A\u4F20</h3>\n            <p>\u4E0A\u4F20\u6210\u529F\u540E\u81EA\u52A8\u63D2\u5165\u5230\u6B63\u6587</p>\n            <aj-xhr-upload ref=\"uploadControl\" :action=\"uploadUrl\" :is-img-upload=\"true\" :hidden-field=\"imgName\"\n                :img-place=\"ajResources.commonAsset + '/images/imgBg.png'\">\n            </aj-xhr-upload>\n            <div>\u4E0A\u4F20\u9650\u5236\uFF1A{{text.maxSize}}kb \u6216\u4EE5\u4E0B\uFF0C\u5206\u8FA8\u7387\uFF1A{{text.maxHeight}}x{{text.maxWidth}}</div>\n        </aj-layer>\n    "], ["\n        <aj-layer>\n            <h3>\u56FE\u7247\u4E0A\u4F20</h3>\n            <p>\u4E0A\u4F20\u6210\u529F\u540E\u81EA\u52A8\u63D2\u5165\u5230\u6B63\u6587</p>\n            <aj-xhr-upload ref=\"uploadControl\" :action=\"uploadUrl\" :is-img-upload=\"true\" :hidden-field=\"imgName\"\n                :img-place=\"ajResources.commonAsset + '/images/imgBg.png'\">\n            </aj-xhr-upload>\n            <div>\u4E0A\u4F20\u9650\u5236\uFF1A{{text.maxSize}}kb \u6216\u4EE5\u4E0B\uFF0C\u5206\u8FA8\u7387\uFF1A{{text.maxHeight}}x{{text.maxWidth}}</div>\n        </aj-layer>\n    "])),
    data: function () {
        return {
            text: {}
        };
    },
    props: {
        uploadUrl: { type: String, required: true },
        imgName: { type: String, required: false },
        imgId: { type: Number, required: false },
        imgPlace: String // 图片占位符，用户没有选定图片时候使用的图片
    },
    mounted: function () {
        var obj = this.$refs.uploadControl;
        this.text = { maxSize: obj.limitSize || 600, maxHeight: obj.imgMaxHeight, maxWidth: obj.imgMaxWidth };
    },
    methods: {
        /**
         * 显示上传控件
         *
         * @param {Function} callback 上传成功之后的回调函数
         */
        show: function (callback) {
            if (callback)
                this.$refs.uploadControl.uploadOk_callback = callback;
            this.$children[0].show();
        }
    }
});

"use strict";
// aj.searchPanel = {
//     methods: {
//         searchBy(field, clz) {
//             var param = {};
//             param[field] = aj(clz).value;
//             if (!param[field]) {
//                 aj.alert('请输入搜索的关键字');
//                 return;
//             }
//             aj.apply(this.$refs.pager.extraParam, param);
//             this.reload();
//         },
//         // 选择任意字段
//         searchAny(field, clz) {
//             var param = {};
//             param.filterField = field;
//             param.filterValue = aj(clz).value;
//             if (!param.filterValue) {
//                 aj.alert('请输入搜索的关键字');
//                 return;
//             }
//             aj.apply(this.$refs.pager.extraParam, param);
//             this.reload();
//         },
//         // 清空查询参数
//         clearSearch() {
//             this.$refs.pager.extraParam = {};
//             this.reload();
//         }
//     }
// };

"use strict";

/**
 * 下拉列表
 */
Vue.component('aj-form-select', {
    template: html(__makeTemplateObject(["\n        <select :name=\"name\" class=\"aj-form-select\">\n            <template v-for=\"value, key, index in options\">\n                <option v-if=\"index === selectedIndex\" selected :value=\"value\" >{{key}}</option>\n                <option v-else :value=\"value\" >{{key}}</option>\n            </template>\n        </select>\n    "], ["\n        <select :name=\"name\" class=\"aj-form-select\">\n            <template v-for=\"value, key, index in options\">\n                <option v-if=\"index === selectedIndex\" selected :value=\"value\" >{{key}}</option>\n                <option v-else :value=\"value\" >{{key}}</option>\n            </template>\n        </select>\n    "])),
    props: {
        name: { type: String, required: true },
        options: { type: Object, required: true },
        selectedIndex: { type: Number } // starts form 0
    }
});

"use strict";
/**
 * 表单工具函数
 */
var aj;
(function (aj) {
    var form;
    (function (form) {
        var utils;
        (function (utils) {
            /**
             * 获取表单控件的值
             *
             * @param el
             * @param cssSelector
             */
            function getFormFieldValue(_el, cssSelector) {
                var el = _el.$(cssSelector);
                if (el)
                    return el.value;
                else
                    throw "\u627E\u4E0D\u5230" + cssSelector + "\u5143\u7D20";
            }
            utils.getFormFieldValue = getFormFieldValue;
            /**
             * 指定 id 的那个 option 选中
             *
             * @param this
             * @param id
             */
            function selectOption(id) {
                this.$el.$('option', function (i) {
                    console.log(i.value);
                    if (i.value == id)
                        i.selected = true;
                });
            }
            utils.selectOption = selectOption;
        })(utils = form.utils || (form.utils = {}));
    })(form = aj.form || (aj.form = {}));
})(aj || (aj = {}));

"use strict";
/**
 * 表单验证器
 *
 * https://www.w3cplus.com/css/form-validation-part-2-the-constraint-validation-api-javascript.html
 * https://github.com/cferdinandi/validate/blob/master/src/js/validate.js
 */
var aj;
(function (aj) {
    var form;
    (function (form_1) {
        /**
         * 验证字段
         *
         * @param field
         */
        function hasError(field) {
            // if (!field || !field.getAttribute)
            //     console.log(field);
            if (field.disabled || field.type === 'file' || field.type === 'reset' || field.type === 'submit' || field.type === 'button')
                return ""; // 忽略部分元素
            var validity = field.validity; // 获取 validity
            if (!validity)
                return '浏览器不支持 HTML5 表单验证';
            if (validity.valid) // 如果通过验证,就返回 ""
                return "";
            if (validity.valueMissing) // 如果是必填字段但是字段为空时
                return '该项是必填项';
            if (validity.typeMismatch) { // 如果类型不正确
                if (field.type === 'email')
                    return '请输入有效的邮件地址';
                if (field.type === 'url')
                    return '请输入一个有效的网址';
                return '请输入正确的类型';
            }
            if (validity.tooShort) // 如果输入的字符数太短
                return 'Please lengthen this text to ' + field.getAttribute('minLength') + ' characters or more. You are currently using ' + field.value.length + ' characters.';
            if (validity.tooLong) // 如果输入的字符数太长
                return 'Please shorten this text to no more than ' + field.getAttribute('maxLength') + ' characters. You are currently using ' + field.value.length + ' characters.';
            if (validity.badInput) // 如果数字输入类型输入的值不是数字
                return 'Please enter a number.';
            if (validity.stepMismatch) // 如果数字值与步进间隔不匹配
                return 'Please select a valid value.';
            if (validity.rangeOverflow) // 如果数字字段的值大于max的值
                return 'Please select a value that is no more than ' + field.getAttribute('max') + '.';
            if (validity.rangeUnderflow) // 如果数字字段的值小于min的值
                return 'Please select a value that is no less than ' + field.getAttribute('min') + '.';
            if (validity.patternMismatch) { // 如果模式不匹配
                var title = field.getAttribute('title');
                if (title) // 如果包含模式信息，返回自定义错误
                    return title;
                return '格式要求不正确'; // 否则, 返回一般错误
            }
            return 'The value you entered for this field is invalid.'; // 如果是其他的错误, 返回一个通用的 catchall 错误
        }
        /**
         *
         * @param field
         * @param error
         * @param isNewLine
         */
        function showError(field, error, isNewLine) {
            var _a;
            var id = field.id || field.name; // 获取字段 id 或者 name
            if (!id)
                return;
            field.classList.add('error'); // 将错误类添加到字段
            // 检查错误消息字段是否已经存在 如果没有, 就创建一个
            var message = field.form.querySelector('.error-message#error-for-' + id);
            if (!message) {
                message = document.createElement('div');
                message.className = 'error-message';
                message.id = 'error-for-' + id;
                (_a = field.parentNode) === null || _a === void 0 ? void 0 : _a.insertBefore(message, field.nextSibling);
            }
            field.setAttribute('aria-describedby', 'error-for-' + id); // 添加ARIA role 到字段
            message.innerHTML = error; // 更新错误信息
            if (!isNewLine) // 显示错误信息
                message.style.display = 'inline-block';
            message.style.visibility = 'visible';
        }
        ;
        /**
         * 移除所有的错误信息
         *
         * @param field
         */
        function removeError(field) {
            var id = field.id || field.name; // 获取字段的 id 或者 name
            if (!id)
                return;
            field.classList.remove('error'); // 删除字段的错误类
            field.removeAttribute('aria-describedby'); // 移除字段的 ARIA role
            var message = field.form.querySelector('.error-message#error-for-' + id + ''); // 检查 DOM 中是否有错误消息
            if (message) {
                message.innerHTML = ''; // 如果有错误消息就隐藏它
                message.style.display = 'none';
                message.style.visibility = 'hidden';
            }
        }
        /**
         *
         * @param el
         */
        function Validator(el) {
            var isMsgNewLine = el.dataset.msgNewline === "true";
            el.setAttribute('novalidate', 'true');
            // 监听所有的失去焦点的事件
            document.addEventListener('blur', function (event) {
                var el = event.target, errMsg = hasError(el);
                if (errMsg) { // 如果有错误,就把它显示出来
                    showError(el, errMsg, isMsgNewLine);
                    return;
                }
                removeError(el); // 否则, 移除所有存在的错误信息
            }, true);
        }
        form_1.Validator = Validator;
        Validator.hasError = hasError;
        /**
         * 是否通过验证
         *
         * @param form
         */
        Validator.onSubmit = function (form) {
            var fields = form.elements; // 获取所有的表单元素
            // 验证每一个字段
            // 将具有错误的第一个字段存储到变量中以便稍后我们将其默认获得焦点
            var error, hasErrors = null;
            for (var i = 0, j = fields.length; i < j; i++) {
                var el = fields[i];
                error = hasError(el);
                if (error) {
                    showError(el, error);
                    if (!hasErrors) // 如果有错误,停止提交表单并使出现错误的第一个元素获得焦点
                        hasErrors = el;
                }
            }
            if (hasErrors) {
                hasErrors.focus();
                return false;
            }
            return true;
        };
    })(form = aj.form || (aj.form = {}));
})(aj || (aj = {}));

"use strict";

;
(function () {
    Vue.component('aj-xhr-upload', {
        template: html(__makeTemplateObject(["\n            <div class=\"aj-xhr-upload\" :style=\"{display: buttonBottom ? 'inherit': 'flex'}\">\n                <input v-if=\"hiddenField\" type=\"hidden\" :name=\"hiddenField\" :value=\"hiddenFieldValue\" />\n                <div v-if=\"isImgUpload\">\n                    <a :href=\"imgPlace\" target=\"_blank\">\n                        <img class=\"upload_img_perview\"\n                            :src=\"(isFileSize && isExtName && imgBase64Str) ? imgBase64Str : imgPlace\" />\n                    </a>\n                </div>\n                <div class=\"pseudoFilePicker\">\n                    <label :for=\"'uploadInput_' + radomId\">\n                        <div>\n                            <div>+</div>\u70B9\u51FB\u9009\u62E9{{isImgUpload ? '\u56FE\u7247': '\u6587\u4EF6'}}\n                        </div>\n                    </label>\n                </div>\n                <input type=\"file\" :name=\"fieldName\" class=\"hide\" :id=\"'uploadInput_' + radomId\" @change=\"onUploadInputChange\"\n                    :accept=\"isImgUpload ? 'image/*' : accpectFileType\" />\n                <div v-if=\"!isFileSize || !isExtName\">{{errMsg}}</div>\n                <div v-if=\"isFileSize && isExtName\">\n                    {{fileName}}<br />\n                    <button @click.prevent=\"doUpload\"\n                        style=\"min-width:110px;\">{{progress && progress !== 100 ? '\u4E0A\u4F20\u4E2D ' + progress + '%': '\u4E0A\u4F20'}}</button>\n                </div>\n            </div>\n        "], ["\n            <div class=\"aj-xhr-upload\" :style=\"{display: buttonBottom ? 'inherit': 'flex'}\">\n                <input v-if=\"hiddenField\" type=\"hidden\" :name=\"hiddenField\" :value=\"hiddenFieldValue\" />\n                <div v-if=\"isImgUpload\">\n                    <a :href=\"imgPlace\" target=\"_blank\">\n                        <img class=\"upload_img_perview\"\n                            :src=\"(isFileSize && isExtName && imgBase64Str) ? imgBase64Str : imgPlace\" />\n                    </a>\n                </div>\n                <div class=\"pseudoFilePicker\">\n                    <label :for=\"'uploadInput_' + radomId\">\n                        <div>\n                            <div>+</div>\u70B9\u51FB\u9009\u62E9{{isImgUpload ? '\u56FE\u7247': '\u6587\u4EF6'}}\n                        </div>\n                    </label>\n                </div>\n                <input type=\"file\" :name=\"fieldName\" class=\"hide\" :id=\"'uploadInput_' + radomId\" @change=\"onUploadInputChange\"\n                    :accept=\"isImgUpload ? 'image/*' : accpectFileType\" />\n                <div v-if=\"!isFileSize || !isExtName\">{{errMsg}}</div>\n                <div v-if=\"isFileSize && isExtName\">\n                    {{fileName}}<br />\n                    <button @click.prevent=\"doUpload\"\n                        style=\"min-width:110px;\">{{progress && progress !== 100 ? '\u4E0A\u4F20\u4E2D ' + progress + '%': '\u4E0A\u4F20'}}</button>\n                </div>\n            </div>\n        "])),
        props: {
            action: { type: String, required: true },
            fieldName: String,
            limitSize: Number,
            hiddenField: { type: String, default: null },
            hiddenFieldValue: String,
            limitFileType: String,
            accpectFileType: String,
            isImgUpload: Boolean,
            imgPlace: String,
            imgMaxWidth: { type: Number, default: 1920 },
            imgMaxHeight: { type: Number, default: 1680 },
            buttonBottom: Boolean // 上传按钮是否位于下方
        },
        data: function () {
            var _this = this;
            return {
                isFileSize: false,
                isExtName: false,
                isImgSize: false,
                errMsg: null,
                newlyId: null,
                radomId: Math.round(Math.random() * 1000),
                uplodedFileUrl: null,
                uploadOk_callback: function (json) {
                    if (json.isOk) {
                        _this.uplodedFileUrl = json.imgUrl;
                        if (_this.hiddenField)
                            _this.$el.$('input[name=' + _this.hiddenField + ']').value = json.imgUrl;
                    }
                    aj.xhr.defaultCallBack(json);
                },
                imgBase64Str: null,
                progress: 0,
                fileName: '' // 获取文件名称，只能是名称，不能获取完整的文件录
            };
        },
        methods: {
            /**
             *
             * @param this
             * @param $event
             */
            onUploadInputChange: function ($event) {
                var _this = this;
                var fileInput = $event.target;
                if (!fileInput.files || !fileInput.files[0])
                    return;
                var ext = fileInput.value.split('.').pop(); // 扩展名
                this.$fileObj = fileInput.files[0]; // 保留引用
                this.$fileName = this.$fileObj.name;
                this.$fileType = this.$fileObj.type;
                var size = this.$fileObj.size;
                if (this.limitSize) {
                    this.isFileSize = size < this.limitSize;
                    this.errMsg = "要上传的文件容量过大，请压缩到 " + this.limitSize + "kb 以下";
                }
                else
                    this.isFileSize = true;
                if (this.limitFileType) {
                    this.isExtName = new RegExp(this.limitFileType, 'i').test(ext);
                    this.errMsg = '根据文件后缀名判断，此文件不能上传';
                }
                else
                    this.isExtName = true;
                readBase64.call(this, fileInput.files[0]);
                if (this.isImgUpload) {
                    var imgEl = new Image();
                    imgEl.onload = function () {
                        if (imgEl.width > _this.imgMaxWidth || imgEl.height > _this.imgMaxHeight) {
                            _this.isImgSize = false;
                            _this.errMsg = '图片大小尺寸不符合要求哦，请重新图片吧~';
                        }
                        else {
                            _this.isImgSize = true;
                        }
                    };
                }
                getFileName.call(this);
            },
            /**
             * 执行上传
             *
             * @param this
             */
            doUpload: function () {
                var _this = this;
                var fd = new FormData();
                if (this.$blob)
                    fd.append("file", this.$blob, this.$fileName);
                else
                    fd.append("file", this.$fileObj);
                var xhr = new XMLHttpRequest();
                //@ts-ignore
                xhr.onreadystatechange = aj.xhr.requestHandler.delegate(null, this.uploadOk_callback, 'json');
                xhr.open("POST", this.action, true);
                xhr.onprogress = function (e) {
                    var progress = 0, p = ~~(e.loaded * 1000 / e.total);
                    p = p / 10;
                    if (progress !== p)
                        progress = p;
                    _this.progress = progress;
                };
                xhr.send(fd);
            }
        }
    });
    // 文件头判别，看看是否为图片
    var imgHeader = { "jpeg": "/9j/4", "gif": "R0lGOD", "png": "iVBORw" };
    /**
     * 获取文件名称，只能是名称，不能获取完整的文件目录
     *
     * @param this
     */
    function getFileName() {
        var _a;
        var v = this.$el.$('input[type=file]').value, arr = v.split('\\');
        this.fileName = (_a = arr.pop()) === null || _a === void 0 ? void 0 : _a.trim();
    }
    /**
     *
     * @param this
     * @param file
     */
    function readBase64(file) {
        var _this = this;
        var reader = new FileReader();
        reader.onload = function (_e) {
            var e = _e;
            _this.imgBase64Str = e.target.result;
            if (_this.isImgUpload) {
                var imgEl = new Image();
                imgEl.onload = function () {
                    if (file.size > 300 * 1024) // 大于 300k 才压缩
                        aj.img.compress(imgEl, _this);
                    if (imgEl.width > _this.imgMaxWidth || imgEl.height > _this.imgMaxHeight) {
                        _this.isImgSize = false;
                        _this.errMsg = '图片大小尺寸不符合要求哦，请裁剪图片重新上传吧~';
                    }
                    else
                        _this.isImgSize = true;
                };
                imgEl.src = _this.imgBase64Str;
                // 文件头判别，看看是否为图片
                for (var i in imgHeader) {
                    if (~_this.imgBase64Str.indexOf(imgHeader[i])) {
                        _this.isExtName = true;
                        return;
                    }
                }
                _this.errMsg = "亲，改了扩展名我还能认得你不是图片哦";
            }
        };
        reader.readAsDataURL(file);
    }
})();
