
namespace aj.widget.img {
    /**
     * 显示头像
     */
    export class Avatar extends VueComponent {
        name = 'aj-avatar';

        template = html`
            <a :href="avatar" target="_blank">
                <img :src="avatar" style="max-width:50px;max-height:60px;vertical-align: middle;" 
                    @mouseenter="mouseEnter"
                    @mouseleave="mouseLeave" />
            </a>
        `;

        props = {
            avatar: { type: String, required: true }
        };

        /**
         * 头像图片地址
         */
        avatar: string = "";

        mouseEnter(this: Avatar): void {
            if (imageEnlarger)
                imageEnlarger.imgUrl = this.avatar;
        }
        mouseLeave(): void {
            if (imageEnlarger)
                imageEnlarger.imgUrl = null;
        }
    }

    new Avatar().register();
}
