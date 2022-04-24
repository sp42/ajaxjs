import { xhr_del } from '../../util/xhr';
import { dateFormat } from '../../util/utils';

export default {
    afterDelete(cb?: Function): Function {
        return function (j: RepsonseResult) {
            if (j.isOk) {
                cb && cb(j);
                this.$Message.success('删除成功');
            }
            else
                this.$Message.info('删除失败。' + j.msg);
        }
    },

    delInfo(index: number) {
        xhr_del(`${this.API}/${this.list.data[index].id}`, (j: RepsonseResult) => {
            if (j.isOk) {
                this.list.data.splice(index, 1);
                this.list.total--;
                this.$Message.success('删除成功');
            } else
                this.$Message.info('删除失败。' + j.msg);
        });
    },

    /**
     * id 列
     */
    id: { title: '#', width: 60, key: 'id', align: 'center' },

    getListResult(j: RepsonseResult): void {
        if (j.result) {
            this.list.data = j.result;
            // @ts-ignore
            this.list.total = j.total;
        }
        else this.$Message.warning(j.msg || '获取数据失败');
    },

    /**
     * 创建日期
     */
    createDate: {
        title: '创建日期',/*  key: 'createDate', */ width: 220, align: 'center', render(h: Function, params: any) {
            return h('div', dateFormat.call(new Date(params.row.createDate), 'yyyy-MM-dd hh:mm'));
        }
    },

    /**
     * 分类标签
     */
    tags: { title: '分类标签', minWidth: 100, key: 'tagsNames', align: 'center', ellipsis: true },

    status: {
        title: '状态', width: 70,
        render(h: Function, params: any) {
            let str = '';

            switch (params.row.stat) {
                case null:
                case 1:
                    str = '启用';
                    break;
                case 0:
                    str = '禁用';
                    break;
                case 2:
                    str = '已删除';
                default:
                    str = "启用";
            }

            return h('div', str);
        }
    }
};