namespace aj.img {
    /**
     * 悬浮显示大图
     */
    interface ImageEnlarger extends Vue {
        /**
         * 图片地址
         */
        imgUrl: string | null;
    }

    export var imageEnlarger: ImageEnlarger; // 单例

    export function initImageEnlarger(): ImageEnlarger {
        imageEnlarger = <ImageEnlarger>new Vue({
            el: document.body.appendChild(document.createElement('div')),
            template: '<div class="aj-image-large-view"><div><img :src="imgUrl" /></div></div>',
            data: {
                imgUrl: null
            },
            mounted(this: ImageEnlarger): void {// 不能用 onmousemove 直接绑定事件
                // @ts-ignore
                document.addEventListener('mousemove', aj.throttle((e: MouseEvent) => {
                    if (this.imgUrl) {
                        let w: number = 0, imgWidth: number = (<HTMLElement>this.$el.$('img')).clientWidth;
                        if (imgWidth > e.pageX)
                            w = imgWidth;

                        let el: HTMLElement = <HTMLElement>this.$el.$('div');
                        el.style.top = (e.pageY + 20) + 'px';
                        el.style.left = (e.pageX - el.clientWidth + w) + 'px';
                    }
                }, 50, 5000), false);
            }
        });

        return imageEnlarger;
    }
}