namespace aj.widget.img {
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
                document.addEventListener('mousemove', throttle((e: MouseEvent) => {
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

    Vue.component('aj-img-thumb', {
        template:
            `<a class="aj-img-thumb" :href="getImgUrl" v-if="imgUrl" target="_blank">
                <img :src="getImgUrl"
                    :onmouseenter="'aj.widget.img.imageEnlarger.imgUrl = \\'' + getImgUrl + '\\';'" 
                    onmouseleave="aj.widget.img.imageEnlarger.imgUrl = null;" />
            </a>`,
        props: {
            imgUrl: String
        },
        computed: {
            getImgUrl(this: ImageEnlarger): string {
                if (!this.imgUrl)
                    return "";

                if (this.imgUrl.indexOf('http') != -1) // 图片地址已经是完整的 http 地址，直接返回
                    return this.imgUrl;
                // uploadFile.imgPerfix

                if (!this.ajResources.imgPerfix)
                    throw "未提供图片前缀地址";

                return this.ajResources.imgPerfix + this.imgUrl;
            }
        }
    });
}