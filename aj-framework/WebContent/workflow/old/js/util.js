Raphael.el.addClass = function(className) {
    this.node.setAttribute("class", className);
    return this;
}

;(function(){
	var copyIsArray,  
	    toString = Object.prototype.toString,  
	    hasOwn = Object.prototype.hasOwnProperty,
	    class2type = {  
	        '[object Boolean]' : 'boolean',  
	        '[object Number]' : 'number',  
	        '[object String]' : 'string',  
	        '[object Function]' : 'function',  
	        '[object Array]' : 'array',  
	        '[object Date]' : 'date',  
	        '[object RegExp]' : 'regExp',  
	        '[object Object]' : 'object'  
	    },  
	    type = function(obj) {  
	        return obj == null ? String(obj) : class2type[toString.call(obj)] || "object";  
	    },  
	  
	    isWindow = function(obj) {  
	        return obj && typeof obj === "object" && "setInterval" in obj;  
	    },  
	  
	    isArray = Array.isArray || function(obj) {  
	        return type(obj) === "array";  
	    },  
	  
	    isPlainObject = function(obj) {  
	        if (!obj || type(obj) !== "object" || obj.nodeType || isWindow(obj)) {  
	            return false;  
	        }  
	  
	        if (obj.constructor && !hasOwn.call(obj, "constructor")  
	                && !hasOwn.call(obj.constructor.prototype, "isPrototypeOf")) {  
	            return false;  
	        }  
	  
	        var key;  
	        for (key in obj) {  
	        }  
	  
	        return key === undefined || hasOwn.call(obj, key);  
	    };
	
	function extend() {
	    // 默认不进行深拷贝
	    var deep = false;
	    var name, options, src, copy, clone, copyIsArray;
	    var length = arguments.length;
	    // 记录要复制的对象的下标
	    var i = 1;
	    // 第一个参数不传布尔值的情况下，target 默认是第一个参数
	    var target = arguments[0] || {};
	    // 如果第一个参数是布尔值，第二个参数是 target
	    if (typeof target == 'boolean') {
	        deep = target;
	        target = arguments[i] || {};
	        i++;
	    }
	    // 如果target不是对象，我们是无法进行复制的，所以设为 {}
	    if (typeof target !== "object" && !isFunction(target)) {
	        target = {};
	    }
	
	    // 循环遍历要复制的对象们
	    for (; i < length; i++) {
	        // 获取当前对象
	        options = arguments[i];
	        // 要求不能为空 避免 extend(a,,b) 这种情况
	        if (options != null) {
	            for (name in options) {
	                // 目标属性值
	                src = target[name];
	                // 要复制的对象的属性值
	                copy = options[name];
	
	                // 解决循环引用
	                if (target === copy) {
	                    continue;
	                }
	
	                // 要递归的对象必须是 plainObject 或者数组
	                if (deep && copy && (isPlainObject(copy) ||
	                        (copyIsArray = Array.isArray(copy)))) {
	                    // 要复制的对象属性值类型需要与目标属性值相同
	                    if (copyIsArray) {
	                        copyIsArray = false;
	                        clone = src && Array.isArray(src) ? src : [];
	
	                    } else {
	                        clone = src && isPlainObject(src) ? src : {};
	                    }
	
	                    target[name] = extend(deep, clone, copy);
	
	                } else if (copy !== undefined) {
	                    target[name] = copy;
	                }
	            }
	        }
	    }
	
	    return target;
	};
		
	aj.extend = extend;
})();