import { xhr_get, xhr_del } from '../../util/xhr';
import list from './list';

/**
 * 通用列表 mixins，适合跟数据服务绑定
 * 必须指定 API 地址
 * 
 */
export default {
    data() {
        return {
            API: '',        // 接口地址
            list: [],       // 列表数据
            page: {         // 分页参数
                total: 0,
                current: 1,
                pageSize: 10,
            }
        }
    },
    methods: {
        onPage(pageNo: number): void {
            let start: number = (pageNo - 1) * this.page.pageSize,
                end: number = start + this.page.pageSize;

            this.getData(start, end);
        },

        /**
         * 加载数据
         * 
         * @param start 
         * @param limit 
         */
        getData(start: number, limit: number): void {
            xhr_get(`${this.API}/list`,
                (j: RepsonseResult) => {
                    this.list = j.result;
                    this.page.total = j.total;
                },
                { start: start, limit: limit }
            );
        },

        /**
         * 删除
         *
         * @param  记录在 list 的索引（不是 id）
         */
        handleDelete(index: number): void {
            xhr_del(`${this.API}?id=${this.list[index].id}`, list.afterDelete(() => this.list.splice(index, 1)).bind(this));
        }
    }
};