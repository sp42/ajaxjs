/**
 * 调整正文字体大小
 */
Vue.component('aj-adjust-font-size', {
    template: `
        <div class="aj-adjust-font-size" @click="onClk">
            <span>字体大小</span>
            <ul>
                <li><label><input type="radio" name="fontSize" /> 小</label></li>
                <li><label><input type="radio" name="fontSize" /> 中</label></li>
                <li><label><input type="radio" name="fontSize" /> 大</label></li>
            </ul>
        </div>
    `,
    props: {
        articleTarget: { type: String, default: 'article p' }// 正文所在的位置，通过 CSS Selector 定位
    },
    methods: {
        onClk(this: Vue, e: Event): void {
            let el: Element = <Element>e.target;
            let setFontSize = (fontSize: string): void => {
                document.body.$(this.$props.articleTarget, (p: HTMLParagraphElement) => p.style.fontSize = fontSize);
            }

            if (el.tagName != 'LABEL')
                el = <Element>el.up('label');

            if (el.innerHTML.indexOf('大') != -1)
                setFontSize('12pt');
            else if (el.innerHTML.indexOf('中') != -1)
                setFontSize('10.5pt');
            else if (el.innerHTML.indexOf('小') != -1)
                setFontSize('9pt');
        }
    }
});