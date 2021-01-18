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
})(aj || (aj = {}));
