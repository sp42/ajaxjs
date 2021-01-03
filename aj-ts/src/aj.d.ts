
// declare var aj: (cssSelector: cssSelector, fn?: Function) => Element | null;


/**
 * AJAXJS
 */
declare namespace aj {
    declare var msg: TopMsg;

    /**
      * 顯示確定的對話框
      * 
      * @param {String} text 显示的文本
      * @param {Function} callback 回调函数
      */
    declare var alert = (text: string, callback?: Function): void => { };

    /**
     * 顯示“是否”選擇的對話框
     * 
     * @param {String} text         显示的文本
     * @param {Function} callback   回调函数
     */
    declare var showConfirm = (text: string, callback?: Function, showSave?: boolean): void => { };
    declare var simpleOk = (text: string, callback?: Function): void => { };

    namespace admin {

    }
}

declare var Raphael: any;

declare class Vue {
    public $el: HTMLElement;

    public $props: any;

    public $refs: any;

    public BUS: any;

    public $parent: Vue;

    public $options: any;

    public static options: any;

    public ajResources = {
        imgPerfix: ""
    };

    constructor(cfg: any) {
    }

    public $destroy() { }

    public $emit(e: string, ...obj: any) { }

    public static component(string, Object): void {
    }

    public static set(...any): void {
    }
}

declare interface BaseObject {
    /**
     * 实体 id
     */
    id: number;

    /**
     * 实体名称
     */
    name: string; RepsonseResult
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
    result?: any;

    /**
     * 进行新建的时候返回的实体 id
     */
    newlyId?: number
}

/**
 * 图片上传响应的消息
 */
declare interface ImgUploadRepsonseResult extends RepsonseResult {
    imgUrl: string;
    fullUrl: string;
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
     * 子节点
     */
    children: TreeNode[];
}

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