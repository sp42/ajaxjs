export default {
    methods: {
        onTabClk($event: Event): void {
            let el: HTMLElement = <HTMLElement>$event.target;

            if (el.tagName == 'I')
                el = <HTMLElement>el.parentNode;

            if (el.tagName != 'LI') {
                // alert('点击错误，请重试');
                return;
            }

            let ul: HTMLElement = <HTMLElement>el.parentNode;
            let old = ul.querySelector('li.active');
            let style = this.$el.querySelector('.content').style;

            if (old != el) {
                if (style.display === 'none')
                    style.display = 'block';

                old.classList.remove('active');
                el.classList.add('active');

                let index: number = Array.prototype.indexOf.call(ul.children, el);

                let toShow = this.$el.querySelector('.content').children[index];
                let oldTab = this.$el.querySelector('.content section.show');
                oldTab.classList.remove('show');
                toShow.classList.add('show');
            } else
                style.display = (style.display === 'none') ? 'block' : 'none';

        }
    }
}