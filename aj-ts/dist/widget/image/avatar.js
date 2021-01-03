"use strict";
Vue.component('aj-avatar', {
    template: "\n        <a :href=\"avatar\" target=\"_blank\">\n            <img :src=\"avatar\" style=\"max-width:50px;max-height:60px;vertical-align: middle;\" @mouseenter=\"mouseEnter\" @mouseleave=\"mouseLeave\" />\n        </a>\n    ",
    props: {
        avatar: { type: String, required: true }
    },
    methods: {
        mouseEnter: function () {
            if (aj.img.imageEnlarger)
                aj.img.imageEnlarger.imgUrl = this.avatar;
        },
        mouseLeave: function () {
            if (aj.img.imageEnlarger)
                aj.img.imageEnlarger.imgUrl = null;
        }
    }
});
