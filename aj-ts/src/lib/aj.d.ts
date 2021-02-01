/**
 * AJAXJS UI 库占据 aj 一个全局变量
 */
declare namespace aj {
    /**
     * Web 虚拟目录
     */
    declare var ctx: string;

    declare var msg: TopMsg;

    /**
      * 顯示確定的對話框
      * 
      * @param {String} text        显示的文本
      * @param {Function} callback  回调函数，可选
      */
    declare var alert = (text: string, callback?: Function): void => { };

    /**
     * 顯示“是否”選擇的對話框
     * 
     * @param {String} text         显示的文本
     * @param {Function} callback   回调函数
     */
    declare var showConfirm = (text: string, callback?: Function, showSave?: boolean): void => { };

    // aj.admin
    namespace admin {
    }
}

// declare var aj: (cssSelector: cssSelector, fn?: Function) => Element | null;
declare var EXIF: any;

/**
 * 实体，一般至少有 id 和 name 字段
 */
declare interface BaseObject {
    /**
     * 实体 id
     */
    id: number;

    /**
     * 实体名称
     */
    name: string;
}

/**
 * 后端响应的消息
 */
declare interface RepsonseResult {
    /**
     * 操作代码
     */
    code?: number;

    /**
     * 操作是否成功
     */
    isOk: boolean;

    /**
     * 操作说明
     */
    msg: string;

    /**
     * 结果，实体，可以是任意类型
     */
    result: BaseObject[];

    /**
     * 进行新建的时候返回的实体 id
     */
    newlyId?: number
}

/**
 * 图片上传响应的消息
 */
declare interface ImgUploadRepsonseResult extends RepsonseResult {
    /**
     * 相对地址
     */
    imgUrl: string;

    /**
     * 图片绝对地址，http 开头的
     */
    fullUrl: string;

    /**
     * 图片列表
     */
    pics: string[];
}

/**
 * 树列表
 */
declare interface TreeList {

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
            children : [ {
                d : 3
            } ]
        }
    };`
 */
declare interface TreeNode extends BaseObject {
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
    children: TreeNode[];
}

/**
 * 包裹 TreeNode 的 Map
 * key 为 TreeNode.id
 */
declare type TreeMap = { [key: string]: TreeNode };

/**
 * 表单里面字段控件
 */
declare interface FormFieldElementComponent {
    /**
     * 字段名称 name
     */
    fieldName: string;

    /**
     * 字段的值
     */
    fieldValue: string;
}

/**
 * CSS 选择符
 */
declare type cssSelector = string;

// 官方 API 还没提供，自己写一个
interface FileReaderEventTarget extends EventTarget {
    result: string
}

interface FileReaderEvent extends Event {
    target: FileReaderEventTarget;
    getMessage(): string;
}

/**
 * JSON 实体
 */
declare type JsonParam = { [key: string]: string | number | boolean | JsonParam };

/**
 * key 和 value 都是 string 类型的 JSON 实体
 */
declare type StringJsonParam = { [key: string]: string };

/**
 * 对象包含 RPC 请求
 */
declare interface Ajax {
    /**
     * 接口地址
     */
    apiUrl: string;

    /**
     * 真实发送的请求，可能包含 QueryString
     */
    realApiUrl?: string;

    /**
     * 每次请求都附带的参数，一经修改就不可修改的
     */
    baseParam?: JsonParam;

    /**
     *  与 baseParam 合并后每次请求可发送的，可以修改的
     */
    extraParam?: JsonParam;

    /**
     * 上次发送的请求
     */
    lastRequestParam?: JsonParam;

    /**
     * 请求结果
     */
    result?: any;

    /**
     * 是否自动加载数据
     */
    isAutoLoad: boolean;

    /**
     * 请求 GET 数据
     */
    getData(): void;

    /**
     * 得到数据后的回调
     */
    onLoad?: (j: RepsonseResult) => void;
}