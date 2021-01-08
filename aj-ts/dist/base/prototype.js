"use strict";
Element.prototype.$ = function (cssSelector, fn) {
    if (typeof fn == 'function') {
        var children = this.querySelectorAll(cssSelector);
        if (children && fn)
            // @ts-ignore
            Array.prototype.forEach.call(children, fn);
        return children;
    }
    else
        // @ts-ignore
        return this.querySelector.apply(this, arguments);
};
Element.prototype.up = function (tagName, className) {
    if (tagName && className)
        throw '只能任选一种参数，不能同时传';
    var el = this.parentNode;
    tagName = tagName && tagName.toUpperCase();
    while (el) {
        if (tagName && el.tagName == tagName)
            return el;
        if (className && el.className && ~el.className.indexOf(className))
            return el;
        el = el.parentNode;
    }
    return null;
};
Function.prototype.delegate = function () {
    var _args = [];
    for (var _i = 0; _i < arguments.length; _i++) {
        _args[_i] = arguments[_i];
    }
    var self = this, scope = this.scope, args = arguments, aLength = arguments.length, fnToken = 'function';
    return function () {
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
};
/**
 * 日期格式化
 *
 * @author meizz
 */
Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds() // 毫秒   
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) {
            var obj = (RegExp.$1.length == 1) ? o[k] : ("00" + o[k]).substr(("" + o[k]).length);
            // @ts-ignore
            fmt = fmt.replace(RegExp.$1, obj);
        }
    return fmt;
};
/*
    polyfill JavaScript-Canvas-to-Blob 解决了 HTMLCanvasElement.toBlob 的兼容性
    https://github.com/blueimp/JavaScript-Canvas-to-Blob
*/
if (!HTMLCanvasElement.prototype.toBlob) {
    Object.defineProperty(HTMLCanvasElement.prototype, 'toBlob', {
        value: function (callback, type, quality) {
            var binStr = atob(this.toDataURL(type, quality).split(',')[1]), len = binStr.length, arr = new Uint8Array(len);
            for (var i = 0; i < len; i++)
                arr[i] = binStr.charCodeAt(i);
            callback(new Blob([arr], { type: type || 'image/png' }));
        }
    });
}
