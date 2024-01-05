/**
 * 树节点
 */
type DS_TreeNode_Project = {
    title: string,
    contextmenu: boolean,
    expand?: boolean,
    loading: boolean,
    render: Function,
    projectData: any,
    children?: DS_TreeNode_Service[]
};

/**
 * 树节点（服务）
 */
type DS_TreeNode_Service = {
    title: string,
    contextmenu: boolean,
    expand?: boolean,
    /**
     * 是否创建的
     */
    isCreate?: boolean,
    data: any,
    id: string,
    parentNode: DS_TreeNode_Service | any,
    render?: Function,
    children?: DS_TreeNode_Service[]
};