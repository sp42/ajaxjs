﻿/* 
 **************************************************************************
	 _____   _____   _   _   
	| ____| |  _  \ | | / /  
	| |__   | | | | | |/ /   
	|  __|  | | | | | |\ \   
	| |___  | |_| | | | \ \  
	|_____| |_____/ |_|  \_\ 
 
	Module: 	edk.js
	Version: 	alpha 0.1.3
	Author: 	SP42
	Email: 		sp42@ajaxjs.com
	Web: 		www.ajaxjs.com

	说明：
	一、尽管框架这玩意不好做，用现成的库方便，但在编写框架时，还是可以学到许多一般应用无法学到的知识。
	二、本代码可以免费使用、修改，希望我的程序能为您的工作带来方便，同时请保留这份请息。
	三、在其他优秀库的面前，edk.js算很简单的东西，其实基本上都谈不上什么版权之类的，但为了“做做样子”，不想漏了什么东西有遗憾，还是贴上版权，呵呵。

	---Edk Licenses: Berkeley Software Distribution，BSD.-----

	BEGIN LICENSE BLOCK
	
	Copyright 版权所有 (c) 2011 Frank Cheung
	任何获得本软件副本及相关文档文件（下面简称为“软件”）的个人都可以免费获得不受限制处置本软件的权限，
	包括不受限制地使用、复制、修改、合并、出版、分发、重新许可或者销售本软件的副本，
	并且在满足下述条件时，允许本软件的受让人获得下述权限：

	在本软件的所有或者重要部分中包含上述版权公告信息和本权限公告信息。

	本软件不提供保证，不包含任何类型的保证（无论是明指的还是暗喻的），
	包含但不限于关于本软件的适销性、特定用途的适用性和无侵权保证。
	在任何情况下，无论是否签订了合约、存在侵权行为还是在其他情况下，
	本软件作者或版权持有人不对由本软件直接或间接产生的
	或由使用本软件或处置本软件所产生的任何索赔、损坏或者其他责任。
	
	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:
	
	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.
	
	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	THE SOFTWARE.
	
	END LICENSE BLOCK

 ***************************************************************************/

//*************************************************************************
// Edk.js它具备very core的特性。
// 此外，它既可以在客户端运行，也可以满足服务端DSL的要求，一物两用。
//*************************************************************************
/* 
 * 初始化命名空间。$$表示Edk，何解？没什么，只是因为Edk不如敲打“$$”来得轻松：） 
if(typeof exports !== "undefined") {
	$$ = exports;
} else {
	$$ = {};// if(!this['$$'])  $$ = {};
}
*/
var  $$ = $$ || this.exports || {};
var $$$ = $$ || this.exports || {}; // for higher layer

/**
 * 拷贝对象。
 * 对于这个方法在整个库的中使用频率，可以说屡见不鲜，甚至在其他 js 库中，也是汗牛充栋的。
 * @param {Object} a 目标对象
 * @param {Object} b 源对象
 */
$$.apply = function(a, b){
	a = a || new Object;
	for(var i in b)
		a[i] = b[i];
	return a;
}

/**
 * <code>var s = {
    toString : function(){return 's';}
};
move(s, 's.b8b.yuy.kj', 1);
move(s, 's.b8b', 1);
move(s, 's.zzz', 0);
 </ocde>
alert(s.b7b)
 * 不返回结果，只是在对象身上处理。
 * @param {Object} container
 * @param {String} _namespaces
 * @param {Boolean} isOvrToString
 */
$$.move = function (container, _namespaces, isOvrToString){
    var 
         moveAt
        ,_namespaces = _namespaces.split(".")
        ,moveStrAt
        ,newSpace;
    
    moveAt = container; // first
    
    if(!_namespaces || !_namespaces.length || _namespaces.length == 1) return null;

    // container断言存在了的，所以从1开始循环……
    for(var i = 1, j = _namespaces.length; i < j; i++){
        moveStrAt = _namespaces[i];
        if(!moveAt[moveStrAt]){
            newSpace = new Object;
            if(isOvrToString){
                // 对象自身的反射，利用局部变量来保存一份引用
                // 例如:
                // defineLiteral('bar', 'baz', 'qux','quux'); // =>
                // bar.baz.qux.quux // bar.baz.qux.quux.toString(); //
                // =>'bar.baz.qux.quux'
                var backStr = [container.toString()];
                for(var k = 1; k <= i; k++){
                    backStr.push(_namespaces[k]);
                }
                backStr = backStr.join('.');
                newSpace.toString = function(){
                    return backStr;
                }
            } 
            moveAt[moveStrAt] = newSpace;   
        }
        // 前一位( moveAt[moveStrAt] = newSpace)已经非undefined了，可以移到下一位。
        moveAt = moveAt[moveStrAt]; 
    }
}

/**
 * 检查对象的类型。支持 null/undefined/String/Array/Nubmer/Boolean/Date/RegExp。
 * @param	{Any}		foo 要被检查类型的对象。
 * @return	{Funciton}
 */
$$.type = function(foo){
	if (foo === null) {
		return 'null';
	}else if(typeof foo == 'undefined'){
		return 'undefined';
	}
	
	// constructor 判断比 instanceof 更精确，因为 instanceof 对父类亦有效。
	switch (foo.constructor){
		case Boolean :
			return Number;
		case Number :
			return Number;
		case String :
			return String;
		case Array :
			return Array;
		case Date :
			return Date;
		case RegExp :
			return RegExp;
	}

	if(foo instanceof Boolean){
		return Number;
	}else if(foo instanceof Number){
		return Number;
	}else if(foo instanceof String){
		return String;
	}else if(foo instanceof Array){
		return Array;
	}else if(foo instanceof Date){
		return Date;
	}else if(foo instanceof RegExp){
		return RegExp;
	}
	
	switch (typeof foo){
		case 'undefined' :
		case 'unknown' :
		case 'function' :
		case 'regexp' :
			return 'null';
		case 'boolean' :
		case 'number' :
			return Number;
		case 'date' :
			return Date;
		case 'string' :
			return String;
		case 'object' :
			return Object;
		case 'array' :
			return Array;
	}
	
	return null; // 什么类型都判断不了……
}

$$.type = function(v){
	var type = 'undefined';
	
	if(v === null){
		type = null;
	}else if(typeof(v) == type){
		// return 'undefined';
	}else if(typeof(v) == 'boolean' || v.constructor == Boolean){
		type = Boolean;
	}else if(typeof(v) == 'number'  || v.constructor == Number){ /* Is isFinite(v) need? */
		type = Number;
	}else if(typeof(v) == 'string'	|| v.constructor == String){
		type = String;
	}else if(typeof(v) == 'function'){
		type = Function;
	}else if(Object.prototype.toString.call(v)  === '[object Object]'){
		type = Object;
	}else if(Object.prototype.toString.apply(v) === '[object Array]'){
		type = Array;
	}else if(v.toString() == new Date(v).toString()){
		type = Date;
	}else if(v.test && v.exec){
		type = RegExp;
	}
	return type;
}

/**
 * 有点类似 eval() 的作用，却不使用 eval()。
 * @param  {Mixed} v
 * @return {Mixed}
 */
$$.getPrimitives = function (v){
    switch(v){
    	case 'null':
    		return null;
    	case 'true':
    		return true;
    	case 'false':
    		return false;
    	case String(Number(v)):
    		return Number(v);
    	case (new Date(v)).toString(): // v is a date but in Stirng Type
			return new Date(v);
    }
	return v; // 未转换。
}

$$.Class = $$.extend = function(namespace, args, constructor, self){
	// 查询类名
	var className;
	for(var i in namespace){
		if(fn == namespace[i]){
			className = i;
			break;	
		}
	}
	
	// 覆盖
	namespace[className] = constructor;
	// 继承
	constructor.prototype = self;
	
	// 静态成员的复制
	var fn = args.callee;
	$$.apply(constructor,	fn);
	$$.apply(self,			fn);
	
	// 执行构造器	
	constructor.apply(self, args);
}

/**
 * @param {Object}		namespace
 * @param {Array}		args
 * @param {Object}		self
 * @param {Function}	constructor （可选的）
 */
$$._Class = function(namespace, args, self, constructor){
	// 查询类名
	var className;
	for(var i in namespace){
		if(fn == namespace[i]){
			className = i;
			break;	
		}
	}
	// 构造函数可以不写
	constructor = constructor || new Function;
	
	// 覆盖
	namespace[className]  = constructor
	// 继承
	constructor.prototype = self;
	
	// 静态成员的复制
	var fn = args.callee;
	$$.apply(constructor,	fn);
	$$.apply(self,			fn);
	
	// 执行构造器	
	constructor.apply(self, args);
}

/**
 * 将一个数组插入另外一个数组的指定位置。
 * @param	{Array}		target
 * @param	{Number}	insertIndex 
 * @param	{Array}		arr
 * @return	{Array}
 */
$$.Array_Insert = function(target, insertIndex, arr){
	return target.splice.apply(target, Array.concat.call(insertIndex, 0, arr));
}

/**
 * 删掉某个元素。
 * @param	{Array}	arr
 * @param	{Mixed}	el
 * @return	{Mixed}
 */
$$.Array_Remove = function(arr, el){
	var index = -1;
	for(var i = 0, j = arr.length; i < j; i++){
		if(arr[i] == el){
			index = i;
			break;
		}
	}
	arr.splice(index, 1);
	return el;
}


/**
 * <code>var s = {
    toString : function(){return 's';}
};
move(s, 's.b8b.yuy.kj', 1);
move(s, 's.b8b', 1);
move(s, 's.zzz', 0);
 </ocde>
alert(s.b7b)
 * 不返回结果，只是在对象身上处理。
 * @param {Object} container
 * @param {String} _namespaces
 * @param {Boolean} isOvrToString
 */
$$.move = function (container, _namespaces, isOvrToString){
    var 
         moveAt
        ,_namespaces = _namespaces.split(".")
        ,moveStrAt
        ,newSpace;
    
    moveAt = container; // first
    
    if(!_namespaces || !_namespaces.length || _namespaces.length == 1) return null;

    // container断言存在了的，所以从1开始循环……
    for(var i = 1, j = _namespaces.length; i < j; i++){
        moveStrAt = _namespaces[i];
        if(!moveAt[moveStrAt]){
            newSpace = new Object;
            if(isOvrToString){
                // 对象自身的反射，利用局部变量来保存一份引用
                // 例如:
                // defineLiteral('bar', 'baz', 'qux','quux'); // =>
                // bar.baz.qux.quux // bar.baz.qux.quux.toString(); //
                // =>'bar.baz.qux.quux'
                var backStr = [container.toString()];
                for(var k = 1; k <= i; k++){
                    backStr.push(_namespaces[k]);
                }
                backStr = backStr.join('.');
                newSpace.toString = function(){
                    return backStr;
                }
            } 
            moveAt[moveStrAt] = newSpace;   
        }
        // 前一位( moveAt[moveStrAt] = newSpace)已经非undefined了，可以移到下一位。
        moveAt = moveAt[moveStrAt]; 
    }
}

/**
 * 裁剪字符串两旁的空白符，保留中间空白符，例如：
 * Trims whitespace from either end of a string, leaving spaces within the string intact.  Example:
 * <pre><code>
var s = '  foo bar  ';
alert('-' + s + '-');         // alerts "- foo bar -"
alert('-' + s.trim() + '-');  // alerts "-foo bar-"
</code></pre>
 * @return {String} 已裁剪的字符串。The trimmed string
 */
$$.trim = function(){
	var regexp = /^[\s\t\r\n]+|[\s\t\r\n]+$/g;
	$$.trim = function(str){	  // At first run, with override it.
		return str.replace(regexp, '');
	};
	return $$.trim(arguments[0]); // first run
};

/**
 * 定义带标记的字符串，并用传入的字符替换标记。每个标记必须是唯一的，而且必须要像{0},{1}...{n}这样地自增长。
 * Allows you to define a tokenized string and pass an arbitrary number of arguments to replace the tokens.  Each
 * token must be unique, and must increment in the format {0}, {1}, etc.  
 * 例如：Example usage:
 * <pre><code>
var cls = 'my-class', text = 'Some text';
var s = String.format('&lt;div class="{0}">{1}&lt;/div>', cls, text);
//s现在是字符串：s now contains the string: '&lt;div class="my-class">Some text&lt;/div>'
</code></pre>
*  注意这是 Edk 库入侵 js 原型对象的两个方法的其中之一，其实若不是这两个方法调用的频繁，决不会贸然加入原型里面去。
 * @param  {String} string 带标记的字符串，或者可变长度的字符串参数。The tokenized string to be formatted, or arguments list
 * @param  {String} value1 第一个值，替换{0}。The value to replace token {0}
 * @param  {String} value2 第二个值，替换{1}...等等（可以有任意多个）。Etc...
 * @return {String} 转化过的字符串。The formatted string
 */
String.prototype.format = function(){
	var 
	 regexp 	= /{(\d+)}/g
	,replace 	= String.prototype.replace;
	
	function replaceEl(m, i){
		return this[i]; // this为arguments
	};
	/* At first run, with override it. */
	var fn = String.prototype.format = function(){
		replaceEl.scope = arguments;// 委托作用域
		return replace.call(this, regexp, replaceEl.delegate());
	}
	return fn.apply(this, arguments); // first run
};

$$.to16 = {
	/**
	 * 和escape()差不多，后面的一组数字xxx都是表示该字符在字符集表里面的编码的16进制数字，即%XXXX和/uXXXX都是一样的。
	 * 比如输入十六进制5BA0，我们可用/u5BA0表示，又可以用%u5BA0的字符串表示，转换成十进制是23456，再转unicode则是“宠”字。
	 * 结果都一样。下面给出相关的转换函数。
	 */
    on : function (str) {
        var arr = [];
        var hex;
        for (var i = 0 ; i < str.length ;i++) {
        	hex = ("00" + str.charCodeAt(i).toString(16)).slice(-4);
            arr.push(hex);
        }
        
        return "\\u" + arr.join("\\u");
    }
    ,un : function (str) {
        return unescape(str.replace(/\/\//g, "%"));
    }
};

/**
 * 日期格式化。
 * 详见我博客文章：http://blog.csdn.net/zhangxin09/archive/2011/01/01/6111294.aspx
 * e.g: new Date().format("yyyy-MM-dd hh:mm:ss")
 * @param 	{String} format
 * @return	{String}
 */
Date.prototype.format = function(format){
    var $1, o = {
         "M+": this.getMonth() + 1 		// 月份，从0开始算
        ,"d+": this.getDate()    		// 日期
        ,"h+": this.getHours()   		// 小时
        ,"m+": this.getMinutes() 		// 分钟
        ,"s+": this.getSeconds() 		// 秒钟
       									// 季度quarter
        ,"q+": Math.floor((this.getMonth() + 3) / 3)  
        ,"S" : this.getMilliseconds() 	// 千秒
    };
    var key, value;

    if (/(y+)/.test(format)){
    	 $1		= RegExp.$1
    	,format = format.replace($1, String(this.getFullYear()).substr(4 - $1));
    }
    // 如果没有指定该参数，则子字符串将延续到 stringvar 的最后。
    for (key in o){
	    if(new RegExp("(" + key + ")").test(format)){
    		 $1		= RegExp.$1
	    	,value	= String(o[key])
	    	,value	= $1.length == 1 ? value : ("00" + value).substr(value.length)
    		,format = format.replace($1, value);
	    }
    } 
    return format;	
}

/**
 * delegate() 即是委派的意思，Function.prototype.delegate() 就是函数的委托。
 * delegate 方法字在 edk.js 中应用广泛，大体可以分为两个地方的使用，即在函数的参数中使用和在函数的作用域中使用。
 * 后者指的是封装对象（this或者环绕闭包）。delegate 方法可以使得函数的 this 指针改变。
 * forece Args！ so there's no args, it server as Args!
 * 预设函数的作用域。考查下面两个方法：
	Function.prototype.delegateScope = function (scope){
		var self = this;
	    return function(){
	        return self.apply(scope, arguments);
	    }
	}
	
	Function.prototype.delegateArgs = function(){
	    var 
		     self 	 = this
		    ,args	 = arguments;
		    
	    return function(){
	        return self.apply(this, args);
	    };
	}
 * @return {Function}
 */
Function.prototype.delegate = function(){
    var 
     self 	 = this
    ,scope	 = this.scope 
    ,delArgs = arguments
    ,aLength = arguments.length
    ,fnToken = 'function';

    return function(){
    	var args 	= Array.prototype.slice.call(delArgs);			// 保证每次调用方法不同，拷贝一份新的委托参数列表。
        var bLength = arguments.length;
    	var Length  = (aLength > bLength) ? aLength : bLength;
        
    	// mission one:
        for(var i = 0; i < Length; i++){
	    	if(arguments[i]){
	    		args[i] = arguments[i]; // 拷贝参数
	    	}	    	
        }
        
        args.length = Length;// 在 MS JScript下面，rguments 作为数字来使用还是有问题，就是 length 不能自动更新。修正如左:
        
        
    	// mission two:
        for(var i = 0, j = args.length; i < j; i++){
	    	var _arg = args[i];
	    	if(_arg && typeof _arg == fnToken && _arg.late == true){
	    		args[i] = _arg.apply(scope || this, args);
	    	}
        }   
        // 在 MS jscript下面，rguments作为数字来使用还是有问题，就是length不能自动更新。修正如下:
        args.length = Length;
        return self.apply(scope || this, args);
    };
}

/**
 * 修改function的参数。
 * @param  {Number}		argIndex	指定哪一个参数？
 * @param  {Function}	filterFn 	修改的函数
 * @param  {Boolean}	isArray		还要指定第几个的参数？
 * @return {Function}
 */
Function.prototype.delegateArg = function(argIndex, filterFn, isArray){
    var 
	 self	 = this
	,toArray = Array.prototype.slice;
    	
    return function(){
        var args = toArray.call(arguments, 0);	// 变为真正数组。
        
        if(isArray){
        	args[argIndex] = filterFn.apply(this, arguments);
        }else{
	        args[argIndex] = filterFn.call (this, arguments[argIndex]);
        }
        
        return self.apply(this, args);
    };
}

/**
 * 设置一个后置函数。
 * @param  {Function} composeFn
 * @return {Function}
 */
Function.prototype.after = function(composeFn){
    var self = this;

    return function(){
        var result	= self.apply(this, arguments);
        
        return result && (typeof result.pop != 'undefined') && (typeof result.pop != 'unknown')
    		 ? composeFn.apply(this, result)
    		 : composeFn.call (this, result);
    };
}

/**
 * 设置一个前置函数。
 * @param  {Function} sequenceFn
 * @return {Function}
 */
Function.prototype.before = function(sequenceFn){
    var self = this;

    return function(){
        var result = sequenceFn.apply(this, arguments);

        return result && (typeof result.pop != 'undefined') && (typeof result.pop != 'unknown')
    		 ? self.apply(this, result)
    		 : self.call (this, result);
    };
}

/**
 * fn与参数的关系。
 * @param {Array}	arr
 * @param {Object} 	init
 * @param {Any}
 */
Function.prototype.reduce = function(arr, init){
	for(var i = 0, j = arr.length; i < j; i++){
		init = this.call(init || this, arr[i]);
	}
	return init;
}

/**
 * @param  {Array} 		arr
 * @return {Function}
 */
Function.prototype.map = function(arr){
	var 
	 self		= this
	,fixArgs	= Array.prototype.slice;
		
	return function(){
		var 
		 args = fixArgs.call(arguments, 1)
	 	,result;
		 	
		for(var i = 0, j = arr.length, result = new Array(j); i < j; i++){
			result[i] = self.apply(this, args.concat(arr[i]));
		}
		return result;
	}
}

/**
 * fn与参数的关系。
 * @param {Array}	arr
 * @param {Object} 	init
 * @param {Any}
 */
Function.prototype.reduce = function(arr, init){
	for(var i = 0, j = arr.length; i < j; i++){
		init = this.call(init || this, arr[i]);
	}
	return init;
}
  

/**
 * @class $$.Event
 * Event类，就是一个提供事件服务的类，写得简简单单，不求多元、繁复（明显没有比Ext都考虑得多，那是一种方向）。
 * 好像但凡研究JS到一定阶段的人，都要搞清楚事件吧，嗯～必修课。
 * 事件的立论基础大家可以从观察者模式（observable）得到许多灵感，当然就是必须有第三方的“中立”观察者，
 * 一边提供订阅事件的接口，一边让组件触发事件的 fireEvent()。
 * 前言：不得不承认，有时候从新写一个库是一件很辛苦的事情。但是相比较之下，直接使用别人写好的软件来修改，难道这样痛苦的程度就会减少吗？
基于事件模式的设计JS的一大好处就是对 Function 天然的良性支持。这一点纵观眼下多少语言都未必能够赋予这种地位，有的即使赋予了，但率直来讲，也没有JS的那样的流通性的地位。试问一下，现今有多少主流语言是对Function这一类型主打的？若没有JS带来的影响，可能这一观念认识知道的人，不计发烧玩家、专家级人马，少之又少，或是又是其他具有“忽悠”价值的概念来代替这一朴质的概念……
事件本身与 Function 形影不离。好多 GUI 系统都以 Function 为编程的中心，因为制定 GUI 的过程往往就是制定 Function 的过程。事件类型一旦明确用哪一个好之后，开发人员所关心的便是这个事件到底应该执行些什么过程。若过程可以以一个变量或对象来表示，再给他起个名字，我们从某个角度上理解，也可以说该过程被抽象化了。抽象有什么好处？就是把一些必要的过程先写好，以后在复用。直截了当的说，我定义函数为一变量，可以全局变量，也可以私有变量，看您怎么用它——还可以当作参数传来传去。
也是一下子这样说有点跨越性太大，不易接受。如果是真的，操作起来究竟有什么好处呢？首先我们得从了解函数其意义的本质上去入手理解。什么？函数是什么？？这不是初中层次的内容？……哎呀，插一句，不是怕大家见笑哦，我就是看了什么好东西，一味东拿来一点西拿来一点拼装一起，还自诩高级，其实没有扎实的基础知识联系，准吃亏！哎～回头来还比不过老老实实的说明一下基础内容。——各位已深明大义的看官请掠过。
 */
$$.event = function(){
	var events = {};
	
	this.addEvents = function(){
		for(var i = 0, j = arguments.length; i < j; i++){
			events[arguments[i].toLowerCase()] = [];
		}
	}
	
	/**
	  * 添加一个事件侦听器。
	  * @param	{String}   name
	  * @param	{Function} fn
	  * @return {this}
	  */
	this.addListener = this.on = function(name, eventHandler) {
		var eventQueen = events[name.toLowerCase()];
		
		if(!eventQueen){
			throw '没有该事件！请使用addEvent()增加事件';
		}

		eventQueen.push(eventHandler);
		
		return this;
	}
	
	/**
	  * 触发事件。
	  * @param {String} name
	  * @param {Array}  args
	  * @return {Boolean}
	  */
	this.fireEvent = function(name) {
		var eventQueen = events[name.toLowerCase()]; // listeners
		var args = eventQueen.length && Array.prototype.slice.call(arguments, 1); 
					
		var result;
		var output = [];
		
		for (var i = 0, j = eventQueen.length; i < j; i++) {
			result = eventQueen[i].apply(this, args);
			
			if(result === false){
				break;
			}else{
				output.push(result);
			}
		}
	
		return output;
	}
	
	/**
	  * 移除事件侦听器。须传入原来的函数句柄。
	  * @param  {String}   name
	  * @param  {Function} fn
	  * @return {this}
	  */
	this.removeListener = function(name, fn) {
		if (events[name]) {
			Array.remove(events[name], fn);
		}
		return this;
	}
}

/**
 * 提供一个跨浏览器的事件登记器。
 * 返回一个符合当前浏览器的函数用来添加事件,对于非 ie 下的浏览器也让其可以支持 mouseleave/mouseenter
 * @param {Object/String}	el
 * @param {String}			eventName
 * @param {Function}		fn
 * @param {Boolean}			capture
 */
$$.event.on = $$.on = function(el, eventName, fn, capture) {
    if($$.isIE){
        el.attachEvent("on" + eventName, fn);
    }else{
        if (eventName == 'mouseenter') {
            fn = fn.createInterceptor(checkRelatedTarget);
            el.addEventListener(MOUSEOVER, fn, capture);
        } else if (eventName == 'mouseleave') {
            fn = fn.createInterceptor(checkRelatedTarget);
            el.addEventListener(MOUSEOUT, fn, capture);
        } else {
            el.addEventListener(eventName, fn, capture);
        }        
    }
}
 
/**
 *  通用的跟踪方法，用于调试所用。For Debug Use.
 */
$$.trace = l/* very short short hand so that you can call l(xxx) easily.*/ = function(a){
	if(typeof console != 'undefined' && console.log){
		console.log(a);
	}else if(typeof $$.console != 'undefined' && $$.console.log){
		$$.console.log(a);
	}else if(typeof(Response) != 'undefined' ){
        Response.write(a);
    }else{
		// using alert()
		var fn = arguments.callee;
		fn.count++;		
		if(fn.count > fn.maxShow ){
			return;
		}else if(fn.count == fn.maxShow ){
	        alert('Too much errors! It remains many errors, but not going to show.');
	    }else{
	        alert(a);
	    }
    }
}

$$.trace.count		= 0;
$$.trace.maxShow	= 10;

/**
 * 返回XHR对象。
 * 注意 url 必须以http开头的完整路径。
 * 
 * @param {Object} cfg
 * @cfg {Object} post 
 * @cfg {Object} params 与 post 相同
 * @cfg	{String} referer 定义 HTTP-REFERER 变量，请求方说明，例如 http://www.163.com/
 */
$$.request = function(cfg){
	var isNative = typeof XMLHttpRequest != 'undefined';
	
	if(!isNative){
		var progId, progIds = [
			 'MSXML2.ServerXMLHTTP.5.0'
			,'MSXML2.ServerXMLHTTP.3.0'
			,'MSXML2.ServerXMLHTTP'
			,'MSXML2.XMLHTTP.6.0'
			,'MSXML2.XMLHTTP.3.0'
			,'MSXML2.XMLHTTP'
			,'Microsoft.XMLHTTP'
		];
		
		// try 是写在 for 循环里面的，
		// 每当遇到一个 try 语句，异常的框架就放到堆栈上面，直到所有的try语句都完成。
		// 如果下一级的 try 语句没有对某种异常进行处理，堆栈就会展开，直到遇到有处理这种异常的 try 语句。
		// 这是十分浪费性能的做法，但暂时又找不到恰当的解决方式，于是不得已采用了这种 Hack 的方法。
		for (var i = 0, j = progIds.length; i < j; ++i){
			try {
				progId = progIds[i];
				new ActiveXObject(progId); // no referer at all
				break;
			} catch (e){
			}
		}
	}
	
	if(!isNative && !progId){
		throw '没有XHR组件！';
	}
	
	/**
	 * 将对象转换为字符串，并进行 encodeURIComponent 编码。
	 * @param {Object} hash
	 * @return {String}
	 */
	function hash2str(hash){
		var str = [];
		
		for(var i in hash){
			str.push(i + '=' + encodeURIComponent(hash[i]));
		}
		
		return str.join('&');
	}
	$$.request = function(cfg){
		var xhr = isNative ? new XMLHttpRequest() : new ActiveXObject(progId);
		
		if(!xhr){
			throw '系统创建XHR对象失败！';
		}
		
		if(!cfg || !cfg.url){
			throw '不符合最低参数之要求！';
		}
		
		var method = (cfg.isPost || cfg.post || cfg.params) ? "POST" : "GET";
		cfg.isPost = true;
		
		if(cfg.method){
			method = cfg.method.toUpperCase();
		}
		
		var asyncCallback = cfg.fn || cfg.callback;
		
		var isAysc;
		isAysc = typeof cfg.isAysc == 'undefined' ? true /* 默认是异步的 */: cfg.isAysc;
		isAysc = isAysc && asyncCallback && typeof(asyncCallback) == 'function';
		isAysc = !!isAysc;	// ff 必须输入 Boolean，ie则没那么严格。
		
		xhr.open(method, cfg.url, isAysc);
		
		if(isAysc){			// 为协调 ie，将 onreadystatechange 置于 open() 后。
			xhr.onreadystatechange = function(){
				switch (xhr.readyState){
					case 1: break;
					case 2: break;
					case 3: break;
					case 4:
						asyncCallback(xhr, cfg.url);
						
						// 避免 ie 内存泄露
						$$.isIE && typeof setTimeout != 'undefined' && setTimeout(function () { 
						    xhr.onreadystatechange = new Function();
						    xhr = null;
							delete xhr;
						}, 0);

						break;
					default:
						throw '通讯有问题！';
				}
			}
		}
	
		cfg.isPost		&& xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	    cfg.referer 	&& xhr.setRequestHeader("Referer", 	 cfg.referer);
	    cfg.contentType && xhr.setRequestHeader("Content-Type", cfg.contentType); // you may set xhr.setRequestHeader("Accept", "text/json");
		
		// 可接受文档格式，注意，这里默认为 text/json。
		if(cfg.accept && cfg.accept != 'text/json'){
			xhr.setRequestHeader("Accept", 	cfg.accept);
		}else{
			xhr.setRequestHeader("Accept", 'text/json');  
		}
		
		/* For Serverside xhr ONLY */
		if(!isNative && progId.indexOf('Server') != -1){	// 为服务端 XHR 的设置
			xhr.setTimeouts(30000, 30000, 30000, 30000);	// 设置超时 Timeouts in ms for parts of communication: resolve, connect, send (per packet), receive (per packet)
			xhr.setOption(2, 13056); 						// 忽略 SSL Ignore all SSL errors
		}
		
		xhr.send(cfg.isPost ? hash2str(cfg.post || cfg.params) : null);
		
		if(!isAysc)return xhr;
	}
	return $$.request(cfg);
}

/**
 * 兼容浏览器与服务端的 XML 加载器。注意，当前模式下关闭了异步的通讯方式。
 * create a document object
 * @param	{String}	xml		XML 文档路径或者 XML 文档片段。
 * @param	{Boolean}	isNode	true 表示为送入的为 XML 文档片段。
 * @return	{Object} the document
 */
$$.loadXML = function(xml, isNode){
	var doc;
	
	if(typeof ActiveXObject != 'undefined'){
		doc = $$.xml.doc();
	}else if(typeof document != 'undefined' && !isNode){
		if(document.implementation && document.implementation.createDocument){
			doc = document.implementation.createDocument("", "", null);
		}  
	}else if(typeof DOMParser != 'undefined' && isNode){
		doc = new DOMParser().parseFromString(xml, "text/xml");		// 加载XML片段（Moliza Firefox）
		return doc;
	}
	
	if(!doc){
		throw '创建XML文档对象失败！';
	}
	
	doc.async = false;  // 关闭异步特性
	
	if		(xml && !isNode && (doc.load(xml) 	 == false)){		// 加载一份完整的XML文档(Moliza Firefox 与 IE均如此)
		throw '加载XML文档资源失败！';
	}else if(xml &&  isNode && (doc.loadXML(xml) == false)){		// 加载XML片段（IE）
		throw '加载XML片段失败！';									
	}
	
	return doc;
}