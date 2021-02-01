/**
 * 起始时间、截止时间的范围选择
 */
Vue.component('aj-form-between-date', {
    template: html`
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
        valid(this: Vue, ev: Event): void {
            let start = (<HTMLInputElement>this.$el.$('input[name=startDate]')).value, end = (<HTMLInputElement>this.$el.$('input[name=endDate]')).value;

            if (!start || !end) {
                aj.alert("输入数据不能为空");
                ev.preventDefault();
                return;
            }

            if (new Date(start) > new Date(end)) {
                aj.alert("起始日期不能晚于结束日期");
                ev.preventDefault();
                return;
            }

            //@ts-ignore
            if (this.isAjax) {
                ev.preventDefault();
                let grid: any = this.$parent.$parent;
                aj.apply(grid.$refs.pager.extraParam, {
                    startDate: start, endDate: end
                });
                grid.reload();
            }
        }
    }
});