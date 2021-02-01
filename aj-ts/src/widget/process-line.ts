
namespace aj.widget {
    /**
     * 进度条
     */
    export class ProcessLine extends VueComponent {
        name = 'aj-process-line';

        template = html`
            <div class="aj-process-line">
                <div class="process-line">
                    <div v-for="(item, index) in items" :class="{current: index == current, done: index < current}">
                        <span>{{index + 1}}</span>
                        <p>{{item}}</p>
                    </div>
                </div>
            </div>
        `;

        props = {
            items: {
                type: Array,
                default() {
                    return ['Step 1', 'Step 2', 'Step 3'];
                }
            }
        };

        items = [];

        current: number = 0;

        /**
         * 
         * @param i 
         */
        go(i: number): void {
            this.current = i;
        }

        /**
         * 
         */
        perv(): void {
            let perv: number = this.current - 1;
            if (perv < 0)
                perv = this.items.length - 1;

            this.go(perv);
        }

        /**
         * 
         */
        next(): void {
            let next: number = this.current + 1;
            if (this.items.length == next)
                next = 0; // 循环

            this.go(next);
        }
    }

    new ProcessLine().register();
}