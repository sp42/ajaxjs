/**
 * 标准表格
 */
interface Grid extends Vue, GridSectionModel, Ajax {
    /**
     * 数据层，控制分页
     */
    $store: any;

    /**
     * 工具条 UIUI
     */
    $toolbar: GridToolbar;

    /**
     * 行 UIUI
     */
    $row: Vue;

    showAddNew: boolean;

    list: GridRecord[];

    /**
     * 重新加载数据
     */
    reload(): void;
}

/**
 * 选区模型
 */
interface GridSectionModel extends Vue {
    isSelectAll: boolean;

    /**
     * 选择的行，key 是 id，value 为 true 表示选中
     * 没选中，则删除这个 key，所以也不存在 value 为 false 的情况
     */
    selected: { [key: number]: boolean };

    selectedTotal: number;

    maxRows: number;

    /**
     * 批量删除
     */
    batchDelete(): void;
}

/**
 * 工具条
 */
interface GridToolbar extends Vue {
    $parent: Grid;
}

/**
 * 行的 UIUI
 */
interface GridEditRow extends Vue {
    /**
     * 固定不可编辑的字段
     */
    filterField: Array<string>;

    /**
     * 每行记录它的 id
     */
    id: string;

    /**
     * 每行记录的数据
     */
    data: any;

    /**
     * 是否处于编辑模式
     */
    isEditMode: boolean;

    /**
     * 表格是否可以被编辑
     */
    enableInlineEdit: boolean;

    /**
     * 单元格渲染器的类型，这是一个有序的数组
     */
    columns: Array<CellRenderer>;

    $parent: Grid;
}

/**
 * 新建记录的行
 */
interface EditRowCreate extends Vue {
    /**
     * 创建的 API 地址
     */
    createApi: string,

    /**
     * 是否处于编辑模式
     */
    isEditMode: boolean
}

/**
 * 单元格索引类型，指定 json 的 key（索引） 直接返回 value
 */
type CellRendererKey = string;
/**
 * 单元格渲染器函数
 */
type CellRendererFn = (j: JsonParam) => string;

/**
 * 复杂的单元格渲染器
 */
interface CellRendererConfig {
    /**
     * 可否被编辑编辑
     */
    editMode: boolean;

    key: CellRendererKey;

    renderer: CellRendererFn;

    editRenderer: CellRendererFn;

    /**
     * 数据类型
     */
    type: String | Number | Boolean;
}

/**
 * 单元格渲染器，可以存在以下几种类型之一
 */
type CellRenderer = CellRendererKey | CellRendererFn | CellRendererConfig;

/**
 * 某个记录的修改过的部分，其中 id 用于标识，
 * 其余字段就是修改过的 key & value
 */
interface DirtyData extends JsonParam {
    /**
     * 修改过记录的 id 标识
     */
    id: string;
}

/**
 * 行记录数据
 */
interface GridRecord extends BaseObject {
    /**
     * 修改过记录
     */
    dirty?: DirtyData;
}

