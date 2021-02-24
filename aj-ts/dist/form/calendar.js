"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};
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
