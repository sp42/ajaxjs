import WidgetDef from '../meta/widget-definition';

export default {
    methods: {
        activeSection(e: MouseEvent, item: RenderedMeta): void {
            if ('isWysiwyg' in this && !this.isWysiwyg)
                return;

            let el = <HTMLElement>e.currentTarget;
            showBorder(el, ".on-selected-border", item, 1);
            console.log('选中组件', item);

            this.ActiveWdiget = item;
        },
        showHoverBorder(e: MouseEvent, item: RenderedMeta): void {
            if ('isWysiwyg' in this && !this.isWysiwyg)
                return;

            // if (item == this.ActiveWdiget)
            //     return;

            let el = <HTMLElement>e.target;
            showBorder(el, ".mouse-hover", item);
        },

        hideHoverBorder(): void {
            if ('isWysiwyg' in this && !this.isWysiwyg)
                return;

            // @ts-ignore
            document.querySelector(".mouse-hover").style.display = "none";
        },

        hideActivedBorder(): void {
            if ('isWysiwyg' in this && !this.isWysiwyg)
                return;

            // @ts-ignore
            document.querySelector(".on-selected-border").style.display = "none";
        },
    },
};

/**
 * 隐藏的组件，有些子组件是没在元数据里面的
 */
const HIDDEN_WIDGET = {
    FormItem: '表单项容器',
    Col: '栅格列容器',
};

function showBorder(el: HTMLElement, cssSelector: string, item: RenderedMeta, offset: number = 0): void {
    let box: DOMRect = el.getBoundingClientRect();

    let name: string = WidgetDef.typeChineseMap[item.type];
    if (!name)
        name = HIDDEN_WIDGET[item.type];

    let mouseHover = <HTMLElement>document.querySelector(cssSelector);
    mouseHover.style.top = box.y - offset + "px";
    mouseHover.style.left = box.x - offset + "px";
    mouseHover.style.width = box.width + offset * 2 + "px";
    mouseHover.style.height = box.height + offset * 2 + "px";

    let h: string = `${name}#${item.uid}`;
    if (item.key)
        h += `[${item.key}]`;
    mouseHover.querySelector(".info div").innerHTML = h;
    mouseHover.style.display = "block";
}