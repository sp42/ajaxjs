/**
 * 进度条
 */
interface ProcessLine {
    items: string[];
    current: number;
    go(i: number): void;
}

Vue.component('aj-process-line', {
    template: `
        <div class="aj-process-line">
            <div class="process-line">
                <div v-for="(item, index) in items" :class="{current: index == current, done: index < current}">
                    <span>{{index + 1}}</span><p>{{item}}</p>
                </div>
            </div>
        </div>    
    `,
    props: {
        items: {
            type: Array,
            default() {
                return ['Step 1', 'Step 2', 'Step 3'];
            }
        }
    },
    data() {
        return {
            current: 0
        };
    },
    methods: {
        /**
         * 
         * @param this 
         * @param i 
         */
        go(this: ProcessLine, i: number): void {
            this.current = i;
        },

        /**
         * 
         * @param this 
         */
        perv(this: ProcessLine): void {
            let perv: number = this.current - 1;
            if (perv < 0)
                perv = this.items.length - 1;

            this.go(perv);
        },

        /**
         * 
         * @param this `
         */
        next(this: ProcessLine): void {
            let next: number = this.current + 1;
            if (this.items.length == next)
                next = 0; // 循环

            this.go(next);
        }
    }
});