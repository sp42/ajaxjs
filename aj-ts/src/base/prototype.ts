interface Element {
    /**
     * 查找元素
     * 
     * @param cssSelector   CSS 选择器
     * @param fn            可选，当送入该参数的时候，表示使用 querySelectorAll 来查询多个 dom 元素，故 fn 是个遍历器函数，其参数列表如 item、index、array
     */
    $(cssSelector: cssSelector, fn?: Function): Element | NodeListOf<Element> | null,

    /**
     * 查找父元素，支持 标签名称 或 样式名称，任选其一而不能同时传。
     * 
     * @param tagName	标签名称
     * @param className	样式名称
     * @returns 父级元素，如果没有找到返回 null
     */
    up(tagName: string, className?: string): Element | null
}

Element.prototype.$ = function (cssSelector: cssSelector, fn?: Function): Element | NodeListOf<Element> | null {
    if (typeof fn == 'function') {
        let children = this.querySelectorAll(cssSelector);

        if (children && fn)
            // @ts-ignore
            Array.prototype.forEach.call(children, fn);

        return children;
    } else
        // @ts-ignore
        return this.querySelector.apply(this, arguments);
}

Element.prototype.up = function (tagName: string, className: string): Element | null {
    if (tagName && className)
        throw '只能任选一种参数，不能同时传';

    let el: Element = <Element>this.parentNode;
    tagName = tagName && tagName.toUpperCase();

    while (el) {
        if (tagName && el.tagName == tagName)
            return el;

        if (className && el.className && ~el.className.indexOf(className))
            return el;

        el = <Element>el.parentNode;
    }

    return null;
}

interface Function {
    /**
     * 函数委托 参见 http://blog.csdn.net/zhangxin09/article/details/8508128
     *
     * @return {Function}
     */
    delegate(): Function
}

Function.prototype.delegate = function (this: any): Function {
    var self = this, scope = this.scope, args = arguments, aLength: number = arguments.length, fnToken: string = 'function';

    return function (this: any) {
        var bLength = arguments.length, Length = (aLength > bLength) ? aLength : bLength;

        // mission one:
        for (var i = 0; i < Length; i++)
            if (arguments[i])
                args[i] = arguments[i]; // 拷贝参数

        args.length = Length; // 在
        // MSjscript下面，arguments作为数字来使用还是有问题，就是length不能自动更新。修正如左:

        // mission two:
        for (var i = 0, j = args.length; i < j; i++) {
            var _arg = args[i];
            if (_arg && typeof _arg == fnToken && _arg.late == true)
                args[i] = _arg.apply(scope || this, args);
        }

        return self.apply(scope || this, args);
    };
}

interface Date {
    /**
     * 格式化日期
     * 
     * @param fmt 日期格式
     */
    format(fmt: string): string
}

/**
 * 日期格式化
 * 
 * @author meizz
 */
Date.prototype.format = function (fmt: string): string {
    let o: { [key: string]: number } = {
        "M+": this.getMonth() + 1,                  // 月份   
        "d+": this.getDate(),                       // 日   
        "h+": this.getHours(),                      // 小时   
        "m+": this.getMinutes(),                    // 分   
        "s+": this.getSeconds(),                    // 秒   
        "q+": Math.floor((this.getMonth() + 3) / 3),// 季度   
        "S": this.getMilliseconds()                 // 毫秒   
    };

    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));

    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) {
            let obj = (RegExp.$1.length == 1) ? o[k] : ("00" + o[k]).substr(("" + o[k]).length);
            // @ts-ignore
            fmt = fmt.replace(RegExp.$1, obj);
        }

    return fmt;
}

/*
    polyfill JavaScript-Canvas-to-Blob 解决了 HTMLCanvasElement.toBlob 的兼容性
    https://github.com/blueimp/JavaScript-Canvas-to-Blob
*/
if (!HTMLCanvasElement.prototype.toBlob) {
    Object.defineProperty(HTMLCanvasElement.prototype, 'toBlob', {
        value(callback: Function, type: string, quality: string) {
            let binStr = atob(this.toDataURL(type, quality).split(',')[1]), len: number = binStr.length, arr: Uint8Array = new Uint8Array(len);

            for (var i = 0; i < len; i++)
                arr[i] = binStr.charCodeAt(i);

            callback(new Blob([arr], { type: type || 'image/png' }));
        }
    });
}