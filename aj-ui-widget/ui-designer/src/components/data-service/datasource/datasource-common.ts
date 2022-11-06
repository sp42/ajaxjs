import { xhr_get } from '../../../util/xhr';
import { getCookie } from '../../../util/cookies';

export default {
    data() {
        return {
            datasource: {
                list: [],   // 数据源
                id: 0,      // 选择的数据源 id
            },
        };
    },
    methods: {
        /**
         * 获取数据源
         */
        getDatasource(): void {
            let p: any = { start: 0, limit: 999 };

            let dp_appId: string = window.sessionStorage.getItem('APP_ID');
            if (dp_appId)
                p.appId = dp_appId;

            xhr_get(`${this.apiRoot}/admin/datasource`, (j: JsonResponse) => {
                this.datasource.list = j.data;
                this.datasource.id = this.datasource.list[0].id; // 默认显示第一个数据源的
                this.getData();
            }, p);
        },
    },
    watch: {
        'datasource.id'() {
            this.getData();
        }
    }
};