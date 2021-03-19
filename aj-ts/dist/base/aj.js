"use strict";
var aj;
(function (aj) {
    /**
     * 复制 b 对象到 a 对象身上
     *
     * @param {Object} 目标对象
     * @param {Object} 源对象
     */
    function apply(a, b, c) {
        for (var i in b)
            a[i] = b[i];
        return c ? aj.apply(a, c) : a;
    }
    aj.apply = apply;
    /**
     * 加载脚本
     *
     * @param url   脚本地址
     * @param id    脚本元素 id
     * @param cb    回调函数
     */
    function loadScript(url, id, cb) {
        var script = document.createElement("script");
        script.src = url;
        if (cb)
            script.onload = cb;
        if (id)
            script.id = id;
        document.getElementsByTagName("head")[0].appendChild(script);
    }
    aj.loadScript = loadScript;
    /**
      * 并行和串行任务
      *
      * @author https://segmentfault.com/a/1190000013265925
      * @param arr
      * @param finnaly
      */
    function parallel(arr, _finally) {
        var fn, index = 0;
        // @ts-ignore
        var statusArr = Array(arr.length).fill().map(function () { return ({
            isActive: false,
            data: null
        }); });
        var isFinished = function () {
            return statusArr.every(function (item) { return item.isActive === true; });
        };
        var resolve = function (index) {
            return function (data) {
                statusArr[index].data = data;
                statusArr[index].isActive = true;
                var isFinish = isFinished();
                if (isFinish) {
                    var datas = statusArr.map(function (item) { return item.data; });
                    _finally(datas);
                }
            };
        };
        // @ts-ignore
        while ((fn = arr.shift())) {
            fn(resolve(index)); // 给 resolve 函数追加参数,可以使用 bind 函数实现,这里使用了柯里化
            index++;
        }
    }
    aj.parallel = parallel;
    /**
     * 函数节流
     *
     * @author https://www.cnblogs.com/moqiutao/p/6875955.html
     * @param fn
     * @param delay
     * @param mustRunDelay
     */
    function throttle(fn, delay, mustRunDelay) {
        var timer, t_start;
        return function () {
            var _this = this;
            var t_curr = +new Date();
            window.clearTimeout(timer);
            if (!t_start)
                t_start = t_curr;
            if (t_curr - t_start >= mustRunDelay) {
                // @ts-ignore
                fn.apply(this, arguments);
                t_start = t_curr;
            }
            else {
                var args = arguments;
                // @ts-ignore
                timer = window.setTimeout(function () { return fn.apply(_this, args); }, delay);
            }
        };
    }
    aj.throttle = throttle;
    ;
    aj.SELECTED = "selected";
    aj.SELECTED_CSS = "." + aj.SELECTED;
    /**
     * 判断是否 Vue 配置字段
     *
     * @param name
     */
    function isVueCfg(name) {
        return name == 'template' || name == 'data' || name == 'mixins'
            || name == 'computed' || name == 'mounted' || name == 'beforeCreate' || name == "watch";
    }
    /**
     *
     * @param name
     * @param props
     */
    function isPropsField(name, props) {
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
    function isSimplePropsField(value) {
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
    var VueComponent = /** @class */ (function () {
        function VueComponent() {
            this.$el = document.body;
            this.props = {};
            this.$children = [];
        }
        // public propsFactory: any;
        // public propsFactory(): { [key: string]: any };
        VueComponent.prototype.$destroy = function () { };
        VueComponent.prototype.$emit = function (e) {
            var obj = [];
            for (var _i = 1; _i < arguments.length; _i++) {
                obj[_i - 1] = arguments[_i];
            }
        };
        /**
         * 转换为 ClassAPI
         */
        VueComponent.prototype.register = function (instanceFields) {
            var cfg = {
                props: {},
                methods: {}
            };
            var dataFields = {};
            var props;
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
                var value = this[i];
                if (isVueCfg(i))
                    cfg[i] = value;
                else if (isSimplePropsField(value))
                    cfg.props[i] = value;
                else if (typeof value == 'function')
                    cfg.methods[i] = value;
                else if (isPropsField(i, props))
                    cfg.props[i] = props[i];
                else if (i[0] != '$') // 如果不是 $ 开头的，就是 data fields，$xxx 表示实例变量，不参与数据驱动，节省资源
                    dataFields[i] = value;
            }
            for (var i_1 in props) { // 补充缺少的 prop。这些 prop 只在 markup 中使用，故不需要在 class 中列出
                if (!(i_1 in cfg.props))
                    cfg.props[i_1] = props[i_1];
            }
            // 注意如果 类有了 data(){}，那么 data 属性将会失效（仅作提示用），改读取 data() {} 的
            if (!cfg.data)
                cfg.data = function () {
                    return JSON.parse(JSON.stringify(dataFields)); // 深度 Clone 对象
                };
            // console.log(cfg)
            // @ts-ignore
            if (cfg.watch && this.watchFactory)
                // @ts-ignore
                aj.apply(cfg.watch, this.watchFactory());
            Vue.component(this.name, cfg);
        };
        return VueComponent;
    }());
    aj.VueComponent = VueComponent;
})(aj || (aj = {}));
// VS Code 高亮 HTML 用
var html = String;
