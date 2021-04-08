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

    /**
     * 是否嵌入一个 Vue 的组件去参与渲染
     */
    isComponent: boolean;

    key?: CellRendererKey;

    renderer: CellRendererFn;

    editRenderer?: CellRendererFn;

    /**
     * 数据类型
     */
    type?: String | Number | Boolean;
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