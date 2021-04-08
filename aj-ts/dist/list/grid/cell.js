"use strict";
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
