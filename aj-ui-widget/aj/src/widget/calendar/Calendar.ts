/**
 * 日期选择器
 */
export default {
    props: {
        showTime: Boolean // 是否显示时间，即“时分秒”
    },
    data() {
        let date: Date = new Date;
        return {
            date: date,
            year: date.getFullYear(),
            month: date.getMonth() + 1,
            day: 1
        };
    },
    mounted(): void {
        this.$options.watch.date.call(this);
    },
    watch: {
        date(): void {
            this.year = this.date.getFullYear();
            this.month = this.date.getMonth() + 1;
            this.render();
        }
    },
    methods: {
        /**
         * 画日历
         */
        render(): void {
            let arr: number[] = this.getDateArr(),// 用来保存日期列表
                frag: DocumentFragment = document.createDocumentFragment();// 插入日期

            while (arr.length) {
                let row: HTMLTableRowElement = document.createElement("tr"); // 每个星期插入一个 tr

                for (let i = 1; i <= 7; i++) { // 每个星期有7天
                    let cell: HTMLTableCellElement = document.createElement("td");

                    if (arr.length) {
                        let d: number = arr.shift();

                        if (d) {
                            let text: string = this.year + '-' + this.month + '-' + d;
                            cell.title = text;  // 保存日期在 title 属性
                            cell.className = 'day day_' + text;
                            cell.innerHTML = d + "";

                            let on: Date = new Date(this.year, this.month - 1, d);

                            // 判断是否今日
                            if (isSameDay(on, this.date)) {
                                cell.classList.add('onToday');
                                // this.onToday && this.onToday(cell);// 点击 今天 时候触发的事件
                            }
                            // 判断是否选择日期
                            // this.selectDay && this.onSelectDay && this.isSameDay(on, this.selectDay) && this.onSelectDay(cell);
                        }
                    }

                    row.appendChild(cell);
                }

                frag.appendChild(row);
            }

            // 先清空内容再插入
            let tbody = <HTMLElement>this.$el.querySelector("table tbody");
            tbody.innerHTML = '';
            tbody.appendChild(frag);
        },

        /**
         * 获取指定的日期
         * 
         * @param dateType 
         * @param month 
         */
        getDate(dateType: 'preMonth' | 'nextMonth' | 'setMonth' | 'preYear' | 'nextYear', month: number): void {
            let nowYear: number = this.date.getFullYear(),
                nowMonth: number = this.date.getMonth() + 1; // 当前日期

            switch (dateType) {
                case 'preMonth':// 上一月
                    this.date = new Date(nowYear, nowMonth - 2, 1);
                    break;
                case 'nextMonth':// 下一月
                    this.date = new Date(nowYear, nowMonth, 1);
                    break;
                case 'setMonth':// 指定月份
                    this.date = new Date(nowYear, month - 1, 1);
                    break;
                case 'preYear':// 上一年
                    this.date = new Date(nowYear - 1, nowMonth - 1, 1);
                    break;
                case 'nextYear':// 下一年
                    this.date = new Date(nowYear + 1, nowMonth - 1, 1);
                    break;
            }
        },

        /**
         * 
         * 
         * @param querySelectoreven
         */
        setMonth(ev: Event): void {
            let el: HTMLSelectElement = <HTMLSelectElement>ev.target;
            this.getDate('setMonth', Number(el.selectedOptions[0].value));
        },

        /**
         * 获取空白的非上月天数 + 当月天数
         * 
         * @param this 
         */
        getDateArr(): number[] {
            let arr: number[] = [];

            // 算出这个月1号距离前面的星期天有多少天
            for (let i = 1, firstDay: number = new Date(this.year, this.month - 1, 1).getDay(); i <= firstDay; i++)
                arr.push(0);

            // 这个月有多少天。用上个月然后设置日子参数为 0，就可以得到本月有多天
            for (let i = 1, monthDay: number = new Date(this.year, this.month, 0).getDate(); i <= monthDay; i++)
                arr.push(i);

            return arr;
        },

        /**
         * 获取日期
         * 
         * @param event 
         */
        pickDay(ev: Event): string {
            let el: HTMLElement = <HTMLElement>ev.target,
                date: string = el.title;
            this.$emit('pickdate', date);

            return date;
        },

        /**
         * 
         * @param event 
         */
        pickupTime(event: Event): void {
            let hour: HTMLSelectElement = <HTMLSelectElement>this.$el.querySelector('.hour'),
                minute: HTMLSelectElement = <HTMLSelectElement>this.$el.querySelector('.minute'),
                time = hour.selectedOptions[0].value + ':' + minute.selectedOptions[0].value;

            this.$emit('pick-time', time);
        }
    }
};

/**
  * 判断两个日期是否同一日
  * 
  * @param d1 
  * @param d2 
  */
function isSameDay(d1: Date, d2: Date): boolean {
    return (d1.getFullYear() == d2.getFullYear() && d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate());
}
