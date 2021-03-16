new Vue({
    el: document.querySelector('.main table'),
    data: {
        arr: []
    },
    mounted() {
        aj.xhr.get('getTimeTrendRpt/', json => {
            this.arr = json.body.data[0].result.items[1];
        });
    }
});

aj.xhr.get('getCommonTrackRpt/', json => {
    let data = json.body.data[0].result;

    for (var i in data) {
        new Vue({
            el: document.body.$('.' + i),
            data: { arr: data[i].items }
        });
    }
});

var now = new Date(), last7days = new Date(), yesterday = new Date();
last7days.setDate(now.getDate() - 8);
yesterday.setDate(now.getDate() - 1);

new Vue({
    el: '.chart',
    data: {
        startDate: last7days.format("yyyy-MM-dd"),
        endDate: yesterday.format("yyyy-MM-dd"),
        values: [{ name: 'Page A', uv: 4000, pv: 2400, amt: 2400 }]
    },
    mounted() {
        this.query();
    },
    methods: {
        query(this: Vue): void {
            let startDate = this.$children[0].date,
                endDate = this.$children[1].date;

            aj.xhr.get('getTrend', json => {
                // 转换格式
                let arr = json.body.data[0].result.items,
                    days = arr[0].reverse(),
                    value = arr[1].reverse(),
                    newArr = [];

                for (var i = 0, j = days.length; i < j; i++)
                    newArr.push({
                        name: days[i],
                        pv: value[i][0],
                        uv: value[i][1]
                    });

                //@ts-ignore
                this.values = newArr;
            }, {
                start_date: startDate,
                end_date: endDate
            });
        }
    }
});

let settings: Vue = new Vue({
    el: '.settings',
    mounted() {
        aj.xhr.form(this.$el.$('form'));
    }
});

function showSetting() {
    settings.$children[0].show();
}