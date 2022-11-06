/**
 * find() 调用堆栈
 */
let find_stack: JsonTree[] = [];

/**
 * 在数组中查找某个元素，并可返回调用堆栈
 * 
 * @param arr 
 * @param target 
 * @param onGet 
 * @returns 
 */
function find(arr: JsonTree[], target: any, onGet?: (target: JsonTree, arr: JsonTree[], index: number, find_stack?: JsonTree[]) => void): JsonTree {
    for (let i = 0; i < arr.length; i++) {
        let k: JsonTree = arr[i];

        if (k === target) {
            onGet && onGet(target, arr, i, find_stack);
            find_stack = [];
            return k;
        }

        if (k && k.children) {
            find_stack.push(k);
            let r: JsonTree = find(k.children, target, onGet);
            find_stack.pop();

            if (r)
                return r;
        }
    }
}

/**
 * 组件 uid 的计数器
 */
let widget_total: number = 1;

/**
 * 为每个组件初始化其 uid
 * 
 * @param arr 组件数组
 * @returns 
 */
function makeUid(arr: JsonTree[]): JsonTree[] {
    for (let i = 0; i < arr.length; i++) {
        let item: JsonTree = arr[i];

        if (item.hasOwnProperty('type')) {
            // @ts-ignore
            item.uid = widget_total++;
        }

        if (item.children)
            makeUid(item.children);
    }

    return arr;
}

/**
 * 返回新 uid
 * 
 * @returns 新 uid
 */
function getUid(): number {
    return widget_total++;
}

export default { find, makeUid, getUid };