
namespace aj.list.tree {
    /**
     * 排序
     * 父id 必须在子 id 之前，不然下面 findParent() 找不到后面的父节点，故先排序
     * 
     * @param jsonArray 
     */
    export function makeTree(jsonArray: TreeNode[]): TreeNode[] {
        let arr: TreeNode[] = [];

        for (var i = 0, j = jsonArray.length; i < j; i++) {
            let n: TreeNode = jsonArray[i];

            if (n.pid === -1)
                arr.push(n);
            else {
                let parentNode = findParentInArray(arr, n.pid);

                if (parentNode) {
                    if (!parentNode.children)
                        parentNode.children = [];

                    parentNode.children.push(n);
                } else
                    console.log('parent not found!');
            }
        }

        return arr;
    }

    /**
     * 根据传入 id 在一个数组中查找父亲节点
     * 
     * @param jsonArray 
     * @param id 
     */
    export function findParentInArray(jsonArray: TreeNode[], id: number): TreeNode | null {
        for (let i = 0, j = jsonArray.length; i < j; i++) {
            let map: TreeNode = jsonArray[i];

            if (map.id == id)
                return map;

            if (map.children) {
                let result = findParentInArray(map.children, id);

                if (result != null)
                    return result;
            }
        }

        return null;
    }

    /**
     * 根据传入 id 查找父亲节点
     * 
     * @param map 
     * @param id 
     */
    export function findParentInMap(map: TreeMap, id: string): TreeNode | null {
        for (let i in map) {
            if (i == id)
                return map[i];

            let c = map[i].children;

            if (c) {
                for (let q = 0, p = c.length; q < p; q++) {
                    let result = findParentInMap(c[q], id);

                    if (result != null)
                        return result;
                }
            }
        }

        return null;
    }

    var stack: TreeMap[] = [];

    /**
     * 遍历各个元素，输出
     * 
     * @param map 
     * @param cb 
     */
    export function output(map: TreeMap, cb: (node: TreeNode, id: string) => void): void {
        stack.push(map);

        for (var i in map) {
            map[i].level = stack.length;// 层数，也表示缩进多少个字符
            cb(map[i], i);

            let c = map[i].children;

            if (c) {
                for (var q = 0, p = c.length; q < p; q++)
                    output(c[q], cb);
            }
        }

        stack.pop();
    }

    /**
     * 生成树，将扁平化的数组结构 还原为树状的结构
     * 父id 必须在子id之前，不然下面 findParent() 找不到后面的父节点，故前提条件是，这个数组必须先排序
     * 
     * @param jsonArray 
     */
    export function toTree(jsonArray: TreeNode[]): TreeMap {
        if (!jsonArray)
            return {};

        let m: TreeMap = {};

        for (var i = 0, j = jsonArray.length; i < j; i++) {
            let n: TreeNode = jsonArray[i], parentNode = findParentInMap(m, n.pid + "");

            if (parentNode == null) {	// 没有父节点，那就表示这是根节点，保存之
                m[n.id] = n;			// id 是key，value 新建一对象
            } else { 					// 有父亲节点，作为孩子节点保存
                let obj: TreeMap = {};
                obj[n.id] = n;

                if (!parentNode.children)
                    parentNode.children = [];

                parentNode.children.push(obj);
            }
        }

        return m;
    }

    /**
     * 渲染 Option 标签的 DOM
     * 
     * @param jsonArray 
     * @param select 
     * @param selectedId 
     * @param cfg 
     */
    export function rendererOption(jsonArray: TreeNode[], select: HTMLSelectElement, selectedId?: string, cfg?: TreeOption): void {
        if (cfg && cfg.makeAllOption) {
            let option: HTMLOptionElement = document.createElement('option');
            option.value = option.innerHTML = "全部分类";
            select.appendChild(option);
        }

        // 生成 option
        let temp: DocumentFragment = document.createDocumentFragment();

        output(toTree(jsonArray), (node: TreeNode, nodeId: string) => {
            let option: HTMLOptionElement = document.createElement('option'); // 节点
            option.value = nodeId;

            if (selectedId && selectedId == nodeId) // 选中的
                option.selected = true;

            option.dataset['pid'] = node.pid + "";
            //option.style= "padding-left:" + (node.level - 1) +"rem;";
            option.innerHTML = new Array(node.level * 5).join('&nbsp;') + (node.level == 1 ? '' : '└─') + node.name;
            temp.appendChild(option);
        });

        select.appendChild(temp);
    }

}