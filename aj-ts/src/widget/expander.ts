namespace aj.widget {
    /**
     * 展开闭合器
     */
    export class Expander extends VueComponent {
        name = "aj-expander";

        template = html `
            <div class="aj-expander" :style="'height:' + (expended ? openHeight : closeHeight) + 'px;'">
                <div :class="expended ? 'closeBtn' : 'openBtn'" @click="expended = !expended;"></div>
                <slot></slot>
            </div>
        `;

        expended = false;

        openHeight = { type: Number, "default": 200 };

        closeHeight = { type: Number, "default": 50 };
    }

    new Expander().register();
}