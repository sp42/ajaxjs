import WidgetDef from '../meta/widget-definition';
import draggable from "vuedraggable";
import TabCommon from "./tab-common";
import WidgetList from "./widget-list.vue";

export default {
    components: { draggable, WidgetList },
    mixins: [TabCommon],
    props: {
        mainCmp: { required: true },
    },
    data() {
        return {
            widgetListPane: '0',
            widgetDef: WidgetDef,
            widgetAutoCompleteData: getArr()
        };
    },
    methods: {
        /**
         * 必须要有这个方法才能过滤
         * 
         * @param value 
         * @param option 
         * @returns 
         */
        filterMethod(value: string, option: any): boolean {
            return option.toUpperCase().indexOf(value.toUpperCase()) !== -1;
        },

        /**
         * 定位到所在的面板，显示那个结果，并隐藏其他项
         * 
         * @param v 
         * @returns 
         */
        onWidgetSearched(v: string): void {
            let def: WidgetDefinition;
            for (let i in WidgetDef.namesMap) {
                if (v === i)
                    def = WidgetDef.namesMap[i];
            }

            if (!def) {
                alert('找不到对应的组件');
                return;
            }

            let result: FilterResult = null;
            result = find("1", def.type, this.mainCmp.widgetDef.base);
            if (!result)
                result = find("2", def.type, this.mainCmp.widgetDef.form);
            if (!result)
                result = find("3", def.type, this.mainCmp.widgetDef.adv);

            if (!result) {
                alert('找不到元素');
                return;
            }

            this.widgetListPane = result.groupIndex;
            result.list.forEach(item => item.isHide = def.type !== item.type); // 只是该面板下的隐藏，其他面板的因为 accordion，没用隐藏的必要
        },

        /**
         * 清空搜索框后复位搜索
         * 
         * @param collapsePane 
         */
        resetSearch(collapsePane: string): void {
            this.widgetListPane = '0';

            let lambad = (item: WidgetDefinition) => item.isHide = false; // 取消隐藏
            this.mainCmp.widgetDef.base.forEach(lambad);
            this.mainCmp.widgetDef.form.forEach(lambad);
            this.mainCmp.widgetDef.adv.forEach(lambad);
        }
    }

}

/**
 * 转换为 AutoComplete 所需的数组
 * 
 * @returns 
 */
function getArr(): string[] {
    let map: {} = WidgetDef.namesMap;
    let arr: string[] = new Array();

    for (let i in map)
        arr.push(i);

    return arr;
}

/**
 * 过滤结果
 */
type FilterResult = {
    groupIndex: string;
    def: WidgetDefinition;
    list: WidgetDefinition[];
};

/**
 * 根据 type 在 list 找到所在 group 的信息
 * 
 * @param groupIndex  组件分类的索引
 * @param type        组件类型
 * @param list        组件分类列表
 * @returns 过滤结果
 */
function find(groupIndex: string, type: string, list: WidgetDefinition[]): FilterResult {
    let def: WidgetDefinition;

    for (let i = 0; i < list.length; i++) {
        let _def: WidgetDefinition = list[i];
        if (type === _def.type) {
            def = _def;
            break;
        }
    }

    if (!def)
        return null;

    return {
        groupIndex: groupIndex,
        def: def,
        list: list
    };
}
