import Operate from './rbac-operate.vue';
import TreeTable from '../../../../components/tree-table/index.vue';
import { UserRole_List } from './list-cfg';
import TreeSelector from './../../../tree-table/tree-selector.vue';

const TEST_resources = [{ "name": "设备模块", "id": 3, "group": "充电桩系统" }, { "name": "运营商模块", "id": 4, "group": "充电桩系统" }, { "name": "厂商模块", "id": 5, "group": "充电桩系统" }, { "name": "图文系统", "id": 1, "group": "基本业务" }, { "name": "留言系统", "id": 2, "group": "基本业务" }];
export default {
    components: { Operate, TreeTable, TreeSelector },
    data() {
        try {
            TEST_resources.sort((a: any, b: any) => a.id < b.id ? -1 : 1);
        } catch (e) {

        }

        console.log(TEST_resources)
        const api = window['config'].dsApiRoot + '/api/cms/user_role';

        return {
            roleListApi: {
                list: api + '/list',
                create: api,
                update: api,
                delete: api,
            },
            resources: Object.freeze(TEST_resources),
            UserRole_List,
            roleInherit: {
                isShow: false,
                sonRoleName: 'ddd'
            }
        };
    },
    mounted() {
        megeCell('.group');
    }
}

// 合并单元格
function megeCell(columnClass: string): void {
    // 收集所有的列
    let arr = document.querySelectorAll(columnClass);

    var map = {};
    for (var i = 0, j = arr.length; i < j; i++) {
        var td: HTMLElement = <HTMLElement>arr[i];
        var id: string = td.innerHTML;

        if (undefined === map[id]) map[id] = 1; // 统计跨行的有多少，用一个 map 装着
        else map[id] = ++map[id];
    }

    var stack = [];
    for (var i = 0, j = arr.length; i < j; i++) {
        var td: HTMLElement = <HTMLElement>arr[i];
        var id: string = td.innerHTML;
        var tds: number = map[id];

        if (tds > 1 && stack.length === 0) {// 标记
            for (var q = i; q < i + tds; q++) {// 连续 tds 个都是要合并单元格的
                if (q == i) {
                    arr[q].classList.add('firtstOne');
                    // @ts-ignore
                    arr[q].dataset.rowSpan = tds;
                } else {
                    arr[q].classList.add('die');
                }

                stack.push(arr[q]); // 入栈
            }
        }

        if (stack.length && td === stack[0]) {
            stack.shift();// 退栈  

            if (td.className.indexOf('firtstOne') != -1) {
            } else {
                td.classList.add('die'); // 要删除的元素
            }
        }
    }

    [].forEach.call(document.querySelectorAll('.firtstOne'), i => {
        i.setAttribute('rowspan', i.dataset.rowSpan);
    });
    [].forEach.call(document.querySelectorAll('.die'), i => {
        i.parentNode.removeChild(i);
    });
}
