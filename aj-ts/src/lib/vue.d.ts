declare class Vue {
    public $el: HTMLElement;

    public $props: any;

    public $refs: any;

    public BUS: any;

    public $parent: Vue;

    public $options: any;

    public $children: any[];

    public static options: any;

    public ajResources = {
        imgPerfix: "",
        ctx: ""
    };

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
    
}