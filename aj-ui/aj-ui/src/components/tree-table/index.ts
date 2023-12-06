import { xhr_get, xhr_post, xhr_put, xhr_del } from '../../util/xhr';
import TreeSelector from './tree-selector.vue';

export default {
    components: { TreeSelector },
    props: {
        name: { type: String, require: true },
        list: { type: Array, default() { return []; } },
        api: { type: Object, require: true },
        formFields: { type: Array, default() { return []; } }
    },
    data() {
        let fields: any[] = [ // 动态传入 列配置
            {
                title: '#',
                width: 100,
                align: 'center',
                key: 'id'
            },

            ...this.list,

            {
                title: '修改日期',
                align: 'center',
                width: 150,
                render(h: Function, { row }) {
                    if (row.updateDate) {
                        let arr: string[] = row.updateDate.split(':');
                        arr.pop();
                        return h('span', arr.join(':'));
                    }
                },
                // key: 'createDate'
            },
            // {
            //     title: '顺序',
            //     key: 'index',
            //     align: 'center',
            //     width: 120
            // },
            {
                title: '编辑',
                align: 'center',
                width: 270,
                slot: 'action'
            }
        ];

        return {
            isCreateTop: false,
            isUpdate: false,
            edit: {
                isShowEdit: false,
                row: {}, // 正在编辑的对象，如果新建的时候，这个值为 null
                parent: '',
                parentId: 0,
            },
            columns16: fields,
            data: [
                {
                    id: '100',
                    name: 'John Brown',
                    age: 18,
                    address: 'New York No. 1 Lake Park'
                },
                {
                    id: '101',
                    name: 'Jim Green',
                    age: 24,
                    address: 'London No. 1 Lake Park',
                    _showChildren: true,
                    children: [
                        {
                            id: '10100',
                            name: 'John Brown',
                            age: 18,
                            address: 'New York No. 1 Lake Park'
                        },
                        {
                            id: '10101',
                            name: 'Joe Blackn',
                            age: 30,
                            address: 'Sydney No. 1 Lake Park'
                        },
                        {
                            id: '10102',
                            name: 'Jon Snow',
                            age: 26,
                            address: 'Ottawa No. 2 Lake Park',
                            children: [
                                {
                                    id: '1010200',
                                    name: 'Jim Green',
                                    age: 24,
                                    address: 'New York No. 1 Lake Park'
                                }
                            ]
                        }
                    ]
                },
                {
                    id: '102',
                    name: 'Joe Black',
                    age: 30,
                    address: 'Sydney No. 1 Lake Park'
                },
                {
                    id: '103',
                    name: 'Jon Snow',
                    age: 26,
                    address: 'Ottawa No. 2 Lake Park'
                }
            ]
        }
    },
    mounted() {
        this.getData();
    },
    methods: {
        getData() {
            // xhr_get(`${window['config'].dsApiRoot}/api/cms/sys_datadict/list`, (j: any) => {
            xhr_get(`${this.api.list}`, (j: any) => {
                this.data = tranListToTreeData(j.result);
                // console.log(tranListToTreeData(j.result));
            });
        },

        /**
         * 新建子节点
         * 
         * @param name  父节点的名称
         * @param id    父节点的 id
         */
        createTreeNode(name: string, id: number, parentId: number): void {
            this.isCreateTop = false;
            this.isUpdate = false;
            this.edit.isShowEdit = true;
            this.edit.parent = name;
            this.edit.parentId = id;
            this.edit.row = {}; // v-if 隐藏 Select 会有问题，改用 v-show
            this.$refs.selectParent.selectId = id; // 选中那个 option
        },

        /**
         * 编辑节点
         * 
         * @param name  父节点的名称
         * @param id    父节点的 id
         */
        editTreeNode(row: any): void {
            this.isCreateTop = false;
            this.isUpdate = true;
            this.edit.isShowEdit = true;
            this.edit.parent = row.name;
            this.edit.parentId = row.id;
            this.edit.row = row;
            this.$refs.selectParent.selectId = row.parentId; // 选中那个 option
        },

        /**
         * 创建顶级节点
         */
        createTop() {
            this.isUpdate = false;
            this.isCreateTop = true;
            this.edit.isShowEdit = true;
            this.edit.row = {};
        },

        /**
         * 保存
         */
        save(): void {
            if (this.isUpdate) {
                let obj: any = Object.assign({}, this.edit.row);
                delete obj._index;
                delete obj._rowKey;
                delete obj.children;
                delete obj._showChildren;
                obj.parentId = this.$refs.selectParent.selectId;

                xhr_put(`${this.api.update}`, (j: RepsonseResult) => {
                    if (j.status) {
                        this.getData()
                        this.$Message.success(j.message)
                    } else
                        this.$Message.error(j.message || '修改失败');
                }, obj);
            } else if (this.isCreateTop) {
                this.edit.row.parentId = 0;

                xhr_post(`${this.api.create}`, (j: RepsonseResult) => {
                    if (j.status) {
                        this.getData()
                        this.$Message.success(j.message)
                    } else
                        this.$Message.error(j.message || '创建失败');
                }, this.edit.row);
            } else {
                let obj: any = Object.assign({}, this.edit.row);
                obj.parentId = this.$refs.selectParent.selectId;

                xhr_post(`${this.api.create}`, (j: RepsonseResult) => {
                    if (j.status) {
                        this.getData()
                        this.$Message.success(j.message)
                    } else
                        this.$Message.error(j.message || '创建失败');
                }, obj);
            }
        },
        delTreeNode({ id }) {
            xhr_del(`${this.api.delete}`, (j: RepsonseResult) => {
                if (j.status) {
                    this.getData()
                    this.$Message.success(j.message)
                } else
                    this.$Message.error(j.message || '创建失败');
            }, { id: id });
        },
        delAllTreeNode() { alert(9) },
    },
    computed: {
        getTitle(): string {
            if (this.isUpdate)
                return '编辑[' + this.edit.row.name + ']';
            else if (this.isCreateTop)
                return '新建顶级节点';
            else
                return '新建子' + this.name;
        }
    }
};

/**
 * 树节点
 */
export type TreeMap = {
    id: number;

    /**
     * 父 id，输入时候必填
     */
    parentId: number;

    /**
     * iView Table 控件的展开属性
     */
    _showChildren: boolean;

    /**
     * 子节点，生成 Tree 之后出现
     */
    children?: TreeMap[];
}

/**
 * 数组转成树
 * 
 * @param list     数组
 * @param parentId 顶级父 id
 * @returns 
 * @returns 
 */
// function tranListToTreeData(list: TreeMap[], parentId: number): TreeMap[] {
//     // 先找到所有的根节点
//     let tranList: TreeMap[] = list.filter((it: TreeMap) => it.parentId === parentId);

//     // 传入 id  list进行递归 在筛选出 他的父级 是一个数组
//     tranList.forEach((itm: TreeMap) => itm.children = tranListToTreeData(list, itm.id));

//     return tranList;
// }

/**
 * 数组转成树
 * 
 * @param list 数组
 * @returns JSON Tree
 */
function tranListToTreeData(list: TreeMap[]): TreeMap[] {
    let treeList: TreeMap[] = [];// 最终要产出的树状数据的数组
    let map = {};     // 所有项都使用对象存储起来

    list.forEach((item: TreeMap) => { // 建立一个映射关系：通过 id 快速找到对应的元素
        if (!item.children)
            item.children = [];
        item._showChildren = true;

        map[item.id] = item;
    });

    list.forEach((item: TreeMap) => {
        // 对于每一个元素来说，先找它的上级
        //    如果能找到，说明它有上级，则要把它添加到上级的 children 中去
        //    如果找不到，说明它没有上级，直接添加到 treeList
        let parent = map[item.parentId];


        if (parent)// 如果存在则表示 item 不是最顶层的数据
            parent.children.push(item);
        else
            treeList.push(item);// 如果不存在 则是顶层数据
    });

    return treeList;
}