/**
 * 显示头像
 */
interface Avatar {
    /**
     * 头像图片地址
     */
    avatar: string;
}
Vue.component('aj-avatar', {
    template: `
        <a :href="avatar" target="_blank">
            <img :src="avatar" style="max-width:50px;max-height:60px;vertical-align: middle;" @mouseenter="mouseEnter" @mouseleave="mouseLeave" />
        </a>
    `,
    props: {
        avatar: { type: String, required: true }
    },
    methods: {
        mouseEnter(this: Avatar): void {
            if (aj.img.imageEnlarger)
                aj.img.imageEnlarger.imgUrl = this.avatar;
        },
        mouseLeave(): void {
            if (aj.img.imageEnlarger)
                aj.img.imageEnlarger.imgUrl = null;
        }
    }
});