namespace aj.tree {
    export interface SimpleJsonMap<T> {
        [key: string]: SimpleJsonMap<T> | T;
    }

    /**
     * JSON 中 value 值可能出现的类型
     */
    export type JsonValue = null | boolean | number | string;

    /**
     * JSON 里面 map，并且可以不断嵌套下级的，故有 JsonMap 类型。
     */
    export interface JsonMap<T> {
        [key: string]: JsonValue | JsonMap<T> | T;
    }

    /**
     * 寻找节点
     * 
     * @param obj 
     * @param queue 
     * @returns 
     */
    export function findNodesHolder<T>(obj: JsonMap<T>, queue: string[]): JsonValue | JsonMap<T> | T {
        if (!queue.shift)
            return null;

        let first: string | undefined = queue.shift();

        for (let i in obj) {
            if (i === first) {
                let target: JsonValue | JsonMap<T> | T = obj[i];

                if (queue.length == 0) // 找到了
                    return target;
                else
                    return findNodesHolder(<JsonMap<T>>obj[i], queue);
            }
        }

        return null;
    }

    //------------------------------------------------------------------------------

    interface Map {
        [key: string]: any;
    }

    /**
     * 树节点
     * 
     *  参考结构
     * `
        var map = {
            a : 1,
            b : 2,
            c : {
                c1: 1,
                c2: 2,
                children : [ {
                    d : 3
                } ]
            }
        };`
    */
    export interface TreeNode extends Map {
        /**
         * 实体 id
         */
        id: number;

        /**
         * 实体名称
         */
        name: string;

        /**
         * 父节点的 id
         */
        pid: number;

        /**
         * 所在的第几层
         */
        level: number;

        /**
         * 子节点
         */
        children?: TreeNode | TreeNode[];
    }

    /**
     * 扁平的节点列表
     * 原始未处理的树节点
     */
    export type FlatTreeNodeList = TreeNode[];

    //------------------------------------------------------------------------------


    /**
     * 根据传入 id 在一个数组中查找父亲节点
     * 
     * @param map 
     * @param id 
     */
    export function findParentInArray(arr: TreeNode[], id: number): TreeNode | null {
        for (let i = 0; i < arr.length; i++) {
            let n: TreeNode = arr[i];
            if (id == n.id)
                return n;

            let c: TreeNode[] = <TreeNode[]>n.children;

            if (c) {
                let result: TreeNode | null = findParentInArray(c, id);
                if (result != null)
                    return result;
            }
        }

        return null;
    }

    /**
     * 生成树，将扁平化的数组结构 还原为树状的 Array结构
     * 父id 必须在子 id 之前，不然下面 findParent() 找不到后面的父节点，故先排序
     * 
     * @param jsonArray 
     */
    export function toTreeArray(jsonArray: TreeNode[]): TreeNode[] {
        let arr: TreeNode[] = [];

        for (let i = 0, j = jsonArray.length; i < j; i++) {
            let n: TreeNode = jsonArray[i];

            if (n.pid === -1)
                arr.push(n);
            else {
                let parentNode: TreeNode | null = findParentInArray(arr, n.pid);

                if (parentNode) {
                    if (!parentNode.children)
                        parentNode.children = [];

                    (<TreeNode[]>parentNode.children).push(n);
                } else
                    console.log('parent not found!');
            }
        }

        return arr;
    }

    //------------------------------------------------------------------------------

    /**
     * 根据传入 id 查找父亲节点
     * 
     * @param map 
     * @param id 
     */
    export function findParentInMap(map: TreeNode, id: string): TreeNode | null {
        for (let i in map) {
            if (i == id)
                return map[i];

            let c: TreeNode = <TreeNode>map[i].children;

            if (c) {
                for (let j in c) {
                    let result: TreeNode | null = findParentInMap(c[j], id);

                    if (result != null)
                        return result;
                }
            }
        }

        return null;
    }

    /**
     * 生成树，将扁平化的数组结构 还原为树状的 Map 结构
     * 父id 必须在子id之前，不然下面 findParent() 找不到后面的父节点，故前提条件是，这个数组必须先排序
     * 
     * @param jsonArray 
     */
    export function toTreeMap(jsonArray: FlatTreeNodeList): TreeNode | null {
        if (!jsonArray)
            return null;

        let m: TreeNode = <TreeNode>{};

        for (let i = 0, j = jsonArray.length; i < j; i++) {
            let n: TreeNode = jsonArray[i];

            if (n.pid === -1)
                m[n.id] = n;
            else {
                let parentNode: TreeNode | null = findParentInMap(m, n.pid + "");

                if (parentNode) {
                    if (!parentNode.children)
                        parentNode.children = <TreeNode>{};

                    (<TreeNode>parentNode.children)[n.id] = n;
                } else
                    console.log('parent not found!');
            }
        }

        return m;
    }

    let stack: TreeNode[] = [];

    /**
     * 遍历各个元素，输出
     * 
     * @param map 
     * @param cb 
     */
    export function output(map: TreeNode, cb: (node: TreeNode, id: string) => void): void {
        stack.push(map);

        for (let i in map) {
            map[i].level = stack.length;// 层数，也表示缩进多少个字符
            cb && cb(map[i], i);

            let c: TreeNode = <TreeNode>map[i].children;
            c && output(c, cb);
        }

        stack.pop();
    }
}