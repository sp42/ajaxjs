/**
 * 日期选择器
 */
interface Calendar extends Vue {
    date: Date,

    year: number;

    month: number;

    day: number;

    /**
     * 画日历
     */
    render(): void;

    /**
     * 获取指定的日期
     * 
     * @param this 
     * @param dateType 
     * @param month 
     */
    getDate(this: Calendar, dateType: 'preMonth' | 'nextMonth' | 'setMonth' | 'preYear' | 'nextYear', month: number): void;

    /**
     * 获取空白的非上月天数 + 当月天数
     * 
     * @param this 
     */
    getDateArr(this: Calendar): number[];

    /**
     * 判断两个日期是否同一日
     * 
     * @param d1 
     * @param d2 
     */
    isSameDay(d1: Date, d2: Date): boolean;

    /**
     * 点击 今天 时候触发的事件
     */
    onToday: Function;
}

Vue.component('aj-form-calendar', {
    template: `
        <div class="aj-form-calendar">
            <div class="selectYearMonth">
                <a href="###" @click="getDate('preYear')" class="preYear" title="上一年">&lt;</a> 
                <select @change="setMonth" v-model="month">
                    <option value="1">一月</option><option value="2">二月</option><option value="3">三月</option><option value="4">四月</option>
                    <option value="5">五月</option><option value="6">六月</option><option value="7">七月</option><option value="8">八月</option>
                    <option value="9">九月</option><option value="10">十月</option><option value="11">十一月</option><option value="12">十二月</option>
                </select>
                <a href="###" @click="getDate('nextYear')" class="nextYear" title="下一年">&gt;</a>
            </div>
            <div class="showCurrentYearMonth">
                <span class="showYear">{{year}}</span>/<span class="showMonth">{{month}}</span>
            </div>
            <table>
                <thead>
                    <tr><td>日</td><td>一</td><td>二</td><td>三</td><td>四</td><td>五</td><td>六</td></tr>
                </thead>
                <tbody @click="pickDay"></tbody>
            </table>
            <div v-if="showTime" class="showTime">
                时 <select class="hour aj-select"><option v-for="n in 24">{{n}}</option></select>
                分 <select class="minute aj-select"><option v-for="n in 61">{{n - 1}}</option></select>
                <a href="#" @click="pickupTime">选择时间</a>
            </div>
        </div>    
    `,
    data() {
        let date: Date = new Date;
        return {
            date: date,
            year: date.getFullYear(),
            month: date.getMonth() + 1,
            day: 1
        };
    },
    props: { showTime: false },     // 是否显示时间，即“时分秒”
    mounted(): void {
        this.$options.watch.date.call(this);
    },
    watch: {
        date(this: Calendar): void {
            this.year = this.date.getFullYear();
            this.month = this.date.getMonth() + 1;
            this.render();
        }
    },
    methods: {
        render(this: Calendar): void {
            let arr: number[] = this.getDateArr();// 用来保存日期列表
            let frag: DocumentFragment = document.createDocumentFragment();// 插入日期

            while (arr.length) {
                var row: HTMLTableRowElement = document.createElement("tr"); // 每个星期插入一个 tr

                for (var i = 1; i <= 7; i++) { // 每个星期有7天
                    var cell: HTMLTableDataCellElement = document.createElement("td");

                    if (arr.length) {
                        var d: number | undefined = arr.shift();

                        if (d) {
                            cell.innerHTML = d + "";
                            let text: string = this.year + '-' + this.month + '-' + d;
                            cell.className = 'day day_' + text;
                            cell.title = text;  // 保存日期在 title 属性

                            var on: Date = new Date(this.year, this.month - 1, d);

                            // 判断是否今日
                            if (this.isSameDay(on, this.date)) {
                                cell.classList.add('onToday');
                                this.onToday && this.onToday(cell);
                            }
                            // 判断是否选择日期
                            // this.selectDay && this.onSelectDay && this.isSameDay(on, this.selectDay) &&
                            // this.onSelectDay(cell);
                        }
                    }

                    row.appendChild(cell);
                }

                frag.appendChild(row);
            }

            // 先清空内容再插入(ie的table不能用innerHTML)
            // while (el.hasChildNodes())
            // el.removeChild(el.firstChild);

            let tbody = <HTMLElement>this.$el.$("table tbody");
            tbody.innerHTML = '';
            tbody.appendChild(frag);
        },

        /**
         * 获取指定的日期
         * 
         * @param this 
         * @param dateType 
         * @param month 
         */
        getDate(this: Calendar, dateType: 'preMonth' | 'nextMonth' | 'setMonth' | 'preYear' | 'nextYear', month: number): void {
            let nowYear: number = this.date.getFullYear(), nowMonth: number = this.date.getMonth() + 1; // 当前日期

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
         * @param this 
         * @param $event 
         */
        setMonth(this: Calendar, $event: Event): void {
            let el: HTMLSelectElement = <HTMLSelectElement>$event.target;
            this.getDate('setMonth', Number(el.selectedOptions[0].value));
        },

        /**
         * 获取空白的非上月天数 + 当月天数
         * 
         * @param this 
         */
        getDateArr(this: Calendar): number[] {
            let arr: number[] = [];
            // 用 当月第一天 在一周中的日期值 作为 当月 离 第一天的天数
            for (var i = 1, firstDay = new Date(this.year, this.month - 1, 1).getDay(); i <= firstDay; i++)
                arr.push(0);

            // 用 当月最后一天 在一个月中的 日期值 作为 当月的天数
            for (var i = 1, monthDay = new Date(this.year, this.month, 0).getDate(); i <= monthDay; i++)
                arr.push(i);

            return arr;
        },

        /**
         * 获取日期
         * 
         * @param this 
         * @param $event 
         */
        pickDay(this: Calendar, $event: Event): string {
            let el: HTMLElement = <HTMLElement>$event.target, date: string = el.title;
            this.$emit('pick-date', date);

            return date;
        },

        /**
         * 
         * @param this 
         * @param $event 
         */
        pickupTime(this: Calendar, $event: Event): void {
            let time = this.$el.$('.hour').selectedOptions[0].value + ':' + this.$el.$('.minute').selectedOptions[0].value;
            this.$emit('pick-time', time);
        },

        /**
         * 判断两个日期是否同一日
         * 
         * @param d1 
         * @param d2 
         */
        isSameDay(d1: Date, d2: Date): boolean {
            return (d1.getFullYear() == d2.getFullYear() && d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate());
        }
    }
});

/**
 * 
 */
Vue.component('aj-form-between-date', {
    template: `
        <form action="." method="GET" class="dateRange" @submit="valid">
            <aj-form-calendar-input :date-only="true" :position-fixed="true" placeholder="起始时间" field-name="startDate" ></aj-form-calendar-input>
            - <aj-form-calendar-input :date-only="true" :position-fixed="true" placeholder="截至时间" field-name="endDate"></aj-form-calendar-input>
            <button class="aj-btn">按时间筛选</button>
        </form>    
    `,
    props: {
        isAjax: { type: Boolean, default: true }// 是否 AJAX 模式
    },
    methods: {
        valid(this: Vue, e: Event): void {
            var start = this.$el.$('input[name=startDate]').value, end = this.$el.$('input[name=endDate]').value;

            if (!start || !end) {
                aj.alert("输入数据不能为空");
                e.preventDefault();
                return;
            }

            if (new Date(start) > new Date(end)) {
                aj.alert("起始日期不能晚于结束日期");
                e.preventDefault();
                return;
            }

            if (this.isAjax) {
                e.preventDefault();
                var grid = this.$parent.$parent;
                aj.apply(grid.$refs.pager.extraParam, {
                    startDate: start, endDate: end
                });
                grid.reload();
            }
        }
    }
});