
/**
 * 折叠菜单
 */
namespace aj.widget {
    export class AccordionMenu extends VueComponent {
        name = "aj-accordion-menu";

        template = '<ul class="aj-accordion-menu" @click="onClk"><slot></slot></ul>';

        onClk(ev: Event) {
            let children: HTMLCollection = this.$el.children;
            highlightSubItem(ev);
            let _btn: Element = <Element>ev.target;

            if (_btn && _btn.tagName == 'H3' && (<Element>_btn.parentNode).tagName == 'LI') {
                _btn = <Element>_btn.parentNode;

                for (let btn, i = 0, j = children.length; i < j; i++) {
                    btn = children[i];
                    let ul = btn.querySelector('ul');

                    if (btn == _btn) {
                        if (btn.className.indexOf('pressed') != -1) {
                            btn.classList.remove('pressed'); // 再次点击，隐藏！
                            if (ul)
                                ul.style.height = '0px';
                        } else {
                            if (ul)
                                ul.style.height = ul.scrollHeight + 'px';
                            btn.classList.add('pressed');
                        }
                    } else {
                        btn.classList.remove('pressed');
                        if (ul)
                            ul.style.height = '0px';
                    }
                }
            } else
                return;
        }
    }

    new AccordionMenu().register();
    
    /**
     * 内部子菜单的高亮
     * 
     * @param ev 
     */
    function highlightSubItem(ev: Event) {
        var li: Element, el: Element = <Element>ev.target;

        if (el.tagName == 'A' && el.getAttribute('target')) {
            li = <Element>el.parentNode;
            (<Element>li.parentNode)?.$('li', (_el: Element) => {
                if (_el == li)
                    _el.classList.add('selected');
                else
                    _el.classList.remove('selected');
            });
        }
    }
}
