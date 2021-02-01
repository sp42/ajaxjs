/**
 * 调整正文字体大小
 */
namespace aj.widget.page {
    export class AdjustFontSize extends VueComponent {
        name = 'aj-adjust-font-size';

        template = html`
            <div class="aj-adjust-font-size">
                <span>字体大小</span>
                <ul @click="onClk">
                    <li><label><input type="radio" name="fontSize" /> 小</label></li>
                    <li><label><input type="radio" name="fontSize" /> 中</label></li>
                    <li><label><input type="radio" name="fontSize" /> 大</label></li>
                </ul>
            </div>
        `;

        articleTarget = { type: String, default: 'article p' };// 正文所在的位置，通过 CSS Selector 定位

        onClk(ev: Event): void {
            let el: Element = <Element>ev.target;
            let setFontSize = (fontSize: string): void => {
                document.body.$(this.$props.articleTarget, (p: HTMLParagraphElement) => p.style.fontSize = fontSize);
            }

            if (el.tagName == 'LABEL' || el.tagName == 'input') {
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
    }

    new AdjustFontSize().register();
}