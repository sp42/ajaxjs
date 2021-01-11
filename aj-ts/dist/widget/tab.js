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
// aj.widget.tabable = (() => {
//     // 按次序选中目标
//     var select = (_new) => {
//         var oldSelected = _new.parentNode.$('.selected');
//         if (_new === oldSelected) // 没变化
//             return;
//         oldSelected && oldSelected.classList.remove('selected');
//         _new.classList.add('selected');
//     }
//     return {
//         mounted(this: Vue) {
//             var ul = <HTMLElement>this.$el.$('.aj-simple-tab-horizontal > ul');
//             ul.onclick = (e: Event) => {
//                 let el = <HTMLElement>e.target;
//                 select(el);
//                 let index = Array.prototype.indexOf.call(el.parentElement?.children, el);
//                 let _new = this.$el.$('.aj-simple-tab-horizontal > div')?.children[index];
//                 select(_new);
//             };
//             // @ts-ignore
//             ul.onclick({ target: ul.children[0] });
//             //this.$options.watch.selected.call(this, 0);
//         }
//     };
// })();
