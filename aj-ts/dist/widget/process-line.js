"use strict";
Vue.component('aj-process-line', {
    template: "\n        <div class=\"aj-process-line\">\n            <div class=\"process-line\">\n                <div v-for=\"(item, index) in items\" :class=\"{current: index == current, done: index < current}\">\n                    <span>{{index + 1}}</span><p>{{item}}</p>\n                </div>\n            </div>\n        </div>    \n    ",
    props: {
        items: {
            type: Array,
            default: function () {
                return ['Step 1', 'Step 2', 'Step 3'];
            }
        }
    },
    data: function () {
        return {
            current: 0
        };
    },
    methods: {
        /**
         *
         * @param this
         * @param i
         */
        go: function (i) {
            this.current = i;
        },
        /**
         *
         * @param this
         */
        perv: function () {
            var perv = this.current - 1;
            if (perv < 0)
                perv = this.items.length - 1;
            this.go(perv);
        },
        /**
         *
         * @param this `
         */
        next: function () {
            var next = this.current + 1;
            if (this.items.length == next)
                next = 0; // 循环
            this.go(next);
        }
    }
});
