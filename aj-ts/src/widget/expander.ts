Vue.component("aj-expander", {
    template: `
        <div class="aj-expander" :style="'height:' + (expended ? openHeight : closeHeight) + 'px;'">
            <div :class="expended ? 'closeBtn' : 'openBtn'" @click="expended = !expended;"></div>
            <slot></slot>
        </div>
    `,
    data() {
        return { expended: false };
    },
    props: {
        openHeight: { type: Number, "default": 200 },
        closeHeight: { type: Number, "default": 50 }
    }
});
