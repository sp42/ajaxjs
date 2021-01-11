
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
     * @param url 
     * @param id 
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
    export function parallel(arr: [], finnaly: Function) {
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

                    finnaly(datas);
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
     * 获取表单控件的值
     * 
     * @param el 
     * @param cssSelector 
     */
    export function getFormFieldValue(_el: HTMLElement, cssSelector: cssSelector): string {
        let el = _el.$(cssSelector);
        if (el) {

            return (<HTMLInputElement>el).value;
        } else
            throw `找不到${cssSelector}元素`;
    }
}