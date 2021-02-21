declare class Vue {
    public $el: HTMLElement;

    public $props: any;

    public $refs: any;

    public BUS: any;

    public $parent: Vue;

    public $options: any;

    public $children: any[];

    public static options: any;

    constructor(cfg: any) {
    }

    public $watch(...any): void;

    public $set(...any): void;

    public $destroy() { }

    public $emit(e: string, ...obj: any) { }

    public static component(string, Object): void {
    }

    public static set(...any): void {
    }

    public static extend(...any): any {
    }

    public ajResources = { // 我自己扩展的，非 vue 官方 API
        imgPerfix: "",
        ctx: ""
    };


}