
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
        return name == 'template' || name == 'data' || name == 'mixins'
            || name == 'computed' || name == 'mounted' || name == 'beforeCreate' || name == "watch";
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
        // console.log(value)
        if (value === String || value === Boolean || value === Number || value && value.type)
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

        public $el: HTMLElement = document.body;

        public $props: any;

        public props: { [key: string]: any } = {};

        public BUS: any;

        public $parent: any;

        public $options: any;

        public $refs: any;

        public $children: Vue[] = [];

        // public propsFactory: any;

        // public propsFactory(): { [key: string]: any };

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

            let props: { [key: string]: any };

            //@ts-ignore
            if (typeof this.propsFactory == 'function') // fn 用于继承时候的复制
                //@ts-ignore
                props = this.propsFactory();
            else
                props = this.props;

            for (var i in this) {
                if (i == 'constructor' || i == 'name' || i == 'register' || i == 'propsFactory' || i == 'watchFactory' ||
                    i == 'props' || i == '$destroy' || i == "$el" || i == "$emit" || i == "$options")
                    continue;

                let value: any = this[i];

                if (isVueCfg(i))
                    cfg[i] = value;
                else if (isSimplePropsField(value))
                    cfg.props[i] = value;
                else if (typeof value == 'function')
                    cfg.methods[i] = value;
                else if (isPropsField(i, props))
                    cfg.props[i] = props[i];
                else if (i[0] != '$')// 如果不是 $ 开头的，就是 data fields，$xxx 表示实例变量，不参与数据驱动，节省资源
                    dataFields[i] = value;
            }

            for (let i in props) { // 补充缺少的 prop。这些 prop 只在 markup 中使用，故不需要在 class 中列出
                if (!(i in cfg.props))
                    cfg.props[i] = props[i];
            }

            // 注意如果 类有了 data(){}，那么 data 属性将会失效（仅作提示用），改读取 data() {} 的
            if (!cfg.data)
                cfg.data = () => {
                    return JSON.parse(JSON.stringify(dataFields)); // 深度 Clone 对象
                }

            // console.log(cfg)

            // @ts-ignore
            if (cfg.watch && this.watchFactory)
                // @ts-ignore
                aj.apply(cfg.watch, this.watchFactory());

            Vue.component(this.name, cfg);
        }
    }
}

// VS Code 高亮 HTML 用
const html = String;