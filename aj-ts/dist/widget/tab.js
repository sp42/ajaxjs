"use strict";
/**
 * Tab 组件
 *
 * 参考：https://vuejs.org/v2/guide/components.html#Content-Distribution-with-Slots
 */
Vue.component('aj-tab', {
    template: "\n        <div :class=\"isVertical ? 'aj-simple-tab-vertical' : 'aj-tab' \">\n\t      <button v-for=\"tab in tabs\" v-bind:key=\"tab.name\"\n\t        v-bind:class=\"['tab-button', { active: currentTab.name === tab.name }]\"\n\t        v-on:click=\"currentTab = tab\">{{tab.name}}\n\t      </button>\n\t      <component v-bind:is=\"currentTab.component\" class=\"tab\"></component>\n        </div>\n    ",
    props: {
        isVertical: Boolean // 是否垂直方向的布局，默认 false,
    },
    data: function () {
        return {
            tabs: [],
            currentTab: {}
        };
    },
    mounted: function () {
        var arr = this.$slots.default;
        for (var i = 0; i < arr.length; i++) {
            var el = arr[i];
            if (el.tag === 'textarea') {
                this.tabs.push({
                    name: el.data.attrs['data-title'],
                    component: {
                        template: '<div>' + el.children[0].text + "</div>"
                    }
                });
            }
        }
        this.currentTab = this.tabs[0];
    }
});
aj.widget.tabable = (function () {
    // 按次序选中目标
    var select = function (_new) {
        var oldSelected = _new.parentNode.$('.selected');
        if (_new === oldSelected) // 没变化
            return;
        oldSelected && oldSelected.classList.remove('selected');
        _new.classList.add('selected');
    };
    return {
        mounted: function () {
            var _this = this;
            var ul = this.$el.$('.aj-simple-tab-horizontal > ul');
            ul.onclick = function (e) {
                var _a, _b;
                var el = e.target;
                select(el);
                var index = Array.prototype.indexOf.call((_a = el.parentElement) === null || _a === void 0 ? void 0 : _a.children, el);
                var _new = (_b = _this.$el.$('.aj-simple-tab-horizontal > div')) === null || _b === void 0 ? void 0 : _b.children[index];
                select(_new);
            };
            // @ts-ignore
            ul.onclick({ target: ul.children[0] });
            //this.$options.watch.selected.call(this, 0);
        }
    };
})();
