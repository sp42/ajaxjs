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
                _this.template = html(__makeTemplateObject(["\n        <div class=\"aj-form-calendar\">\n            <div class=\"selectYearMonth\">\n                <a href=\"###\" @click=\"getDate('preYear')\" class=\"preYear\" title=\"\u4E0A\u4E00\u5E74\">&lt;</a>\n                <select @change=\"setMonth\" v-model=\"month\">\n                    <option value=\"1\">\u4E00\u6708</option>\n                    <option value=\"2\">\u4E8C\u6708</option>\n                    <option value=\"3\">\u4E09\u6708</option>\n                    <option value=\"4\">\u56DB\u6708</option>\n                    <option value=\"5\">\u4E94\u6708</option>\n                    <option value=\"6\">\u516D\u6708</option>\n                    <option value=\"7\">\u4E03\u6708</option>\n                    <option value=\"8\">\u516B\u6708</option>\n                    <option value=\"9\">\u4E5D\u6708</option>\n                    <option value=\"10\">\u5341\u6708</option>\n                    <option value=\"11\">\u5341\u4E00\u6708</option>\n                    <option value=\"12\">\u5341\u4E8C\u6708</option>\n                </select>\n                <a href=\"###\" @click=\"getDate('nextYear')\" class=\"nextYear\" title=\"\u4E0B\u4E00\u5E74\">&gt;</a>\n            </div>\n            <div class=\"showCurrentYearMonth\">\n                <span class=\"showYear\">{{year}}</span>/<span class=\"showMonth\">{{month}}</span>\n            </div>\n            <table>\n                <thead>\n                    <tr>\n                        <td>\u65E5</td>\n                        <td>\u4E00</td>\n                        <td>\u4E8C</td>\n                        <td>\u4E09</td>\n                        <td>\u56DB</td>\n                        <td>\u4E94</td>\n                        <td>\u516D</td>\n                    </tr>\n                </thead>\n                <tbody @click=\"pickDay\"></tbody>\n            </table>\n            <div v-if=\"showTime\" class=\"showTime\">\n                \u65F6 <select class=\"hour aj-select\">\n                    <option v-for=\"n in 24\">{{n}}</option>\n                </select>\n                \u5206 <select class=\"minute aj-select\">\n                    <option v-for=\"n in 61\">{{n - 1}}</option>\n                </select>\n                <a href=\"#\" @click=\"pickupTime\">\u9009\u62E9\u65F6\u95F4</a>\n            </div>\n        </div>"], ["\n        <div class=\"aj-form-calendar\">\n            <div class=\"selectYearMonth\">\n                <a href=\"###\" @click=\"getDate('preYear')\" class=\"preYear\" title=\"\u4E0A\u4E00\u5E74\">&lt;</a>\n                <select @change=\"setMonth\" v-model=\"month\">\n                    <option value=\"1\">\u4E00\u6708</option>\n                    <option value=\"2\">\u4E8C\u6708</option>\n                    <option value=\"3\">\u4E09\u6708</option>\n                    <option value=\"4\">\u56DB\u6708</option>\n                    <option value=\"5\">\u4E94\u6708</option>\n                    <option value=\"6\">\u516D\u6708</option>\n                    <option value=\"7\">\u4E03\u6708</option>\n                    <option value=\"8\">\u516B\u6708</option>\n                    <option value=\"9\">\u4E5D\u6708</option>\n                    <option value=\"10\">\u5341\u6708</option>\n                    <option value=\"11\">\u5341\u4E00\u6708</option>\n                    <option value=\"12\">\u5341\u4E8C\u6708</option>\n                </select>\n                <a href=\"###\" @click=\"getDate('nextYear')\" class=\"nextYear\" title=\"\u4E0B\u4E00\u5E74\">&gt;</a>\n            </div>\n            <div class=\"showCurrentYearMonth\">\n                <span class=\"showYear\">{{year}}</span>/<span class=\"showMonth\">{{month}}</span>\n            </div>\n            <table>\n                <thead>\n                    <tr>\n                        <td>\u65E5</td>\n                        <td>\u4E00</td>\n                        <td>\u4E8C</td>\n                        <td>\u4E09</td>\n                        <td>\u56DB</td>\n                        <td>\u4E94</td>\n                        <td>\u516D</td>\n                    </tr>\n                </thead>\n                <tbody @click=\"pickDay\"></tbody>\n            </table>\n            <div v-if=\"showTime\" class=\"showTime\">\n                \u65F6 <select class=\"hour aj-select\">\n                    <option v-for=\"n in 24\">{{n}}</option>\n                </select>\n                \u5206 <select class=\"minute aj-select\">\n                    <option v-for=\"n in 61\">{{n - 1}}</option>\n                </select>\n                <a href=\"#\" @click=\"pickupTime\">\u9009\u62E9\u65F6\u95F4</a>\n            </div>\n        </div>"]));
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
                                var text = this.year + '-' + this.month + '-' + d;
                                cell.title = text; // 保存日期在 title 属性
                                cell.className = 'day day_' + text;
                                cell.innerHTML = d + "";
                                var on = new Date(this.year, this.month - 1, d);
                                // 判断是否今日
                                if (isSameDay(on, this.date)) {
                                    cell.classList.add('onToday');
                                    // this.onToday && this.onToday(cell);// 点击 今天 时候触发的事件
                                }
                                // 判断是否选择日期
                                // this.selectDay && this.onSelectDay && this.isSameDay(on, this.selectDay) && this.onSelectDay(cell);
                            }
                        }
                        row.appendChild(cell);
                    }
                    frag.appendChild(row);
                }
                // 先清空内容再插入
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
             * @param $even
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
                // 算出这个月1号距离前面的星期天有多少天
                for (var i = 1, firstDay = new Date(this.year, this.month - 1, 1).getDay(); i <= firstDay; i++)
                    arr.push(0);
                // 这个月有多少天。用上个月然后设置日子参数为 0，就可以得到本月有多天
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
                _this.template = html(__makeTemplateObject(["\n\t\t\t<div class=\"aj-china-area\">\n\t\t\t\t<span>\u7701</span>\n\t\t\t\t<select v-model=\"province\" class=\"aj-select\" style=\"width:150px;\" :name=\"provinceName || 'locationProvince'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in addressData[86]\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t\t<span>\u5E02 </span>\n\t\t\t\t<select v-model=\"city\" class=\"aj-select\" style=\"width:150px;\" :name=\"cityName || 'locationCity'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in citys\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t\t<span>\u533A</span>\n\t\t\t\t<select v-model=\"district\" class=\"aj-select\" style=\"width:150px;\" :name=\"districtName || 'locationDistrict'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in districts\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t</div>\n\t\t"], ["\n\t\t\t<div class=\"aj-china-area\">\n\t\t\t\t<span>\u7701</span>\n\t\t\t\t<select v-model=\"province\" class=\"aj-select\" style=\"width:150px;\" :name=\"provinceName || 'locationProvince'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in addressData[86]\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t\t<span>\u5E02 </span>\n\t\t\t\t<select v-model=\"city\" class=\"aj-select\" style=\"width:150px;\" :name=\"cityName || 'locationCity'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in citys\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t\t<span>\u533A</span>\n\t\t\t\t<select v-model=\"district\" class=\"aj-select\" style=\"width:150px;\" :name=\"districtName || 'locationDistrict'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in districts\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t</div>\n\t\t"]));
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
    (function (form) {
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
                    var id = form.utils.getFormFieldValue(this.$el, 'input[name=id]'), title = form.utils.getFormFieldValue(this.$el, 'input[name=name]');
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
    var xhr_upload;
    (function (xhr_upload) {
        /**
         * 属性较多，设一个抽象类
         */
        var BaseFileUploader = /** @class */ (function (_super) {
            __extends(BaseFileUploader, _super);
            function BaseFileUploader() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.fieldName = "";
                _this.fieldValue = "";
                /**
                 * 不重复的 id，用关于关联 label 与 input[type=file]
                 */
                _this.radomId = 0;
                /**
                 * 上传路径，必填
                 */
                _this.action = "";
                /**
                 * 允许文件选择器列出的文件类型
                 */
                _this.accpectFileType = "";
                /**
                 * 限制的文件扩展名，这是一个正则。如无限制，不设置或者空字符串
                 * 正则如：txt|pdf|doc
                 */
                _this.limitFileType = "";
                /**
                 * 文件大小
                 */
                _this.fileSize = 0;
                /**
                 * 获取文件名称，只能是名称，不能获取完整的文件目录
                 */
                _this.fileName = '';
                /**
                 * 文件对象，实例属性
                 */
                _this.$fileObj = null;
                /**
                 * 二进制数据，用于图片预览
                 */
                _this.$blob = null;
                /**
                 * 上传按钮是否位于下方
                 */
                _this.buttonBottom = false;
                /**
                 * 文件大小限制，单位：KB。
                 * 若为 0 则不限制
                 */
                _this.limitSize = 0;
                /**
                 * 上传进度百分比
                 */
                _this.progress = 0;
                /**
                 * 错误信息。约定：只有为空字符串，才表示允许上传。
                 */
                _this.errMsg = "init";
                /**
                 * 固定的错误结构，元素[0]为文件大小，[1]为文件类型。
                 * 如果元素非 true，表示不允许上传。
                 */
                _this.errStatus = [false, false];
                /**
                 * 成功上传之后的文件 id
                 */
                _this.newlyId = "";
                /**
                 * 上传之后的回调函数
                 */
                _this.$uploadOk_callback = function (json) {
                    if (json.isOk)
                        this.fieldValue = json.imgUrl;
                    aj.xhr.defaultCallBack(json);
                };
                return _this;
            }
            return BaseFileUploader;
        }(aj.VueComponent));
        /**
         * 文件上传器
         */
        var FileUploader = /** @class */ (function (_super) {
            __extends(FileUploader, _super);
            function FileUploader() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = "aj-file-uploader";
                _this.template = html(__makeTemplateObject(["\n            <div class=\"aj-file-uploader\">\n                <input type=\"hidden\" :name=\"fieldName\" :value=\"fieldValue\" />\n                <input type=\"file\" :id=\"'uploadInput_' + radomId\" @change=\"onUploadInputChange\" :accept=\"accpectFileType\" />\n            \n                <label class=\"pseudoFilePicker\" :for=\"'uploadInput_' + radomId\">\n                    <div @drop=\"onDrop\" ondragover=\"event.preventDefault();\">\n                        <div>+</div>\u70B9\u51FB\u9009\u62E9\u6587\u4EF6<br />\u6216\u62D6\u653E\u5230\u6B64\n                    </div>\n                </label>\n            \n                <div class=\"msg\" v-if=\"errMsg != ''\">\n                    \u5141\u8BB8\u7C7B\u578B\uFF1A{{limitFileType || '\u65E0\u9650\u5236'}}\n                    <br />\n                    \u5141\u8BB8\u5927\u5C0F\uFF1A{{limitSize ? changeByte(limitSize * 1024) : '\u65E0\u9650\u5236'}}\n                    <span class=\"slot\"></span>\n                </div>\n                <div class=\"msg\" v-if=\"errMsg == ''\">\n                    {{fileName}}<div v-if=\"fileSize\">{{changeByte(fileSize)}}</div>\n                    <button @click.prevent=\"doUpload\">{{progress && progress !== 100 ? '\u4E0A\u4F20\u4E2D ' + progress + '%': '\u4E0A\u4F20'}}</button>\n                </div>\n            </div>\n        "], ["\n            <div class=\"aj-file-uploader\">\n                <input type=\"hidden\" :name=\"fieldName\" :value=\"fieldValue\" />\n                <input type=\"file\" :id=\"'uploadInput_' + radomId\" @change=\"onUploadInputChange\" :accept=\"accpectFileType\" />\n            \n                <label class=\"pseudoFilePicker\" :for=\"'uploadInput_' + radomId\">\n                    <div @drop=\"onDrop\" ondragover=\"event.preventDefault();\">\n                        <div>+</div>\u70B9\u51FB\u9009\u62E9\u6587\u4EF6<br />\u6216\u62D6\u653E\u5230\u6B64\n                    </div>\n                </label>\n            \n                <div class=\"msg\" v-if=\"errMsg != ''\">\n                    \u5141\u8BB8\u7C7B\u578B\uFF1A{{limitFileType || '\u65E0\u9650\u5236'}}\n                    <br />\n                    \u5141\u8BB8\u5927\u5C0F\uFF1A{{limitSize ? changeByte(limitSize * 1024) : '\u65E0\u9650\u5236'}}\n                    <span class=\"slot\"></span>\n                </div>\n                <div class=\"msg\" v-if=\"errMsg == ''\">\n                    {{fileName}}<div v-if=\"fileSize\">{{changeByte(fileSize)}}</div>\n                    <button @click.prevent=\"doUpload\">{{progress && progress !== 100 ? '\u4E0A\u4F20\u4E2D ' + progress + '%': '\u4E0A\u4F20'}}</button>\n                </div>\n            </div>\n        "]));
                _this.props = {
                    action: { type: String, required: true },
                    fieldName: String,
                    limitSize: { type: Number, default: 20000 },
                    limitFileType: String,
                    accpectFileType: String,
                    buttonBottom: Boolean,
                    radomId: { type: Number, default: function () { return Math.round(Math.random() * 1000); } }
                };
                _this.watch = {
                    fileName: function (newV) {
                        if (!this.limitFileType) { // 无限制，也不用检查，永远是 true
                            Vue.set(this.errStatus, 0, true);
                            return;
                        }
                        if (newV && this.limitFileType) {
                            var ext = newV.split('.').pop(); // 扩展名，fileInput.value.split('.').pop(); 也可以获取
                            if (!new RegExp(this.limitFileType, 'i').test(ext)) {
                                var msg_1 = "\u4E0A\u4F20\u6587\u4EF6\u4E3A " + newV + "\uFF0C<br />\u62B1\u6B49\uFF0C\u4E0D\u652F\u6301\u4E0A\u4F20 *." + ext + " \u7C7B\u578B\u6587\u4EF6";
                                Vue.set(this.errStatus, 0, msg_1);
                            }
                            else
                                Vue.set(this.errStatus, 0, true); // 检查通过
                        }
                    },
                    fileSize: function (newV) {
                        if (!this.limitSize) { // 无限制，也不用检查，永远是 true
                            Vue.set(this.errStatus, 1, true);
                            return;
                        }
                        if (this.limitSize && newV > this.limitSize * 1024) {
                            var msg_2 = "\u8981\u4E0A\u4F20\u7684\u6587\u4EF6\u5BB9\u91CF\u8FC7\u5927(" + this.changeByte(newV) + ")\uFF0C\u8BF7\u538B\u7F29\u5230 " + this.changeByte(this.limitSize * 1024) + " \u4EE5\u4E0B";
                            Vue.set(this.errStatus, 1, msg_2);
                        }
                        else
                            Vue.set(this.errStatus, 1, true);
                    },
                    errStatus: function (newV) {
                        var j = newV.length;
                        if (!j)
                            return;
                        var msg = "";
                        for (var i = 0; i < j; i++) {
                            var err = newV[i];
                            if (err === false)
                                return; // 未检查完，退出
                            if (err && typeof err == 'string')
                                msg += err + '；<br/>';
                        }
                        // 到这步，所有检查完毕
                        if (msg) { // 有错误
                            aj.alert(msg);
                            this.errMsg = msg;
                        }
                        else { // 全部通过，复位
                            this.errMsg = "";
                            this.errStatus = [false, false];
                        }
                    }
                };
                return _this;
            }
            /**
             * 选择文件后触发的事件
             *
             * @param ev
             */
            FileUploader.prototype.onUploadInputChange = function (ev) {
                var fileInput = ev.target;
                if (!fileInput.files || !fileInput.files[0])
                    return;
                // this.errStatus = [false, false, false];
                this.onFileGet(fileInput.files);
            };
            FileUploader.prototype.onDrop = function (ev) {
                var _a;
                ev.preventDefault(); // 阻止进行拖拽时浏览器的默认行为，即自动打开图片
                if ((_a = ev.dataTransfer) === null || _a === void 0 ? void 0 : _a.files)
                    this.onFileGet(ev.dataTransfer.files);
            };
            FileUploader.prototype.onFileGet = function (files) {
                var file = files[0], fileType = file.type;
                this.$fileObj = file;
                this.fileName = file.name;
                this.fileSize = file.size;
            };
            /**
             * 字节 Byte 转化成 KB，MB，GB
             *
             * @param limit
             */
            FileUploader.prototype.changeByte = function (limit) {
                var size = "";
                if (limit < 0.1 * 1024) // 小于 0.1KB，则转化成 B
                    size = limit.toFixed(2) + "B";
                else if (limit < 0.1 * 1024 * 1024) // 小于 0.1MB，则转化成 KB
                    size = (limit / 1024).toFixed(2) + "KB";
                else if (limit < 0.1 * 1024 * 1024 * 1024) // 小于 0.1GB，则转化成 MB
                    size = (limit / (1024 * 1024)).toFixed(2) + "MB";
                else // 其他转化成 GB
                    size = (limit / (1024 * 1024 * 1024)).toFixed(2) + "GB";
                var index = size.indexOf("."); // 获取小数点处的索引
                if (size.substr(index + 1, 2) == "00") // 获取小数点后两位的值，判断后两位是否为 00，如果是则删除 00                
                    return size.substring(0, index) + size.substr(index + 3, 2);
                return size;
            };
            /**
             * 执行上传
             *
             * @param this
             */
            FileUploader.prototype.doUpload = function () {
                // this.$uploadOk_callback({ isOk: true, msg: "ok!", imgUrl: "fdfdf" });
                // return;
                var _this = this;
                var fd = new FormData();
                if (this.$blob)
                    fd.append("file", this.$blob, this.fileName);
                else if (this.$fileObj)
                    fd.append("file", this.$fileObj);
                var xhr = new XMLHttpRequest();
                //@ts-ignore
                xhr.onreadystatechange = aj.xhr.requestHandler.delegate(null, this.$uploadOk_callback, 'json');
                xhr.open("POST", this.action, true);
                xhr.onprogress = function (ev) {
                    var progress = 0, p = ~~(ev.loaded * 1000 / ev.total);
                    p = p / 10;
                    if (progress !== p)
                        progress = p;
                    _this.progress = progress;
                };
                xhr.send(fd);
            };
            return FileUploader;
        }(BaseFileUploader));
        xhr_upload.FileUploader = FileUploader;
        /**
         * 用于继承，获取方法句柄
         */
        xhr_upload.fileUploader = new FileUploader();
        xhr_upload.fileUploader.register();
    })(xhr_upload = aj.xhr_upload || (aj.xhr_upload = {}));
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
                _this.template = html(__makeTemplateObject(["\n            <div class=\"aj-form-html-editor\">\n                <ul class=\"toolbar\" @click=\"onToolBarClk\">\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u4F53\" class=\"fa-font\"></i>\n                        <div class=\"fontfamilyChoser\" @click=\"onFontfamilyChoserClk\">\n                            <a style=\"font-family: '\u5B8B\u4F53'\">\u5B8B\u4F53</a>\n                            <a style=\"font-family: '\u9ED1\u4F53'\">\u9ED1\u4F53</a>\n                            <a style=\"font-family: '\u6977\u4F53'\">\u6977\u4F53</a>\n                            <a style=\"font-family: '\u96B6\u4E66'\">\u96B6\u4E66</a>\n                            <a style=\"font-family: '\u5E7C\u5706'\">\u5E7C\u5706</a>\n                            <a style=\"font-family: 'Microsoft YaHei'\">Microsoft YaHei</a>\n                            <a style=\"font-family: Arial\">Arial</a>\n                            <a style=\"font-family: 'Arial Narrow'\">Arial Narrow</a>\n                            <a style=\"font-family: 'Arial Black'\">Arial Black</a>\n                            <a style=\"font-family: 'Comic Sans MS'\">Comic Sans MS</a>\n                            <a style=\"font-family: Courier\">Courier</a>\n                            <a style=\"font-family: System\">System</a>\n                            <a style=\"font-family: 'Times New Roman'\">Times New Roman</a>\n                            <a style=\"font-family: Verdana\">Verdana</a>\n                        </div>\n                    </li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u53F7\" class=\"fa-header\"></i>\n                        <div class=\"fontsizeChoser\" @click=\"onFontsizeChoserClk\">\n                            <a style=\"font-size: xx-small; \">\u6781\u5C0F</a>\n                            <a style=\"font-size: x-small;  \">\u7279\u5C0F</a>\n                            <a style=\"font-size: small;    \">\u5C0F</a>\n                            <a style=\"font-size: medium;   \">\u4E2D</a>\n                            <a style=\"font-size: large;    \">\u5927</a>\n                            <a style=\"font-size: x-large;  \">\u7279\u5927</a>\n                            <a style=\"font-size: xx-large; line-height: 140%\">\u6781\u5927</a>\n                        </div>\n                    </li>\n                    <li><i title=\"\u52A0\u7C97\" class=\"bold fa-bold\"></i></li>\n                    <li><i title=\"\u659C\u4F53\" class=\"italic fa-italic\"></i></li>\n                    <li><i title=\"\u4E0B\u5212\u7EBF\" class=\"underline fa-underline\"></i></li>\n                    <li><i title=\"\u5DE6\u5BF9\u9F50\" class=\"justifyleft fa-align-left\"></i></li>\n                    <li><i title=\"\u4E2D\u95F4\u5BF9\u9F50\" class=\"justifycenter fa-align-center\"></i></li>\n                    <li><i title=\"\u53F3\u5BF9\u9F50\" class=\"justifyright fa-align-right\"></i></li>\n                    <li><i title=\"\u6570\u5B57\u7F16\u53F7\" class=\"insertorderedlist fa-list-ol\"></i></li>\n                    <li><i title=\"\u9879\u76EE\u7F16\u53F7\" class=\"insertunorderedlist fa-list-ul\"></i></li>\n                    <li><i title=\"\u589E\u52A0\u7F29\u8FDB\" class=\"outdent fa-outdent\"></i></li>\n                    <li><i title=\"\u51CF\u5C11\u7F29\u8FDB\" class=\"indent fa-indent\"></i></li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u4F53\u989C\u8272\" class=\"fa-paint-brush\"></i>\n                        <div class=\"fontColor colorPicker\" v-html=\"createColorPickerHTML()\" @click=\"onFontColorPicker\"></div>\n                    </li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u80CC\u666F\u989C\u8272\" class=\"fa-pencil\"></i>\n                        <div class=\"bgColor colorPicker\" v-html=\"createColorPickerHTML()\" @click=\"onFontBgColorPicker\"></div>\n                    </li>\n                    <li><i title=\"\u589E\u52A0\u94FE\u63A5\" class=\"createLink fa-link\"></i></li>\n                    <li><i title=\"\u589E\u52A0\u56FE\u7247\" class=\"insertImage fa-file-image-o\"></i></li>\n                    <li><i title=\"\u4E00\u952E\u5B58\u56FE\" class=\"saveRemoteImage2Local fa-hdd-o\"></i></li>\n                    <li><i title=\"\u6E05\u7406 HTML\" class=\"cleanHTML fa-eraser\"></i></li>\n                    <li><i title=\"\u5207\u6362\u5230\u4EE3\u7801\" class=\"switchMode fa-code\"></i></li>\n                </ul>\n            \n                <div class=\"editorBody\">\n                    <iframe srcdoc=\"<html><body></body></html>\"></iframe>\n                    <slot></slot>\n                </div>\n                <aj-form-popup-upload ref=\"uploadLayer\" :upload-url=\"uploadImageActionUrl\"></aj-form-popup-upload>\n            </div>\n        "], ["\n            <div class=\"aj-form-html-editor\">\n                <ul class=\"toolbar\" @click=\"onToolBarClk\">\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u4F53\" class=\"fa-font\"></i>\n                        <div class=\"fontfamilyChoser\" @click=\"onFontfamilyChoserClk\">\n                            <a style=\"font-family: '\u5B8B\u4F53'\">\u5B8B\u4F53</a>\n                            <a style=\"font-family: '\u9ED1\u4F53'\">\u9ED1\u4F53</a>\n                            <a style=\"font-family: '\u6977\u4F53'\">\u6977\u4F53</a>\n                            <a style=\"font-family: '\u96B6\u4E66'\">\u96B6\u4E66</a>\n                            <a style=\"font-family: '\u5E7C\u5706'\">\u5E7C\u5706</a>\n                            <a style=\"font-family: 'Microsoft YaHei'\">Microsoft YaHei</a>\n                            <a style=\"font-family: Arial\">Arial</a>\n                            <a style=\"font-family: 'Arial Narrow'\">Arial Narrow</a>\n                            <a style=\"font-family: 'Arial Black'\">Arial Black</a>\n                            <a style=\"font-family: 'Comic Sans MS'\">Comic Sans MS</a>\n                            <a style=\"font-family: Courier\">Courier</a>\n                            <a style=\"font-family: System\">System</a>\n                            <a style=\"font-family: 'Times New Roman'\">Times New Roman</a>\n                            <a style=\"font-family: Verdana\">Verdana</a>\n                        </div>\n                    </li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u53F7\" class=\"fa-header\"></i>\n                        <div class=\"fontsizeChoser\" @click=\"onFontsizeChoserClk\">\n                            <a style=\"font-size: xx-small; \">\u6781\u5C0F</a>\n                            <a style=\"font-size: x-small;  \">\u7279\u5C0F</a>\n                            <a style=\"font-size: small;    \">\u5C0F</a>\n                            <a style=\"font-size: medium;   \">\u4E2D</a>\n                            <a style=\"font-size: large;    \">\u5927</a>\n                            <a style=\"font-size: x-large;  \">\u7279\u5927</a>\n                            <a style=\"font-size: xx-large; line-height: 140%\">\u6781\u5927</a>\n                        </div>\n                    </li>\n                    <li><i title=\"\u52A0\u7C97\" class=\"bold fa-bold\"></i></li>\n                    <li><i title=\"\u659C\u4F53\" class=\"italic fa-italic\"></i></li>\n                    <li><i title=\"\u4E0B\u5212\u7EBF\" class=\"underline fa-underline\"></i></li>\n                    <li><i title=\"\u5DE6\u5BF9\u9F50\" class=\"justifyleft fa-align-left\"></i></li>\n                    <li><i title=\"\u4E2D\u95F4\u5BF9\u9F50\" class=\"justifycenter fa-align-center\"></i></li>\n                    <li><i title=\"\u53F3\u5BF9\u9F50\" class=\"justifyright fa-align-right\"></i></li>\n                    <li><i title=\"\u6570\u5B57\u7F16\u53F7\" class=\"insertorderedlist fa-list-ol\"></i></li>\n                    <li><i title=\"\u9879\u76EE\u7F16\u53F7\" class=\"insertunorderedlist fa-list-ul\"></i></li>\n                    <li><i title=\"\u589E\u52A0\u7F29\u8FDB\" class=\"outdent fa-outdent\"></i></li>\n                    <li><i title=\"\u51CF\u5C11\u7F29\u8FDB\" class=\"indent fa-indent\"></i></li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u4F53\u989C\u8272\" class=\"fa-paint-brush\"></i>\n                        <div class=\"fontColor colorPicker\" v-html=\"createColorPickerHTML()\" @click=\"onFontColorPicker\"></div>\n                    </li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u80CC\u666F\u989C\u8272\" class=\"fa-pencil\"></i>\n                        <div class=\"bgColor colorPicker\" v-html=\"createColorPickerHTML()\" @click=\"onFontBgColorPicker\"></div>\n                    </li>\n                    <li><i title=\"\u589E\u52A0\u94FE\u63A5\" class=\"createLink fa-link\"></i></li>\n                    <li><i title=\"\u589E\u52A0\u56FE\u7247\" class=\"insertImage fa-file-image-o\"></i></li>\n                    <li><i title=\"\u4E00\u952E\u5B58\u56FE\" class=\"saveRemoteImage2Local fa-hdd-o\"></i></li>\n                    <li><i title=\"\u6E05\u7406 HTML\" class=\"cleanHTML fa-eraser\"></i></li>\n                    <li><i title=\"\u5207\u6362\u5230\u4EE3\u7801\" class=\"switchMode fa-code\"></i></li>\n                </ul>\n            \n                <div class=\"editorBody\">\n                    <iframe srcdoc=\"<html><body></body></html>\"></iframe>\n                    <slot></slot>\n                </div>\n                <aj-form-popup-upload ref=\"uploadLayer\" :upload-url=\"uploadImageActionUrl\"></aj-form-popup-upload>\n            </div>\n        "]));
                _this.props = {
                    fieldName: { type: String, required: true },
                    uploadImageActionUrl: String // 图片上传路径
                };
                _this.fieldName = "";
                _this.fieldValue = "";
                /**
                 * 图片上传路径
                 */
                _this.uploadImageActionUrl = "";
                _this.toolbarEl = document.body;
                _this.iframeEl = document.body;
                _this.iframeDoc = document;
                _this.sourceEditor = document.body;
                _this.mode = "iframe";
                return _this;
            }
            HtmlEditor.prototype.mounted = function () {
                var _this = this;
                var el = this.$el;
                this.mode = 'iframe'; // 当前可视化编辑 iframe|textarea
                this.toolbarEl = el.$('.toolbar');
                this.iframeEl = el.$('iframe');
                // 这个方法只能写在 onload 事件里面， 不写 onload 里还不执行
                this.iframeEl.contentWindow.onload = function (ev) {
                    var iframeDoc = _this.iframeEl.contentWindow.document;
                    iframeDoc.designMode = 'on';
                    iframeDoc.addEventListener('paste', onImagePaste.bind(_this)); // 直接剪切板粘贴上传图片
                    _this.iframeDoc = iframeDoc;
                    new MutationObserver(function (mutationsList, observer) {
                        if (_this.mode === 'iframe')
                            _this.sourceEditor.value = _this.iframeDoc.body.innerHTML;
                    }).observe(iframeDoc.body, { attributes: true, childList: true, subtree: true, characterData: true });
                    _this.sourceEditor.value && _this.setIframeBody(_this.sourceEditor.value); // 有内容
                };
                this.sourceEditor = el.$('textarea');
                this.sourceEditor.classList.add("hide");
                this.sourceEditor.name = this.fieldName;
                this.sourceEditor.oninput = function (ev) {
                    if (_this.mode === 'textarea')
                        _this.setIframeBody(_this.sourceEditor.value);
                };
                this.uploadImgMgr = this.$refs.uploadLayer;
            };
            /**
            * 输入 HTML 内容
            *
            * @param html
            */
            HtmlEditor.prototype.setIframeBody = function (html) {
                this.iframeDoc.body.innerHTML = html;
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
                    this.mode = 'textarea';
                    grayImg.call(this, true);
                }
                else {
                    this.iframeEl.classList.remove('hide');
                    this.sourceEditor.classList.add('hide');
                    this.mode = 'iframe';
                    grayImg.call(this, false);
                }
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
                            this.uploadImgMgr.show(function (json) {
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
                        this.setIframeBody(HtmlSanitizer.SanitizeHtml(this.iframeDoc.body.innerHTML)); // 清理冗余 HTML
                        break;
                    case 'saveRemoteImage2Local':
                        saveRemoteImage2Local.call(this);
                        break;
                    default:
                        this.format(clsName);
                }
            };
            /**
             * 通过 document.execCommand() 来操纵可编辑内容区域的元素
             *
             * @param type 命令的名称
             * @param para 一些命令（例如 insertImage）需要额外的参数（insertImage 需要提供插入 image 的 url），默认为 null
             */
            HtmlEditor.prototype.format = function (type, para) {
                if (para)
                    this.iframeDoc.execCommand(type, false, para);
                else
                    this.iframeDoc.execCommand(type, false);
                this.iframeEl.contentWindow.focus();
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
            });
        }
        /**
         * 一键存图
         *
         * @param this
         */
        function saveRemoteImage2Local() {
            var arr = this.iframeDoc.querySelectorAll('img'), remotePicArr = new Array(), srcs = [];
            for (var i = 0, j = arr.length; i < j; i++) {
                var imgEl = arr[i], src = imgEl.getAttribute('src');
                if (/^http/.test(src)) {
                    remotePicArr.push(imgEl);
                    srcs.push(src);
                }
            }
            if (srcs.length)
                aj.xhr.post('../downAllPics/', function (json) {
                    var _arr = json.pics;
                    for (var i = 0, j = _arr.length; i < j; i++)
                        remotePicArr[i].src = "images/" + _arr[i]; // 改变 DOM 的旧图片地址为新的
                    aj.alert('所有图片下载完成。');
                }, { pics: srcs.join('|') });
            else
                aj.alert('未发现有远程图片');
        }
        /**
         * 粘贴图片
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
            var items = ev.clipboardData && ev.clipboardData.items, file = null; // file 就是剪切板中的图片文件
            if (items && items.length) { // 检索剪切板 items
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
                    // 复用上传的方法
                    Vue.options.components["aj-xhr-upload"].extendOptions.methods.doUpload.call({
                        action: _this.uploadImageActionUrl,
                        progress: 0,
                        uploadOk_callback: function (j) {
                            if (j.isOk)
                                this.format("insertImage", this.ajResources.imgPerfix + j.imgUrl);
                        },
                        $blob: newBlob,
                        $fileName: 'foo.jpg' // 文件名不重要，反正上传到云空间会重命名
                    });
                });
            }
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
        new HtmlEditor().register();
    })(form = aj.form || (aj.form = {}));
})(aj || (aj = {}));

"use strict";

var aj;
(function (aj) {
    var xhr_upload;
    (function (xhr_upload) {
        /**
         * 图片占位符，用户没有选定图片时候使用的图片
         */
        var emptyImg = "data:image/svg+xml,%3Csvg class='icon' viewBox='0 0 1024 1024' xmlns='http://www.w3.org/2000/svg' width='200' height='200'%3E%3Cpath d='M304.128 456.192c48.64 0 88.064-39.424 88.064-88.064s-39.424-88.064-88.064-88.064-88.064 39.424-88.064 88.064 39.424 88.064 88.064 88.064zm0-116.224c15.36 0 28.16 12.288 28.16 28.16s-12.288 28.16-28.16 28.16-28.16-12.288-28.16-28.16 12.288-28.16 28.16-28.16z' " +
            "fill='%23e6e6e6'/%3E%3Cpath d='M887.296 159.744H136.704C96.768 159.744 64 192 64 232.448v559.104c0 39.936 32.256 72.704 72.704 72.704h198.144L500.224 688.64l-36.352-222.72 162.304-130.56-61.44 143.872 92.672 214.016-105.472 171.008h335.36C927.232 864.256 960 832 960 791.552V232.448c0-39.936-32.256-72.704-72.704-72.704zm-138.752 71.68v.512H857.6c16.384 0 30.208 13.312 30.208 30.208v399.872L673.28 408.064l75.264-176.64zM304.64 " +
            "792.064H165.888c-16.384 0-30.208-13.312-30.208-30.208v-9.728l138.752-164.352 104.96 124.416-74.752 79.872zm81.92-355.84l37.376 228.864-.512.512-142.848-169.984c-3.072-3.584-9.216-3.584-12.288 0L135.68 652.8V262.144c0-16.384 13.312-30.208 30.208-30.208h474.624L386.56 436.224zm501.248 325.632c0 16.896-13.312 30.208-29.696 30.208H680.96l57.344-93.184-87.552-202.24 7.168-7.68 229.888 272.896z' fill='%23e6e6e6'/%3E%3C/svg%3E";
        var emptyImg2 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALMAAAB2CAYAAACDMaL0AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA1xpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDM0MiwgMjAxMC8wMS8xMC0xODowNjo0MyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo5RkM5NjQxRDQxQUNFNDExOTcwMkU4MjNEOTc3MDU5RiIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo4QTc3NDNFQkFDNDIxMUU0QTEzMkQyOUQzOTFFQkMzRCIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo4QTc3NDNFQUFDNDIxMUU0QTEzMkQyOUQzOTFFQkMzRCIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6OUZDOTY0MUQ0MUFDRTQxMTk3MDJFODIzRDk3NzA1OUYiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6OUZDOTY0MUQ0MUFDRTQxMTk3MDJFODIzRDk3NzA1OUYiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz77Tka5AAADFElEQVR42uzd0W3aUBiAUVNFlTqBO4VRn/scT1FvQaaAKWCK0hX6FKZghb44Rr2uLGqCL+AY7HMkqyitSHC+XP5riDoryzIZs91ulzAJ5SfngJGYiZnREDNiBjGDmEHMiBnEDGIGMYOYETOIGcQMYkbMIGYQM4gZxIyYQcwgZhAzYgYxg5hBzPDP0wQeYzmR7+Wf6vhdHd+tzDy6z9Xxzco8HZvq2I/sMaXVUYTbX8Q8DasQ8+vIHlcWfkAXNoDTsR9hyMlIH5OYETPjeNYRs1OAmEHM0I8np+BuHK4Xz8PtrdNhZX5UeXUsq+NndfwIt3Onxcr8iCvyIeD6Vbyi8XErtJX54Vbl4sTfFU6PlTlWFv58HfBzt4Xs+rGYo9XheGlYzA9tEebTe2RlNjNHbb6yls3XR/qV/H0337GNDaCYY1flonH7eYCvYRtW4GbQqzDyGHuMGZ3n5PTE1YWPXhFfGnP7vhE0Yu50BeG5Zayogxri6X0jR2PGpaty8c4cfYtX3/LEb3+IuecrA3mH0K+ZnbPG/J0mXvgwZvQ8XpxbMesIY5766xU9O7r/1UBzuJhHbB05huxDoF2u9ebv/JAsGlct+n7WMWbQatFxREg7rvbGDTEPqstmMO0Q8iKMH5lTKuYhV+dzm8E84r68R1nMgyvOrMwx+gg69S0Sc9cV9dSIEBvmMtzPreOzARRzVNBt4WZJ/Asjyws3g0YUMd/UrYJKIzeD9fXrwiZSzLewPArpmqgOq/k84t9n4fOvQ9hWaTHfdDM4D4H1vdLnJ36o7vmXCgbhN03iV+f67ZrXhrQO93WwPTOSLFu+jnrjZ/MXzMpy3P9Lwm63u/cH+JL8/+b841k55hlgZsxgyNW+nqOzDqsyxoy71nwT1NcwdvRxPVrMfPhMnlwwXojZKbjbsQMzM2IGMYOYQcwgZsQMYgYxg5gRMzy+0b+fGTGDmEHMIGYQM2IGMYOYQcyIGcQMYgYxg5gRM4gZxAxiRswgZhAziBnEjJhBzCBmEDNiBjGDmEHMIGbEDGIGMYOYmaY3AQYAXojZOq5J9RcAAAAASUVORK5CYII=";
        // 文件头判别，看看是否为图片
        var imgHeader = { "jpeg": "/9j/4", "gif": "R0lGOD", "png": "iVBORw" };
        var ImgFileUploader = /** @class */ (function (_super) {
            __extends(ImgFileUploader, _super);
            function ImgFileUploader() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = "aj-img-uploder";
                _this.template = "<div class=\"aj-img-uploader\">\n                <img :src=\"imgSrc\" />" + xhr_upload.fileUploader.template + "\n            </div>";
                /**
                 * 预览图像显示的内容，可以是图片的 url 也可以是图片的 base64 形式
                 */
                _this.imgSrc = emptyImg2;
                _this.imgMaxWidth = 0;
                _this.imgMaxHeight = 0;
                /**
                 * 图片前缀
                 */
                _this.imgPerfix = "";
                return _this;
            }
            ImgFileUploader.prototype.propsFactory = function () {
                var p = aj.apply({
                    imgMaxWidth: { type: Number, default: 1920 },
                    imgMaxHeight: { type: Number, default: 1680 },
                    imgPerfix: { type: String }
                }, this.props);
                p.accpectFileType = { type: String, default: "image/*" };
                p.limitFileType = { type: String, default: 'jpg|png|gif|jpeg' };
                return p;
            };
            // onUploadInputChange(ev: Event): void {
            //     super.onUploadInputChange(ev);
            //     // fileUploader.onUploadInputChange.call(this, ev);
            //     this.readBase64();
            // }
            ImgFileUploader.prototype.beforeCreate = function () {
                this.$options.template = this.$options.template.replace('<span class="slot"></span>', '<br />最大尺寸：{{imgMaxWidth}}x{{imgMaxHeight}}');
            };
            ImgFileUploader.prototype.mounted = function () {
                var _this = this;
                if (this.fieldValue)
                    this.imgSrc = this.imgPerfix + this.fieldValue;
                var imgEl = this.$el.$('img');
                imgEl.onload = function () {
                    if (imgEl.width > _this.imgMaxWidth || imgEl.height > _this.imgMaxHeight) {
                        _this.errMsg = '图片大小尺寸不符合要求哦，请裁剪图片重新上传吧~';
                    }
                    // if (this.fileSize > 300 * 1024)  // 大于 300k 才压缩
                    //     img.compressAsBlob(imgEl, (blob: Blob): void => {
                    //         this.$blob = blob;
                    //     });
                };
            };
            ImgFileUploader.prototype.watchFactory = function () {
                return {
                    imgBase64Str: function (newV) {
                        // 文件头判别，看看是否为图片
                        var isPic = false;
                        for (var i in imgHeader) {
                            if (~newV.indexOf(imgHeader[i])) {
                                isPic = true;
                                break;
                            }
                        }
                        if (!isPic) {
                            var msg_1 = '亲，改了扩展名我还能认得你不是图片哦';
                            Vue.set(this.errStatus, 2, msg_1);
                            aj.alert(msg_1);
                        }
                        else
                            Vue.set(this.errStatus, 2, "");
                    },
                    errMsg: function (newV) {
                        if (!newV) // 没有任何错误才显示图片
                            this.readBase64();
                    }
                };
            };
            ImgFileUploader.prototype.readBase64 = function () {
                var _this = this;
                var reader = new FileReader();
                reader.onload = function (ev) {
                    var fileReader = ev.target;
                    _this.imgSrc = fileReader.result;
                };
                this.$fileObj && reader.readAsDataURL(this.$fileObj);
            };
            return ImgFileUploader;
        }(xhr_upload.FileUploader));
        xhr_upload.ImgFileUploader = ImgFileUploader;
        new ImgFileUploader().register();
    })(xhr_upload = aj.xhr_upload || (aj.xhr_upload = {}));
})(aj || (aj = {}));

"use strict";

/**
* 将上传控件嵌入到一个浮出层中
*/
Vue.component('aj-form-popup-upload', {
    template: html(__makeTemplateObject(["\n        <aj-layer>\n            <h3>\u56FE\u7247\u4E0A\u4F20</h3>\n            <p>\u4E0A\u4F20\u6210\u529F\u540E\u81EA\u52A8\u63D2\u5165\u5230\u6B63\u6587</p>\n            <aj-img-uploder ref=\"uploadControl\" :action=\"uploadUrl\"></aj-img-uploder>\n        </aj-layer>\n    "], ["\n        <aj-layer>\n            <h3>\u56FE\u7247\u4E0A\u4F20</h3>\n            <p>\u4E0A\u4F20\u6210\u529F\u540E\u81EA\u52A8\u63D2\u5165\u5230\u6B63\u6587</p>\n            <aj-img-uploder ref=\"uploadControl\" :action=\"uploadUrl\"></aj-img-uploder>\n        </aj-layer>\n    "])),
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
                    if (i.value == id)
                        i.selected = true;
                });
            }
            utils.selectOption = selectOption;
        })(utils = form.utils || (form.utils = {}));
    })(form = aj.form || (aj.form = {}));
})(aj || (aj = {}));

"use strict";
var aj;
(function (aj) {
    var form;
    (function (form_1) {
        if (!('validity' in document.createElement('input')))
            window.alert("不支持 HTML5 表单验证");
        /**
         * 表单验证器
         */
        var Validator = /** @class */ (function () {
            function Validator(el) {
                this.errorElements = [];
                // let isMsgNewLine: boolean = el.dataset.msgNewline === "true";
                el.setAttribute('novalidate', 'true'); // 禁止浏览器原生的错误提示
                this.el = el;
                this.checkEveryField();
            }
            /**
             * 对每一个表单元素监听事件，一失去焦点就触发表单验证
             */
            Validator.prototype.checkEveryField = function () {
                var _this = this;
                this.el.$('input', function (input) {
                    input.addEventListener('blur', function (ev) {
                        var el = ev.target;
                        if (el.tagName == "A" || Validator.isIgnoreEl(el)) // 忽略部分元素；a 元素也有 blur 事件，忽略之
                            return;
                        var result = Validator.check(el);
                        if (result) { // 如果有错误,就把它显示出来
                            _this.errorElements.push(result);
                            _this.showError(result);
                        }
                        else
                            _this.removeError(el); // 否则, 移除所有存在的错误信息
                    }, true);
                });
            };
            /**
             *
             * @param err
             */
            Validator.prototype.showError = function (err) {
                var _a;
                var el = err.el, id = el.id || el.name; // 获取字段 id 或者 name
                if (!id)
                    return;
                err.el.classList.add('error'); // 将错误类添加到字段
                // 检查错误消息字段是否已经存在 如果没有, 就创建一个
                var message = err.el.form.$(".error-message#error-for-" + id);
                if (!message) {
                    message = document.createElement('div');
                    message.className = 'error-message';
                    message.id = 'error-for-' + id;
                    (_a = el.parentNode) === null || _a === void 0 ? void 0 : _a.insertBefore(message, el.nextSibling);
                }
                el.setAttribute('aria-describedby', 'error-for-' + id); // 添加 ARIA role 到字段
                message.innerHTML = err.msg; // 更新错误信息
                // if (!isNewLine)// 显示错误信息
                //     message.style.display = 'inline-block';
                message.classList.remove('hide');
            };
            /**
             * 移除所有的错误信息
             *
             * @param el
             */
            Validator.prototype.removeError = function (el) {
                var id = el.id || el.name; // 获取字段的 id 或者 name
                if (!id)
                    return;
                el.classList.remove('error'); // 删除字段的错误类
                el.removeAttribute('aria-describedby'); // 移除字段的 ARIA role
                var message = el.form.$(".error-message#error-for-" + id); // 检查 DOM 中是否有错误消息
                if (message) {
                    message.innerHTML = ''; // 如果有错误消息就隐藏它
                    message.classList.add('hide');
                }
            };
            /**
             * 是否忽略的表单元素
             *
             * @param el
             */
            Validator.isIgnoreEl = function (el) {
                return el.disabled || el.type === 'file' || el.type === 'reset' || el.type === 'submit' || el.type === 'button';
            };
            /**
             * 验证字段
             *
             * @param field 表单字段元素
             * @returns 若验证通过返回 null，否则返回 ErrorElement
             */
            Validator.check = function (field) {
                // if (!field || !field.getAttribute)
                //     console.log(field);
                var validity = field.validity; // 获取 validity
                if (!validity)
                    throw '浏览器不支持 HTML5 表单验证';
                if (validity.valid) // 通过验证
                    return null;
                else {
                    var result = {
                        el: field,
                        msg: "无效输入" // 通用错误讯息 The value you entered for this field is invalid.
                    };
                    if (validity.valueMissing) // 如果是必填字段但是字段为空时
                        result.msg = '该项是必填项';
                    if (validity.typeMismatch) { // 如果类型不正确
                        if (field.type === 'email')
                            result.msg = '请输入有效的邮件地址';
                        else if (field.type === 'url')
                            result.msg = '请输入一个有效的网址';
                        else
                            result.msg = '请输入正确的类型';
                    }
                    if (validity.tooShort)
                        result.msg = "\u8BF7\u8F93\u5165\u81F3\u5C11" + field.getAttribute('minLength') + "\u4E2A\u5B57\u7B26\u3002\u5F53\u524D\u4F60\u8F93\u5165\u4E86" + field.value.length + "\u4E2A\u5B57\u7B26\u3002";
                    // 'Please lengthen this text to ' + field.getAttribute('minLength') + ' characters or more. You are currently using ' + field.value.length + ' characters.'
                    if (validity.tooLong)
                        result.msg = "\u4F60\u53EA\u80FD\u8F93\u5165\u6700\u591A" + field.getAttribute('maxLength') + "\u4E2A\u5B57\u7B26\u3002\u5F53\u524D\u4F60\u8F93\u5165\u4E86" + field.value.length + "\u4E2A\u5B57\u7B26\u3002";
                    // 'Please shorten this text to no more than ' + field.getAttribute('maxLength') + ' characters. You are currently using ' + field.value.length + ' characters.';
                    if (validity.badInput)
                        result.msg = '请输入数字';
                    if (validity.stepMismatch) // 如果数字值与步进间隔不匹配
                        result.msg = '请选择一个有效值';
                    if (validity.rangeOverflow) // 如果数字字段的值大于 max 的值
                        result.msg = "\u8BF7\u9009\u62E9\u5C0F\u4E8E " + field.getAttribute('max') + " \u7684\u503C";
                    // return 'Please select a value that is no more than ' + field.getAttribute('max') + '.';
                    if (validity.rangeUnderflow)
                        result.msg = "\u8BF7\u9009\u62E9\u5927\u4E8E " + field.getAttribute('min') + " \u7684\u503C";
                    // return 'Please select a value that is no less than ' + field.getAttribute('min') + '.';
                    if (validity.patternMismatch)
                        result.msg = field.getAttribute('title') || '格式要求不正确';
                    return result;
                }
            };
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
                    error = this.check(el);
                    if (error) {
                        // showError(el);
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
            return Validator;
        }());
        form_1.Validator = Validator;
    })(form = aj.form || (aj.form = {}));
})(aj || (aj = {}));
// ; (function (window, document, undefined) {  // 确保 ValidityState 全部被支持 (所有的功能) 
//     var supported = function () {
//         var input = document.createElement('input');
//         return ('validity' in input && 'badInput' in input.validity && 'patternMismatch' in input.validity && 'rangeOverflow' in input.validity && 'rangeUnderflow' in input.validity && 'stepMismatch' in input.validity && 'tooLong' in input.validity && 'tooShort' in input.validity && 'typeMismatch' in input.validity && 'valid' in input.validity && 'valueMissing' in input.validity);
//     };
//     /** 
//      *  Generate the field validity object
//      *  @param {Node]} field The field to validate 
//      *  @return {Object} The validity object 
//      **/
//     var getValidityState = function (field) {
//         // 变量 
//         var type = field.getAttribute('type') || input.nodeName.toLowerCase(); 
//         var isNum = type === 'number' || type === 'range'; 
//         var length = field.value.length; var valid = true; 
//         //检测支持性
//         var checkValidity = {
//             badInput: (isNum && length > 0 && !/[-+]?[0-9]/.test(field.value)),
//             // 数字字段的值不是数字 
//             patternMismatch: (field.hasAttribute('pattern') && length > 0 && new RegExp(field.getAttribute('pattern')).test(field.value) === false),
//             // 输入的值不符合模式 
//             rangeOverflow: (field.hasAttribute('max') && isNum && field.value > 1 && parseInt(field.value, 10) > parseInt(field.getAttribute('max'), 10)),
//             // 数字字段的值大于max属性值 
//             rangeUnderflow: (field.hasAttribute('min') && isNum && field.value > 1 && parseInt(field.value, 10) < parseInt(field.getAttribute('min'), 10)),
//             // 数字字段的值小于min属性值
//             stepMismatch: (field.hasAttribute('step') && field.getAttribute('step') !== 'any' && isNum && Number(field.value) % parseFloat(field.getAttribute('step')) !== 0),
//             // 数字字段的值不符合 stepattribute 
//             tooLong: (field.hasAttribute('maxLength') && field.getAttribute('maxLength') > 0 && length > parseInt(field.getAttribute('maxLength'), 10)),
//             // 用户在具有maxLength属性的字段中输入的值的长度大于属性值
//             tooShort: (field.hasAttribute('minLength') && field.getAttribute('minLength') > 0 && length > 0 && length < parseInt(field.getAttribute('minLength'), 10)),
//             // 用户在具有minLength属性的字段中输入的值的长度小于属性值 
//             typeMismatch: (length > 0 && ((type === 'email' && !/^([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22))*\x40([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d))*$/.test(field.value)) || (type === 'url' && !/^(?:(?:https?|HTTPS?|ftp|FTP):\/\/)(?:\S+(?::\S*)?@)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-zA-Z\u00a1-\uffff0-9]-*)*[a-zA-Z\u00a1-\uffff0-9]+)(?:\.(?:[a-zA-Z\u00a1-\uffff0-9]-*)*[a-zA-Z\u00a1-\uffff0-9]+)*)(?::\d{2,5})?(?:[\/?#]\S*)?$/.test(field.value)))),
//             // email 或者 URL 字段的值不是一个 email地址或者 URL 
//             valueMissing: (field.hasAttribute('required') && (((type === 'checkbox' || type === 'radio') && !field.checked) || (type === 'select' && field.options[field.selectedIndex].value < 1) || (type !== 'checkbox' && type !== 'radio' && type !== 'select' && length < 1))) // 必填字段没有值 
//         };
//         // 检查是否有错误 
//         for (var key in checkValidity) {
//             if (checkValidity.hasOwnProperty(key)) {
//                 // If there's an error, change valid value 
//                 if (checkValidity[key]) { valid = false; break; }
//             }
//         }
//         // 给 validity 对象添加 valid 属性
//         checkValidity.valid = valid;
//         // 返回对象 
//         return checkValidity;
//     };
//     // 如果不支持完整的 ValidityState 功能，则可以使用polyfill 
//     if (!supported()) {
//         Object.defineProperty(HTMLInputElement.prototype, 'validity', {
//             get: function ValidityState() {
//                 return getValidityState(this);
//             },
//             configurable: true,
//         });
//     }
// })(window, document);
