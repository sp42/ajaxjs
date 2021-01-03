"use strict";
Vue.component('aj-form-calendar-input', {
    template: "\n        <div class=\"aj-form-calendar-input\" :class=\"{'show-time': showTime}\" @mouseover=\"onMouseOver\">\n            <div class=\"icon fa fa-calendar\"></div>\n            <input :placeholder=\"placeholder\" size=\"12\" :name=\"fieldName\" :value=\"date + (dateOnly ? '' : ' ' + time)\" type=\"text\" autocomplete=\"off\"/>\n            <aj-form-calendar ref=\"calendar\" :show-time=\"showTime\" @pick-date=\"recEvent\" @pick-time=\"recTimeEvent\">\n            </aj-form-calendar>\n        </div>\n    ",
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
            var arr = this.fieldValue.split(' ')[0];
            var _arr = arr.split('-');
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
        onMouseOver: function ($event) {
            if (this.positionFixed) {
                var el = $event.currentTarget;
                var b = el.getBoundingClientRect();
                var c = this.$el.$('.aj-form-calendar');
                c.style.top = (b.top + el.clientHeight - 0) + 'px';
                c.style.left = ((b.left - 0) + 0) + 'px';
            }
        }
    }
});
