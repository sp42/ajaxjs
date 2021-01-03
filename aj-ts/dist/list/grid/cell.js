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
            this.BUS.$emit('on-' + this.type + '-select', this.$parent.form);
        }
    }
});
Vue.component('aj-grid-open-link', {
    template: '<a href="#" @click="fireSelect"><i class="fa fa-external-link"></i> 详情</a>',
    methods: {
        fireSelect: function () {
            this.BUS.$emit('on-open-link-clk', this.$parent.form);
        }
    }
});
