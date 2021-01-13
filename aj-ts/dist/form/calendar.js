"use strict";
var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};
Vue.component('aj-form-calendar', {
    template: html(__makeTemplateObject(["\n        <div class=\"aj-form-calendar\">\n            <div class=\"selectYearMonth\">\n                <a href=\"###\" @click=\"getDate('preYear')\" class=\"preYear\" title=\"\u4E0A\u4E00\u5E74\">&lt;</a> \n                <select @change=\"setMonth\" v-model=\"month\">\n                    <option value=\"1\">\u4E00\u6708</option><option value=\"2\">\u4E8C\u6708</option><option value=\"3\">\u4E09\u6708</option><option value=\"4\">\u56DB\u6708</option>\n                    <option value=\"5\">\u4E94\u6708</option><option value=\"6\">\u516D\u6708</option><option value=\"7\">\u4E03\u6708</option><option value=\"8\">\u516B\u6708</option>\n                    <option value=\"9\">\u4E5D\u6708</option><option value=\"10\">\u5341\u6708</option><option value=\"11\">\u5341\u4E00\u6708</option><option value=\"12\">\u5341\u4E8C\u6708</option>\n                </select>\n                <a href=\"###\" @click=\"getDate('nextYear')\" class=\"nextYear\" title=\"\u4E0B\u4E00\u5E74\">&gt;</a>\n            </div>\n            <div class=\"showCurrentYearMonth\">\n                <span class=\"showYear\">{{year}}</span>/<span class=\"showMonth\">{{month}}</span>\n            </div>\n            <table>\n                <thead>\n                    <tr><td>\u65E5</td><td>\u4E00</td><td>\u4E8C</td><td>\u4E09</td><td>\u56DB</td><td>\u4E94</td><td>\u516D</td></tr>\n                </thead>\n                <tbody @click=\"pickDay\"></tbody>\n            </table>\n            <div v-if=\"showTime\" class=\"showTime\">\n                \u65F6 <select class=\"hour aj-select\"><option v-for=\"n in 24\">{{n}}</option></select>\n                \u5206 <select class=\"minute aj-select\"><option v-for=\"n in 61\">{{n - 1}}</option></select>\n                <a href=\"#\" @click=\"pickupTime\">\u9009\u62E9\u65F6\u95F4</a>\n            </div>\n        </div>    \n    "], ["\n        <div class=\"aj-form-calendar\">\n            <div class=\"selectYearMonth\">\n                <a href=\"###\" @click=\"getDate('preYear')\" class=\"preYear\" title=\"\u4E0A\u4E00\u5E74\">&lt;</a> \n                <select @change=\"setMonth\" v-model=\"month\">\n                    <option value=\"1\">\u4E00\u6708</option><option value=\"2\">\u4E8C\u6708</option><option value=\"3\">\u4E09\u6708</option><option value=\"4\">\u56DB\u6708</option>\n                    <option value=\"5\">\u4E94\u6708</option><option value=\"6\">\u516D\u6708</option><option value=\"7\">\u4E03\u6708</option><option value=\"8\">\u516B\u6708</option>\n                    <option value=\"9\">\u4E5D\u6708</option><option value=\"10\">\u5341\u6708</option><option value=\"11\">\u5341\u4E00\u6708</option><option value=\"12\">\u5341\u4E8C\u6708</option>\n                </select>\n                <a href=\"###\" @click=\"getDate('nextYear')\" class=\"nextYear\" title=\"\u4E0B\u4E00\u5E74\">&gt;</a>\n            </div>\n            <div class=\"showCurrentYearMonth\">\n                <span class=\"showYear\">{{year}}</span>/<span class=\"showMonth\">{{month}}</span>\n            </div>\n            <table>\n                <thead>\n                    <tr><td>\u65E5</td><td>\u4E00</td><td>\u4E8C</td><td>\u4E09</td><td>\u56DB</td><td>\u4E94</td><td>\u516D</td></tr>\n                </thead>\n                <tbody @click=\"pickDay\"></tbody>\n            </table>\n            <div v-if=\"showTime\" class=\"showTime\">\n                \u65F6 <select class=\"hour aj-select\"><option v-for=\"n in 24\">{{n}}</option></select>\n                \u5206 <select class=\"minute aj-select\"><option v-for=\"n in 61\">{{n - 1}}</option></select>\n                <a href=\"#\" @click=\"pickupTime\">\u9009\u62E9\u65F6\u95F4</a>\n            </div>\n        </div>    \n    "])),
    data: function () {
        var date = new Date;
        return {
            date: date,
            year: date.getFullYear(),
            month: date.getMonth() + 1,
            day: 1
        };
    },
    props: { showTime: false },
    mounted: function () {
        this.$options.watch.date.call(this);
    },
    watch: {
        date: function () {
            this.year = this.date.getFullYear();
            this.month = this.date.getMonth() + 1;
            this.render();
        }
    },
    methods: {
        render: function () {
            var arr = this.getDateArr(); // 用来保存日期列表
            var frag = document.createDocumentFragment(); // 插入日期
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
                            if (this.isSameDay(on, this.date)) {
                                cell.classList.add('onToday');
                                this.onToday && this.onToday(cell);
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
        },
        /**
         * 获取指定的日期
         *
         * @param this
         * @param dateType
         * @param month
         */
        getDate: function (dateType, month) {
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
        },
        /**
         *
         *
         * @param this
         * @param $event
         */
        setMonth: function ($event) {
            var el = $event.target;
            this.getDate('setMonth', Number(el.selectedOptions[0].value));
        },
        /**
         * 获取空白的非上月天数 + 当月天数
         *
         * @param this
         */
        getDateArr: function () {
            var arr = [];
            // 用 当月第一天 在一周中的日期值 作为 当月 离 第一天的天数
            for (var i = 1, firstDay = new Date(this.year, this.month - 1, 1).getDay(); i <= firstDay; i++)
                arr.push(0);
            // 用 当月最后一天 在一个月中的 日期值 作为 当月的天数
            for (var i = 1, monthDay = new Date(this.year, this.month, 0).getDate(); i <= monthDay; i++)
                arr.push(i);
            return arr;
        },
        /**
         * 获取日期
         *
         * @param this
         * @param $event
         */
        pickDay: function ($event) {
            var el = $event.target, date = el.title;
            this.$emit('pick-date', date);
            return date;
        },
        /**
         *
         * @param this
         * @param $event
         */
        pickupTime: function ($event) {
            var hour = this.$el.$('.hour'), minute = this.$el.$('.minute');
            var time = hour.selectedOptions[0].value + ':' + minute.selectedOptions[0].value;
            this.$emit('pick-time', time);
        },
        /**
         * 判断两个日期是否同一日
         *
         * @param d1
         * @param d2
         */
        isSameDay: function (d1, d2) {
            return (d1.getFullYear() == d2.getFullYear() && d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate());
        }
    }
});
