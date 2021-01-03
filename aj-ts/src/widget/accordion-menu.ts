
/**
 * 折叠菜单
 */
; (() => {
    interface AccordionMenu extends Vue {
        children: HTMLCollection;
    }

    Vue.component('aj-accordion-menu', {
        template: '<ul class="aj-accordion-menu" @click="onClk"><slot></slot></ul>',
        methods: {
            onClk(this: AccordionMenu, $event: Event) {
                this.children = this.$el.children;
                highlightSubItem($event);

                var _btn: Element = <Element>$event.target;

                if (_btn && _btn.tagName == 'H3' && (<Element>_btn.parentNode).tagName == 'LI') {
                    _btn = <Element>$event.target;
                    _btn = <Element>_btn.parentNode;

                    for (var btn, i = 0, j = this.children.length; i < j; i++) {
                        btn = this.children[i];
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
    });

    /**
     * 内部子菜单的高亮
     * 
     * @param $event 
     */
    function highlightSubItem($event: Event) {
        var li: Element, el: Element = <Element>$event.target;

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
})();