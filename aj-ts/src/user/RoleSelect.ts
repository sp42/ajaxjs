// Vue.component('aj-tree-user-role-select', {
//     mixins: [aj.treeLike],
//     template: '<select name="roleId" class="aj-select"></select>',
//     props: {
//         value: { type: Number, required: false },// 请求远端的分类 id，必填
//         json: Array,
//         noJump: { type: Boolean, defualt: false }// 是否自动跳转
//     },
//     mounted() {
//         this.rendererOption(this.json, this.$el, this.value, { makeAllOption: false });

//         if (!this.noJump)
//             this.$el.onchange = () => location.assign("?roleId=" + this.$el.options[this.$el.selectedIndex].value);
//     }
// });
