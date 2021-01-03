"use strict";
Vue.component('aj-tree-user-role-select', {
    mixins: [aj.treeLike],
    template: '<select name="roleId" class="aj-select"></select>',
    props: {
        value: { type: Number, required: false },
        json: Array,
        noJump: { type: Boolean, defualt: false } // 是否自动跳转
    },
    mounted: function () {
        var _this = this;
        this.rendererOption(this.json, this.$el, this.value, { makeAllOption: false });
        if (!this.noJump)
            this.$el.onchange = function () { return location.assign("?roleId=" + _this.$el.options[_this.$el.selectedIndex].value); };
    }
});
