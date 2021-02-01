
namespace aj {
    /**
     * 复制 b 对象到 a 对象身上
     * 
     * @param {Object} 目标对象
     * @param {Object} 源对象
     */
    export function apply(a: any, b: any, c?: any): any {
        for (var i in b)
            a[i] = b[i];

        return c ? aj.apply(a, c) : a;
    }

    /**
     * 加载脚本
     * 
     * @param url   脚本地址
     * @param id    脚本元素 id
     * @param cb    回调函数
     */
    export function loadScript(url: string, id?: string, cb?: (ev: Event) => any): void {
        var script: HTMLScriptElement = document.createElement("script");
        script.src = url;
        if (cb)
            script.onload = cb;

        if (id)
            script.id = id;

        document.getElementsByTagName("head")[0].appendChild(script);
    }

    /**
      * 并行和串行任务 
      * 
      * @author https://segmentfault.com/a/1190000013265925
      * @param arr 
      * @param finnaly 
      */
    export function parallel(arr: [], _finally: Function) {
        let fn: Function, index: number = 0;
        // @ts-ignore
        let statusArr = Array(arr.length).fill().map(() => ({
            isActive: false,
            data: null
        }));

        let isFinished = function () {
            return statusArr.every((item: any) => item.isActive === true);
        }

        let resolve = function (index: number): Function {
            return function (data: any) {
                statusArr[index].data = data;
                statusArr[index].isActive = true;
                let isFinish = isFinished();

                if (isFinish) {
                    let datas = statusArr.map((item: any) => item.data);

                    _finally(datas);
                }
            };
        };
        // @ts-ignore
        while ((fn = arr.shift())) {
            fn(resolve(index));// 给 resolve 函数追加参数,可以使用 bind 函数实现,这里使用了柯里化
            index++;
        }
    }

    /**
     * 函数节流
     * 
     * @author https://www.cnblogs.com/moqiutao/p/6875955.html
     * @param fn 
     * @param delay 
     * @param mustRunDelay 
     */
    export function throttle(fn: Function, delay: number, mustRunDelay: number): Function {
        var timer: number, t_start: number;

        return function () {
            var t_curr = +new Date();
            window.clearTimeout(timer);

            if (!t_start)
                t_start = t_curr;

            if (t_curr - t_start >= mustRunDelay) {
                // @ts-ignore
                fn.apply(this, arguments);
                t_start = t_curr;
            } else {
                var args = arguments;
                // @ts-ignore
                timer = window.setTimeout(() => fn.apply(this, args), delay);
            }
        };
    };

    export var SELECTED = "selected";
    export var SELECTED_CSS = "." + SELECTED;

    /**
     * 判断是否 Vue 配置字段
     * 
     * @param name 
     */
    function isVueCfg(name: string): boolean {
        return name == 'template' || name == 'data' || name == 'mixins' || name == 'computed' || name == 'mounted' || name == "watch";
    }

    /**
     * 
     * @param name 
     * @param props 
     */
    function isPropsField(name: string, props: { [key: string]: any }): boolean {
        if (props && props[name])
            return true;
        else
            return false;
    }

    /**
     * 判断是否 props 字段
     * 
     * @param value 
     */
    function isSimplePropsField(value: any): boolean {
        console.log(value)
        if (value === String || value === Boolean || value === Number || value.type)
            return true;
        else
            return false;
    }

    /**
     * 为让 Vue 组件使用 Class 风格，通过一个类似语法糖的转换器
     * 这是实验性质的
     */
    export abstract class VueComponent {
        /**
         * 组件名字，必填
         */
        abstract name: string;

        $el: HTMLElement = document.body;

        public $props: any;

        props: { [key: string]: any } = {};

        public BUS: any;

        public $destroy() { }

        public $emit(e: string, ...obj: any) { }

        /**
         * 转换为 ClassAPI
         */
        register(instanceFields?: string[]): void {
            let cfg: { [key: string]: any } = {
                props: {},
                methods: {}
            };

            let dataFields: { [key: string]: any } = {};

            for (var i in this) {
                if (i == 'constructor' || i == 'name' || i == 'register' || i == '$destroy' || i == "$emit")
                    continue;

                let value: any = this[i];

                if (isVueCfg(i))
                    cfg[i] = value;
                else if (isSimplePropsField(value))
                    cfg.props[i] = value;
                else if (typeof value == 'function')
                    cfg.methods[i] = value;
                else if (isPropsField(i, this.props))
                    cfg.props[i] = this.props[i];
                else // data fiels
                    dataFields[i] = value;
            }

            // 注意如果 类有了 data(){}，那么 data 属性将会失效，改读取 data() {} 的
            if (!cfg.data)
                cfg.data = () => {
                    return dataFields;
                }

            console.log(cfg)

            Vue.component(this.name, cfg);
        }
    }
}

// VS Code 高亮 HTML 用
const html = String;