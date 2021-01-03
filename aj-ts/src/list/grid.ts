/**
 * 
 */
interface Grid {
    sortKey: string;
    sortOrders: { [key: string]: number };
    filterKey: string;
    data: [];
}

Vue.component('aj-grid', {
    template: `
        <div>
            <form action="?" method="GET" style="float:right;">
                <input type="hidden" name="searchField" value="content" />
                <input type="text" name="searchValue" placeholder="请输入搜索之关键字" class="aj-input" />
                <button style="margin-top: 0;" class="aj-btn">搜索</button>
            </form>
            <table class="aj-grid ajaxjs-borderTable">
                <thead>
                    <tr>
                        <th v-for="key in columns" @click="sortBy(key)" :class="{ active: sortKey == key }">
                            {{ key | capitalize }}
                            <span class="arrow" :class="sortOrders[key] > 0 ? 'asc' : 'dsc'"></span>
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="entry in filteredData">
                        <td v-for="key in columns" v-html="entry[key]"></td>
                    </tr>
                </tbody>
            </table>
        </div>
    `,
    props: {
        data: Array,
        columns: Array,
        filterKey: String
    },
    data() {
        let sortOrders: { [key: string]: number } = {};
        this.columns.forEach((key: string) => sortOrders[key] = 1);

        return {
            sortKey: '',
            sortOrders: sortOrders
        };
    },
    computed: {
        filteredData(this: Grid) {
            var sortKey: string = this.sortKey;
            var filterKey: string = this.filterKey && this.filterKey.toLowerCase();
            var order = this.sortOrders[sortKey] || 1;
            var data = this.data;

            if (filterKey) {
                data = data.filter((row) => {
                    return Object.keys(row).some(key => String(row[key]).toLowerCase().indexOf(filterKey) > -1);
                });
            }

            if (sortKey) {
                data = data.slice().sort((a, b) => {
                    a = a[sortKey];
                    b = b[sortKey];

                    return (a === b ? 0 : a > b ? 1 : -1) * order;
                });
            }

            return data;
        }
    },
    filters: {
        capitalize(str: string): string {
            return str.charAt(0).toUpperCase() + str.slice(1);
        }
    },
    methods: {
        sortBy(this: Grid, key: string): void {
            this.sortKey = key;
            this.sortOrders[key] = this.sortOrders[key] * -1;
        }
    }
});