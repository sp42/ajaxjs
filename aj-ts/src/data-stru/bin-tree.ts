
/**
 * 节点
 */
class TreeNode {
    /**
     * 节点索引
     */
    key: number;

    /**
     * 实际的内容数据
     */
    value: any;

    /**
     * 左子节点
     */
    left?: TreeNode;

    /**
     * 右子节点
     */
    right?: TreeNode;

    /**
     * 创建一个节点
     * 
     * @param key   节点索引
     * @param value 实际的内容数据
     */
    constructor(key: number, value?: any) {
        this.key = key;

        if (value)
            this.value = value;
    }
}

/**
 * 树
 */
class BinaryTree {
    /**
     * 根节点
     */
    public root: TreeNode | undefined;

    /**
     * 插入节点。
     * 如果根节点为空，则插入到根节点。如果不是，插入到根节点下面
     * 
     * @param newNode 
     * @returns 
     */
    public insert(newNode: TreeNode): void {
        if (!this.root) {
            this.root = newNode;
            return;
        }

        this.insertNode(this.root, newNode);
    }

    /**
     * 实际的插入方法。这是一个递归的方法。
     * 
     * @param node      被插入的父节点
     * @param newNode   插入的子节点
     */
    private insertNode(node: TreeNode, newNode: TreeNode): void {
        if (node.key > newNode.key) { // 比父节点小，插入在左边
            if (node.left) {
                this.insertNode(node.left, newNode); // 如果非空，则要再一次判断，插入到子节点下面
                return;
            }

            node.left = newNode;
            return;
        } else {// 比父节点大，插入在右边
            if (node.right) {
                this.insertNode(node.right, newNode);// 如果非空，则要再一次判断，插入到子节点下面
                return;
            }

            node.right = newNode;
            return;
        }
    }

    /**
     * 记录遍历的顺序
     */
    private traversal: number[] = [];

    /**
     * 中序遍历
     * 
     * @returns 
     */
    public inorderTraversal(): number[] {
        this.traversal = [];

        if (this.root)
            this.inorderTraversalNode(this.root);

        return this.traversal;
    }

    private inorderTraversalNode(node: TreeNode): void {
        if (node.left)
            this.inorderTraversalNode(node.left);

        this.traversal.push(node.key);

        if (node.right)
            this.inorderTraversalNode(node.right);
    }

    /**
     * 前序遍历
     */
    public preorderTraversal(): number[] {
        this.traversal = [];

        if (this.root)
            this.preorderTraversalNode(this.root);

        return this.traversal;
    }

    /**
     * Preorder Traversal Recursion
     * 
     * @param node
     */
    private preorderTraversalNode(node: TreeNode): void {
        this.traversal.push(node.key);

        if (node.left)
            this.preorderTraversalNode(node.left);

        if (node.right)
            this.preorderTraversalNode(node.right);
    }

    /**
     * 后序遍历 
     */
    public postorderTraversal(): number[] {
        this.traversal = [];
        if (this.root)
            this.postorderTraversalNode(this.root);

        return this.traversal;
    }

    /**
     * Postorder Traversal Recursion
     * 
     * @param node
     */
    private postorderTraversalNode(node: TreeNode): void {
        if (node.left)
            this.postorderTraversalNode(node.left);

        if (node.right)
            this.postorderTraversalNode(node.right);

        this.traversal.push(node.key);
    }

    /**
     * 由于大的在右边，只需要一直寻找最右边的就行了。寻找最小的也同理。
     * 
     * @returns 
     */
    public findMax(): number {
        return this.root ? this.findMaxNode(this.root).key : 0;
    }

    public findMaxNode(node: TreeNode): TreeNode {
        if (node && node.right)
            return this.findMaxNode(node.right);

        return node;
    }

    public findMin(): number {
        return this.root ? this.findMinNode(this.root).key : 0;
    }

    public findMinNode(node: TreeNode): TreeNode {
        if (node && node.left)
            return this.findMinNode(node.left);

        return node;
    }

    /**
     * 删除节点
     * 
     * @param key 节点索引
     */
    public delete(key: number): void {
        this.root = this.deleteNode(this.root, key);
    }

    /**
     * 
     * @param node 
     * @param key   节点索引
     * @returns 
     */
    deleteNode(node: TreeNode | undefined, key: number): TreeNode | undefined {
        if (!node)
            return undefined; // 如果是空就直接返回空

        if (key < node.key) {
            node.left = this.deleteNode(node.left, key);
            return node;
        } else if (key > node.key) {
            node.right = this.deleteNode(node.right, key);
            return node;
        }

        if (!node.left && !node.right) {
            node = undefined;
            return node;
        }

        if (!node.left) {
            node = node.right;
            return node;
        }

        if (!node.right) {
            node = node.left;
            return node;
        }

        const aux = this.findMinNode(node.right);
        node.key = aux.key;
        node.right = this.deleteNode(node.right, aux.key);

        return node;
    }

    /**
     * 获取二叉树节点个数
     * 
     * @returns 二叉树节点个数
     */
    public size(): number {
        return this.root ? this._size(this.root) : 0;
    }

    /**
     * 
     * @param subTree 
     * @returns 
     */
    private _size(subTree: TreeNode | undefined): number {
        if (!subTree)
            return 0;
        else
            return 1 + this._size(subTree.left) + this._size(subTree.right);
    }

    /**
     * 获取二叉树层级数
     * 
     * @returns 二叉树层级数
     */
    public height(): number {
        return this.root ? this._height(this.root) : 0;
    }

    /**
     * 
     * @param subTree 
     * @returns 
     */
    private _height(subTree: TreeNode | undefined): number {
        if (!subTree)
            return 0;
        else {
            let i = this._height(subTree.left),
                j = this._height(subTree.right);

            return i < j ? (j + 1) : (i + 1);
        }
    }
}

// test
let rootNode: TreeNode = new TreeNode(3, "B");
let tree: BinaryTree = new BinaryTree();
tree.insert(rootNode);
tree.insert(new TreeNode(2, "B"));
tree.insert(new TreeNode(1, "A"));
tree.insert(new TreeNode(4, "B"));
tree.insert(new TreeNode(5, "B"));



